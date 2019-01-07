package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlRenderer;
import com.armadialogcreator.arma.control.impl.utility.*;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.canvas.CanvasContext;
import com.armadialogcreator.canvas.Region;
import com.armadialogcreator.core.ControlClassOld;
import com.armadialogcreator.core.ControlProperty;
import com.armadialogcreator.core.ControlPropertyLookup;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVColorArray;
import com.armadialogcreator.core.sv.SVFont;
import com.armadialogcreator.core.sv.SVNumericValue;
import com.armadialogcreator.expression.Env;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 A renderer for {@link ComboControl}

 @author Kayler
 @since 07/23/2017 */
public class ComboRenderer extends ArmaControlRenderer implements BasicTextRenderer.UpdateCallback {

	private BlinkControlHandler blinkControlHandler;
	private BasicTextRenderer textRenderer;

	private TooltipRenderer tooltipRenderer;
	private final ImageOrTextureHelper arrowEmpty_combo = new ImageOrTextureHelper(this);

	private final ImageOrTextureHelper arrowFull_combo = new ImageOrTextureHelper(this);
	private Color colorSelect = Color.RED;
	private Color colorDisabled = Color.BLACK;
	private Color colorSelectBackground = Color.BLACK;
	private double wholeHeight;
	private final ScrollbarRenderer scrollbarRenderer;

	private int menuHeightInPixels = 0;
	private final Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};
	/** True if the drop down menu of the combobox is visible, false otherwise */
	private boolean menuDown = false;


	public ComboRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this,
				null, ControlPropertyLookup.COLOR_TEXT,
				ControlPropertyLookup.STYLE, ControlPropertyLookup.SIZE_EX,
				ControlPropertyLookup.SHADOW, true, this
		);
		textRenderer.setText("Placeholder");

		ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND);
		{
			addValueListener(colorBackground.getPropertyLookup(), (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
				}
			});
			colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));

		}

		myControl.findProperty(ControlPropertyLookup.COLOR_TEXT).setValueIfAbsent(true, new SVColorArray(getTextColor()));
		myControl.findProperty(ControlPropertyLookup.FONT).setValueIfAbsent(true, SVFont.DEFAULT);

		blinkControlHandler = new BlinkControlHandler(this, ControlPropertyLookup.BLINKING_PERIOD);

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.TOOLTIP
		);

		addValueListener(ControlPropertyLookup.ARROW_EMPTY, (observer, oldValue, newValue) -> {
			arrowEmpty_combo.updateAsync(newValue);
		});
		addValueListener(ControlPropertyLookup.ARROW_FULL, (observer, oldValue, newValue) -> {
			arrowFull_combo.updateAsync(newValue);
		});

		addValueListener(ControlPropertyLookup.COLOR_SELECT, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorSelect = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		addValueListener(ControlPropertyLookup.WHOLE_HEIGHT, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVNumericValue) {
				wholeHeight = ((SVNumericValue) newValue).toDouble();
				updateMenuPixelHeight();
				requestRender();
			}
		});
		addValueListener(ControlPropertyLookup.COLOR_SELECT_BACKGROUND, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorSelectBackground = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		{
			ControlClassOld comboScrollBar = myControl.findNestedClass(ComboControl.NestedClassName_ComboScrollBar);

			scrollbarRenderer = new ScrollbarRenderer(comboScrollBar, this,
					ControlPropertyLookup.THUMB, ControlPropertyLookup.ARROW_FULL,
					ControlPropertyLookup.ARROW_EMPTY, ControlPropertyLookup.BORDER,
					ControlPropertyLookup.COLOR
			);
		}
	}

	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		boolean preview = paintPreview(canvasContext);

		if (!isEnabled()) {
			Color oldTextColor = textRenderer.getTextColor();
			super.paint(gc, canvasContext);
			textRenderer.setTextColor(colorDisabled);
			textRenderer.paint(gc);
			textRenderer.setTextColor(oldTextColor);
			//in Arma 3, when combo is disabled, the arrow isn't visible
		} else {
			if (preview) {
				blinkControlHandler.paint(gc);
				if (this.mouseOver) {
					canvasContext.paintLast(tooltipRenderFunc);
				}
			}
			Color textColor = textRenderer.getTextColor();
			if (preview && focused && !menuDown) {
				Color backgroundColor = getBackgroundColor();
				textRenderer.setTextColor(colorSelect);
				setBackgroundColor(colorSelectBackground);
				super.paint(gc, canvasContext);
				textRenderer.paint(gc);
				textRenderer.setTextColor(textColor);
				setBackgroundColor(backgroundColor);
			} else {
				super.paint(gc, canvasContext);
				textRenderer.paint(gc);
			}

			if (preview && menuDown) {
				paintArrow(gc, arrowFull_combo);
				{ //draw drop down menu
					final int textPadding = (int) Math.round(getWidth() * BasicTextRenderer.TEXT_PADDING);
					int textAndScrollbarWidth = textRenderer.getTextWidth() + textPadding + ScrollbarRenderer.SCROLLBAR_WIDTH;
					int menuX1 = x1;
					int menuY1 = y2;
					int menuX2 = textAndScrollbarWidth > getWidth() ? x1 + textAndScrollbarWidth : x2;
					int menuY2 = y2 + menuHeightInPixels;
					gc.setStroke(backgroundColor);
					Region.fillRectangle(gc, menuX1, menuY1, menuX2, menuY2);

					scrollbarRenderer.paint(gc, true, menuX2 - ScrollbarRenderer.SCROLLBAR_WIDTH, menuY1, menuHeightInPixels);

					//this is to guarantee that the text purposefully placed out of bounds on the control are clipped
					gc.rect(menuX1, menuY1, menuX2 - menuX1 - ScrollbarRenderer.SCROLLBAR_WIDTH, menuY2 - menuY1);
					gc.closePath();
					gc.clip();
					//draw text for drop down menu

					int allTextHeight = 0;
					int leftTextX = x1 + textPadding;
					int textHeight = textRenderer.getTextLineHeight();
					while (allTextHeight <= menuHeightInPixels && textHeight > 0) { //<= to make sure text goes out of bounds of menu to force scrollbar
						int rowY1 = menuY1 + allTextHeight;
						int rowY2 = rowY1 + textHeight;
						if (mouseOverY >= rowY1 && mouseOverY < rowY2) {
							//mouse is over this row
							gc.setStroke(colorSelectBackground);
							Region.fillRectangle(gc, x1, rowY1, menuX2, rowY2);
							gc.setStroke(backgroundColor);
							textRenderer.setTextColor(colorSelect);
							textRenderer.paint(gc, leftTextX, menuY1 + allTextHeight);
							textRenderer.setTextColor(textColor);
						} else {
							textRenderer.paint(gc, leftTextX, menuY1 + allTextHeight);
						}
						allTextHeight += textHeight;
					}
				}

			} else {
				paintArrow(gc, arrowEmpty_combo);
			}
		}
	}

	private void paintArrow(GraphicsContext gc, ImageOrTextureHelper helper) {
		final int arrowWidth = getHeight();
		final int arrowHeight = arrowWidth;
		final int arrowX = x2 - arrowWidth;
		final int arrowY = y1;

		switch (helper.getMode()) {
			case Image: {
				gc.drawImage(helper.getImage(), arrowX, arrowY, arrowWidth, arrowHeight);
				break;
			}
			case LoadingImage: {
				break;
			}
			case Texture: {
				TexturePainter.paint(gc, helper.getTexture(), arrowX, arrowY, arrowX + arrowWidth, arrowY + arrowHeight);
				break;
			}
			case TextureError: {
				paintTextureError(gc, arrowX, arrowY, arrowWidth, arrowHeight);
				break;
			}
			case ImageError: {
				paintImageError(gc, arrowX, arrowY, arrowWidth, arrowHeight);
				break;
			}
			default:
				throw new IllegalStateException("unknown mode:" + arrowFull_combo.getMode());
		}
	}

	@NotNull
	public Color getTextColor() {
		return textRenderer.getTextColor();
	}

	@Override
	public boolean canHaveFocus() {
		return true;
	}

	@Override
	public void mousePress(@NotNull MouseButton mb) {
		super.mousePress(mb);

	}

	@Override
	public void mouseRelease() {
		super.mouseRelease();
		menuDown = !menuDown;
	}

	@Override
	public boolean containsPoint(int x, int y) {
		int menuHeight = menuDown ? menuHeightInPixels : 0;
		if (x1 <= x && y1 <= y) {
			if (x2 >= x && (y2 + menuHeight) >= y) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void positionUpdate(boolean initializingPosition) {
		updateMenuPixelHeight();
	}

	private void updateMenuPixelHeight() {
		menuHeightInPixels = (int) Math.round(wholeHeight * resolution.getViewportHeight());
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		if (!focused) {
			menuDown = false;
		}
	}
}
