package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
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

	private final PictureOrTextureHelper animTextureNormal = new PictureOrTextureHelper(this);
	private final PictureOrTextureHelper animTextureDisabled = new PictureOrTextureHelper(this);
	private final PictureOrTextureHelper animTextureOver = new PictureOrTextureHelper(this);
	private final PictureOrTextureHelper animTexturePressed = new PictureOrTextureHelper(this);
	private final PictureOrTextureHelper animTextureFocused = new PictureOrTextureHelper(this);
	private final PictureOrTextureHelper animTextureDefault = new PictureOrTextureHelper(this);

	/** secondary text color (text color alternates between "color" and "color2") */
	private Color color2 = Color.BLACK;
	private Color colorFocused = Color.BLACK;
	private Color colorDisabled = Color.BLACK;
	private Color colorBackgroundFocused = Color.BLACK;


	/**
	 Alternating color helper. if control has focus (but mouse isn't over control).
	 There is only one because both the alternating text color and alternating bg colors are synced
	 */
	private final AlternatorHelper<Color> focusedColorAlternator = new AlternatorHelper<>(500);

	public ShortcutButtonRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this, ControlPropertyLookup.TEXT,
				ControlPropertyLookup.COLOR, ControlPropertyLookup.STYLE, ControlPropertyLookup.SIZE,
				ControlPropertyLookup.SHADOW
		);

		ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND);
		colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));
		if (colorBackground.getValue() instanceof SVColor) {
			setBackgroundColor(((SVColor) colorBackground.getValue()).toJavaFXColor());
		}
		colorBackground.getValueObserver().addListener(
				new ValueListener<SerializableValue>() {
					@Override
					public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
						if (newValue instanceof SVColor) {
							getBackgroundColorObserver().updateValue((SVColor) newValue);
						}
					}
				}
		);

		attachPicOrTexPropertyListener(ControlPropertyLookup.ANIM_TEXTURE_NORMAL, animTextureNormal);
		attachPicOrTexPropertyListener(ControlPropertyLookup.ANIM_TEXTURE_DISABLED, animTextureDisabled);
		attachPicOrTexPropertyListener(ControlPropertyLookup.ANIM_TEXTURE_OVER, animTextureOver);
		attachPicOrTexPropertyListener(ControlPropertyLookup.ANIM_TEXTURE_PRESSED, animTexturePressed);
		attachPicOrTexPropertyListener(ControlPropertyLookup.ANIM_TEXTURE_FOCUSED, animTextureFocused);
		attachPicOrTexPropertyListener(ControlPropertyLookup.ANIM_TEXTURE_DEFAULT, animTextureDefault);

		myControl.findProperty(ControlPropertyLookup.DEFAULT).getValueObserver().addListener(
				(observer, oldValue, newValue) -> {
					requestFocus = newValue instanceof SVBoolean && ((SVBoolean) newValue).isTrue();
					requestRender();
				}
		);


		blinkControlHandler = new BlinkControlHandler(myControl.findProperty(ControlPropertyLookup.BLINKING_PERIOD));

		myControl.findProperty(ControlPropertyLookup.COLOR).setValueIfAbsent(true, new SVColorArray(getTextColor()));
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

	private void attachPicOrTexPropertyListener(ControlPropertyLookupConstant lookup, PictureOrTextureHelper helper) {
		myControl.findProperty(lookup).getValueObserver().addListener((observer, oldValue, newValue) -> {
			helper.updateAsync(newValue);
		});
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
				//todo
			} else {
				if (this.mouseOver) {
					
				} else if (focused) {
					double ratio = focusedColorAlternator.updateAndGetRatio();

				} else {

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
