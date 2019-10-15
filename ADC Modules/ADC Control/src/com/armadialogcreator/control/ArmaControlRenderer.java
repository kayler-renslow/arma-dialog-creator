package com.armadialogcreator.control;

import com.armadialogcreator.canvas.*;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigPropertyKey;
import com.armadialogcreator.core.ConfigPropertyProxy;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVColorArray;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.layout.Bounds;
import com.armadialogcreator.util.*;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Base class for JavaFX canvas rendering of arma controls

 @author Kayler
 @since 05/20/2016. */
public class ArmaControlRenderer implements UIRenderer, DataInvalidator {
	public static final UpdateListenerGroup<UpdateListenerGroup.NoData> UNIVERSAL_RENDER_GROUP = new UpdateListenerGroup<>();

	protected final ArmaControl myControl;
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final ArmaResolution resolution;
	private final ValueObserver<SVColor> globalBackgroundColorObserver;

	private final UpdateListenerGroup<Resolution> resolutionUpdateGroup = new UpdateListenerGroup<>();
	private final NotNullValueObserver<Boolean> enabledObserver = new NotNullValueObserver<>(isEnabled());
	private final NotNullValueObserver<Boolean> visibleObserver = new NotNullValueObserver<>(true);

	protected final Env env;
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

	public ArmaControlRenderer(@NotNull ArmaControl control, @NotNull ArmaResolution resolution, @NotNull Env env) {
		this.resolution = resolution;
		this.env = env;
		this.myControl = control;
		globalBackgroundColorObserver = new ValueObserver<>(new SVColorArray(ColorUtil.toARGB(Color.BLACK)));
		globalBackgroundColorObserver.addListener(new ValueListener<>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SVColor> observer, SVColor oldValue, SVColor newValue) {
				if (newValue != null) {
					//					setBackgroundColor(newValue.toJavaFXColor());
				} else {
					//					setBackgroundColor(Color.TRANSPARENT);
				}
				requestRender();
			}
		});

		/*
		xProperty = control.createPropertyProxy(ConfigPropertyLookup.X, new SVExpression("0", env));
		yProperty = control.createPropertyProxy(ConfigPropertyLookup.Y, new SVExpression("0", env));
		wProperty = control.createPropertyProxy(ConfigPropertyLookup.W, new SVExpression("1", env));
		hProperty = control.createPropertyProxy(ConfigPropertyLookup.H, new SVExpression("1", env));

		final NotNullValueListener<SerializableValue> positionValueListener = new NotNullValueListener<>() {
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
*/
		enabledObserver.addListener((ob, od, nw) -> requestRender());

	}

	@NotNull
	public Env getEnv() {
		return env;
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
	 Return true if should paint in preview form, false if should paint in editor form.
	 */
	public boolean paintPreview() {
		return false;
	}


	@Override
	public @NotNull Bounds getBounds() {
		return myControl.getBounds();
	}

	@Override
	public boolean isEnabled() {
		return enabledObserver.getValue();
	}

	@Override
	public void setEnabled(boolean enabled) {
		enabledObserver.updateValue(enabled);
	}

	@Override
	public boolean isGhost() {
		return enabledObserver.getValue() && visibleObserver.getValue();
	}

	@NotNull
	public NotNullValueObserver<Boolean> getEnabledObserver() {
		return enabledObserver;
	}

	@NotNull
	public NotNullValueObserver<Boolean> getVisibleObserver() {
		return visibleObserver;
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
	public void setGhost(boolean ghost) {
		enabledObserver.updateValue(false);
		visibleObserver.updateValue(false);
	}

	@Override
	public @NotNull UpdateListenerGroup<UpdateListenerGroup.NoData> getRenderUpdateGroup() {
		return null;
	}

	@Override
	public @Nullable Border getBorder() {
		return null;
	}

	@Override
	public @NotNull Color getBackgroundColor() {
		return null;
	}

	@Override
	public void paint(@NotNull Graphics g, @NotNull RenderMode mode) {

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
	public void addValueListener(@NotNull ConfigPropertyKey key, @NotNull SerializableValue valueWhenAbsent,
								 @NotNull NotNullValueListener<SerializableValue> listener) {
		addValueListener(myControl, key.getPropertyName(), valueWhenAbsent, listener);
	}

	/**
	 Adds a value listener to {@link #myControl} with the provided property name.
	 Afterwards, it invokes the listener with the current value.
	 */
	public void addValueListener(@NotNull String propertyName, @NotNull SerializableValue valueWhenAbsent,
								 @NotNull NotNullValueListener<SerializableValue> listener) {
		addValueListener(myControl, propertyName, valueWhenAbsent, listener);
	}

	/**
	 Adds a value listener to the given {@link ConfigClass} with the provided property name.
	 Afterwards, it invokes the listener with the current value.
	 */
	public void addValueListener(@NotNull ConfigClass owner, @NotNull String propertyName,
								 @NotNull SerializableValue valueWhenAbsent, @NotNull NotNullValueListener<SerializableValue> l) {
		ConfigPropertyProxy property = owner.createPropertyProxy(propertyName, valueWhenAbsent);
		property.addValueListener(l);
		l.valueUpdated(property.getValueObserver(), property.getValue(), property.getValue());
	}

	/**
	 Adds a value listener to the given {@link ConfigClass} with the provided key.
	 Invokes {@link #addValueListener(ConfigClass, String, SerializableValue, NotNullValueListener)} using {@link ConfigPropertyKey#getPropertyName()}
	 */
	public void addValueListener(@NotNull ConfigClass owner, @NotNull ConfigPropertyKey key,
								 @NotNull SerializableValue valueWhenAbsent, @NotNull NotNullValueListener<SerializableValue> l) {
		addValueListener(owner, key.getPropertyName(), valueWhenAbsent, l);
	}

	/**
	 Used by Arma Preview to let this renderer know that the user's mouse is over the control in the preview.
	 When this control is requested to be rendered in preview mode ({@link #paintPreview()} returns true),
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
	 @see Graphics#paintCheckerboard(int, int, int, int, Color, Color, int)
	 */
	public static void paintCheckerboard(@NotNull Graphics g, int x, int y, int w, int h, @NotNull Color color1, @NotNull Color color2) {
		g.paintCheckerboard(x, y, w, h, color1, color2, 10);
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

	 @param g graphics
	 */
	public void paintTextureError(@NotNull Graphics g, int x, int y, int w, int h) {
		paintError(g, Color.DEEPPINK, x, y, w, h);
	}

	/**
	 Use this when an image couldn't be determined for background

	 @param g graphics
	 */
	public void paintImageError(@NotNull Graphics g, int x, int y, int w, int h) {
		paintError(g, Color.RED, x, y, w, h);
	}

	public static void paintImageLoading(@NotNull Graphics g, @NotNull Paint bgColor, int x1, int y1, int x2, int y2) {
		g.setStroke(bgColor);
		g.fillRectangle(x1, y1, x2, y2);
		g.setStroke(Color.MAGENTA);
		g.strokeLine(x1, y1, x2, y2);
		g.strokeLine(x2, y1, x1, y2);
	}

	/**
	 Use this when a background color array couldn't be determined

	 @param g graphics
	 */
	public void paintBackgroundColorError(@NotNull Graphics g, int x, int y, int w, int h) {
		paintError(g, Color.BLACK, x, y, w, h);
	}

	/**
	 Invokes {@link #paintCheckerboard(Graphics, int, int, int, int, Color, Color)}
	 with the provided color and {@link #getBackgroundColor()} as the checkerboard colors
	 */
	public void paintError(@NotNull Graphics g, @NotNull Color color, int x, int y, int w, int h) {
		paintCheckerboard(g, x, y, w, h, color, getBackgroundColor());
	}


	@Override
	public void invalidate() {

	}

}
