package com.armadialogcreator.arma.control.impl.utility;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlRenderer;
import com.armadialogcreator.canvas.Region;
import com.armadialogcreator.core.old.ControlPropertyLookupConstant;
import com.armadialogcreator.core.sv.SVColor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/04/2017 */
public class TooltipRenderer implements BasicTextRenderer.UpdateCallback {
	private static final Font TOOLTIP_FONT = Font.font(14);
	private final ArmaControl control;
	private Color backgroundColor = null;
	private Color borderColor = null;

	private final BasicTextRenderer textRenderer;

	public TooltipRenderer(@NotNull ArmaControl control, @NotNull ArmaControlRenderer renderer,
						   @NotNull ControlPropertyLookupConstant tooltipBackgroundColor,
						   @NotNull ControlPropertyLookupConstant tooltipTextColor,
						   @NotNull ControlPropertyLookupConstant tooltipBorderColor,
						   @NotNull ControlPropertyLookupConstant tooltipText
	) {
		this.control = control;
		textRenderer = new BasicTextRenderer(
				control, renderer, tooltipText, tooltipTextColor, null, null, null,
				false, this
		);
		textRenderer.setFont(TOOLTIP_FONT);
		control.findProperty(tooltipBackgroundColor).getValueObserver().addListener((observer, oldValue, newValue) ->
		{
			if (newValue instanceof SVColor) {
				backgroundColor = ((SVColor) newValue).toJavaFXColor();
			} else {
				backgroundColor = null;
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
	}

	public void paint(GraphicsContext gc, int tooltipX, int tooltipY) {
		if (backgroundColor == null || borderColor == null || textRenderer.getText().length() == 0) {
			return;
		}
		gc.save();

		int textWidth = textRenderer.getTextWidth();
		int textHeight = textRenderer.getTextLineHeight();
		tooltipY = tooltipY - textHeight * 2;
		int padding = 5;
		int tooltipX2 = tooltipX + textWidth + padding + padding;
		int tooltipHeight = textHeight + padding + padding;
		int tooltipY2 = tooltipY + tooltipHeight;

		gc.setStroke(backgroundColor);
		Region.fillRectangle(gc, tooltipX, tooltipY, tooltipX2, tooltipY2);
		gc.setStroke(borderColor);
		Region.strokeRectangle(gc, tooltipX, tooltipY, tooltipX2, tooltipY2);

		textRenderer.paint(gc, tooltipX + padding, tooltipY + padding);

		gc.restore();
	}

	private void requestRender() {
		control.getRenderer().requestRender();
	}
}
