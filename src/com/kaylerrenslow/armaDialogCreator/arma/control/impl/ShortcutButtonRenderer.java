package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.AlternatorHelper;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BasicTextRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TooltipRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 @author Kayler
 @since 11/21/2016 */
public class ShortcutButtonRenderer extends ArmaControlRenderer {
	private BasicTextRenderer textRenderer;
	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;
	private Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};

	/** color of drop shadow behind button */
	private Color colorShadow = Color.BLACK;

	/** bg color when mouse is over control */
	private Color colorBackgroundActive = Color.BLACK;
	/** bg color when control is disabled */
	private Color colorBackgroundDisabled = Color.BLACK;
	/** color of left border */
	private Color colorBorder = Color.BLACK;
	/** text color if control is disabled */
	private Color colorDisabled = Color.BLACK;
	private Color colorFocused = Color.BLACK, colorFocused2 = Color.BLACK;
	/**
	 alternating bg color helper. if control has focus (but mouse isn't over control), colorFocused and
	 colorFocused2 will alternate
	 */
	private final AlternatorHelper<Color> focusedColorAlternator = new AlternatorHelper<>(500);

	public ShortcutButtonRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this, ControlPropertyLookup.TEXT,
				ControlPropertyLookup.COLOR, ControlPropertyLookup.STYLE, ControlPropertyLookup.SIZE,
				ControlPropertyLookup.SHADOW
		);

		myControl.findProperty(ControlPropertyLookup.ANIM_TEXTURE_NORMAL).getValueObserver().addListener(
				new ValueListener<SerializableValue>() {
					@Override
					public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
						getBackgroundColorObserver().updateValue((SVColor) newValue);
					}
				}
		);


		myControl.findProperty(ControlPropertyLookup.DEFAULT).getValueObserver().addListener(
				(observer, oldValue, newValue) -> {
					requestFocus = newValue instanceof SVBoolean && ((SVBoolean) newValue).isTrue();
					requestRender();
				}
		);


		blinkControlHandler = new BlinkControlHandler(myControl.findProperty(ControlPropertyLookup.BLINKING_PERIOD));

		ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND);
		colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));
		if (colorBackground.getValue() instanceof SVColor) {
			setBackgroundColor(((SVColor) colorBackground.getValue()).toJavaFXColor());
		}
		myControl.findProperty(ControlPropertyLookup.COLOR_TEXT).setValueIfAbsent(true, new SVColorArray(getTextColor()));
		myControl.findProperty(ControlPropertyLookup.TEXT).setValueIfAbsent(true, SVString.newEmptyString());


		tooltipRenderer = new TooltipRenderer(
				this.myControl,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.TOOLTIP
		);

		requestRender();
	}

	@Override
	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		boolean preview = paintPreview(canvasContext);
		if (preview) {
			blinkControlHandler.paint(gc);
		}

		final int controlWidth = getWidth();
		final int controlHeight = getHeight();

		if (preview) {
			Color oldBgColor = this.backgroundColor;
			Color oldTextColor = textRenderer.getTextColor();
			if (!this.isEnabled()) {
				//set background color to the disabled color

			} else {
				if (this.mouseOver) {
					//if the mouse is over this control, set the background color to backgroundColorActive

				} else if (focused) {
					double ratio = focusedColorAlternator.updateAndGetRatio();

				}
			}

			if (mouseButtonDown == MouseButton.PRIMARY) {

				super.paint(gc, canvasContext);

			} else {
				super.paint(gc, canvasContext);
			}

			textRenderer.paint(gc);

			//reset the colors again
			setBackgroundColor(oldBgColor);
			textRenderer.setTextColor(oldTextColor);

			if (this.mouseOver) {
				canvasContext.paintLast(tooltipRenderFunc);
			}
		} else {
			super.paint(gc, canvasContext);
			textRenderer.paint(gc);
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
