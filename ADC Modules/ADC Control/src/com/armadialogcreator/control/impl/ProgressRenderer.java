package com.armadialogcreator.control.impl;

import com.armadialogcreator.canvas.Border;
import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.utility.*;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVControlStyleGroup;
import com.armadialogcreator.core.sv.SVNull;
import com.armadialogcreator.expression.Env;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 A renderer for {@link XSliderControl}

 @author Kayler
 @since 07/27/2017 */
public class ProgressRenderer extends ArmaControlRenderer {

	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;
	private Color colorBar = Color.RED;
	private final ImageOrTextureHelper textureHelper = new ImageOrTextureHelper(this);
	private final TintedImageHelperRenderer tintedTexture = new TintedImageHelperRenderer();
	private final AlternatorHelper progressAlternator = new AlternatorHelper(5 * 1000);
	private double progress = 0.7;
	private boolean horizProgress = false;

	private final Consumer<Graphics> tooltipRenderFunc = g -> {
		tooltipRenderer.paint(g, this.mouseOverX, this.mouseOverY);
	};


	public ProgressRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		this.border = new Border(2, Color.BLACK);
		addValueListener(ConfigPropertyLookup.COLOR_FRAME, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				getBackgroundColorObserver().updateValue((SVColor) newValue);
			}
		});

		blinkControlHandler = new BlinkControlHandler(this, ConfigPropertyLookup.BLINKING_PERIOD);

		addValueListener(ConfigPropertyLookup.COLOR_BAR, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorBar = ((SVColor) newValue).toJavaFXColor();
				updateTintedTexture();
				requestRender();
			}
		});

		addValueListener(ConfigPropertyLookup.TEXTURE, SVNull.instance, (observer, oldValue, newValue) -> {
			textureHelper.updateAsync(newValue, mode -> {
				updateTintedTexture();
				return null;
			});
		});

		addValueListener(ConfigPropertyLookup.STYLE, SVNull.instance, (observer, oldValue, newValue) -> {
			newValue = MiscHelpers.getGroup(this.env, newValue, control);
			if (newValue != null) {
				SVControlStyleGroup g = (SVControlStyleGroup) newValue;
				horizProgress = g.hasStyle(ControlStyle.HORIZONTAL);
				requestRender();
			}
		});

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
				ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
				ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
				ConfigPropertyLookup.TOOLTIP
		);

		updateTintedTexture();
	}

	public void paint(@NotNull Graphics g) {
		boolean preview = paintPreview();
		if (preview) {
			if (isEnabled()) {
				blinkControlHandler.paint(g);
			}

			if (this.mouseOver) {
				g.paintLast(tooltipRenderFunc);
			}
			this.progress = progressAlternator.updateAndGetRatio();
		}

		tintedTexture.setToPreviewMode(preview);

		paintBorder(g);

		//progress filler
		double progress = preview ? this.progress : 0.7;
		int x2 = horizProgress ? (int) (x1 + getWidth() * progress) : this.x2;
		int y1 = !horizProgress ? (int) (this.y2 - getHeight() * (progress)) : this.y1;

		switch (textureHelper.getMode()) {
			case Image: {
				tintedTexture.updatePosition(x1, y1, x2 - x1, y2 - y1);
				tintedTexture.paintTintedImage(g);
				break;
			}
			case ImageError: {
				paintImageError(g, x1, y1, getWidth(), getHeight());
				break;
			}
			case LoadingImage: {
				paintImageLoading(g, getBackgroundColor(), x1, y1, x2, y2);
				break;
			}
			case Texture: {
				TexturePainter.paint(g, textureHelper.getTexture(), colorBar, x1, y1, x2, y2);
				break;
			}
			case TextureError: {
				paintTextureError(g, x1, y1, x2, y2);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode : " + textureHelper.getMode());
		}

		tintedTexture.setToPreviewMode(false);
	}

	private void updateTintedTexture() {
		if (textureHelper.getImage() == null) {
			tintedTexture.updateImage(null); //help garbage collection
			return;
		}
		tintedTexture.updateEffect(textureHelper.getImage(), colorBar, x1, y1, getWidth(), getHeight(), true);
	}

	@Override
	public void setBackgroundColor(@NotNull Color color) {
		super.setBackgroundColor(color);
		this.border.setColor(color);
	}
}
