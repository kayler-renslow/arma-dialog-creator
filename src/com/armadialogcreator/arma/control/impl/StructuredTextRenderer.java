package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlRenderer;
import com.armadialogcreator.arma.control.impl.utility.BlinkControlHandler;
import com.armadialogcreator.arma.control.impl.utility.TextHelper;
import com.armadialogcreator.arma.control.impl.utility.TextShadow;
import com.armadialogcreator.arma.control.impl.utility.TooltipRenderer;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.arma.util.TextSection;
import com.armadialogcreator.canvas.CanvasContext;
import com.armadialogcreator.core.old.ControlClassOld;
import com.armadialogcreator.core.old.ControlProperty;
import com.armadialogcreator.core.old.ControlPropertyLookup;
import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.data.olddata.ImageHelper;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.FontMetrics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

	private @NotNull List<GraphicTextSection> sections = Collections.emptyList();
	private BlinkControlHandler blinkControlHandler;
	private TooltipRenderer tooltipRenderer;

	private double size = 0;
	private @Nullable Double attributesSize = null;

	private final SectionData defaultSectionData = new SectionData();

	private String text = null;

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
			this.text = TextHelper.getText(newValue);
			/*
			todo when the renderer is fully implemented, uncomment this
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
			*/
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
			ControlClassOld attributes = myControl.findNestedClass(StructuredTextControl.NestedClassName_Attributes);
			addValueListener(attributes, ControlPropertyLookup.COLOR__HEX, (observer, oldValue, newValue) -> {
				Color c = null;
				if (newValue instanceof SVColor) {
					c = ((SVColor) newValue).toJavaFXColor();
				}
				defaultSectionData.textColor = c;
				requestRender();
			});
			addValueListener(attributes, ControlPropertyLookup.ALIGN, (observer, oldValue, newValue) -> {
				String alignment = newValue == null ? "" : newValue.toString();
				defaultSectionData.alignment = getAlignment(alignment);
				requestRender();
			});
			addValueListener(attributes, ControlPropertyLookup.SHADOW_COLOR, (observer, oldValue, newValue) -> {
				Color c = null;
				if (newValue instanceof SVColor) {
					c = ((SVColor) newValue).toJavaFXColor();
				}
				defaultSectionData.shadowColor = c;
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

		updateSectionsFont();
	}

	private void updateSectionsFont() {
		for (GraphicTextSection section : sections) {
			section.updateFont(this.size, attributesSize, resolution);
		}
		defaultSectionData.updateFont(this.size, attributesSize, resolution);
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
		gc.setFill(defaultSectionData.textColor == null ? Color.RED : defaultSectionData.textColor);
		gc.setFont(defaultSectionData.font);
		gc.fillText(this.text, x1 + (int) (getWidth() * .025), y1 + (int) (getHeight() * .025));
	}

	/** Note (July 29, 2017) this is the implementation for painting structured text. Notice it isn't done yet */
	@Deprecated
	private void paintStructuredText(@NotNull GraphicsContext gc) {
		final int controlWidth = getWidth();
		gc.beginPath();
		gc.rect(x1, y1, controlWidth, getHeight());
		gc.closePath();
		gc.clip(); //prevent text going out of bounds


		int rowWidth = 0;
		int rowY = y1;

		for (GraphicTextSection section : sections) {
			gc.setFont(section.font);
			if (section.textColor == null) {
				gc.setFill(defaultSectionData.textColor);
			} else {
				gc.setFill(section.textColor);
			}

			boolean outOfBounds = section.textWidth + rowWidth > controlWidth;
			if (outOfBounds) {
				FontMetrics fontMetrics = new FontMetrics(section.font);
				StringBuilder sb = new StringBuilder(section.text);
				//break the text into multiple lines
				while (section.textWidth + rowWidth > controlWidth) {

				}
			} else {

			}
		}
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

	private static class GraphicTextSection extends SectionData {
		public final TextSection.TagName tagName;

		public GraphicTextSection(@NotNull TextSection section) {
			this.tagName = section.getTagName();
			this.text = section.getText();
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
								double[] buffer = {0, 0, 0, 0};
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
								double[] buffer = {0, 0, 0, 0};
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

	}

	private static class SectionData {
		public @NotNull Font font = Font.font(15);
		public Color textColor;
		public TextAlignment alignment;
		public boolean underline;
		public volatile Image image;
		public Color shadowColor;
		public TextShadow shadow;
		public String text;
		public Double textSizePercent;
		public int textWidth;
		public int textHeight;

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

			FontMetrics fontMetrics = new FontMetrics(font);
			this.textWidth = fontMetrics.computeStringWidth(text);
			this.textHeight = fontMetrics.getLineHeight();
		}
	}

}
