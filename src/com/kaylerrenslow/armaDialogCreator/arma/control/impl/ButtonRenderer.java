package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.AlternatorHelper;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BasicTextRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TooltipRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Region;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 @author Kayler
 @since 11/21/2016 */
public class ButtonRenderer extends ArmaControlRenderer {
	private BasicTextRenderer textRenderer;
	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;
	private Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};

	private double offsetX, offsetY, borderSize, offsetPressedX, offsetPressedY;

	/** color of drop shadow behind button */
	private Color colorShadow = Color.BLACK;
	/** bg color when mouse is over control */
	private Color colorBackgroundActive = Color.BLACK;
	/** bg color when control is disabled */
	private Color colorBackgroundDisabled = Color.BLACK;
	/** color of left border */
	private Color colorBorder = Color.BLACK;
	/** text color if control is disabled */
	private Color colorDisabled = Color.BLACK;
	private Color colorFocused = Color.BLACK, colorFocused2 = Color.BLACK;

	/**
	 alternating bg color helper. if control has focus (but mouse isn't over control), colorFocused and
	 colorFocused2 will alternate
	 */
	private final AlternatorHelper<Color> focusedColorAlternator = new AlternatorHelper<>(500);

	public ButtonRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this, ControlPropertyLookup.TEXT,
				ControlPropertyLookup.COLOR_TEXT, ControlPropertyLookup.STYLE, ControlPropertyLookup.SIZE_EX,
				ControlPropertyLookup.SHADOW
		);

		{
			ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND);
			addValueListener(colorBackground.getPropertyLookup(), (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
				}
			});
			colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));
		}

		addValueListener(ControlPropertyLookup.COLOR_SHADOW, (observer,
															  oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorShadow = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		addValueListener(ControlPropertyLookup.OFFSET_X, (observer, oldValue,
														  newValue) -> {
			if (newValue instanceof SVNumericValue) {
				offsetX = ((SVNumericValue) newValue).toDouble();
				requestRender();
			}
		});
		addValueListener(ControlPropertyLookup.OFFSET_Y,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVNumericValue) {
						offsetY = ((SVNumericValue) newValue).toDouble();
						requestRender();
					}
				}
		);
		addValueListener(ControlPropertyLookup.OFFSET_PRESSED_X,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVNumericValue) {
						offsetPressedX = ((SVNumericValue) newValue).toDouble();
						requestRender();
					}
				}
		);
		addValueListener(ControlPropertyLookup.OFFSET_PRESSED_Y,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVNumericValue) {
						offsetPressedY = ((SVNumericValue) newValue).toDouble();
						requestRender();
					}
				}
		);
		addValueListener(ControlPropertyLookup.COLOR_BACKGROUND_ACTIVE,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						colorBackgroundActive = ((SVColor) newValue).toJavaFXColor();
						requestRender();
					}
				}
		);
		addValueListener(ControlPropertyLookup.COLOR_BACKGROUND_DISABLED,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						colorBackgroundDisabled = ((SVColor) newValue).toJavaFXColor();
						requestRender();
					}
				}
		);
		addValueListener(ControlPropertyLookup.COLOR_FOCUSED,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						colorFocused = ((SVColor) newValue).toJavaFXColor();
						requestRender();
					}
				}
		);
		addValueListener(ControlPropertyLookup.COLOR_FOCUSED2,
				(observer, oldValue, newValue) -> {
					if (newValue == null) {
						colorFocused2 = null;
						requestRender();
						return;
					}
					if (newValue instanceof SVColor) {
						colorFocused2 = ((SVColor) newValue).toJavaFXColor();
						requestRender();
					}
				}
		);
		addValueListener(ControlPropertyLookup.DEFAULT,
				(observer, oldValue, newValue) -> {
					requestFocus = newValue instanceof SVBoolean && ((SVBoolean) newValue).isTrue();
					requestRender();
				}
		);
		addValueListener(ControlPropertyLookup.BORDER_SIZE,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVNumericValue) {
						borderSize = ((SVNumericValue) newValue).toDouble();
					} else {
						borderSize = -1;
					}
					requestRender();
				}
		);
		addValueListener(ControlPropertyLookup.COLOR_BORDER,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						colorBorder = ((SVColor) newValue).toJavaFXColor();
						requestRender();
					}
				}
		);
		blinkControlHandler = new BlinkControlHandler(this, ControlPropertyLookup.BLINKING_PERIOD);


		myControl.findProperty(ControlPropertyLookup.COLOR_TEXT).setValueIfAbsent(true, new SVColorArray(getTextColor()));
		myControl.findProperty(ControlPropertyLookup.TEXT).setValueIfAbsent(true, SVString.newEmptyString());


		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.TOOLTIP
		);

		requestRender();
	}

	@Override
	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		boolean preview = paintPreview(canvasContext);

		final int controlWidth = getWidth();
		final int controlHeight = getHeight();

		if (isEnabled()) {
			if (preview) {
				blinkControlHandler.paint(gc);
			}
			//won't draw shadow if not enabled
			Paint old = gc.getStroke();
			gc.setStroke(colorShadow);
			int w = (int) Math.round(controlWidth * offsetX);
			int h = (int) Math.round(controlHeight * offsetY);
			Region.fillRectangle(gc, x1 + w, y1 + h, x2 + w, y2 + h);
			gc.setStroke(old);
		}

		if (borderSize > 0) {
			final int trim = 1;
			int borderWidth = (int) Math.round(controlWidth * borderSize);
			int borderX = this.x1 - borderWidth;

			Paint old = gc.getStroke();
			gc.setStroke(colorBorder);
			Region.fillRectangle(gc, borderX, this.y1 + trim, borderX + borderWidth, y2 - trim);
			gc.setStroke(old);
		}

		if (preview) {
			Color oldBgColor = this.backgroundColor;
			Color oldTextColor = textRenderer.getTextColor();
			if (!this.isEnabled()) {
				//set background color to the disabled color
				setBackgroundColor(colorBackgroundDisabled);
				textRenderer.setTextColor(colorDisabled);
				super.paint(gc, canvasContext);
			} else if (mouseButtonDown == MouseButton.PRIMARY) {
				int oldX1 = this.x1;
				int oldX2 = this.x2;
				int oldY1 = this.y1;
				int oldY2 = this.y2;

				this.x1 = (int) (oldX1 + Math.round(controlWidth * offsetPressedX));
				this.y1 = (int) (oldY1 + Math.round(controlHeight * offsetPressedY));
				this.x2 = x1 + controlWidth;
				this.y2 = y1 + controlHeight;

				setBackgroundColor(colorBackgroundActive);
				super.paint(gc, canvasContext);

				this.x1 = oldX1;
				this.x2 = oldX2;
				this.y1 = oldY1;
				this.y2 = oldY2;
			} else if (this.mouseOver) {
				//if the mouse is over this control, set the background color to backgroundColorActive
				setBackgroundColor(colorBackgroundActive);
				super.paint(gc, canvasContext);
			} else if (focused) {
				double ratio = focusedColorAlternator.updateAndGetRatio();
				setBackgroundColor(
						colorFocused.interpolate(colorFocused2 == null ? backgroundColor : colorFocused2, ratio)
				);
				super.paint(gc, canvasContext);
			} else {
				super.paint(gc, canvasContext);
			}
			textRenderer.paint(gc);

			//reset the colors again
			setBackgroundColor(oldBgColor);
			textRenderer.setTextColor(oldTextColor);
		} else {
			super.paint(gc, canvasContext);
			textRenderer.paint(gc);
		}

		if (preview && this.mouseOver) {
			canvasContext.paintLast(tooltipRenderFunc);
		}

	}

	@NotNull
	public Color getTextColor() {
		return textRenderer.getTextColor();
	}

	@Override
	public boolean canHaveFocus() {
		return true;
	}

}
