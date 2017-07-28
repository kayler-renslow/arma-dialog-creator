package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TextHelper;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TextShadow;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.TooltipRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.StructuredTextParseException;
import com.kaylerrenslow.armaDialogCreator.arma.util.StructuredTextParser;
import com.kaylerrenslow.armaDialogCreator.arma.util.TextSection;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.data.ImageHelper;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 A renderer for {@link StructuredTextControl}

 @author Kayler
 @since 07/28/2017 */
public class StructuredTextRenderer extends ArmaControlRenderer {

	private @NotNull List<GraphicTextSection> sections = Collections.emptyList();
	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;

	private @Nullable Color attributesColor = null;
	private @NotNull TextAlignment attributesAlign = TextAlignment.LEFT;
	private @Nullable Color attributesShadowColor = null;
	private double size = 0;
	private @Nullable Double attributesSize = null;

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
				List<TextSection> sections = p.parse();
				this.sections = new ArrayList<>(sections.size());
				for (TextSection section : sections) {
					this.sections.add(new GraphicTextSection(section));
				}
				updateSectionsFont();
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
				updateSectionsFont();
				requestRender();
			}
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
				attributesAlign = getAlignment(alignment);
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
					attributesSize = null;
				}
				updateSectionsFont();
				requestRender();
			});

		}
	}

	private void updateSectionsFont() {
		for (GraphicTextSection section : sections) {
			section.updateFont(this.size, attributesSize, resolution);
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
		gc.beginPath();
		gc.rect(x1, y1, getWidth(), getHeight());
		gc.closePath();
		gc.clip(); //prevent text going out of bounds

	}

	private static TextAlignment getAlignment(String alignment) {
		switch (alignment.toLowerCase()) {
			case "center": {
				return TextAlignment.CENTER;
			}
			case "right": {
				return TextAlignment.RIGHT;
			}
			case "left": {
				//fallthrough
			}
			default: {
				return TextAlignment.LEFT;
			}
		}
	}

	private static class GraphicTextSection {
		private static final double[] buffer = {0, 0, 0, 0};
		private final TextSection.TagName tagName;
		private Font font;
		private Color textColor;
		private TextAlignment alignment = TextAlignment.LEFT;
		private boolean underline;
		private volatile Image image;
		private Color shadowColor;
		private TextShadow shadow;
		private String text;
		private Double textSizePercent;

		public GraphicTextSection(@NotNull TextSection section) {
			this.text = section.getText();
			this.tagName = section.getTagName();
			switch (section.getTagName()) {
				case A: {
					break;
				}
				case T: {
					{ //size
						String sizeAttr = section.getAttributes().get("size");
						if (sizeAttr != null) {
							try {
								textSizePercent = Double.parseDouble(sizeAttr);
							} catch (NumberFormatException ignore) {

							}
						}

					}
					{ //color
						String hexColor = section.getAttributes().get("color");
						if (hexColor != null) {
							try {
								SVHexColor.getColorArray(buffer, hexColor);
								textColor = Color.color(buffer[0], buffer[1], buffer[2]);
							} catch (IllegalArgumentException ignore) {

							}
						}
					}
					{ //alignment
						String alignmentAttr = section.getAttributes().get("align");
						if (alignmentAttr != null) {
							alignment = getAlignment(alignmentAttr);
						}
					}
					{ //shadow
						String shadowAttr = section.getAttributes().get("shadow");
						if (shadowAttr != null) {
							shadow = TextShadow.getTextShadow(shadowAttr);
						}
					}
					{ //shadow color
						String shadowHex = section.getAttributes().get("shadowColor");
						if (shadowHex != null) {
							try {
								SVHexColor.getColorArray(buffer, shadowHex);
								shadowColor = Color.color(buffer[0], buffer[1], buffer[2]);
							} catch (IllegalArgumentException ignore) {

							}
						}
					}
					{ //underline
						String underlineAttr = section.getAttributes().get("underline");
						if (underlineAttr != null) {
							this.underline = underlineAttr.equals("true");
						}
					}
					break;
				}
				case Br: {
					break;
				}
				case Img: {
					String img = section.getAttributes().get("image");
					if (img != null) {
						ImageHelper.getImageAsync(new SVString(img), image1 -> {
							this.image = image1;
							return null;
						});
					}
					break;
				}
				case Root: {
					break;
				}
			}
		}

		public void updateFont(double size, Double attributesSize, ArmaResolution resolution) {
			if (textSizePercent == null) {
				if (attributesSize == null) {
					font = TextHelper.getFont(resolution, size);
				} else {
					font = TextHelper.getFont(resolution, size * attributesSize);
				}
			} else {
				font = TextHelper.getFont(resolution, textSizePercent);
			}
		}
	}
}
