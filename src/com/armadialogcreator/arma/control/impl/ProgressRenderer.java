package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlRenderer;
import com.armadialogcreator.arma.control.TintedImageHelperRenderer;
import com.armadialogcreator.arma.control.impl.utility.*;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.control.ControlProperty;
import com.armadialogcreator.control.ControlPropertyLookup;
import com.armadialogcreator.control.ControlStyle;
import com.armadialogcreator.control.sv.SVColor;
import com.armadialogcreator.control.sv.SVColorArray;
import com.armadialogcreator.control.sv.SVControlStyleGroup;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.uicanvas.Border;
import com.armadialogcreator.gui.uicanvas.CanvasContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

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

	private final Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};


	public ProgressRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		this.border = new Border(2, Color.BLACK);
		{
			ControlProperty colorFrame = myControl.findProperty(ControlPropertyLookup.COLOR_FRAME);
			addValueListener(colorFrame.getPropertyLookup(), (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
				}
			});
			colorFrame.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));
		}

		blinkControlHandler = new BlinkControlHandler(this, ControlPropertyLookup.BLINKING_PERIOD);

		addValueListener(ControlPropertyLookup.COLOR_BAR, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorBar = ((SVColor) newValue).toJavaFXColor();
				updateTintedTexture();
				requestRender();
			}
		});

		addValueListener(ControlPropertyLookup.TEXTURE, (observer, oldValue, newValue) -> {
			textureHelper.updateAsync(newValue, mode -> {
				updateTintedTexture();
				return null;
			});
		});

		addValueListener(ControlPropertyLookup.STYLE, (observer, oldValue, newValue) -> {
			newValue = MiscHelpers.getGroup(newValue, control);
			if (newValue != null) {
				SVControlStyleGroup g = (SVControlStyleGroup) newValue;
				horizProgress = g.hasStyle(ControlStyle.HORIZONTAL);
				requestRender();
			}
		});

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.TOOLTIP
		);

		updateTintedTexture();
	}

	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		boolean preview = paintPreview(canvasContext);
		if (preview) {
			if (isEnabled()) {
				blinkControlHandler.paint(gc);
			}

			if (this.mouseOver) {
				canvasContext.paintLast(tooltipRenderFunc);
			}
			this.progress = progressAlternator.updateAndGetRatio();
		}

		tintedTexture.setToPreviewMode(preview);

		paintBorder(gc);

		//progress filler
		double progress = preview ? this.progress : 0.7;
		int x2 = horizProgress ? (int) (x1 + getWidth() * progress) : this.x2;
		int y1 = !horizProgress ? (int) (this.y2 - getHeight() * (progress)) : this.y1;

		switch (textureHelper.getMode()) {
			case Image: {
				tintedTexture.updatePosition(x1, y1, x2 - x1, y2 - y1);
				tintedTexture.paintTintedImage(gc);
				break;
			}
			case ImageError: {
				paintImageError(gc, x1, y1, getWidth(), getHeight());
				break;
			}
			case LoadingImage: {
				paintImageLoading(gc, backgroundColor, x1, y1, x2, y2);
				break;
			}
			case Texture: {
				TexturePainter.paint(gc, textureHelper.getTexture(), colorBar, x1, y1, x2, y2);
				break;
			}
			case TextureError: {
				paintTextureError(gc, x1, y1, x2, y2);
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
	public void setBackgroundColor(@NotNull Color paint) {
		super.setBackgroundColor(paint);
		this.border.setColor(paint);
	}
}
