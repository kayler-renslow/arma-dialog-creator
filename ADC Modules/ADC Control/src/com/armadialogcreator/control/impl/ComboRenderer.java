package com.armadialogcreator.control.impl;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.utility.*;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVNull;
import com.armadialogcreator.core.sv.SVNumericValue;
import com.armadialogcreator.expression.Env;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

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
	private Color colorSelect = Color.BLACK;
	private Color colorDisabled = Color.BLACK;
	private Color colorSelectBackground = Color.BLACK;
	private double wholeHeight;
	private final ScrollbarRenderer scrollbarRenderer;

	private int menuHeightInPixels = 0;
	private final Consumer<Graphics> tooltipRenderFunc = g -> {
		tooltipRenderer.paint(g, this.mouseOverX, this.mouseOverY);
	};
	/** True if the drop down menu of the combobox is visible, false otherwise */
	private boolean menuDown = false;


	public ComboRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this,
				null, ConfigPropertyLookup.COLOR_TEXT,
				ConfigPropertyLookup.STYLE, ConfigPropertyLookup.SIZE_EX,
				ConfigPropertyLookup.SHADOW, this
		);
		textRenderer.setText("Placeholder");

		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				getBackgroundColorObserver().updateValue((SVColor) newValue);
			}
		});


		textRenderer.setTextColor(getTextColor());

		blinkControlHandler = new BlinkControlHandler(this, ConfigPropertyLookup.BLINKING_PERIOD);

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
				ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
				ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
				ConfigPropertyLookup.TOOLTIP
		);

		addValueListener(ConfigPropertyLookup.ARROW_EMPTY, SVNull.instance, (observer, oldValue, newValue) -> {
			arrowEmpty_combo.updateAsync(newValue);
		});
		addValueListener(ConfigPropertyLookup.ARROW_FULL, SVNull.instance, (observer, oldValue, newValue) -> {
			arrowFull_combo.updateAsync(newValue);
		});

		addValueListener(ConfigPropertyLookup.COLOR_SELECT, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorSelect = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		addValueListener(ConfigPropertyLookup.WHOLE_HEIGHT, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVNumericValue) {
				wholeHeight = ((SVNumericValue) newValue).toDouble();
				updateMenuPixelHeight();
				requestRender();
			}
		});
		addValueListener(ConfigPropertyLookup.COLOR_SELECT_BACKGROUND, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorSelectBackground = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		{
			ConfigClass comboScrollBar = myControl.findNestedClass(ComboScrollbarControlSpec.CLASS_NAME);

			scrollbarRenderer = new ScrollbarRenderer(comboScrollBar, this,
					ConfigPropertyLookup.THUMB, ConfigPropertyLookup.ARROW_FULL,
					ConfigPropertyLookup.ARROW_EMPTY, ConfigPropertyLookup.BORDER,
					ConfigPropertyLookup.COLOR
			);
		}
	}

	public void paint(@NotNull Graphics g) {
		boolean preview = paintPreview();

		if (!isEnabled()) {
			Color oldTextColor = textRenderer.getTextColor();
			super.paint(g);
			textRenderer.setTextColor(colorDisabled);
			textRenderer.paint(g);
			textRenderer.setTextColor(oldTextColor);
			//in Arma 3, when combo is disabled, the arrow isn't visible
		} else {
			if (preview) {
				blinkControlHandler.paint(g);
				if (this.mouseOver) {
					g.paintLast(tooltipRenderFunc);
				}
			}
			Color textColor = textRenderer.getTextColor();
			if (preview && focused && !menuDown) {
				Color backgroundColor = getBackgroundColor();
				textRenderer.setTextColor(colorSelect);
				setBackgroundColor(colorSelectBackground);
				super.paint(g);
				textRenderer.paint(g);
				textRenderer.setTextColor(textColor);
				setBackgroundColor(backgroundColor);
			} else {
				super.paint(g);
				textRenderer.paint(g);
			}

			if (preview && menuDown) {
				paintArrow(g, arrowFull_combo);
				{ //draw drop down menu
					final int textPadding = (int) Math.round(getWidth() * BasicTextRenderer.TEXT_PADDING);
					int textAndScrollbarWidth = textRenderer.getTextWidth() + textPadding + ScrollbarRenderer.SCROLLBAR_WIDTH;
					int menuX1 = x1;
					int menuY1 = y2;
					int menuX2 = textAndScrollbarWidth > getWidth() ? x1 + textAndScrollbarWidth : x2;
					int menuY2 = y2 + menuHeightInPixels;
					g.setStroke(backgroundColor);
					g.fillRectangle(menuX1, menuY1, menuX2, menuY2);

					scrollbarRenderer.paint(g, true, menuX2 - ScrollbarRenderer.SCROLLBAR_WIDTH, menuY1, menuHeightInPixels);

					//this is to guarantee that the text purposefully placed out of bounds on the control are clipped
					g.rect(menuX1, menuY1, menuX2 - menuX1 - ScrollbarRenderer.SCROLLBAR_WIDTH, menuY2 - menuY1);
					g.closePath();
					g.clip();
					//draw text for drop down menu

					int allTextHeight = 0;
					int leftTextX = x1 + textPadding;
					int textHeight = textRenderer.getTextLineHeight();
					while (allTextHeight <= menuHeightInPixels && textHeight > 0) { //<= to make sure text goes out of bounds of menu to force scrollbar
						int rowY1 = menuY1 + allTextHeight;
						int rowY2 = rowY1 + textHeight;
						if (mouseOverY >= rowY1 && mouseOverY < rowY2) {
							//mouse is over this row
							g.setStroke(colorSelectBackground);
							g.fillRectangle(x1, rowY1, menuX2, rowY2);
							g.setStroke(backgroundColor);
							textRenderer.setTextColor(colorSelect);
							textRenderer.paint(g, leftTextX, menuY1 + allTextHeight);
							textRenderer.setTextColor(textColor);
						} else {
							textRenderer.paint(g, leftTextX, menuY1 + allTextHeight);
						}
						allTextHeight += textHeight;
					}
				}

			} else {
				paintArrow(g, arrowEmpty_combo);
			}
		}
	}

	private void paintArrow(@NotNull Graphics g, ImageOrTextureHelper helper) {
		final int arrowWidth = getHeight();
		final int arrowHeight = arrowWidth;
		final int arrowX = x2 - arrowWidth;
		final int arrowY = y1;
		switch (helper.getMode()) {
			case Image: {
				g.drawImage(helper.getImage(), arrowX, arrowY, arrowWidth, arrowHeight);
				break;
			}
			case LoadingImage: {
				break;
			}
			case Texture: {
				TexturePainter.paint(g, helper.getTexture(), arrowX, arrowY, arrowX + arrowWidth, arrowY + arrowHeight);
				break;
			}
			case TextureError: {
				paintTextureError(g, arrowX, arrowY, arrowWidth, arrowHeight);
				break;
			}
			case ImageError: {
				paintImageError(g, arrowX, arrowY, arrowWidth, arrowHeight);
				break;
			}
			default:
				throw new IllegalStateException();
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

	@Override
	public void invalidate() {
		super.invalidate();
		arrowEmpty_combo.invalidate();
		arrowFull_combo.invalidate();
		scrollbarRenderer.invalidate();
	}
}
