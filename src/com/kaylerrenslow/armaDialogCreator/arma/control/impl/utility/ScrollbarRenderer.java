package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 07/24/2017 */
public class ScrollbarRenderer {
	private final ImageOrTextureHelper thumb, arrowFull, arrowEmpty, border;
	private final ArmaControlRenderer renderer;

	private Color scrollbarColor = null;

	/** How many pixels the scrollbar uses for the width */
	public static final int SCROLLBAR_WIDTH = 16;
	private static final int ARROW_HEIGHT = SCROLLBAR_WIDTH;

	public ScrollbarRenderer(@NotNull ControlClass controlClass,
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

		renderer.addValueListener(controlClass, thumb, (observer, oldValue, newValue) -> {
			this.thumb.updateAsync(newValue);
		});
		renderer.addValueListener(controlClass, arrowFull, (observer, oldValue, newValue) -> {
			this.arrowFull.updateAsync(newValue);
		});
		renderer.addValueListener(controlClass, arrowEmpty, (observer, oldValue, newValue) -> {
			this.arrowEmpty.updateAsync(newValue);
		});
		renderer.addValueListener(controlClass, border, (observer, oldValue, newValue) -> {
			this.border.updateAsync(newValue);
		});

		if (scrollbarColor != null) {
			renderer.addValueListener(controlClass, scrollbarColor, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					this.scrollbarColor = ((SVColor) newValue).toJavaFXColor();
					renderer.requestRender();
				}
			});
		}
	}

	public void paint(@NotNull GraphicsContext gc, int x, int y, int h) {
		final int arrowPadding = 4;
		//top arrow
		paintHelper(gc, arrowEmpty, x, y, SCROLLBAR_WIDTH, ARROW_HEIGHT, false);

		final int borderHeight = h - (ARROW_HEIGHT + arrowPadding) * 2;
		final int borderY = y + ARROW_HEIGHT + arrowPadding;
		paintHelper(gc, border, x, borderY, SCROLLBAR_WIDTH, borderHeight, false);
		paintHelper(gc, thumb, x, borderY, SCROLLBAR_WIDTH, borderHeight / 2, false);

		//bottom arrow
		paintHelper(gc, arrowEmpty, x, y + h - ARROW_HEIGHT, SCROLLBAR_WIDTH, ARROW_HEIGHT, true);
	}

	private void paintHelper(GraphicsContext gc, ImageOrTextureHelper helper, int x, int y, int w, int h, boolean rotate) {
		switch (helper.getMode()) {
			case Image: {
				if (scrollbarColor != null) {
					renderer.paintMultiplyColor(gc, x, y, x + w, y + h, scrollbarColor);
				}
				if (rotate) {
					gc.save();
					gc.translate(x + w / 2, y + h / 2); //move to center of image
					gc.rotate(180);
					gc.drawImage(helper.getImage(), -w / 2, -h / 2, w, h);
					gc.restore();
				} else {
					gc.drawImage(helper.getImage(), x, y, w, h);
				}
				gc.setGlobalBlendMode(BlendMode.SRC_OVER);
				break;
			}
			case LoadingImage: {
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
