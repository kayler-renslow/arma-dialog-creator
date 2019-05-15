package com.armadialogcreator.control.impl;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.utility.*;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigPropertyKey;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.sv.SVBoolean;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVNull;
import com.armadialogcreator.core.sv.SVNumericValue;
import com.armadialogcreator.expression.Env;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 @author Kayler
 @since 11/21/2016 */
public class ShortcutButtonRenderer extends ArmaControlRenderer implements BasicTextRenderer.UpdateCallback {
	private BasicTextRenderer textRenderer;
	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;
	private Consumer<Graphics> tooltipRenderFunc = g -> {
		tooltipRenderer.paint(g, this.mouseOverX, this.mouseOverY);
	};

	private final ImageOrTextureHelper animTextureNormal = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper animTextureDisabled = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper animTextureOver = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper animTexturePressed = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper animTextureFocused = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper animTextureDefault = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper textureNoShortcut = new ImageOrTextureHelper(this);

	/** secondary text color (text color alternates between "color" and "color2") */
	private Color color2 = Color.BLACK;
	private Color colorFocused = Color.BLACK;
	private Color colorDisabled = Color.BLACK;
	private Color colorBackgroundFocused = Color.BLACK;
	private Color colorBackground2 = Color.BLACK;
	private long periodFocusMillis = 500;
	private long periodOverMillis = 500;

	private double hitZone_top = 0;
	private double hitZone_right = 0;
	private double hitZone_bottom = 0;
	private double hitZone_left = 0;

	private double shortcutPos_top = 0;
	private double shortcutPos_left = 0;
	private double shortcutPos_w = 0;
	private double shortcutPos_h = 0;

	private double textPos_top = 0;
	private double textPos_right = 0;
	private double textPos_bottom = 0;
	private double textPos_left = 0;

	/**
	 Alternating color helper. if control has focus (but mouse isn't over control).
	 There is only one because both the alternating text color and alternating bg colors are synced
	 */
	private final AlternatorHelper focusedColorAlternator = new AlternatorHelper(500);

	public ShortcutButtonRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this, ConfigPropertyLookup.TEXT,
				ConfigPropertyLookup.COLOR, ConfigPropertyLookup.STYLE, ConfigPropertyLookup.SIZE,
				ConfigPropertyLookup.SHADOW, this
		);

		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				getBackgroundColorObserver().updateValue((SVColor) newValue);
			}
		});

		attachPicOrTexPropertyListener(ConfigPropertyLookup.ANIM_TEXTURE_NORMAL, animTextureNormal);
		attachPicOrTexPropertyListener(ConfigPropertyLookup.ANIM_TEXTURE_DISABLED, animTextureDisabled);
		attachPicOrTexPropertyListener(ConfigPropertyLookup.ANIM_TEXTURE_OVER, animTextureOver);
		attachPicOrTexPropertyListener(ConfigPropertyLookup.ANIM_TEXTURE_PRESSED, animTexturePressed);
		attachPicOrTexPropertyListener(ConfigPropertyLookup.ANIM_TEXTURE_FOCUSED, animTextureFocused);
		attachPicOrTexPropertyListener(ConfigPropertyLookup.ANIM_TEXTURE_DEFAULT, animTextureDefault);
		attachPicOrTexPropertyListener(ConfigPropertyLookup.TEXTURE_NO_SHORTCUT, textureNoShortcut);

		addValueListener(ConfigPropertyLookup.DEFAULT, SVBoolean.FALSE, (observer, oldValue, newValue) -> {
					requestFocus = newValue instanceof SVBoolean && ((SVBoolean) newValue).isTrue();
					requestRender();
				}
		);

		addValueListener(ConfigPropertyLookup.COLOR2, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				color2 = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.COLOR_FOCUSED, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorFocused = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.COLOR_DISABLED, SVNull.instance, (observer,
																				oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorDisabled = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND_FOCUSED, SVNull.instance, (observer,
																						  oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorBackgroundFocused = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND2, SVNull.instance, (observer,
																				   oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorBackground2 = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.PERIOD_FOCUS, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVNumericValue) {
				periodFocusMillis = Math.round(((SVNumericValue) newValue).toDouble() * 1000);
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.PERIOD_OVER, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVNumericValue) {
				periodOverMillis = Math.round(((SVNumericValue) newValue).toDouble() * 1000);
				requestRender();
			}
		});

		blinkControlHandler = new BlinkControlHandler(this, ConfigPropertyLookup.BLINKING_PERIOD);

		textRenderer.setTextColor(getTextColor());
		textRenderer.setText("");

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
				ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
				ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
				ConfigPropertyLookup.TOOLTIP
		);

		//nested classes
		ConfigClass hitZone = myControl.findNestedClass(HitZoneControlSpec.CLASS_NAME);
		{
			addValueListener(hitZone, ConfigPropertyLookup.TOP, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					hitZone_top = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(hitZone, ConfigPropertyLookup.RIGHT, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					hitZone_right = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(hitZone, ConfigPropertyLookup.BOTTOM, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					hitZone_bottom = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(hitZone, ConfigPropertyLookup.LEFT, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					hitZone_left = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
		}

		ConfigClass textPos = myControl.findNestedClass(TextPosControlSpec.CLASS_NAME);
		{
			addValueListener(textPos, ConfigPropertyLookup.TOP, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					textPos_top = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(textPos, ConfigPropertyLookup.RIGHT, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					textPos_right = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(textPos, ConfigPropertyLookup.BOTTOM, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					textPos_bottom = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(textPos, ConfigPropertyLookup.LEFT, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					textPos_left = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
		}

		ConfigClass shortcutPos = myControl.findNestedClass(ShortcutPosControlSpec.CLASS_NAME);
		{
			addValueListener(shortcutPos, ConfigPropertyLookup.TOP, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					shortcutPos_top = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(shortcutPos, ConfigPropertyLookup.LEFT, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					shortcutPos_left = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(shortcutPos, ConfigPropertyLookup.W, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					shortcutPos_w = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
			addValueListener(shortcutPos, ConfigPropertyLookup.H, SVNull.instance, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					shortcutPos_h = ((SVNumericValue) newValue).toDouble();
					requestRender();
				}
			});
		}

		requestRender();
	}

	private void attachPicOrTexPropertyListener(ConfigPropertyKey lookup, ImageOrTextureHelper helper) {
		addValueListener(lookup, SVNull.instance, (observer, oldValue, newValue) -> {
			helper.updateAsync(newValue);
		});
	}

	@Override
	public void paint(@NotNull Graphics g) {
		boolean preview = paintPreview();

		final int controlWidth = getWidth();
		final int controlHeight = getHeight();
		ImageOrTextureHelper bgTexture = animTextureNormal;

		int textPosX = x1 + (int) (Math.round(controlWidth * textPos_left));
		int textPosY = y1 + (int) (Math.round(controlHeight * textPos_top));

		if (preview) {
			if (isEnabled()) {
				blinkControlHandler.paint(g);
			}

			double ratio = focusedColorAlternator.updateAndGetRatio();
			Color colorBackground = this.backgroundColor;
			Color color = textRenderer.getTextColor();

			if (!isEnabled()) {
				//button is disabled
				bgTexture = animTextureDisabled;
				textRenderer.setTextColor(colorDisabled);
			} else if (mouseButtonDown == MouseButton.PRIMARY) {
				//button is being clicked
				bgTexture = animTexturePressed;
				//background color remains as "colorBackground" property value
				//text color remains as "color" property value
			} else if (mouseOver) {
				//mouse is over the button
				bgTexture = animTextureOver;
				//interpolate "color" with "colorFocused"
				if (periodOverMillis > 0) {
					textRenderer.setTextColor(colorFocused.interpolate(color2, ratio));
					setBackgroundColor(colorBackgroundFocused.interpolate(colorBackground2, ratio));
				}
				//interpolate "colorBackgroundFocused" with "colorBackground2"
				focusedColorAlternator.setAlternateMillis(periodOverMillis);
			} else if (focused) {
				bgTexture = animTextureFocused;
				if (periodFocusMillis > 0) {
					textRenderer.setTextColor(color2.interpolate(colorFocused, ratio));
					setBackgroundColor(colorBackground2.interpolate(colorBackgroundFocused, ratio));
				}
				focusedColorAlternator.setAlternateMillis(periodFocusMillis);
			}

			//paint the background texture/image
			switch (bgTexture.getMode()) {
				case Image: {
					// In arma 3, they do some weird as shit for manipulating the background texture.
					// Currently (July 2017), I don't know how they are doing it. So, I'll just stretch the image to
					// width of the control.
					Image image = bgTexture.getImage();
					if (image == null) {
						throw new IllegalStateException();
					}
					g.drawImage(image, x1, y1, controlWidth, controlHeight);
					g.setGlobalBlendMode(BlendMode.MULTIPLY);
					super.paint(g);
					g.setGlobalBlendMode(BlendMode.SRC_OVER);
					break;
				}
				case ImageError: {
					paintImageError(g, x1, y1, controlWidth, controlHeight);
					break;
				}
				case LoadingImage: {
					super.paint(g);
					break;
				}
				case Texture: {
					TexturePainter.paint(g, bgTexture.getTexture(), getBackgroundColor(), x1, y1, x2, y2);
					break;
				}
				case TextureError: {
					paintTextureError(g, x1, y1, controlWidth, controlHeight);
					break;
				}
			}

			paintShortcutThing(g);

			textRenderer.paint(g, textPosX, textPosY);

			//reset the colors again
			setBackgroundColor(colorBackground);
			textRenderer.setTextColor(color);

			if (this.mouseOver) {
				g.paintLast(tooltipRenderFunc);
			}
		} else {
			super.paint(g);
			paintShortcutThing(g);
			textRenderer.paint(g, textPosX, textPosY);
		}

	}

	private void paintShortcutThing(@NotNull Graphics g) {
		final int controlWidth = getWidth();
		final int controlHeight = getHeight();

		int x1 = (int) (this.x1 + Math.round(controlWidth * shortcutPos_left));
		int y1 = (int) (this.y1 + Math.round(controlHeight * shortcutPos_top));
		int x2 = (int) (this.x1 + Math.round(controlWidth * shortcutPos_w));
		int y2 = (int) (this.y1 + Math.round(controlHeight * shortcutPos_h));
		if (textureNoShortcut.getValue() != null && textureNoShortcut.getValue().toString().length() == 0) {
			//ignore the texture if defined but no text is entered
			return;
		}
		switch (textureNoShortcut.getMode()) {
			case Image: {
				g.drawImage(textureNoShortcut.getImage(), x1, y1, x2 - x1, y2 - y1);
				break;
			}
			case ImageError: {
				paintImageError(g, x1, y1, 30, 30);
				break;
			}
			case LoadingImage: {
				break;
			}
			case Texture: {
				TexturePainter.paint(g, textureNoShortcut.getTexture(), x1, y1, x2, y2);
				break;
			}
			case TextureError: {
				paintTextureError(g, x1, y1, x2 - x1, y2 - y1);
				break;
			}
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

	@Override
	public boolean containsPoint(int x, int y) {
		int controlWidth = getWidth();
		int controlHeight = getHeight();

		//how many pixels from left side that the mouse can't click on
		int leftCut = (int) (Math.round(controlWidth * this.hitZone_left));
		//how many pixels from right side that the mouse can't click on
		int rightCut = (int) (Math.round(controlWidth * this.hitZone_right));
		int topCut = (int) (Math.round(controlHeight * this.hitZone_top));
		int bottomCut = (int) (Math.round(controlHeight * this.hitZone_bottom));

		if (x1 + leftCut <= x && x2 - rightCut >= x) {
			if (y1 + topCut <= y && y2 - bottomCut >= y) {
				return true;
			}
		}
		return false;
	}
}
