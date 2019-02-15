package com.armadialogcreator.control;

import com.armadialogcreator.canvas.*;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyKey;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Base class for JavaFX canvas rendering of arma controls

 @author Kayler
 @since 05/20/2016. */
public class ArmaControlRenderer extends SimpleCanvasComponent implements ViewportCanvasComponent {
	public static final UpdateListenerGroup<UpdateListenerGroup.NoData> UNIVERSAL_RENDER_GROUP = new UpdateListenerGroup<>();

	protected final ArmaControl myControl;
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final ArmaResolution resolution;
	private final ValueObserver<SVColor> globalBackgroundColorObserver;

	private final UpdateListenerGroup<Resolution> resolutionUpdateGroup = new UpdateListenerGroup<>();
	private final ValueObserver<Boolean> enabledObserver = new ValueObserver<>(isEnabled());
	protected final ConfigProperty xProperty, yProperty, wProperty, hProperty;
	protected final Env env;
	private boolean disablePositionPropertyListener = false;
	private boolean disableRecalc = false;
	private final Runnable runnableRequestRender = new Runnable() {
		@Override
		public void run() {
			myControl.getDisplay().renderUpdateGroup().update(UpdateListenerGroup.NoDataInstance);
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
	/**
	 Set by {@link #mousePress(MouseButton)}. If null, the mouse is not currently clicking on this renderer. If not
	 null, the user's mouse is pressing down on this renderer.
	 */
	@Nullable
	protected MouseButton mouseButtonDown;
	/** True if the control has focus, false if it doesn't */
	protected boolean focused = false;
	/**
	 Set to true if the preview should focus to this control. The preview will decide whether or not to actually
	 focus.
	 */
	protected boolean requestFocus = false;

	/** False when the constructor finishes */
	private boolean initializing = true;

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
				} else {
					setBackgroundColor(Color.TRANSPARENT);
				}
				requestRender();
			}
		});

		xProperty = control.findProperty(ConfigPropertyLookup.X);
		yProperty = control.findProperty(ConfigPropertyLookup.Y);
		wProperty = control.findProperty(ConfigPropertyLookup.W);
		hProperty = control.findProperty(ConfigPropertyLookup.H);

		final NotNullValueListener<SerializableValue> positionValueListener = new NotNullValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> observer, @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
				if (disablePositionPropertyListener) {
					return;
				}

				if (xProperty.getValueObserver() == observer) {
					if (!(xProperty.getValue() instanceof SVExpression)) {
						return;
					}
					setXSilent((SVExpression) xProperty.getValue());
				} else if (yProperty.getValueObserver() == observer) {
					if (!(yProperty.getValue() instanceof SVExpression)) {
						return;
					}
					setYSilent((SVExpression) yProperty.getValue());
				} else if (wProperty.getValueObserver() == observer) {
					if (!(wProperty.getValue() instanceof SVExpression)) {
						return;
					}
					setWSilent((SVExpression) wProperty.getValue());
				} else if (hProperty.getValueObserver() == observer) {
					if (!(hProperty.getValue() instanceof SVExpression)) {
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

		initializing = false;
	}

	@NotNull
	public Env getEnv() {
		return env;
	}

	/**
	 Invoked when the x, y, width, or height of the control is updated. Default implementation does nothing.

	 @param initializingPosition true when the renderer's position is being initialized for the first time,
	 false for every other position update
	 */
	protected void positionUpdate(boolean initializingPosition) {

	}

	/**
	 Since the control's update group will update before the renderer's control property value listeners get notified,
	 the re-render must occur AFTER the renderer's internal values change. Invoke
	 this whenever a new render needs to happen.
	 */
	public void requestRender() {
		runnableRequestRender.run();
	}

	/**
	 Return true if {@link CanvasComponent#paint(GraphicsContext, CanvasContext)} should paint in preview form,
	 false if should paint in editor form.

	 @param cc context from {@link CanvasComponent#paint(GraphicsContext, CanvasContext)}
	 */
	public boolean paintPreview(@NotNull CanvasContext cc) {
		return !cc.paintPartial();
	}

	/** Set x and define the x control property. This will also update the renderer's position. */
	public void defineX(SerializableValue x) {
		xProperty.setValue(x);
	}

	/** Set y and define the y control property. This will also update the renderer's position. */
	public void defineY(SerializableValue y) {
		yProperty.setValue(y);
	}

	/** Set w (width) and define the w control property. This will also update the renderer's position. */
	public void defineW(SerializableValue width) {
		wProperty.setValue(width);
	}

	/** Set h (height) and define the h control property. This will also update the renderer's position. */
	public void defineH(SerializableValue height) {
		hProperty.setValue(height);
	}

	/** Just set x position without updating the property. This will also update the renderer's position. */
	protected void setXSilent(SVNumericValue x) {
		if (x == null) {
			return;
		}
		int newX1 = calcScreenX(x.toDouble());
		int oldX1 = x1;
		int dx = newX1 - oldX1;
		setX1Silent(newX1);
		setX2Silent(x2 + dx);//keep old width
	}

	/** Just set the y position without updating the y property. This will also update the renderer's position. */
	protected void setYSilent(SVNumericValue y) {
		if (y == null) {
			return;
		}
		int newY1 = calcScreenY(y.toDouble());
		int oldY1 = y1;
		int dy = newY1 - oldY1;
		setY1Silent(newY1);
		setY2Silent(y2 + dy); //keep old height
	}

	/** Set the width without updating it's control property. This will also update the renderer's position. */
	protected void setWSilent(SVNumericValue width) {
		if (width == null) {
			return;
		}
		int w = calcScreenWidth(width.toDouble());
		setX2Silent(getX1() + w);
	}

	/** Just set height without setting control property. This will also update the renderer's position. */
	protected void setHSilent(SVNumericValue height) {
		if (height == null) {
			return;
		}
		int h = calcScreenHeight(height.toDouble());
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
		positionUpdate(this.initializing);
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
		if (xProperty.getValue() instanceof SVNumericValue) {
			setXSilent((SVNumericValue) xProperty.getValue());
		}
		if (yProperty.getValue() instanceof SVNumericValue) {
			setYSilent((SVNumericValue) yProperty.getValue());
		}
		if (wProperty.getValue() instanceof SVNumericValue) {
			setWSilent((SVNumericValue) wProperty.getValue());
		}
		if (hProperty.getValue() instanceof SVNumericValue) {
			setHSilent((SVNumericValue) hProperty.getValue());
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
	 Adds a value listener to {@link #myControl} with the provided {@link ConfigPropertyKey}.
	 Afterwards, it invokes the listener with the current value.
	 */
	public void addValueListener(@NotNull ConfigPropertyKey key, @NotNull NotNullValueListener<SerializableValue> listener) {
		addValueListener(myControl, key.getPropertyName(), listener);
	}

	/**
	 Adds a value listener to {@link #myControl} with the provided property name.
	 Afterwards, it invokes the listener with the current value.
	 */
	public void addValueListener(@NotNull String propertyName, @NotNull NotNullValueListener<SerializableValue> listener) {
		addValueListener(myControl, propertyName, listener);
	}

	/**
	 Adds a value listener to the given {@link ConfigClass} with the provided property name.
	 Afterwards, it invokes the listener with the current value.
	 */
	public void addValueListener(@NotNull ConfigClass owner, @NotNull String propertyName, @NotNull NotNullValueListener<SerializableValue> l) {
		ConfigProperty property = owner.findProperty(propertyName);
		property.addValueListener(l);
		l.valueUpdated(property.getValueObserver(), property.getValue(), property.getValue());
	}

	/**
	 Adds a value listener to the given {@link ConfigClass} with the provided key.
	 Invokes {@link #addValueListener(ConfigClass, String, NotNullValueListener)} using {@link ConfigPropertyKey#getPropertyName()}
	 */
	public void addValueListener(@NotNull ConfigClass owner, @NotNull ConfigPropertyKey key, @NotNull NotNullValueListener<SerializableValue> l) {
		addValueListener(owner, key.getPropertyName(), l);
	}

	/**
	 Used by Arma Preview to let this renderer know that the user's mouse is over the control in the preview.
	 When this control is requested to be rendered in preview mode ({@link #paintPreview(CanvasContext)} returns true),
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
	 @see Region#paintCheckerboard(GraphicsContext, int, int, int, int, Color, Color, int)
	 */
	public static void paintCheckerboard(@NotNull GraphicsContext gc, int x, int y, int w, int h, @NotNull Color color1, @NotNull Color color2) {
		Region.paintCheckerboard(gc, x, y, w, h, color1, color2, 10);
	}

	/**
	 The mouse was pressed on the given position with the give mouse button. This method assumes that the mouse
	 position was already in bounds of this control.

	 @param mb mouse button used
	 */
	public void mousePress(@NotNull MouseButton mb) {
		this.mouseButtonDown = mb;
	}

	public void mouseRelease() {
		this.mouseButtonDown = null;
	}

	/** @return true if this control would like focus, false if it hasn't requested it */
	public boolean requestingFocus() {
		return this.requestFocus;
	}

	/**
	 Set if this control has focus

	 @param focused true if should have focus, false otherwise
	 */
	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	/**
	 Get if this control can have focus. Default implementation is false.

	 @return true if this renderer can have focus, false if it can't
	 */
	public boolean canHaveFocus() {
		return false;
	}

	/** @return true if this control has focus, false if it doesn't */
	public boolean hasFocus() {
		return focused;
	}

	/**
	 Use this when a texture couldn't be determined for background color

	 @param gc context
	 */
	public void paintTextureError(@NotNull GraphicsContext gc, int x, int y, int w, int h) {
		paintError(gc, Color.DEEPPINK, x, y, w, h);
	}

	/**
	 Use this when an image couldn't be determined for background

	 @param gc context
	 */
	public void paintImageError(@NotNull GraphicsContext gc, int x, int y, int w, int h) {
		paintError(gc, Color.RED, x, y, w, h);
	}

	public static void paintImageLoading(@NotNull GraphicsContext gc, @NotNull Paint bgColor, int x1, int y1, int x2, int y2) {
		gc.setStroke(bgColor);
		Region.fillRectangle(gc, x1, y1, x2, y2);
		gc.setStroke(Color.MAGENTA);
		gc.strokeLine(x1, y1, x2, y2);
		gc.strokeLine(x2, y1, x1, y2);
	}

	/**
	 Use this when a background color array couldn't be determined

	 @param gc context
	 */
	public void paintBackgroundColorError(@NotNull GraphicsContext gc, int x, int y, int w, int h) {
		paintError(gc, Color.BLACK, x, y, w, h);
	}

	/**
	 Invokes {@link #paintCheckerboard(GraphicsContext, int, int, int, int, Color, Color)}
	 with the provided color and {@link #getBackgroundColor()} as the checkerboard colors
	 */
	public void paintError(@NotNull GraphicsContext gc, @NotNull Color color, int x, int y, int w, int h) {
		paintCheckerboard(gc, x, y, w, h, color, getBackgroundColor());
	}

	/**
	 Invokes {@link #paintCheckerboard(GraphicsContext, int, int, int, int, Color, Color)}
	 with the provided color and {@link #getBackgroundColor()} as the checkerboard colors
	 */
	public void paintError(@NotNull GraphicsContext gc, @NotNull Color color) {
		paintError(gc, color, getX1(), getY1(), getWidth(), getHeight());
	}

}
