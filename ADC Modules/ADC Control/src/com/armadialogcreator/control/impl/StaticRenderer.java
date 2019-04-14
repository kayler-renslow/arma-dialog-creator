package com.armadialogcreator.control.impl;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.Texture;
import com.armadialogcreator.control.impl.utility.*;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.expression.Env;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 A renderer for {@link StaticControl}

 @author Kayler
 @since 05/25/2016 */
public class StaticRenderer extends ArmaControlRenderer implements BasicTextRenderer.UpdateCallback {

	private enum RenderType {
		Text, ImageOrTexture, Line, Frame, Error
	}

	private BlinkControlHandler blinkControlHandler;
	private SerializableValue stylePropertyValue;
	private SerializableValue textPropertyValue;

	private BasicTextRenderer textRenderer;
	private TooltipRenderer tooltipRenderer;

	private final Consumer<Graphics> tooltipRenderFunc = g -> {
		tooltipRenderer.paint(g, this.mouseOverX, this.mouseOverY);
	};

	private final ImageOrTextureHelper pictureOrTextureHelper;
	private RenderType renderType = RenderType.Text;
	private RenderType renderTypeForStyle = RenderType.Error;
	private boolean keepImageAspectRatio = false, tileImage = false;
	private int tileW = 0, tileH = 0;

	private final TintedImageHelperRenderer tintedImage = new TintedImageHelperRenderer();

	public StaticRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		pictureOrTextureHelper = new ImageOrTextureHelper(this);
		textRenderer = new BasicTextRenderer(control, this,
				ConfigPropertyLookup.TEXT, ConfigPropertyLookup.COLOR_TEXT,
				ConfigPropertyLookup.STYLE, ConfigPropertyLookup.SIZE_EX,
				ConfigPropertyLookup.SHADOW, this
		);

		textRenderer.setAllowMultiLine(true);

		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				getBackgroundColorObserver().updateValue((SVColor) newValue);
			}
		});

		textRenderer.setTextColor(getTextColor());

		addValueListener(ConfigPropertyLookup.TILE_H, SVNull.instance, (observer, oldValue,
																		newValue) -> {
			if (newValue instanceof SVExpression) {
				SVExpression expr = (SVExpression) newValue;
				tileH = (int) expr.getNumVal();
				updateTintedImage();
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.TILE_W, SVNull.instance, (observer, oldValue,
																		newValue) -> {
			if (newValue instanceof SVExpression) {
				SVExpression expr = (SVExpression) newValue;
				tileW = (int) expr.getNumVal();
				updateTintedImage();
				requestRender();
			}
		});

		textRenderer.setText("");

		blinkControlHandler = new BlinkControlHandler(this, ConfigPropertyLookup.BLINKING_PERIOD);

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
				ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
				ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
				ConfigPropertyLookup.TOOLTIP
		);

		renderTypeForStyle = getRenderTypeFromStyle();
		checkAndSetRenderType();
	}

	private void checkAndSetRenderType() {
		if (renderTypeForStyle == RenderType.ImageOrTexture) {
			pictureOrTextureHelper.updateAsync(textPropertyValue, mode -> {
				updateTintedImage();
				return null;
			});
		}
		renderType = renderTypeForStyle;

		requestRender();
	}

	public void paint(@NotNull Graphics g) {
		boolean preview = paintPreview();
		if (preview) {
			blinkControlHandler.paint(g);
		}

		switch (renderType) {
			case Text: {
				super.paint(g);
				textRenderer.paint(g);
				break;
			}
			case Line: {
				//draw line from top left of control to bottom right of control
				//the text color is the color of the line
				g.setStroke(getTextColor());
				g.strokeLine(getLeftX(), getTopY(), getRightX(), getBottomY());
				break;
			}
			case Frame: {
				g.setStroke(getTextColor());

				int controlWidth = getWidth();

				int textWidth = 0;
				double padding = controlWidth * .02;
				int xLeftOfText = (int) Math.round(x1 + padding);

				//draw the text, if the length is > 0
				if (textRenderer.getText().length() > 0) {
					textWidth = textRenderer.getTextWidth();
					if (textWidth < controlWidth - (2 * padding)) {
						//text will paint within the bounds of the frame
						textRenderer.paint(g, xLeftOfText, y1 - textRenderer.getTextLineHeight() / 2);
					} else {
						//don't paint any text if the text is longer than the frame's width
						textWidth = 0;
					}
				}


				//draw the frame itself

				//draw top line
				if (textWidth > 0) {
					//in Arma 3, the top line is crisp, while the other lines are blurred and 2 pixels in width
					// and the top line is crisp only when there is text
					g.strokeLine(x1, y1, xLeftOfText, y1);
					g.strokeLine(xLeftOfText + textWidth, y1, x2, y1);
				} else {
					g.strokeLine(x1, y1, x2, y1);
				}
				//+0.5 to make the line start crisp
				g.strokeLine(x2, y1, x2, y2); //right line
				g.strokeLine(x1, y2, x2, y2); //bottom line
				g.strokeLine(x1, y1, x1, y2); //left line

				break;
			}
			case ImageOrTexture: {
				switch (pictureOrTextureHelper.getMode()) {
					case Image: {
						Image imageToPaint = pictureOrTextureHelper.getImage();
						if (imageToPaint == null) {
							throw new IllegalStateException("imageToPaint is null");
						}
						tintedImage.paintTintedImage(g);
						break;
					}
					case Texture: {
						Texture texture = pictureOrTextureHelper.getTexture();
						if (texture == null) {
							throw new IllegalStateException("texture is null");
						}
						TexturePainter.paint(g, texture, getTextColor(), x1, y1, x2, y2);
						break;
					}
					case ImageError: {
						paintImageError(g, x1, y1, getWidth(), getHeight());
						break;
					}
					case TextureError: {
						paintTextureError(g, x1, y1, getWidth(), getHeight());
						break;
					}
					case LoadingImage: {
						paintImageLoading(g, getBackgroundColor(), x1, y1, x2, y2);
						break;
					}
				}

				break;
			}
			case Error: {
				paintBackgroundColorError(g, x1, y1, getWidth(), getHeight());
				break;
			}
			default: {
				throw new IllegalStateException("unhandled renderType:" + renderType);
			}
		}

		if (preview) {
			if (this.mouseOver) {
				g.paintLast(tooltipRenderFunc);
			}
		}
	}

	@Override
	protected void positionUpdate(boolean initializingPosition) {
		if (!initializingPosition) {
			updateTintedImage();
		}
	}

	@Override
	public void textColorUpdate(@Nullable SerializableValue newValue) {
		updateTintedImage();
	}

	@Override
	public void textUpdate(@Nullable SerializableValue newValue) {
		textPropertyValue = newValue;
		checkAndSetRenderType();
	}

	@Override
	public void styleUpdate(@Nullable SerializableValue newValue) {
		stylePropertyValue = newValue;
		newValue = MiscHelpers.getGroup(this.env, newValue, myControl);
		if (newValue != null) {
			SVControlStyleGroup group = (SVControlStyleGroup) newValue;
			keepImageAspectRatio = group.hasStyle(ControlStyle.KEEP_ASPECT_RATIO);
			tileImage = group.hasStyle(ControlStyle.TILE_PICTURE);

		}
		renderTypeForStyle = getRenderTypeFromStyle();
		checkAndSetRenderType();
	}

	private void updateTintedImage() {
		if (pictureOrTextureHelper.getImage() == null) {
			tintedImage.updateImage(null); //help garbage collection
			return;
		}
		Image img = pictureOrTextureHelper.getImage();
		Paint fill = getTextColor();
		int x, y, w, h;
		if (keepImageAspectRatio && !tileImage) {
			int imgWidth = (int) img.getWidth();
			int imgHeight = (int) img.getHeight();
			double aspectRatio = imgWidth * 1.0 / imgHeight;

			//We want to make sure that the image doesn't surpass the bounds of the control
			//while also maintaining the aspect ratio. In arma 3, the height of the image will
			//never surpass the height of the control. The width is allowed to surpass the bounds though.

			int drawHeight = getHeight();
			int drawWidth = (int) Math.round(drawHeight * aspectRatio);

			//after the image as been resized to aspect ratio, center the image
			x = getX1() + (getWidth() - drawWidth) / 2;


			y = y1;
			w = drawWidth;
			h = drawHeight;
		} else {
			x = x1;
			y = y1;
			w = getWidth();
			h = getHeight();
		}
		tintedImage.updateEffect(
				pictureOrTextureHelper.getImage(), getTextColor(), x, y, w, h, true
		);
		if (tileImage) {
			int tileW = Math.max(1, this.tileW);
			int tileH = Math.max(1, this.tileH);
			tintedImage.updateImageAsTiled(pictureOrTextureHelper.getImage(), getWidth() / tileW, getHeight() / tileH, true);
		}
	}

	@NotNull
	public Color getTextColor() {
		return textRenderer.getTextColor();
	}

	/**
	 @return the {@link RenderType} to use, or {@link #renderTypeForStyle}
	 if the style value didn't change
	 */
	@NotNull
	private RenderType getRenderTypeFromStyle() {
		SerializableValue value = stylePropertyValue;
		if (value == null) {
			return RenderType.Error;
		}
		value = MiscHelpers.getGroup(this.env, value, this.myControl);
		if (value != null) {
			SVControlStyleGroup group = (SVControlStyleGroup) value;
			for (ControlStyle style : group.getStyleArray()) {
				if (style == ControlStyle.TILE_PICTURE) {
					System.out.println("StaticRenderer.getRenderTypeFromStyle style=" + style);
					return RenderType.ImageOrTexture;
				}
				if (style == ControlStyle.PICTURE) {
					return RenderType.ImageOrTexture;
				}
				if (style == ControlStyle.LINE) {
					return RenderType.Line;
				}
				if (style == ControlStyle.FRAME) {
					return RenderType.Frame;
				}
			}
		}
		return RenderType.Text;
	}

}
