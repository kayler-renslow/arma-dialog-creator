package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.PictureOrTextureHelper;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TexturePainter;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TooltipRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColorArray;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
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
	private final PictureOrTextureHelper arrowEmpty = new PictureOrTextureHelper(this);
	private final PictureOrTextureHelper arrowFull = new PictureOrTextureHelper(this);
	private final PictureOrTextureHelper border = new PictureOrTextureHelper(this);
	private final PictureOrTextureHelper thumb = new PictureOrTextureHelper(this);
	private double progress = 0.7;


	private final Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};

	public XSliderRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);

		ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR);
		{
			colorBackground.getValueObserver().addListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
					if (newValue instanceof SVColor) {
						getBackgroundColorObserver().updateValue((SVColor) newValue);
					}
				}
			});
			colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));

			if (colorBackground.getValue() instanceof SVColor) {
				setBackgroundColor(((SVColor) colorBackground.getValue()).toJavaFXColor());
			}
		}
		blinkControlHandler = new BlinkControlHandler(myControl.findProperty(ControlPropertyLookup.BLINKING_PERIOD));

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.TOOLTIP
		);
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
		}

		final int arrowLeftX = x1;
		final int arrowWidth = getHeight();
		final int arrowRightX = x2 - arrowWidth;

		final int gap = x2 - arrowRightX; //how many pixels the left and right arrows are from the thumb

		final int thumbX = arrowLeftX + gap;
		final int thumbWidth = x2 - gap - arrowWidth;

		//paints the left arrow
		paintArrow(gc, canvasContext, arrowLeftX, mouseButtonDown == null ? arrowEmpty : arrowFull);

		//paints the background behind the thumb
		paintThumb(gc, canvasContext, thumbX, thumbWidth, border);

		//paints the thumb
		paintThumb(gc, canvasContext, thumbX, (int) (thumbWidth * progress), border);

		//paints the right arrow
		paintArrow(gc, canvasContext, arrowRightX, mouseButtonDown == null ? arrowEmpty : arrowFull);

	}

	private void paintThumb(@NotNull GraphicsContext gc, CanvasContext canvasContext,
							int thumbX, int thumbWidth, PictureOrTextureHelper helper) {
		Color backgroundColor = focused ? colorActive : this.backgroundColor;
		switch (helper.getMode()) {
			case Texture: {
				TexturePainter.paint(gc, helper.getTexture(),
						backgroundColor, thumbX, y1, thumbWidth, y1
				);
				break;
			}
			case Image: {
				paintMultiplyColor(gc, thumbX, y1, thumbX + thumbWidth, y2, backgroundColor);
				gc.drawImage(helper.getImage(), thumbX, y1, thumbWidth, getHeight());
				gc.setGlobalBlendMode(BlendMode.SRC_OVER);
				break;
			}
			case ImageError: {
				gc.save();
				gc.rect(thumbX, y1, thumbWidth, getHeight());
				gc.closePath();
				gc.clip();
				paintImageError(gc);
				gc.restore();
				break;
			}
			case TextureError: {
				gc.save();
				gc.rect(thumbX, y1, thumbWidth, getHeight());
				gc.closePath();
				gc.clip();
				paintTextureError(gc);
				gc.restore();
				break;
			}
			case LoadingImage: {
				super.paint(gc, canvasContext);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + helper.getMode());
		}
	}

	private void paintArrow(@NotNull GraphicsContext gc, CanvasContext canvasContext,
							int arrowX, PictureOrTextureHelper helper) {
		Color arrowColor = focused ? colorActive : backgroundColor;
		final int arrowWidth = getHeight();
		switch (helper.getMode()) {
			case Texture: {
				TexturePainter.paint(gc, helper.getTexture(),
						arrowColor, arrowX, y1, arrowWidth, y1
				);
				break;
			}
			case Image: {
				paintMultiplyColor(gc, arrowX, y1, arrowX + arrowWidth, y2, arrowColor);
				gc.drawImage(helper.getImage(), arrowX, y1, arrowWidth, getHeight());
				gc.setGlobalBlendMode(BlendMode.SRC_OVER);
				break;
			}
			case ImageError: {
				gc.save();
				gc.rect(arrowX, y1, arrowWidth, getHeight());
				gc.closePath();
				gc.clip();
				paintImageError(gc);
				gc.restore();
				break;
			}
			case TextureError: {
				gc.save();
				gc.rect(arrowX, y1, arrowWidth, getHeight());
				gc.closePath();
				gc.clip();
				paintTextureError(gc);
				gc.restore();
				break;
			}
			case LoadingImage: {
				super.paint(gc, canvasContext);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + helper.getMode());
		}
	}

	@Override
	public boolean canHaveFocus() {
		return true;
	}
}
