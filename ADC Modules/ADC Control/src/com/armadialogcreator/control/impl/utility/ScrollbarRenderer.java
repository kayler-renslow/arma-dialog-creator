package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.RequirementsConfigClass;
import com.armadialogcreator.core.old.ControlPropertyLookupConstant;
import com.armadialogcreator.core.sv.SVColor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 07/24/2017 */
public class ScrollbarRenderer {
	private final ImageOrTextureHelper thumb, arrowFull, arrowEmpty, border;
	private final TintedImageHelperRenderer tintedThumb = new TintedImageHelperRenderer();
	private final TintedImageHelperRenderer tintedArrowTop = new TintedImageHelperRenderer();
	private final TintedImageHelperRenderer tintedArrowBottom = new TintedImageHelperRenderer();
	private final TintedImageHelperRenderer tintedBorder = new TintedImageHelperRenderer();
	private final ArmaControlRenderer renderer;

	private Color scrollbarColor = null;

	/** How many pixels the scrollbar uses for the width */
	public static final int SCROLLBAR_WIDTH = 16;
	private static final int ARROW_HEIGHT = SCROLLBAR_WIDTH;

	public ScrollbarRenderer(@NotNull ConfigClass controlClass,
							 @NotNull ArmaControlRenderer renderer,
							 @NotNull ControlPropertyLookupConstant thumb,
							 @NotNull ControlPropertyLookupConstant arrowFull,
							 @NotNull ControlPropertyLookupConstant arrowEmpty,
							 @NotNull ControlPropertyLookupConstant border,
							 @Nullable ControlPropertyLookupConstant scrollbarColor
	) {
		this.renderer = renderer;
		this.thumb = new ImageOrTextureHelper(renderer);
		this.arrowEmpty = new ImageOrTextureHelper(renderer);
		this.arrowFull = new ImageOrTextureHelper(renderer);
		this.border = new ImageOrTextureHelper(renderer);

		tintedArrowBottom.rotate(180);

		renderer.addValueListener(controlClass, thumb, (observer, oldValue, newValue) -> {
			this.thumb.updateAsync(newValue, mode -> {
				if (this.thumb.getImage() != null) {
					tintedThumb.updateImage(this.thumb.getImage(), true);
				} else {
					tintedThumb.updateImage(null); //help garbage collection
				}
				return null;
			});
		});
		renderer.addValueListener(controlClass, arrowFull, (observer, oldValue, newValue) -> {
			this.arrowFull.updateAsync(newValue);
		});
		renderer.addValueListener(controlClass, arrowEmpty, (observer, oldValue, newValue) -> {
			this.arrowEmpty.updateAsync(newValue, mode -> {
				if (this.arrowEmpty.getImage() != null) {
					Image img = this.arrowEmpty.getImage();
					tintedArrowTop.updateImage(img, true);
					tintedArrowBottom.updateImage(img, true);
				}
				return null;
			});
		});
		renderer.addValueListener(controlClass, border, (observer, oldValue, newValue) -> {
			this.border.updateAsync(newValue, mode -> {
				if (this.border.getImage() != null) {
					tintedBorder.updateImage(this.border.getImage(), true);
				} else {
					tintedBorder.updateImage(null); //help garbage collection
				}
				return null;
			});
		});

		if (scrollbarColor != null) {
			renderer.addValueListener(controlClass, scrollbarColor, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					this.scrollbarColor = ((SVColor) newValue).toJavaFXColor();
					tintedThumb.updateTint(this.scrollbarColor, true);
					tintedBorder.updateTint(this.scrollbarColor, true);
					tintedArrowTop.updateTint(this.scrollbarColor, true);
					tintedArrowBottom.updateTint(this.scrollbarColor, true);
					renderer.requestRender();
				}
			});
		}


	}

	public void paint(@NotNull GraphicsContext gc, boolean preview, int x, int y, int h) {
		setTintedImagesToPreviewMode(preview);

		final int arrowPadding = 4;
		//top arrow
		paintHelper(gc, arrowEmpty, x, y, SCROLLBAR_WIDTH, ARROW_HEIGHT, tintedArrowTop);

		final int borderHeight = Math.max(1, h - (ARROW_HEIGHT + arrowPadding) * 2);
		final int borderY = y + ARROW_HEIGHT + arrowPadding;
		paintHelper(gc, border, x, borderY, SCROLLBAR_WIDTH, borderHeight, tintedBorder);
		paintHelper(gc, thumb, x, borderY, SCROLLBAR_WIDTH, borderHeight / 2, tintedThumb);

		//bottom arrow
		paintHelper(gc, arrowEmpty, x, y + h - ARROW_HEIGHT, SCROLLBAR_WIDTH, ARROW_HEIGHT, tintedArrowBottom);

		setTintedImagesToPreviewMode(false);
	}

	private void setTintedImagesToPreviewMode(boolean preview) {
		tintedArrowTop.setToPreviewMode(preview);
		tintedArrowBottom.setToPreviewMode(preview);
	}

	private void paintHelper(GraphicsContext gc, ImageOrTextureHelper helper, int x, int y, int w, int h, TintedImageHelperRenderer tinted) {
		switch (helper.getMode()) {
			case Image: {
				tinted.updatePosition(x, y, w, h, true);
				tinted.paintTintedImage(gc);
				break;
			}
			case LoadingImage: {
				ArmaControlRenderer.paintImageLoading(gc, tinted.getTint(), x, y, x + w, y + h);
				break;
			}
			case Texture: {
				TexturePainter.paint(gc, helper.getTexture(), scrollbarColor, x, y, x + w, y + h);
				break;
			}
			case ImageError: {
				renderer.paintImageError(gc, x, y, w, h);
				break;
			}
			case TextureError: {
				renderer.paintTextureError(gc, x, y, w, h);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + helper.getMode());
		}
	}

}
