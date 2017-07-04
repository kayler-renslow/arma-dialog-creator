package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColor;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Region;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/04/2017 */
public class TooltipRenderer {
	private static final Font TOOLTIP_FONT = Font.font(14);
	private final ArmaControl control;
	private Color backgroundColor = null;
	private Color textColor = null;
	private Color borderColor = null;
	private String text = null;

	public TooltipRenderer(@NotNull ArmaControl control,
						   @NotNull ControlPropertyLookupConstant tooltipBackgroundColor,
						   @NotNull ControlPropertyLookupConstant tooltipTextColor,
						   @NotNull ControlPropertyLookupConstant tooltipBorderColor,
						   @NotNull ControlPropertyLookupConstant tooltipText
	) {
		this.control = control;
		control.findProperty(tooltipBackgroundColor).getValueObserver().addListener((observer, oldValue, newValue) ->
		{
			if (newValue instanceof SVColor) {
				backgroundColor = ((SVColor) newValue).toJavaFXColor();
			} else {
				backgroundColor = null;
			}
			requestRender();
		});
		control.findProperty(tooltipTextColor).getValueObserver().addListener((observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				textColor = ((SVColor) newValue).toJavaFXColor();
			} else {
				textColor = null;
			}
			requestRender();
		});
		control.findProperty(tooltipBorderColor).getValueObserver().addListener((observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				borderColor = ((SVColor) newValue).toJavaFXColor();
			} else {
				borderColor = null;
			}
			requestRender();
		});
		control.findProperty(tooltipText).getValueObserver().addListener((observer, oldValue, newValue) -> {
			if (newValue != null) {
				text = TextHelper.getText(newValue);
			} else {
				text = null;
			}
			requestRender();
		});
	}

	public void paint(GraphicsContext gc, int tooltipX, int tooltipY) {
		if (backgroundColor == null || borderColor == null || textColor == null || text == null) {
			return;
		}
		gc.save();

		gc.setFont(TOOLTIP_FONT);

		int textWidth = TextHelper.getWidth(text, TOOLTIP_FONT);
		int textHeight = TextHelper.getHeight(text, TOOLTIP_FONT);
		tooltipY = tooltipY - textHeight * 2;
		int padding = 5;
		int tooltipX2 = tooltipX + textWidth + padding + padding;
		int tooltipHeight = textHeight + padding + padding;
		int tooltipY2 = tooltipY + tooltipHeight;
		int textY = tooltipY + (textHeight + tooltipHeight) / 2;

		gc.setStroke(backgroundColor);
		Region.fillRectangle(gc, tooltipX, tooltipY, tooltipX2, tooltipY2);
		gc.setStroke(borderColor);
		Region.strokeRectangle(gc, tooltipX, tooltipY, tooltipX2, tooltipY2);

		gc.setFill(textColor);
		gc.fillText(text, tooltipX + padding, textY);

		gc.restore();
	}

	private void requestRender() {
		control.getRenderer().requestRender();
	}
}
