package com.armadialogcreator.control.impl;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.utility.BasicTextRenderer;
import com.armadialogcreator.control.impl.utility.BlinkControlHandler;
import com.armadialogcreator.control.impl.utility.TooltipRenderer;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVNull;
import com.armadialogcreator.expression.Env;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 A renderer for {@link EditControl}

 @author Kayler
 @since 07/19/2017 */
public class EditRenderer extends ArmaControlRenderer implements BasicTextRenderer.UpdateCallback {

	private BlinkControlHandler blinkControlHandler;

	private BasicTextRenderer textRenderer;
	private TooltipRenderer tooltipRenderer;
	private Color colorDisabled = Color.BLACK;

	private final Consumer<Graphics> tooltipRenderFunc = g -> {
		tooltipRenderer.paint(g, this.mouseOverX, this.mouseOverY);
	};


	public EditRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this,
				ConfigPropertyLookup.TEXT, ConfigPropertyLookup.COLOR_TEXT,
				ConfigPropertyLookup.STYLE, ConfigPropertyLookup.SIZE_EX,
				ConfigPropertyLookup.SHADOW, this
		);
		textRenderer.setAllowMultiLine(true);

		addValueListener(ConfigPropertyLookup.COLOR_BACKGROUND, SVNull.instance, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				getBackgroundColorObserver().updateValue((SVColor) newValue);
			}
		});

		addValueListener(ConfigPropertyLookup.COLOR_DISABLED, SVNull.instance, (observer, oldValue, newValue)
				-> {
			if (newValue instanceof SVColor) {
				colorDisabled = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		textRenderer.setTextColor(getTextColor());
		textRenderer.setText("");

		blinkControlHandler = new BlinkControlHandler(this, ConfigPropertyLookup.BLINKING_PERIOD);

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
				ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
				ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
				ConfigPropertyLookup.TOOLTIP
		);

	}

	public void paint(@NotNull Graphics g) {
		boolean preview = paintPreview();

		if (!isEnabled()) {
			Color oldTextColor = textRenderer.getTextColor();
			super.paint(g);
			textRenderer.setTextColor(colorDisabled);
			textRenderer.paint(g);
			textRenderer.setTextColor(oldTextColor);
		} else {
			if (preview) {
				blinkControlHandler.paint(g);
			}
			super.paint(g);
			textRenderer.paint(g);
		}

		if (preview) {
			if (this.mouseOver) {
				g.paintLast(tooltipRenderFunc);
			}
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
}
