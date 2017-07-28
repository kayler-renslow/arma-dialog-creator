package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TextHelper;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TooltipRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.StructuredTextParseException;
import com.kaylerrenslow.armaDialogCreator.arma.util.StructuredTextParser;
import com.kaylerrenslow.armaDialogCreator.arma.util.TextSection;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVColorArray;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVNumericValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 A renderer for {@link StructuredTextControl}

 @author Kayler
 @since 07/28/2017 */
public class StructuredTextRenderer extends ArmaControlRenderer {

	private @NotNull List<TextSection> sections = Collections.emptyList();
	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;

	private @Nullable Color attributesColor = null;
	private @NotNull TextAlignment attributesAlign = TextAlignment.LEFT;
	private @Nullable Color attributesShadowColor = null;
	private double attributesSize = 1;
	private double size = 0;

	private final Function<GraphicsContext, Void> tooltipRenderFunc = gc -> {
		tooltipRenderer.paint(gc, this.mouseOverX, this.mouseOverY);
		return null;
	};


	public StructuredTextRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);

		ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND);
		{
			addValueListener(colorBackground.getPropertyLookup(), (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
				} else if (newValue == null) {
					//in Arma 3, colorBackground is an optional property
					getBackgroundColorObserver().updateValue(new SVColorArray(Color.TRANSPARENT));
				}
			});
			colorBackground.setValueIfAbsent(true, new SVColorArray(Color.TRANSPARENT));
		}

		addValueListener(ControlPropertyLookup.TEXT, (observer, oldValue, newValue) -> {
			StructuredTextParser p = new StructuredTextParser(TextHelper.getText(newValue));
			try {
				sections = p.parse();
			} catch (StructuredTextParseException e) {
				sections = Collections.emptyList();
			}
			requestRender();
		});

		blinkControlHandler = new BlinkControlHandler(this, ControlPropertyLookup.BLINKING_PERIOD);

		tooltipRenderer = new TooltipRenderer(
				this.myControl, this,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.TOOLTIP
		);

		addValueListener(ControlPropertyLookup.SIZE, (observer, oldValue, newValue) -> {
			if (newValue instanceof SVNumericValue) {
				size = ((SVNumericValue) newValue).toDouble();
			}
			requestRender();
		});

		{
			ControlClass attributes = myControl.findNestedClass(StructuredTextControl.NestedClassName_Attributes);
			addValueListener(attributes, ControlPropertyLookup.COLOR__HEX, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					attributesColor = ((SVColor) newValue).toJavaFXColor();
				} else if (newValue == null) {
					attributesColor = null;
				}
				requestRender();
			});
			addValueListener(attributes, ControlPropertyLookup.ALIGN, (observer, oldValue, newValue) -> {
				String alignment = newValue == null ? "" : newValue.toString();
				switch (alignment.toLowerCase()) {
					case "center": {
						attributesAlign = TextAlignment.CENTER;
						break;
					}
					case "right": {
						attributesAlign = TextAlignment.RIGHT;
						break;
					}
					case "left": {
						//fallthrough
					}
					default: {
						attributesAlign = TextAlignment.LEFT;
						break;
					}
				}
				if (newValue instanceof SVColor) {
					attributesColor = ((SVColor) newValue).toJavaFXColor();
				} else if (newValue == null) {
					attributesColor = null;
				}
				requestRender();
			});
			addValueListener(attributes, ControlPropertyLookup.SHADOW_COLOR, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVColor) {
					attributesShadowColor = ((SVColor) newValue).toJavaFXColor();
				} else if (newValue == null) {
					attributesShadowColor = null;
				}
				requestRender();
			});
			addValueListener(attributes, ControlPropertyLookup.SIZE, (observer, oldValue, newValue) -> {
				if (newValue instanceof SVNumericValue) {
					attributesSize = ((SVNumericValue) newValue).toDouble();
				} else if (newValue == null) {
					attributesSize = 0;
				}
				requestRender();
			});

		}
	}

	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		boolean preview = paintPreview(canvasContext);

		if (preview) {
			blinkControlHandler.paint(gc);
			if (this.mouseOver) {
				canvasContext.paintLast(tooltipRenderFunc);
			}
		}
		super.paint(gc, canvasContext);

	}
}
