package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import org.jetbrains.annotations.NotNull;

/**
 A utility class for rendering text with a {@link ArmaControlRenderer} which can have the following property updates:
 <ul>
 <li>Text Color</li>
 <li>Text Position (left, center, right)</li>
 </ul>

 @author Kayler
 @since 11/21/2016 */
public class BasicTextRenderer {
	private final ArmaControl control;
	private final ArmaControlRenderer renderer;

	protected Color textColor;

	private Text textObj = new Text();
	private ControlProperty sizeExProperty;

	private enum TextShadow {
		/** Has no shadow */
		None,
		/**
		 Text is rendered twice: once at the original position,
		 but slightly offset with black text and second time is normally
		 at the original position.
		 */
		DropShadow,
		/** The text has a black stroke to it */
		Stroke
	}

	private TextShadow textShadow = TextShadow.None;

	public BasicTextRenderer(ArmaControl control, ArmaControlRenderer renderer, ControlPropertyLookupConstant text,
							 ControlPropertyLookupConstant colorText, ControlPropertyLookupConstant style,
							 ControlPropertyLookupConstant sizeEx, ControlPropertyLookup shadow) {
		this.control = control;
		this.renderer = renderer;
		init(text, colorText, style, sizeEx, shadow);
	}

	private void init(ControlPropertyLookupConstant text, ControlPropertyLookupConstant colorText,
					  ControlPropertyLookupConstant style, ControlPropertyLookupConstant sizeEx,
					  ControlPropertyLookup shadow) {
		textObj.setTextOrigin(VPos.TOP);
		textObj.setBoundsType(TextBoundsType.VISUAL);

		control.findProperty(text).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue == null) {
					setText("");
				} else {
					String tostring = newValue.toString();
					if (tostring.length() < 2) {
						//isn't a string literal like "HELLO ""World"""
						setText(cancelQuotes(tostring));
					} else {
						char first = tostring.charAt(0);
						char last = tostring.charAt(tostring.length() - 1);
						if (first == last && (first == '"' || first == '\'')) {
							if (tostring.length() > 2) { //isn't an empty string like "" or ''
								tostring = tostring.substring(1, tostring.length());
							} else {
								tostring = ""; //empty string
							}
						}
						setText(cancelQuotes(tostring));
					}
				}
				renderer.requestRender();
			}

			private String cancelQuotes(@NotNull String s) {
				s = s.replaceAll("([\"'])\\1", "$1"); //remove any "" or '' and convert to " and '
				return s;
			}
		});
		ControlProperty textColorProp = control.findProperty(colorText);
		if (textColorProp.getValue() != null && textColorProp.getValue() instanceof SVColor) {
			textColor = ((SVColor) textColorProp.getValue()).toJavaFXColor();
		}
		textColorProp.setValueIfAbsent(true, new SVColorArray(renderer.getBackgroundColor().invert()));
		textColorProp.getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof SVColor) {
					setTextColor(((SVColor) newValue).toJavaFXColor());
				}
				renderer.requestRender();
			}
		});
		control.findProperty(shadow).getValueObserver().addListener((observer, oldValue, newValue) -> {
			if (newValue != null) {
				String v = newValue.toString();
				if (v.contains("0")) {
					textShadow = TextShadow.None;
				} else if (v.contains("1")) {
					textShadow = TextShadow.DropShadow;
				} else if (v.contains("2")) {
					textShadow = TextShadow.Stroke;
				}
			} else {
				textShadow = TextShadow.None;
			}
			renderer.requestRender();
		});
		control.findProperty(style).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof SVControlStyleGroup) {
					SVControlStyleGroup group = (SVControlStyleGroup) newValue;

					textObj.setTextAlignment(TextAlignment.LEFT);

					for (ControlStyle style : group.getStyleArray()) {
						if (style == ControlStyle.LEFT) {
							textObj.setTextAlignment(TextAlignment.LEFT);
							continue;
						}
						if (style == ControlStyle.CENTER) {
							textObj.setTextAlignment(TextAlignment.CENTER);
							continue;
						}
						if (style == ControlStyle.RIGHT) {
							textObj.setTextAlignment(TextAlignment.RIGHT);
							continue;
						}
					}
					renderer.requestRender();
				}
			}
		});
		sizeExProperty = control.findProperty(sizeEx);
		sizeExProperty.getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof SVExpression) {
					SVExpression ex = (SVExpression) newValue;
					updateFont(ex);
				}
			}
		});

		if (sizeExProperty.getValue() instanceof SVExpression) {
			updateFont((SVExpression) sizeExProperty.getValue());
		}

		renderer.getResolutionUpdateGroup().addListener(new UpdateGroupListener<Resolution>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<Resolution> group, Resolution data) {
				resolutionUpdate();
			}
		});
	}

	private int getTextWidth() {
		return (int) textObj.getLayoutBounds().getWidth();
	}

	private int getTextHeight() {
		return (int) (textObj.getLayoutBounds().getHeight());
	}

	private int getTextX() {
		int textWidth = getTextWidth();
		switch (textObj.getTextAlignment()) {
			case LEFT: {
				return renderer.getLeftX() + (int) (renderer.getWidth() * 0.02);
			}
			case RIGHT: {
				return renderer.getRightX() - textWidth - (int) (renderer.getWidth() * 0.02);
			}
			default:
			case CENTER: {
				return renderer.getLeftX() + (renderer.getWidth() - textWidth) / 2;
			}
		}
	}

	private int getTextY() {
		int textHeight = getTextHeight();
		return renderer.getTopY() + (renderer.getHeight() + textHeight) / 2;
	}

	public void paint(GraphicsContext gc) {
		gc.beginPath();
		//don't let the text render past the control's bounds
		gc.rect(renderer.getLeftX(), renderer.getTopY(), renderer.getWidth(), renderer.getHeight());
		gc.closePath();
		gc.clip();

		gc.setFont(getFont());
		gc.setFill(textColor);

		switch (textShadow) {
			case None: {
				gc.fillText(getText(), getTextX(), getTextY());
				break;
			}
			case DropShadow: {
				final double offset = 2.0;
				gc.setFill(Color.BLACK);
				gc.fillText(getText(), getTextX() + offset, getTextY() + offset);
				gc.setFill(textColor);
				gc.fillText(getText(), getTextX(), getTextY());
				break;
			}
			case Stroke: {
				gc.setLineWidth(2);
				gc.setStroke(Color.BLACK);
				gc.strokeText(getText(), getTextX(), getTextY());
				gc.fillText(getText(), getTextX(), getTextY());
				break;
			}
			default: {
				throw new IllegalStateException("unknown textShadow=" + textShadow);
			}
		}

	}

	private Font getFont() {
		return textObj.getFont();
	}

	public void setText(String text) {
		this.textObj.setText(text);
	}

	public String getText() {
		return this.textObj.getText();
	}

	public void setTextColor(@NotNull Color color) {
		this.textColor = color;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void updateFont(@NotNull SVExpression sizeEx) {
		textObj.setFont(Font.font(fontSize(sizeEx.getNumVal())));
		renderer.requestRender();
	}

	public double fontSize(double percent) {
		double maxPixels = renderer.getResolution().getViewportHeight();
		return toPoints(maxPixels * percent);
	}

	public double toPoints(double pixels) {
		final double pointsPerInch = 72;
		final double pixelsPerInch = 96;
		return pixels * pointsPerInch / pixelsPerInch;
	}

	public void resolutionUpdate() {
		if (sizeExProperty.getValue() instanceof SVExpression) {
			updateFont((SVExpression) sizeExProperty.getValue());
		}
	}
}
