package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVNull;
import com.armadialogcreator.util.DataInvalidator;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 07/24/2017 */
public class ScrollbarRenderer implements DataInvalidator {
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
							 @NotNull ConfigPropertyLookupConstant thumb,
							 @NotNull ConfigPropertyLookupConstant arrowFull,
							 @NotNull ConfigPropertyLookupConstant arrowEmpty,
							 @NotNull ConfigPropertyLookupConstant border,
							 @Nullable ConfigPropertyLookupConstant scrollbarColor
	) {
		this.renderer = renderer;
		this.thumb = new ImageOrTextureHelper(renderer);
		this.arrowEmpty = new ImageOrTextureHelper(renderer);
		this.arrowFull = new ImageOrTextureHelper(renderer);
		this.border = new ImageOrTextureHelper(renderer);

		tintedArrowBottom.rotate(180);

		renderer.addValueListener(controlClass, thumb, SVNull.instance, (observer, oldValue, newValue) -> {
			this.thumb.updateAsync(newValue, mode -> {
				if (this.thumb.getImage() != null) {
					tintedThumb.updateImage(this.thumb.getImage(), true);
				} else {
					tintedThumb.updateImage(null); //help garbage collection
				}
			});
		});
		renderer.addValueListener(controlClass, arrowFull, SVNull.instance, (observer, oldValue, newValue) -> {
			this.arrowFull.updateAsync(newValue);
		});
		renderer.addValueListener(controlClass, arrowEmpty, SVNull.instance, (observer, oldValue, newValue) -> {
			this.arrowEmpty.updateAsync(newValue, mode -> {
				if (this.arrowEmpty.getImage() != null) {
					Image img = this.arrowEmpty.getImage();
					tintedArrowTop.updateImage(img, true);
					tintedArrowBottom.updateImage(img, true);
				}
			});
		});
		renderer.addValueListener(controlClass, border, SVNull.instance, (observer, oldValue, newValue) -> {
			this.border.updateAsync(newValue, mode -> {
				if (this.border.getImage() != null) {
					tintedBorder.updateImage(this.border.getImage(), true);
				} else {
					tintedBorder.updateImage(null); //help garbage collection
				}
			});
		});

		if (scrollbarColor != null) {
			renderer.addValueListener(controlClass, scrollbarColor, SVNull.instance, (observer, oldValue, newValue) -> {
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

	public void paint(@NotNull Graphics g, boolean preview, int x, int y, int h) {
		setTintedImagesToPreviewMode(preview);

		final int arrowPadding = 4;
		//top arrow
		paintHelper(g, arrowEmpty, x, y, SCROLLBAR_WIDTH, ARROW_HEIGHT, tintedArrowTop);

		final int borderHeight = Math.max(1, h - (ARROW_HEIGHT + arrowPadding) * 2);
		final int borderY = y + ARROW_HEIGHT + arrowPadding;
		paintHelper(g, border, x, borderY, SCROLLBAR_WIDTH, borderHeight, tintedBorder);
		paintHelper(g, thumb, x, borderY, SCROLLBAR_WIDTH, borderHeight / 2, tintedThumb);

		//bottom arrow
		paintHelper(g, arrowEmpty, x, y + h - ARROW_HEIGHT, SCROLLBAR_WIDTH, ARROW_HEIGHT, tintedArrowBottom);

		setTintedImagesToPreviewMode(false);
	}

	private void setTintedImagesToPreviewMode(boolean preview) {
		tintedArrowTop.setToPreviewMode(preview);
		tintedArrowBottom.setToPreviewMode(preview);
	}

	private void paintHelper(@NotNull Graphics g, ImageOrTextureHelper helper, int x, int y, int w, int h, TintedImageHelperRenderer tinted) {
		switch (helper.getMode()) {
			case Image: {
				tinted.updatePosition(x, y, w, h, true);
				tinted.paintTintedImage(g);
				break;
			}
			case LoadingImage: {
				ArmaControlRenderer.paintImageLoading(g, tinted.getTint(), x, y, x + w, y + h);
				break;
			}
			case Texture: {
				TexturePainter.paint(g, helper.getTexture(), scrollbarColor, x, y, x + w, y + h);
				break;
			}
			case ImageError: {
				renderer.paintImageError(g, x, y, w, h);
				break;
			}
			case TextureError: {
				renderer.paintTextureError(g, x, y, w, h);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + helper.getMode());
		}
	}

	@Override
	public void invalidate() {
		thumb.invalidate();
		arrowEmpty.invalidate();
		arrowFull.invalidate();
		border.invalidate();
	}
}
