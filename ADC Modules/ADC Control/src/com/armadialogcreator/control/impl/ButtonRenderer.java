package com.armadialogcreator.control.impl;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.utility.AlternatorHelper;
import com.armadialogcreator.control.impl.utility.BasicTextRenderer;
import com.armadialogcreator.control.impl.utility.BlinkControlHandler;
import com.armadialogcreator.control.impl.utility.TooltipRenderer;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.sv.SVBoolean;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVNull;
import com.armadialogcreator.core.sv.SVNumericValue;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.AColorConstant;
import com.armadialogcreator.util.ColorUtil;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 @author Kayler
 @since 11/21/2016 */
public class ButtonRenderer extends ArmaControlRenderer implements BasicTextRenderer.UpdateCallback {
	private BasicTextRenderer textRenderer;
	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;
	private final Consumer<Graphics> tooltipRenderFunc = g -> {
		tooltipRenderer.paint(g, this.mouseOverX, this.mouseOverY);
	};

	private double offsetX, offsetY, borderSize, offsetPressedX, offsetPressedY;

	/** color of drop shadow behind button */
	private int colorShadow = AColorConstant.BLACK.argb;
	/** bg color when mouse is over control */
	private int colorBackgroundActive = AColorConstant.BLACK.argb;
	/** bg color when control is disabled */
	private int colorBackgroundDisabled = AColorConstant.BLACK.argb;
	/** color of left border */
	private int colorBorder = AColorConstant.BLACK.argb;
	/** text color if control is disabled */
	private Color colorDisabled = Color.BLACK;
	private int colorFocused = AColorConstant.BLACK.argb, colorFocused2 = AColorConstant.BLACK.argb;

	/**
	 alternating bg color helper. if control has focus (but mouse isn't over control), colorFocused and
	 colorFocused2 will alternate
	 */
	private final AlternatorHelper focusedColorAlternator = new AlternatorHelper(500);

	public ButtonRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this, ConfigPropertyLookup.TEXT,
				ConfigPropertyLookup.COLOR_TEXT, ConfigPropertyLookup.STYLE, ConfigPropertyLookup.SIZE_EX,
				ConfigPropertyLookup.SHADOW, this
		);

		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						getBackgroundColorObserver().updateValue((SVColor) newValue);
					}
				});

		addValueListener(ConfigPropertyLookup.COLOR_SHADOW, SVNull.instance, (observer,
																			  oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorShadow = ((SVColor) newValue).toARGB();
				requestRender();
			}
		});

		addValueListener(ConfigPropertyLookup.OFFSET_X, SVNull.instance, (observer, oldValue,
																		  newValue) -> {
			if (newValue instanceof SVNumericValue) {
				offsetX = ((SVNumericValue) newValue).toDouble();
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.OFFSET_Y, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVNumericValue) {
						offsetY = ((SVNumericValue) newValue).toDouble();
						requestRender();
					}
				}
		);
		addValueListener(ConfigPropertyLookup.OFFSET_PRESSED_X, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVNumericValue) {
						offsetPressedX = ((SVNumericValue) newValue).toDouble();
						requestRender();
					}
				}
		);
		addValueListener(ConfigPropertyLookup.OFFSET_PRESSED_Y, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVNumericValue) {
						offsetPressedY = ((SVNumericValue) newValue).toDouble();
						requestRender();
					}
				}
		);
		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND_ACTIVE, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						colorBackgroundActive = ((SVColor) newValue).toARGB();
						requestRender();
					}
				}
		);
		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND_DISABLED, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						colorBackgroundDisabled = ((SVColor) newValue).toARGB();
						requestRender();
					}
				}
		);
		addValueListener(ConfigPropertyLookup.COLOR_FOCUSED, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						colorFocused = ((SVColor) newValue).toARGB();
						requestRender();
					}
				}
		);
		addValueListener(ConfigPropertyLookup.COLOR_FOCUSED2, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue == SVNull.instance) {
						colorFocused2 = -1;
						requestRender();
						return;
					}
					if (newValue instanceof SVColor) {
						colorFocused2 = ((SVColor) newValue).toARGB();
						requestRender();
					}
				}
		);
		addValueListener(ConfigPropertyLookup.DEFAULT, SVBoolean.FALSE,
				(observer, oldValue, newValue) -> {
					requestFocus = newValue instanceof SVBoolean && ((SVBoolean) newValue).isTrue();
					requestRender();
				}
		);
		addValueListener(ConfigPropertyLookup.BORDER_SIZE, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVNumericValue) {
						borderSize = ((SVNumericValue) newValue).toDouble();
					} else {
						borderSize = -1;
					}
					requestRender();
				}
		);
		addValueListener(ConfigPropertyLookup.COLOR_BORDER, SVNull.instance,
				(observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						colorBorder = ((SVColor) newValue).toARGB();
						requestRender();
					}
				}
		);
		blinkControlHandler = new BlinkControlHandler(this, ConfigPropertyLookup.BLINKING_PERIOD);

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
				ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
				ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
				ConfigPropertyLookup.TOOLTIP
		);

		requestRender();
	}

	@Override
	public void paint(@NotNull Graphics g) {
		boolean preview = paintPreview();

		final int controlWidth = getWidth();
		final int controlHeight = getHeight();

		if (isEnabled()) {
			if (preview) {
				blinkControlHandler.paint(g);
			}
			//won't draw shadow if not enabled
			int old = g.getStroke();
			g.setStroke(colorShadow);
			int w = (int) Math.round(controlWidth * offsetX);
			int h = (int) Math.round(controlHeight * offsetY);
			g.fillRectangle(x1 + w, y1 + h, getWidth(), getHeight());
			g.setStroke(old);
		}

		if (borderSize > 0) {
			final int trim = 1;
			int borderWidth = (int) Math.round(controlWidth * borderSize);
			int borderX = this.x1 - borderWidth;

			int old = g.getStroke();
			g.setStroke(colorBorder);
			g.fillRectangle(borderX, this.y1 + trim, borderWidth, getHeight() - trim);
			g.setStroke(old);
		}

		if (preview) {
			int oldBgColor = this.backgroundColorARGB;
			Color oldTextColor = textRenderer.getTextColor();
			if (!this.isEnabled()) {
				//set background color to the disabled color
				setBackgroundColor(colorBackgroundDisabled);
				textRenderer.setTextColor(colorDisabled);
				super.paint(g);
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
				super.paint(g);

				this.x1 = oldX1;
				this.x2 = oldX2;
				this.y1 = oldY1;
				this.y2 = oldY2;
			} else if (this.mouseOver) {
				//if the mouse is over this control, set the background color to backgroundColorActive
				setBackgroundColor(colorBackgroundActive);
				super.paint(g);
			} else if (focused) {
				double ratio = focusedColorAlternator.updateAndGetRatio();
				setBackgroundColor(
						ColorUtil.interpolate(colorFocused, colorFocused2 == -1 ? backgroundColorARGB : colorFocused2, ratio)
				);
				super.paint(g);
			} else {
				super.paint(g);
			}
			textRenderer.paint(g);

			//reset the colors again
			setBackgroundColor(oldBgColor);
			textRenderer.setTextColor(oldTextColor);
		} else {
			super.paint(g);
			textRenderer.paint(g);
		}

		if (preview && this.mouseOver) {
			g.paintLast(tooltipRenderFunc);
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
