package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.core.ConfigPropertyKey;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVNull;
import com.armadialogcreator.util.ColorUtil;
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
						   @NotNull ConfigPropertyKey tooltipBackgroundColor,
						   @NotNull ConfigPropertyKey tooltipTextColor,
						   @NotNull ConfigPropertyKey tooltipBorderColor,
						   @NotNull ConfigPropertyKey tooltipText
	) {
		this.control = control;
		textRenderer = new BasicTextRenderer(
				control, renderer, tooltipText, tooltipTextColor, null, null, null,
				this
		);
		textRenderer.setFont(TOOLTIP_FONT);
		renderer.addValueListener(tooltipBackgroundColor, SVNull.instance, (observer, oldValue, newValue) ->
		{
			if (newValue instanceof SVColor) {
				backgroundColor = ((SVColor) newValue).toJavaFXColor();
			} else {
				backgroundColor = null;
			}
			requestRender(renderer);
		});
		renderer.addValueListener(tooltipBorderColor, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				borderColor = ((SVColor) newValue).toJavaFXColor();
			} else {
				borderColor = null;
			}
			requestRender(renderer);
		});
	}

	public void paint(@NotNull Graphics g, int tooltipX, int tooltipY) {
		if (backgroundColor == null || borderColor == null || textRenderer.getText().length() == 0) {
			return;
		}
		g.save();

		int textWidth = textRenderer.getTextWidth();
		int textHeight = textRenderer.getTextLineHeight();
		tooltipY = tooltipY - textHeight * 2;
		int padding = 5;
		int tooltipX2 = tooltipX + textWidth + padding + padding;
		int tooltipHeight = textHeight + padding + padding;
		int tooltipY2 = tooltipY + tooltipHeight;

		g.setStroke(ColorUtil.toARGB(backgroundColor));
		g.fillRectangle(tooltipX, tooltipY, tooltipX2, tooltipY2);
		g.setStroke(ColorUtil.toARGB(borderColor));
		g.strokeRectangle(tooltipX, tooltipY, tooltipX2, tooltipY2);

		textRenderer.paint(g, tooltipX + padding, tooltipY + padding);

		g.restore();
	}

	private void requestRender(@NotNull ArmaControlRenderer renderer) {
		renderer.requestRender();
	}
}
