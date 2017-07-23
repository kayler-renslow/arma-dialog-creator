package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 A renderer for {@link ComboControl}

 @author Kayler
 @since 07/23/2017 */
public class ComboRenderer extends ArmaControlRenderer {

	private BlinkControlHandler blinkControlHandler;

	private BasicTextRenderer textRenderer;
	private TooltipRenderer tooltipRenderer;

	private final ImageOrTextureHelper arrowEmpty_combo = new ImageOrTextureHelper(this);
	private final ImageOrTextureHelper arrowFull_combo = new ImageOrTextureHelper(this);
	private Color colorSelect = Color.RED;
	private Color colorDisabled = Color.BLACK;
	private double wholeHeight = 0.45;

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
				ControlPropertyLookup.SHADOW
		);

		ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND);
		{
			colorBackground.getValueObserver().addListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
					if (newValue instanceof SVColor) {
						getBackgroundColorObserver().updateValue((SVColor) newValue);
					}
				}
			});
			colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));

			if (colorBackground.getValue() instanceof SVColor) {
				setBackgroundColor(((SVColor) colorBackground.getValue()).toJavaFXColor());
			}
		}

		myControl.findProperty(ControlPropertyLookup.COLOR_TEXT).setValueIfAbsent(true, new SVColorArray(getTextColor()));
		myControl.findProperty(ControlPropertyLookup.FONT).setValueIfAbsent(true, SVFont.DEFAULT);

		blinkControlHandler = new BlinkControlHandler(myControl.findProperty(ControlPropertyLookup.BLINKING_PERIOD));

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.TOOLTIP
		);

		myControl.findProperty(ControlPropertyLookup.ARROW_EMPTY).addValueListener((observer, oldValue, newValue) -> {
			arrowEmpty_combo.updateAsync(newValue);
		});
		myControl.findProperty(ControlPropertyLookup.ARROW_FULL).addValueListener((observer, oldValue, newValue) -> {
			arrowFull_combo.updateAsync(newValue);
		});

		myControl.findProperty(ControlPropertyLookup.COLOR_SELECT).addValueListener((observer, oldValue, newValue) -> {
			if (newValue instanceof SVColor) {
				colorSelect = ((SVColor) newValue).toJavaFXColor();
				requestRender();
			}
		});

		myControl.findProperty(ControlPropertyLookup.WHOLE_HEIGHT).addValueListener((observer, oldValue, newValue) -> {
			if (newValue instanceof SVNumericValue) {
				wholeHeight = ((SVNumericValue) newValue).toDouble();
				requestRender();
			}
		});

		{
			ControlClass comboScrollBar = myControl.findNestedClass(ComboControl.NestedClassName_ComboScrollBar);
			//todo
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
		} else {
			if (preview) {
				blinkControlHandler.paint(gc);
			}
			super.paint(gc, canvasContext);
			if (preview && menuDown) {
				paintArrow(gc, arrowFull_combo);
			} else {
				paintArrow(gc, arrowFull_combo);
			}
			textRenderer.paint(gc);
		}

		if (preview) {
			if (this.mouseOver) {
				canvasContext.paintLast(tooltipRenderFunc);
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
}
