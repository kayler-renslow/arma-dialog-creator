package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BasicTextRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TooltipRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColorArray;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVFont;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVString;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 A renderer for {@link EditControl}

 @author Kayler
 @since 07/19/2017 */
public class EditRenderer extends ArmaControlRenderer {

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
				ControlPropertyLookup.SHADOW
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
