package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.utility.BasicTextRenderer;
import com.armadialogcreator.control.impl.utility.BlinkControlHandler;
import com.armadialogcreator.control.impl.utility.TooltipRenderer;
import com.armadialogcreator.canvas.CanvasContext;
import com.armadialogcreator.core.old.ControlProperty;
import com.armadialogcreator.core.old.ControlPropertyLookup;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.core.sv.SVColorArray;
import com.armadialogcreator.core.sv.SVFont;
import com.armadialogcreator.core.sv.SVString;
import com.armadialogcreator.expression.Env;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 A renderer for {@link EditControl}

 @author Kayler
 @since 07/19/2017 */
public class EditRenderer extends ArmaControlRenderer implements BasicTextRenderer.UpdateCallback {

	private BlinkControlHandler blinkControlHandler;

	private BasicTextRenderer textRenderer;
	private TooltipRenderer tooltipRenderer;
	private Color colorDisabled = Color.BLACK;

	private final Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};


	public EditRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this,
				ControlPropertyLookup.TEXT, ControlPropertyLookup.COLOR_TEXT,
				ControlPropertyLookup.STYLE, ControlPropertyLookup.SIZE_EX,
				ControlPropertyLookup.SHADOW, true, this
		);
		textRenderer.setAllowMultiLine(true);

		ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND);
		{
			addValueListener(colorBackground.getPropertyLookup(), (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
				}
			});

			colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));
		}


		addValueListener(ControlPropertyLookup.COLOR_DISABLED, (observer, oldValue, newValue)
				-> {
			if (newValue instanceof SVColor) {
				colorDisabled = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		myControl.findProperty(ControlPropertyLookup.COLOR_TEXT).setValueIfAbsent(true, new SVColorArray(getTextColor()));

		myControl.findProperty(ControlPropertyLookup.TEXT).setValueIfAbsent(true, SVString.newEmptyString());

		myControl.findProperty(ControlPropertyLookup.FONT).setValueIfAbsent(true, SVFont.DEFAULT);
		blinkControlHandler = new BlinkControlHandler(this, ControlPropertyLookup.BLINKING_PERIOD);

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

		if (!isEnabled()) {
			Color oldTextColor = textRenderer.getTextColor();
			super.paint(gc, canvasContext);
			textRenderer.setTextColor(colorDisabled);
			textRenderer.paint(gc);
			textRenderer.setTextColor(oldTextColor);
		} else {
			if (preview) {
				blinkControlHandler.paint(gc);
			}
			super.paint(gc, canvasContext);
			textRenderer.paint(gc);
		}

		if (preview) {
			if (this.mouseOver) {
				canvasContext.paintLast(tooltipRenderFunc);
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
