package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Region;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.SimpleCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ViewportCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.util.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Base class for JavaFX canvas rendering of arma controls

 @author Kayler
 @since 05/20/2016. */
public class ArmaControlRenderer extends SimpleCanvasComponent implements ViewportCanvasComponent {
	/**
	 Key used for determining if {@link #paint(GraphicsContext, DataContext)} will paint the control for Arma Preview. If key value is true, will paint preview. If key is false, will paint editor
	 version.
	 */
	public static final Key<Boolean> KEY_PAINT_PREVIEW = new Key<>("ArmaControlRenderer.PaintPreview", false);

	protected final ArmaControl myControl;
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final ArmaResolution resolution;
	private final ValueObserver<AColor> globalBackgroundColorObserver;
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
	private final Env env;
	private boolean disablePositionPropertyListener = false;
	private boolean disableRecalc = false;

	public ArmaControlRenderer(@NotNull ArmaControl control, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(0, 0, 0, 0);
		this.resolution = resolution;
		this.env = env;
		this.myControl = control;
		globalBackgroundColorObserver = new ValueObserver<>(new AColor(backgroundColor));
		globalBackgroundColorObserver.addListener(new ValueListener<AColor>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
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
					if (xProperty.getValue() == null) {
						return;
					}
					setXSilent((Expression) xProperty.getValue());
				} else if (yProperty.getValueObserver() == observer) {
					if (yProperty.getValue() == null) {
						return;
					}
					setYSilent((Expression) yProperty.getValue());
				} else if (wProperty.getValueObserver() == observer) {
					if (wProperty.getValue() == null) {
						return;
					}
					setWSilent((Expression) wProperty.getValue());
				} else if (hProperty.getValueObserver() == observer) {
					if (hProperty.getValue() == null) {
						return;
					}
					setHSilent((Expression) hProperty.getValue());
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
			defineX(new Expression("0", env));
		} else {
			setXSilent((Expression) xProperty.getValue());
		}
		if (yProperty.getValue() == null) {
			defineY(new Expression("0", env));
		} else {
			setYSilent((Expression) yProperty.getValue());
		}
		if (wProperty.getValue() == null) {
			defineW(new Expression("1", env));
		} else {
			setWSilent((Expression) wProperty.getValue());
		}
		if (hProperty.getValue() == null) {
			defineH(new Expression("1", env));
		} else {
			setHSilent((Expression) hProperty.getValue());
		}

	}

	/**
	 Since the control's update group will update before the renderer's control property value listeners get notified, the re-render must occur AFTER the renderer's internal values change. Invoke
	 this whenever a new render needs to happen.
	 */
	public final void requestRender() {
		myControl.getRenderUpdateGroup().update(this.getMyControl());
	}

	/**
	 Return true if {@link #paint(GraphicsContext, DataContext)} should paint in preview form, false if should paint in editor form.

	 @param dataContext context from {@link #paint(GraphicsContext, DataContext)}
	 */
	protected boolean paintPreview(@NotNull DataContext dataContext) {
		return KEY_PAINT_PREVIEW.get(dataContext);
	}

	/** Set x and define the x control property. This will also update the renderer's position. */
	public void defineX(Expression x) {
		xProperty.setValue(x);
	}

	/** Set y and define the y control property. This will also update the renderer's position. */
	public void defineY(Expression y) {
		yProperty.setValue(y);
	}

	/** Set w (width) and define the w control property. This will also update the renderer's position. */
	public void defineW(Expression width) {
		wProperty.setValue(width);
	}

	/** Set h (height) and define the h control property. This will also update the renderer's position. */
	public void defineH(Expression height) {
		hProperty.setValue(height);
	}

	/** Just set x position without updating the property. This will also update the renderer's position. */
	protected void setXSilent(Expression x) {
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
	protected void setYSilent(Expression y) {
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
	protected void setWSilent(Expression width) {
		if (width == null) {
			return;
		}
		int w = calcScreenWidth(width.getNumVal());
		setX2Silent(getX1() + w);
	}

	/** Just set height without setting control property. This will also update the renderer's position. */
	protected void setHSilent(Expression height) {
		if (height == null) {
			return;
		}
		int h = calcScreenHeight(height.getNumVal());
		setY2Silent(getY1() + h);
	}

	@Override
	public void setPercentX(double percentX) {
		defineX(new Expression(ArmaPrecision.format(percentX), env));
	}

	@Override
	public void setPercentY(double percentY) {
		defineY(new Expression(ArmaPrecision.format(percentY), env));
	}

	@Override
	public void setPercentW(double percentW) {
		defineW(new Expression(ArmaPrecision.format(percentW), env));
	}

	@Override
	public void setPercentH(double percentH) {
		defineH(new Expression(ArmaPrecision.format(percentH), env));
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
		final Expression x = new Expression(PositionCalculator.getSafeZoneExpressionX(resolution, getX1()), env);
		final Expression y = new Expression(PositionCalculator.getSafeZoneExpressionY(resolution, getY1()), env);
		final Expression w = new Expression(PositionCalculator.getSafeZoneExpressionW(resolution, getWidth()), env);
		final Expression h = new Expression(PositionCalculator.getSafeZoneExpressionH(resolution, getHeight()), env);
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
	public ValueObserver<AColor> getBackgroundColorObserver() {
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
		setXSilent((Expression) xProperty.getValue());
		setYSilent((Expression) yProperty.getValue());
		setWSilent((Expression) wProperty.getValue());
		setHSilent((Expression) hProperty.getValue());

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
