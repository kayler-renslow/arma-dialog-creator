package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColorArray;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVExpression;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Region;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.SimpleCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ViewportCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.util.*;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Base class for JavaFX canvas rendering of arma controls

 @author Kayler
 @since 05/20/2016. */
public class ArmaControlRenderer extends SimpleCanvasComponent implements ViewportCanvasComponent {
	/**
	 Key used for determining if {@link #paint(GraphicsContext, DataContext)} will paint the control for Arma Preview.
	 If key value is true, will paint preview. If key is false, will paint editor version.
	 */
	public static final Key<Boolean> KEY_PAINT_PREVIEW = new Key<>("ArmaControlRenderer.PaintPreview", false);

	protected final ArmaControl myControl;
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final ArmaResolution resolution;
	private final ValueObserver<SVColor> globalBackgroundColorObserver;
	/** A simple value listener that will only invoke {@link #requestRender()} when update is received */
	protected final ValueListener<SerializableValue> renderValueUpdateListener = new ValueListener<SerializableValue>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
			requestRender();
		}
	};

	private final UpdateListenerGroup<Resolution> resolutionUpdateGroup = new UpdateListenerGroup<>();
	private final ValueObserver<Boolean> enabledObserver = new ValueObserver<>(isEnabled());
	protected final ControlProperty xProperty, yProperty, wProperty, hProperty;
	protected final Env env;
	private boolean disablePositionPropertyListener = false;
	private boolean disableRecalc = false;
	private final Runnable runnableRequestRender = new Runnable() {
		@Override
		public void run() {
			if (myControl.getDisplay() != null) {
				myControl.getRenderUpdateGroup().update(getMyControl());
			}
		}
	};
	/** Set by {@link #setMouseOver(int, int, boolean)} */
	protected int mouseOverX,
	/** Set by {@link #setMouseOver(int, int, boolean)} */
	mouseOverY;
	/**
	 True if the mouse is over this control in preview mode. False if the user's mouse isn't over this control in
	 preview mode. Set by {@link #setMouseOver(int, int, boolean)}
	 */
	protected boolean mouseOver;

	public ArmaControlRenderer(@NotNull ArmaControl control, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(0, 0, 0, 0);
		this.resolution = resolution;
		this.env = env;
		this.myControl = control;
		globalBackgroundColorObserver = new ValueObserver<>(new SVColorArray(backgroundColor));
		globalBackgroundColorObserver.addListener(new ValueListener<SVColor>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SVColor> observer, SVColor oldValue, SVColor newValue) {
				if (newValue != null) {
					setBackgroundColor(newValue.toJavaFXColor());
					requestRender();
				}
			}
		});

		xProperty = control.findRequiredProperty(ControlPropertyLookup.X);
		yProperty = control.findRequiredProperty(ControlPropertyLookup.Y);
		wProperty = control.findRequiredProperty(ControlPropertyLookup.W);
		hProperty = control.findRequiredProperty(ControlPropertyLookup.H);

		final ValueListener<SerializableValue> positionValueListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (disablePositionPropertyListener) {
					return;
				}

				if (xProperty.getValueObserver() == observer) {
					if (xProperty.getValue() == null || !(xProperty.getValue() instanceof SVExpression)) {
						return;
					}
					setXSilent((SVExpression) xProperty.getValue());
				} else if (yProperty.getValueObserver() == observer) {
					if (yProperty.getValue() == null || !(yProperty.getValue() instanceof SVExpression)) {
						return;
					}
					setYSilent((SVExpression) yProperty.getValue());
				} else if (wProperty.getValueObserver() == observer) {
					if (wProperty.getValue() == null || !(wProperty.getValue() instanceof SVExpression)) {
						return;
					}
					setWSilent((SVExpression) wProperty.getValue());
				} else if (hProperty.getValueObserver() == observer) {
					if (hProperty.getValue() == null || !(hProperty.getValue() instanceof SVExpression)) {
						return;
					}
					setHSilent((SVExpression) hProperty.getValue());
				} else {
					throw new IllegalStateException("unmatched observer");
				}
				requestRender();
			}
		};
		xProperty.getValueObserver().addListener(positionValueListener);
		yProperty.getValueObserver().addListener(positionValueListener);
		wProperty.getValueObserver().addListener(positionValueListener);
		hProperty.getValueObserver().addListener(positionValueListener);

		enabledObserver.addListener(new ValueListener<Boolean>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean newValue) {
				requestRender();
			}
		});

		if (xProperty.getValue() == null) {
			defineX(new SVExpression("0", env));
		} else {
			setXSilent((SVExpression) xProperty.getValue());
		}
		if (yProperty.getValue() == null) {
			defineY(new SVExpression("0", env));
		} else {
			setYSilent((SVExpression) yProperty.getValue());
		}
		if (wProperty.getValue() == null) {
			defineW(new SVExpression("1", env));
		} else {
			setWSilent((SVExpression) wProperty.getValue());
		}
		if (hProperty.getValue() == null) {
			defineH(new SVExpression("1", env));
		} else {
			setHSilent((SVExpression) hProperty.getValue());
		}

	}

	/**
	 Since the control's update group will update before the renderer's control property value listeners get notified,
	 the re-render must occur AFTER the renderer's internal values change. Invoke
	 this whenever a new render needs to happen.
	 <p>
	 This method can be invoked on any thread.
	 */
	public void requestRender() {
		if (Thread.currentThread() != ArmaDialogCreator.getJavaFXThread()) {
			Platform.runLater(runnableRequestRender);
		} else {
			runnableRequestRender.run();
		}
	}

	/**
	 Return true if {@link #paint(GraphicsContext, DataContext)} should paint in preview form, false if should paint in editor form.

	 @param dataContext context from {@link #paint(GraphicsContext, DataContext)}
	 */
	protected boolean paintPreview(@NotNull DataContext dataContext) {
		return KEY_PAINT_PREVIEW.get(dataContext);
	}

	/** Set x and define the x control property. This will also update the renderer's position. */
	public void defineX(SVExpression x) {
		xProperty.setValue(x);
	}

	/** Set y and define the y control property. This will also update the renderer's position. */
	public void defineY(SVExpression y) {
		yProperty.setValue(y);
	}

	/** Set w (width) and define the w control property. This will also update the renderer's position. */
	public void defineW(SVExpression width) {
		wProperty.setValue(width);
	}

	/** Set h (height) and define the h control property. This will also update the renderer's position. */
	public void defineH(SVExpression height) {
		hProperty.setValue(height);
	}

	/** Just set x position without updating the property. This will also update the renderer's position. */
	protected void setXSilent(SVExpression x) {
		if (x == null) {
			return;
		}
		int newX1 = calcScreenX(x.getNumVal());
		int oldX1 = x1;
		int dx = newX1 - oldX1;
		setX1Silent(newX1);
		setX2Silent(x2 + dx);//keep old width
	}

	/** Just set the y position without updating the y property. This will also update the renderer's position. */
	protected void setYSilent(SVExpression y) {
		if (y == null) {
			return;
		}
		int newY1 = calcScreenY(y.getNumVal());
		int oldY1 = y1;
		int dy = newY1 - oldY1;
		setY1Silent(newY1);
		setY2Silent(y2 + dy); //keep old height
	}

	/** Set the width without updating it's control property. This will also update the renderer's position. */
	protected void setWSilent(SVExpression width) {
		if (width == null) {
			return;
		}
		int w = calcScreenWidth(width.getNumVal());
		setX2Silent(getX1() + w);
	}

	/** Just set height without setting control property. This will also update the renderer's position. */
	protected void setHSilent(SVExpression height) {
		if (height == null) {
			return;
		}
		int h = calcScreenHeight(height.getNumVal());
		setY2Silent(getY1() + h);
	}

	@Override
	public void setPercentX(double percentX) {
		defineX(new SVExpression(ArmaPrecision.format(percentX), env));
	}

	@Override
	public void setPercentY(double percentY) {
		defineY(new SVExpression(ArmaPrecision.format(percentY), env));
	}

	@Override
	public void setPercentW(double percentW) {
		defineW(new SVExpression(ArmaPrecision.format(percentW), env));
	}

	@Override
	public void setPercentH(double percentH) {
		defineH(new SVExpression(ArmaPrecision.format(percentH), env));
	}

	@Override
	public void setPositionPercent(double percentX, double percentY, double percentW, double percentH) {
		setPercentX(percentX);
		setPercentY(percentY);
		setPercentW(percentW);
		setPercentH(percentH);
	}

	@Override
	public double getPercentX() {
		return xProperty.getFloatValue();
	}

	@Override
	public double getPercentY() {
		return yProperty.getFloatValue();
	}

	@Override
	public double getPercentW() {
		return wProperty.getFloatValue();
	}

	@Override
	public double getPercentH() {
		return hProperty.getFloatValue();
	}

	@Override
	public final int calcScreenX(double percentX) {
		return PositionCalculator.getScreenX(resolution, percentX);
	}

	@Override
	public final int calcScreenY(double percentY) {
		return PositionCalculator.getScreenY(resolution, percentY);
	}

	@Override
	public final int calcScreenWidth(double percentWidth) {
		return PositionCalculator.getScreenWidth(resolution, percentWidth);
	}

	@Override
	public final int calcScreenHeight(double percentHeight) {
		return PositionCalculator.getScreenHeight(resolution, percentHeight);
	}


	/** Set the x and y values (and width and height) based upon the renderer's position */
	protected final void recalcPosition() {
		if (disableRecalc) {
			return;
		}
		final SVExpression x = new SVExpression(PositionCalculator.getSafeZoneExpressionX(resolution, getX1()), env);
		final SVExpression y = new SVExpression(PositionCalculator.getSafeZoneExpressionY(resolution, getY1()), env);
		final SVExpression w = new SVExpression(PositionCalculator.getSafeZoneExpressionW(resolution, getWidth()), env);
		final SVExpression h = new SVExpression(PositionCalculator.getSafeZoneExpressionH(resolution, getHeight()), env);
		this.disablePositionPropertyListener = true;
		xProperty.setValue(x);
		yProperty.setValue(y);

		wProperty.setValue(w);
		hProperty.setValue(h);
		this.disablePositionPropertyListener = false;
		requestRender();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		enabledObserver.updateValue(enabled);
	}

	@NotNull
	public ValueObserver<Boolean> getEnabledObserver() {
		return enabledObserver;
	}

	@NotNull
	public ArmaControl getMyControl() {
		return myControl;
	}

	@NotNull
	public ValueObserver<SVColor> getBackgroundColorObserver() {
		return globalBackgroundColorObserver;
	}

	@Override
	public void translate(int dx, int dy) {
		super.translate(dx, dy);
		recalcPosition();
	}

	@Override
	public void scale(int dxl, int dxr, int dyt, int dyb) {
		super.scale(dxl, dxr, dyt, dyb);
		recalcPosition();
	}

	@Override
	public void setPosition(@NotNull Region r) {
		super.setPosition(r);
		recalcPosition();
	}

	@Override
	public void setPosition(int x1, int y1, int x2, int y2) {
		super.setPosition(x1, y1, x2, y2);
		recalcPosition();
	}

	@Override
	public void setPositionWH(int x1, int y1, int width, int height) {
		super.setPositionWH(x1, y1, width, height);
		recalcPosition();
	}

	@Override
	public void setX1(int x1) {
		super.setX1(x1);
		recalcPosition();
	}

	@Override
	public void setY1(int y1) {
		super.setY1(y1);
		recalcPosition();
	}

	@Override
	public void setX2(int x2) {
		super.setX2(x2);
		recalcPosition();
	}

	@Override
	public void setY2(int y2) {
		super.setY2(y2);
		recalcPosition();
	}

	/** Set x1 without recalculating position */
	public void setX1Silent(int x1) {
		disableRecalc = true;
		setX1(x1);
		disableRecalc = false;
	}

	/** Set y1 without recalculating position */
	public void setY1Silent(int y1) {
		disableRecalc = true;
		setY1(y1);
		disableRecalc = false;
	}

	/** Set x2 without recalculating position */
	public void setX2Silent(int x2) {
		disableRecalc = true;
		setX2(x2);
		disableRecalc = false;
	}

	/** Set y2 without recalculating position */
	public void setY2Silent(int y2) {
		disableRecalc = true;
		setY2(y2);
		disableRecalc = false;
	}

	@Override
	public void setGhost(boolean ghost) {
		super.setGhost(ghost);
		//enable observer is already handled
	}

	public void resolutionUpdate(@NotNull Resolution newResolution) {
		if (xProperty.getValue() instanceof SVExpression) {
			setXSilent((SVExpression) xProperty.getValue());
		}
		if (yProperty.getValue() instanceof SVExpression) {
			setYSilent((SVExpression) yProperty.getValue());
		}
		if (wProperty.getValue() instanceof SVExpression) {
			setWSilent((SVExpression) wProperty.getValue());
		}
		if (hProperty.getValue() instanceof SVExpression) {
			setHSilent((SVExpression) hProperty.getValue());
		}

		resolutionUpdateGroup.update(newResolution);
	}

	@NotNull
	public ArmaResolution getResolution() {
		return resolution;
	}

	@NotNull
	public UpdateListenerGroup<Resolution> getResolutionUpdateGroup() {
		return resolutionUpdateGroup;
	}


	/**
	 Used by Arma Preview to let this renderer know that the user's mouse is over the control in the preview.
	 When this control is requested to be rendered in preview mode ({@link #paintPreview(DataContext)} returns true),
	 this renderer will determine what to do with this information.

	 @param mousex mouse x position on the canvas (irrelevant if mouseOver is false)
	 @param mousey mouse y position on the canvas (irrelevant if mouseOver is false)
	 @param mouseOver true if the mouse is over this control, false if it isn't
	 */
	public void setMouseOver(int mousex, int mousey, boolean mouseOver) {
		this.mouseOverX = mousex;
		this.mouseOverY = mousey;
		this.mouseOver = mouseOver;
	}

	/**
	 Paint a checkerboard with the given {@link GraphicsContext} and coordinates

	 @param gc context
	 @param x x pos
	 @param y y pos
	 @param w width
	 @param h height
	 @param color1 one of the colors to use
	 @param color2 other color to use
	 */
	public static void paintCheckerboard(@NotNull GraphicsContext gc, int x, int y, int w, int h, @NotNull Color color1, @NotNull Color color2) {
		final int numBoxes = 10;
		final int boxWidth = w / numBoxes;
		final int boxHeight = h / numBoxes;
		final int remainderWidth = w - numBoxes * boxWidth;
		final int remainderHeight = h - numBoxes * boxHeight;

		gc.save();

		for (int row = 0; row < numBoxes || (row == numBoxes && remainderHeight > 0); row++) {
			//doing <= to make sure that the full height and width is painted
			//since the aspect ratio of the checkerboard may not be equal to aspect ratio of the given width and height

			int yy = y + row * boxHeight;
			for (int box = 0; box < numBoxes || (box == numBoxes && remainderWidth > 0); box++) {
				gc.setStroke((box + row) % 2 == 0 ? color1 : color2);
				int xx = x + box * boxWidth;
				Region.fillRectangle(
						gc, xx, yy,
						xx + (box == numBoxes ? remainderWidth : boxWidth),
						yy + (row == numBoxes ? remainderHeight : boxHeight)
				);
			}
		}

		gc.restore();
	}
}
