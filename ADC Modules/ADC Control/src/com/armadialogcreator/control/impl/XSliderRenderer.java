package com.armadialogcreator.control.impl;

import com.armadialogcreator.canvas.CanvasContext;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.utility.*;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVColorArray;
import com.armadialogcreator.expression.Env;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 A renderer for {@link XSliderControl}

 @author Kayler
 @since 07/21/2017 */
public class XSliderRenderer extends ArmaControlRenderer {

	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;
	private Color colorActive = Color.WHITE;
	private final ImageOrTextureHelper arrowEmpty = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper arrowFull = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper border = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper thumb = new ImageOrTextureHelper(this);

	private final TintedImageHelperRenderer tintedLeftArrow = new TintedImageHelperRenderer();
	private final TintedImageHelperRenderer tintedRightArrow = new TintedImageHelperRenderer();
	private final TintedImageHelperRenderer tintedBorder = new TintedImageHelperRenderer();
	private final TintedImageHelperRenderer tintedThumb = new TintedImageHelperRenderer();

	private double progress = 0.7;

	private final Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};
	private int arrowLeftX, arrowRightX, arrowWidth, thumbX, borderWidth;
	private boolean leftArrowPress = false;
	private boolean rightArrowPress = false;
	private boolean thumbPress = false;

	public XSliderRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);

		tintedRightArrow.flipHorizontally();

		{
			ConfigProperty bgColor = myControl.findProperty(ConfigPropertyLookup.COLOR);
			addValueListener(bgColor.getName(), (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
				}
			});
			bgColor.setValue(new SVColorArray(getBackgroundColor()));
		}

		blinkControlHandler = new BlinkControlHandler(this, ConfigPropertyLookup.BLINKING_PERIOD);

		addValueListener(ConfigPropertyLookup.COLOR_ACTIVE, (observer, oldValue, newValue) ->
		{
			if (newValue instanceof SVColor) {
				colorActive = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		addValueListener(ConfigPropertyLookup.ARROW_EMPTY, (observer, oldValue, newValue) -> {
			arrowEmpty.updateAsync(newValue, mode -> {
				tintedLeftArrow.updateImage(arrowEmpty.getImage());
				tintedRightArrow.updateImage(arrowEmpty.getImage());
				return null;
			});
		});

		addValueListener(ConfigPropertyLookup.ARROW_FULL, (observer, oldValue, newValue) -> {
			arrowFull.updateAsync(newValue);
		});

		addValueListener(ConfigPropertyLookup.BORDER, (observer, oldValue, newValue) -> {
			border.updateAsync(newValue, mode -> {
				tintedBorder.updateImage(border.getImage());
				return null;
			});
		});

		addValueListener(ConfigPropertyLookup.THUMB, (observer, oldValue, newValue) -> {
			thumb.updateAsync(newValue, mode -> {
				tintedThumb.updateImage(thumb.getImage());
				return null;
			});
		});

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
				ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
				ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
				ConfigPropertyLookup.TOOLTIP
		);

		updateTintedImages();
	}

	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		double progress = 0.7;
		boolean preview = paintPreview(canvasContext);
		if (preview) {
			progress = this.progress;
			if (isEnabled()) {
				blinkControlHandler.paint(gc);
			}

			if (this.mouseOver) {
				canvasContext.paintLast(tooltipRenderFunc);
			}
		}

		setTintHelpersToPreviewMode(preview);

		//paints the left arrow
		Color colorTint = (preview && focused && isEnabled()) ? colorActive : backgroundColor;

		paintArrow(gc, colorTint, arrowLeftX, (preview && leftArrowPress) ? arrowFull : arrowEmpty, tintedLeftArrow);

		//paints the background behind the thumb
		paintThumbOrBorder(gc, colorTint, thumbX, borderWidth, border, tintedBorder);

		//paints the thumb
		paintThumbOrBorder(gc, colorTint, thumbX, (int) (borderWidth * progress), thumb, tintedThumb);

		//paints the right arrow
		paintArrow(gc, colorTint, arrowRightX, (preview && rightArrowPress) ? arrowFull : arrowEmpty, tintedRightArrow);


		setTintHelpersToPreviewMode(false);

	}

	private void setTintHelpersToPreviewMode(boolean previewMode) {
		tintedThumb.setToPreviewMode(previewMode);
		tintedLeftArrow.setToPreviewMode(previewMode);
		tintedRightArrow.setToPreviewMode(previewMode);
		tintedBorder.setToPreviewMode(previewMode);
		//we don't need to set border's preview mode because using preview is no different than no preview
	}

	private void paintThumbOrBorder(GraphicsContext gc, Color tint, int thumbX, int thumbWidth, ImageOrTextureHelper helper, TintedImageHelperRenderer tinted) {
		switch (helper.getMode()) {
			case Texture: {
				TexturePainter.paint(gc, helper.getTexture(),
						tint, thumbX, y1, thumbX + thumbWidth, y2
				);
				break;
			}
			case Image: {
				tinted.updateTint(tint);
				tinted.updatePosition(thumbX, y1, thumbWidth, getHeight(), true);
				tinted.paintTintedImage(gc);
				break;
			}
			case ImageError: {
				paintImageError(gc, thumbX, y1, thumbWidth, getHeight());
				break;
			}
			case TextureError: {
				paintTextureError(gc, thumbX, y1, thumbWidth, getHeight());
				break;
			}
			case LoadingImage: {
				ArmaControlRenderer.paintImageLoading(gc, tint, thumbX, y1, thumbX + thumbWidth, y2);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + helper.getMode());
		}
	}

	private void paintArrow(GraphicsContext gc, Color arrowColor, int arrowX, ImageOrTextureHelper helper, TintedImageHelperRenderer tintedArrow) {
		final int arrowSize = getArrowSize();
		switch (helper.getMode()) {
			case Texture: {
				tintedArrow.updateImage(null); //help garbage collection
				TexturePainter.paint(gc, helper.getTexture(),
						arrowColor, arrowX, y1, arrowX + arrowSize, y2
				);
				break;
			}
			case Image: {
				tintedArrow.updateImage(helper.getImage());
				tintedArrow.updateTint(arrowColor);
				tintedArrow.paintTintedImage(gc);
				break;
			}
			case ImageError: {
				paintImageError(gc, arrowX, y1, arrowSize, getHeight());
				break;
			}
			case TextureError: {
				paintTextureError(gc, arrowX, y1, arrowSize, getHeight());
				break;
			}
			case LoadingImage: {
				ArmaControlRenderer.paintImageLoading(gc, backgroundColor, arrowX, y1, arrowX + arrowSize, y2);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + helper.getMode());
		}
	}

	@Override
	protected void positionUpdate(boolean initializingPosition) {
		arrowLeftX = x1;
		arrowWidth = getHeight();
		arrowRightX = x2 - arrowWidth;

		final int gap = 4; //how many pixels the left and right arrows are from the thumb

		borderWidth = getWidth() - gap * 2 - arrowWidth * 2;
		thumbX = arrowLeftX + arrowWidth + gap;

		if (!initializingPosition) {
			updateTintedImages();
		}

	}

	private void updateTintedImages() {
		if (arrowEmpty.getImage() != null) {
			final int arrowSize = getArrowSize();
			tintedLeftArrow.updatePosition(arrowLeftX, y1, arrowSize, arrowSize, true);
			tintedRightArrow.updatePosition(arrowRightX, y1, arrowSize, arrowSize, true);
		}
	}

	@Override
	public void mousePress(@NotNull MouseButton mb) {
		super.mousePress(mb);
		leftArrowPress = false;
		rightArrowPress = false;
		thumbPress = false;

		if (mouseButtonDown == MouseButton.PRIMARY) {
			//check thumb
			if (mouseOverX >= thumbX && mouseOverX <= thumbX + borderWidth) {
				if (mouseOverY >= y1 && mouseOverY <= y2) {
					thumbPress = true;
					return;
				}
			}

			//check left arrow
			if (mouseOverX >= arrowLeftX && mouseOverX <= arrowLeftX + arrowWidth) {
				if (mouseOverY >= y1 && mouseOverY <= y2) {
					leftArrowPress = true;
					return;
				}
			}

			if (mouseOverX >= arrowRightX && mouseOverX <= arrowRightX + arrowWidth) {
				if (mouseOverY >= y1 && mouseOverY <= y2) {
					rightArrowPress = true;
					return;
				}
			}
		}
	}

	@Override
	public void mouseRelease() {
		if (leftArrowPress) {
			this.progress = Math.max(0, this.progress - 0.1);
			if (this.progress < 0.01) {
				this.progress = 0;
			}
		} else if (rightArrowPress) {
			this.progress = Math.min(1, this.progress + 0.1);
			if (this.progress > 0.99) {
				this.progress = 1;
			}
		}
		leftArrowPress = false;
		rightArrowPress = false;
		if (thumbPress) {
			progressUpdateFromMouse(mouseOverX);
		}
		thumbPress = false;
	}

	@Override
	public void setMouseOver(int mousex, int mousey, boolean mouseOver) {
		super.setMouseOver(mousex, mousey, mouseOver);

		if (thumbPress) {
			progressUpdateFromMouse(mousex);
		}

	}

	private void progressUpdateFromMouse(int mousex) {
		//if the thumb is pressed, manipulate the progress
		int thumbX2 = (thumbX + borderWidth);
		if (mousex <= thumbX) {
			this.progress = 0;
		} else if (mousex >= thumbX2) {
			this.progress = 1;
		} else {
			this.progress = 1 - Math.abs((thumbX2 - mousex)) * 1.0 / borderWidth;
		}
	}

	@Override
	public boolean canHaveFocus() {
		return true;
	}

	private int getArrowSize() {
		return getHeight();
	}
}
