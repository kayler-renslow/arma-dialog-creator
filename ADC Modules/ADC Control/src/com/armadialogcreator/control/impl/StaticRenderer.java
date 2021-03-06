package com.armadialogcreator.control.impl;

import com.armadialogcreator.canvas.CanvasContext;
import com.armadialogcreator.canvas.Region;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.Texture;
import com.armadialogcreator.control.impl.utility.*;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.expression.Env;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

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

	private final Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};

	private final ImageOrTextureHelper pictureOrTextureHelper;
	private RenderType renderType = RenderType.Text;
	private RenderType renderTypeForStyle = RenderType.Error;
	private boolean keepImageAspectRatio = false, tileImage = false;
	private int tileW = 0, tileH = 0;

	private final TintedImageHelperRenderer tintedImage = new TintedImageHelperRenderer();
	;

	public StaticRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		pictureOrTextureHelper = new ImageOrTextureHelper(this);
		textRenderer = new BasicTextRenderer(control, this,
				ConfigPropertyLookup.TEXT, ConfigPropertyLookup.COLOR_TEXT,
				ConfigPropertyLookup.STYLE, ConfigPropertyLookup.SIZE_EX,
				ConfigPropertyLookup.SHADOW, true, this
		);

		textRenderer.setAllowMultiLine(true);

		{
			ConfigProperty colorBackground = myControl.findProperty(ConfigPropertyLookup.COLOR_BACKGROUND);
			addValueListener(colorBackground.getName(), (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
				}
			});
			colorBackground.setValue(new SVColorArray(getBackgroundColor()));
		}

		myControl.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(new SVColorArray(getTextColor()));

		myControl.findProperty(ConfigPropertyLookup.TILE_H).getValueObserver().addListener((observer, oldValue,
																							newValue) -> {
			if (newValue instanceof SVExpression) {
				SVExpression expr = (SVExpression) newValue;
				tileH = (int) expr.getNumVal();
				updateTintedImage();
				requestRender();
			}
		});
		myControl.findProperty(ConfigPropertyLookup.TILE_W).getValueObserver().addListener((observer, oldValue,
																							newValue) -> {
			if (newValue instanceof SVExpression) {
				SVExpression expr = (SVExpression) newValue;
				tileW = (int) expr.getNumVal();
				updateTintedImage();
				requestRender();
			}
		});

		myControl.findProperty(ConfigPropertyLookup.TEXT).setValue(SVString.newEmptyString());


		myControl.findProperty(ConfigPropertyLookup.FONT).setValue(SVFont.DEFAULT);
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
		switch (renderTypeForStyle) {
			case ImageOrTexture: {
				pictureOrTextureHelper.updateAsync(textPropertyValue, mode -> {
					updateTintedImage();
					return null;
				});
			}
		}
		renderType = renderTypeForStyle;

		requestRender();
	}

	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		boolean preview = paintPreview(canvasContext);
		if (preview) {
			blinkControlHandler.paint(gc);
		}

		switch (renderType) {
			case Text: {
				super.paint(gc, canvasContext);
				textRenderer.paint(gc);
				break;
			}
			case Line: {
				//draw line from top left of control to bottom right of control
				//the text color is the color of the line
				gc.setStroke(getTextColor());
				gc.strokeLine(getLeftX(), getTopY(), getRightX(), getBottomY());
				break;
			}
			case Frame: {
				gc.setStroke(getTextColor());

				int controlWidth = getWidth();

				int textWidth = 0;
				double padding = controlWidth * .02;
				int xLeftOfText = (int) Math.round(x1 + padding);

				//draw the text, if the length is > 0
				if (textRenderer.getText().length() > 0) {
					textWidth = textRenderer.getTextWidth();
					if (textWidth < controlWidth - (2 * padding)) {
						//text will paint within the bounds of the frame
						textRenderer.paint(gc, xLeftOfText, y1 - textRenderer.getTextLineHeight() / 2);
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
					Region.strokeLine(gc, x1, y1, xLeftOfText, y1);
					Region.strokeLine(gc, xLeftOfText + textWidth, y1, x2, y1);
				} else {
					gc.strokeLine(x1, y1, x2, y1);
				}
				//+0.5 to make the line start crisp
				gc.strokeLine(x2, y1 + 0.5, x2, y2); //right line
				gc.strokeLine(x1, y2, x2, y2); //bottom line
				gc.strokeLine(x1, y1 + 0.5, x1, y2); //left line

				break;
			}
			case ImageOrTexture: {
				switch (pictureOrTextureHelper.getMode()) {
					case Image: {
						Image imageToPaint = pictureOrTextureHelper.getImage();
						if (imageToPaint == null) {
							throw new IllegalStateException("imageToPaint is null");
						}
						tintedImage.paintTintedImage(gc);
						break;
					}
					case Texture: {
						Texture texture = pictureOrTextureHelper.getTexture();
						if (texture == null) {
							throw new IllegalStateException("texture is null");
						}
						TexturePainter.paint(gc, texture, getTextColor(), x1, y1, x2, y2);
						break;
					}
					case ImageError: {
						paintImageError(gc, x1, y1, getWidth(), getHeight());
						break;
					}
					case TextureError: {
						paintTextureError(gc, x1, y1, getWidth(), getHeight());
						break;
					}
					case LoadingImage: {
						paintImageLoading(gc, backgroundColor, x1, y1, x2, y2);
						break;
					}
				}

				break;
			}
			case Error: {
				paintBackgroundColorError(gc, x1, y1, getWidth(), getHeight());
				break;
			}
			default: {
				throw new IllegalStateException("unhandled renderType:" + renderType);
			}
		}

		if (preview) {
			if (this.mouseOver) {
				canvasContext.paintLast(tooltipRenderFunc);
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
