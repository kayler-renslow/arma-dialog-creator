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
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A utility class for rendering text with a {@link ArmaControlRenderer} which can have the following property updates:
 <ul>
 <li>Text color</li>
 <li>Text horizontal alignment (left, center, right)</li>
 <li>Font size</li>
 <li>Text shadow</li>
 </ul>

 @author Kayler
 @since 11/21/2016 */
public class BasicTextRenderer {
	private final ArmaControl control;
	private final ArmaControlRenderer renderer;

	private Color textColor = Color.BLACK;
	private ControlProperty sizeExProperty;

	private TextShadow textShadow = TextShadow.None;

	private int textWidth, textHeight;
	private TextAlignment textAlignment = TextAlignment.CENTER;
	private String text = "";
	private Font font = Font.getDefault();

	public BasicTextRenderer(@NotNull ArmaControl control, @NotNull ArmaControlRenderer renderer,
							 @NotNull ControlPropertyLookupConstant text,
							 @NotNull ControlPropertyLookupConstant colorText, @Nullable ControlPropertyLookupConstant style,
							 @Nullable ControlPropertyLookupConstant sizeEx, @Nullable ControlPropertyLookup shadow) {
		this.control = control;
		this.renderer = renderer; //we can't do control.getRenderer() because it may not be initialized yet
		init(text, colorText, style, sizeEx, shadow);

	}

	private void init(@NotNull ControlPropertyLookupConstant text, @NotNull ControlPropertyLookupConstant colorText,
					  @Nullable ControlPropertyLookupConstant style, @Nullable ControlPropertyLookupConstant sizeEx,
					  @Nullable ControlPropertyLookup shadow) {

		control.findProperty(text).addValueListener((observer, oldValue, newValue) -> {
					setText(TextHelper.getText(newValue));
					renderer.requestRender();
				}
		);
		ControlProperty textColorProp = control.findProperty(colorText);
		textColorProp.setValueIfAbsent(true, new SVColorArray(renderer.getBackgroundColor().invert()));
		textColorProp.addValueListener((observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						setTextColor(((SVColor) newValue).toJavaFXColor());
						renderer.requestRender();
					}
				}
		);
		if (textColorProp.getValue() instanceof SVColor) {
			textColor = ((SVColor) textColorProp.getValue()).toJavaFXColor();
		}
		if (shadow != null) {
			control.findProperty(shadow).addValueListener((observer, oldValue, newValue) -> {
				textShadow = TextHelper.getTextShadow(newValue);
				renderer.requestRender();
			});
		}
		if (style != null) {
			control.findProperty(style).addValueListener((observer, oldValue, newValue) -> {
						if (newValue instanceof SVControlStyleGroup) {
							SVControlStyleGroup group = (SVControlStyleGroup) newValue;

							textAlignment = TextAlignment.LEFT;

							for (ControlStyle controlStyle : group.getStyleArray()) {
								if (controlStyle == ControlStyle.LEFT) {
									textAlignment = TextAlignment.LEFT;
									continue;
								}
								if (controlStyle == ControlStyle.CENTER) {
									textAlignment = TextAlignment.CENTER;
									continue;
								}
								if (controlStyle == ControlStyle.RIGHT) {
									textAlignment = TextAlignment.RIGHT;
									continue;
								}
							}
							renderer.requestRender();
						}
					}
			);
		}
		if (sizeEx != null) {
			sizeExProperty = control.findProperty(sizeEx);
			sizeExProperty.addValueListener((observer, oldValue, newValue) -> {
						if (newValue instanceof SVExpression) {
							SVExpression ex = (SVExpression) newValue;
							updateFontSize(ex);
						}
					}
			);
			if (sizeExProperty.getValue() instanceof SVExpression) {
				updateFontSize((SVExpression) sizeExProperty.getValue());
			}
		}

		renderer.getResolutionUpdateGroup().addListener(new UpdateGroupListener<Resolution>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<Resolution> group, Resolution data) {
				resolutionUpdate();
			}
		});
	}

	public int getTextWidth() {
		return textWidth;
	}

	public int getTextHeight() {
		return textHeight;
	}

	private int getTextX() {
		int textWidth = getTextWidth();
		switch (textAlignment) {
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
		return renderer.getTopY() + (renderer.getHeight() - textHeight) / 2;
	}

	/**
	 Will paint the text where there renderer wants to. This will also create a clip for the text.
	 The text will be clipped if it exceeds the width of the control.

	 @param gc context to use
	 */
	public void paint(GraphicsContext gc) {
		gc.save();
		gc.beginPath();
		//don't let the text render past the control's bounds
		gc.rect(renderer.getLeftX(), renderer.getTopY(), renderer.getWidth(), renderer.getHeight());
		gc.closePath();
		gc.clip();

		paint(gc, getTextX(), getTextY());

		gc.restore();
	}

	/**
	 Paint the text where designated. The text will not be clipped anywhere

	 @param gc context to use
	 @param textX x position of text
	 @param textY y position of text
	 */
	public void paint(GraphicsContext gc, int textX, int textY) {
		TextHelper.paintText(
				gc, textX, textY, getFont(), getText(), textColor, textShadow, Color.BLACK
		);
	}

	public void setText(@NotNull String text) {
		this.text = text;
		FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
		textWidth = Math.round(fontMetrics.computeStringWidth(text));
		textHeight = Math.round(fontMetrics.getLineHeight());
	}

	@NotNull
	public String getText() {
		return text;
	}

	public void setTextColor(@NotNull Color color) {
		this.textColor = color;
	}

	@NotNull
	public Color getTextColor() {
		return textColor;
	}

	public void resolutionUpdate() {
		if (sizeExProperty != null) {
			if (sizeExProperty.getValue() instanceof SVNumericValue) {
				updateFontSize((SVNumericValue) sizeExProperty.getValue());
			}
		}
	}

	@NotNull
	private Font getFont() {
		return font;
	}

	public void setFont(@NotNull Font font) {
		this.font = font;
		updateFontMetrics();
	}

	private void updateFontMetrics() {
		setText(this.text); //update text width and height
		renderer.requestRender();
	}

	private void updateFontSize(@NotNull SVNumericValue sizeEx) {
		setFont(TextHelper.getFont(renderer.getResolution(), sizeEx.toDouble()));
	}
}
