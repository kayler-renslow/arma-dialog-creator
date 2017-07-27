package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColorArray;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Region;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
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
	private double progress = 0.7;

	private final Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};
	private int arrowLeftX, arrowRightX, arrowWidth, thumbX, thumbWidth;
	private boolean leftArrowPress = false;
	private boolean rightArrowPress = false;
	private boolean thumbPress = false;

	public XSliderRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);

		{
			ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR);
			addValueListener(colorBackground.getPropertyLookup(), (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
				}
			});
			colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));
		}

		blinkControlHandler = new BlinkControlHandler(this, ControlPropertyLookup.BLINKING_PERIOD);

		addValueListener(ControlPropertyLookup.COLOR_ACTIVE, (observer, oldValue, newValue) ->
		{
			if (newValue instanceof SVColor) {
				colorActive = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		addValueListener(ControlPropertyLookup.ARROW_EMPTY, (observer, oldValue, newValue) -> {
			arrowEmpty.updateAsync(newValue);
		});

		addValueListener(ControlPropertyLookup.ARROW_FULL, (observer, oldValue, newValue) -> {
			arrowFull.updateAsync(newValue);
		});

		addValueListener(ControlPropertyLookup.BORDER, (observer, oldValue, newValue) -> {
			border.updateAsync(newValue);
		});

		addValueListener(ControlPropertyLookup.THUMB, (observer, oldValue, newValue) -> {
			thumb.updateAsync(newValue);
		});

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.TOOLTIP
		);
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


		//paints the left arrow
		paintArrow(gc, arrowLeftX, leftArrowPress ? arrowFull : arrowEmpty, false);

		//paints the background behind the thumb
		paintThumb(gc, thumbX, thumbWidth, border);

		//paints the thumb
		paintThumb(gc, thumbX, (int) (thumbWidth * progress), thumb);

		//paints the right arrow
		paintArrow(gc, arrowRightX, rightArrowPress ? arrowFull : arrowEmpty, true);

	}

	private void paintThumb(@NotNull GraphicsContext gc, int thumbX, int thumbWidth, ImageOrTextureHelper helper) {
		Color color = (focused && isEnabled()) ? colorActive : this.backgroundColor;
		switch (helper.getMode()) {
			case Texture: {
				TexturePainter.paint(gc, helper.getTexture(),
						color, thumbX, y1, thumbX + thumbWidth, y2
				);
				break;
			}
			case Image: {
				paintMultiplyColor(gc, thumbX, y1, thumbX + thumbWidth, y2, color);
				gc.drawImage(helper.getImage(), thumbX, y1, thumbWidth, getHeight());
				gc.setGlobalBlendMode(BlendMode.SRC_OVER);
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
				gc.setStroke(color);
				Region.fillRectangle(gc, thumbX, y1, thumbX + thumbWidth, y2);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + helper.getMode());
		}
	}

	private void paintArrow(@NotNull GraphicsContext gc, int arrowX, ImageOrTextureHelper helper, boolean rotate) {
		Color arrowColor = (focused && isEnabled()) ? colorActive : backgroundColor;
		final int arrowWidth = getHeight();
		switch (helper.getMode()) {
			case Texture: {
				TexturePainter.paint(gc, helper.getTexture(),
						arrowColor, arrowX, y1, arrowX + arrowWidth, y2
				);
				break;
			}
			case Image: {
				paintMultiplyColor(gc, arrowX, y1, arrowX + arrowWidth, y2, arrowColor);
				if (rotate) {
					MiscHelpers.paintFlippedImage(gc, helper.getImage(), arrowX, y1, arrowWidth, arrowWidth);
				} else {
					gc.drawImage(helper.getImage(), arrowX, y1, arrowWidth, getHeight());
				}
				gc.setGlobalBlendMode(BlendMode.SRC_OVER);
				break;
			}
			case ImageError: {
				paintImageError(gc, arrowX, y1, arrowWidth, getHeight());
				break;
			}
			case TextureError: {
				paintTextureError(gc, arrowX, y1, arrowWidth, getHeight());
				break;
			}
			case LoadingImage: {
				gc.setStroke(backgroundColor);
				Region.fillRectangle(gc, arrowX, y1, arrowX + arrowWidth, y2);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + helper.getMode());
		}
	}

	@Override
	protected void positionUpdate() {
		arrowLeftX = x1;
		arrowWidth = getHeight();
		arrowRightX = x2 - arrowWidth;

		final int gap = 4; //how many pixels the left and right arrows are from the thumb

		thumbWidth = getWidth() - gap * 2 - arrowWidth * 2;
		thumbX = arrowLeftX + arrowWidth + gap;
	}

	@Override
	public void mousePress(@NotNull MouseButton mb) {
		super.mousePress(mb);
		leftArrowPress = false;
		rightArrowPress = false;
		thumbPress = false;

		if (mouseButtonDown == MouseButton.PRIMARY) {
			//check thumb
			if (mouseOverX >= thumbX && mouseOverX <= thumbX + thumbWidth) {
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
		int thumbX2 = (thumbX + thumbWidth);
		if (mousex <= thumbX) {
			this.progress = 0;
		} else if (mousex >= thumbX2) {
			this.progress = 1;
		} else {
			this.progress = 1 - Math.abs((thumbX2 - mousex)) * 1.0 / thumbWidth;
		}
	}

	@Override
	public boolean canHaveFocus() {
		return true;
	}
}
