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

	protected Color textColor;

	private Text textObj = new Text();
	private ControlProperty sizeExProperty;

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
				setText(TextHelper.getText(newValue));
				renderer.requestRender();
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
			textShadow = TextHelper.getTextShadow(newValue);
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

	public int getTextWidth() {
		return (int) textObj.getLayoutBounds().getWidth();
	}

	public int getTextHeight() {
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

	/**
	 Will paint the text where there renderer wants to. This will also create a clip for the text.
	 The text will be clipped if it exceeds the width of the control.

	 @param gc context to use
	 */
	public void paint(GraphicsContext gc) {
		gc.beginPath();
		//don't let the text render past the control's bounds
		gc.rect(renderer.getLeftX(), renderer.getTopY(), renderer.getWidth(), renderer.getHeight());
		gc.closePath();
		gc.clip();

		paint(gc, getTextX(), getTextY());
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
		this.textObj.setText(text);
	}

	@NotNull
	public String getText() {
		return this.textObj.getText();
	}

	public void setTextColor(@NotNull Color color) {
		this.textColor = color;
	}

	@NotNull
	public Color getTextColor() {
		return textColor;
	}

	public void resolutionUpdate() {
		if (sizeExProperty.getValue() instanceof SVExpression) {
			updateFont((SVExpression) sizeExProperty.getValue());
		}
	}

	@NotNull
	private Font getFont() {
		return textObj.getFont();
	}

	private void updateFont(@NotNull SVExpression sizeEx) {
		textObj.setFont(TextHelper.getFont(renderer.getResolution(), sizeEx.getNumVal()));
		renderer.requestRender();
	}
}
