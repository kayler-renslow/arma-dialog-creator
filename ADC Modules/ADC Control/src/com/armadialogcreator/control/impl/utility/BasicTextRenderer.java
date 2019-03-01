package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.canvas.FontMetrics;
import com.armadialogcreator.canvas.Resolution;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 A utility class for rendering text with a {@link ArmaControlRenderer} which can have the following property updates:
 <ul>
 <li>Text color</li>
 <li>Text horizontal alignment (left, center, right)</li>
 <li>Font size</li>
 <li>Text shadow</li>
 <li>Multiple lines</li>
 </ul>

 @author Kayler
 @since 11/21/2016 */
public class BasicTextRenderer {

	public static final double TEXT_PADDING = 0.025;

	private enum Case {
		Upper, Lower, Default
	}

	private final ArmaControl control;
	private final ArmaControlRenderer renderer;
	private final UpdateCallback callback;

	private Color textColor = Color.BLACK;
	private ConfigProperty sizeExProperty;

	private TextShadow textShadow = TextShadow.None;

	private int textWidth, textLineHeight;
	private TextAlignment textAlignment = TextAlignment.CENTER;
	private String textInOriginalCase = "";
	private Font font = Font.getDefault();

	/** True if the text renderer is painting multiple lines, false otherwise */
	private boolean multiline = false;
	private FontMetrics fontMetrics;
	/**
	 An array of Strings where each string in the array is a single line. You can think of this as a String broken
	 up by newline characters.
	 */
	private String[] cachedBrokenLines = null;
	private int lastControlX = -1, lastControlY = -1, lastControlArea = -1;
	/** True if {@link #multiline} is allowed, false if {@link #multiline} should be ignored */
	private boolean allowMultiLine = false;
	private String textInForcedCase = "";
	private Case textCase = Case.Default;

	public BasicTextRenderer(@NotNull ArmaControl control, @NotNull ArmaControlRenderer renderer,
							 @Nullable ConfigPropertyLookupConstant text,
							 @NotNull ConfigPropertyLookupConstant colorText, @Nullable ConfigPropertyLookupConstant style,
							 @Nullable ConfigPropertyLookupConstant sizeEx, @Nullable ConfigPropertyLookup shadow,
							 boolean autoInitializeTextColor, @NotNull BasicTextRenderer.UpdateCallback callback) {
		this.control = control;
		this.renderer = renderer; //we can't do control.getRenderer() because it may not be initialized yet
		this.callback = callback;
		init(text, colorText, style, sizeEx, shadow, autoInitializeTextColor);
	}

	private void init(@Nullable ConfigPropertyLookupConstant text, @NotNull ConfigPropertyLookupConstant colorText,
					  @Nullable ConfigPropertyLookupConstant style, @Nullable ConfigPropertyLookupConstant sizeEx,
					  @Nullable ConfigPropertyLookup shadow, boolean autoInitializeTextColor) {

		setFont(this.font); //pre-set font so that we can initialize text right away. Also, set font metrics

		if (text != null) {
			renderer.addValueListener(text, (observer, oldValue, newValue) -> {

				setText(TextHelper.getText(newValue));
				callback.textUpdate(newValue);
						renderer.requestRender();
					}
			);
		}
		ConfigProperty textColorProp = control.findProperty(colorText);
		if (autoInitializeTextColor) {
			textColorProp.setValue(new SVColorArray(renderer.getBackgroundColor().invert()));
		}
		renderer.addValueListener(colorText, (observer, oldValue, newValue) -> {
					if (newValue instanceof SVColor) {
						setTextColor(((SVColor) newValue).toJavaFXColor());
						callback.textColorUpdate(newValue);
						renderer.requestRender();
					}
				}
		);

		if (shadow != null) {
			renderer.addValueListener(shadow, (observer, oldValue, newValue) -> {
				textShadow = TextShadow.getTextShadow(newValue);
				callback.textShadowUpdate(newValue);
				renderer.requestRender();
			});
		}
		if (style != null) {
			renderer.addValueListener(style,
					(observer, oldValue, newValue) -> {
						newValue = MiscHelpers.getGroup(this.renderer.getEnv(), newValue, control);
						if (newValue != null) {
							SVControlStyleGroup group = (SVControlStyleGroup) newValue;

							textAlignment = TextAlignment.LEFT;
							setMultiline(false);
							textCase = Case.Default;
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
								if (controlStyle == ControlStyle.MULTI) {
									setMultiline(true);
									continue;
								}
								if (controlStyle == ControlStyle.UPPERCASE) {
									textCase = Case.Upper;
									continue;
								}
								if (controlStyle == ControlStyle.LOWERCASE) {
									textCase = Case.Lower;
									continue;
								}
							}
							setText(this.textInOriginalCase); //update the text case
						}
						callback.styleUpdate(newValue);
						renderer.requestRender();
					}
			);
		}
		if (sizeEx != null) {
			sizeExProperty = control.findProperty(sizeEx);
			renderer.addValueListener(sizeEx, (observer, oldValue, newValue) -> {
						if (newValue instanceof SVExpression) {
							SVExpression ex = (SVExpression) newValue;
							updateFontSize(ex);
							renderer.requestRender();
						}
				callback.fontUpdate(newValue);
					}
			);
		}

		renderer.getResolutionUpdateGroup().addListener(new UpdateGroupListener<Resolution>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<Resolution> group, @NotNull Resolution data) {
				resolutionUpdate();
			}
		});
	}

	public int getTextWidth() {
		return textWidth;
	}

	public int getTextLineHeight() {
		return textLineHeight;
	}

	private int getTextX() {
		int textWidth = getTextWidth();
		TextAlignment textAlignment = this.textAlignment;
		if (this.multiline) {
			textAlignment = TextAlignment.LEFT;
		}
		switch (textAlignment) {
			case LEFT: {
				return renderer.getLeftX() + (int) (renderer.getWidth() * TEXT_PADDING);
			}
			case RIGHT: {
				return renderer.getRightX() - textWidth - (int) (renderer.getWidth() * TEXT_PADDING);
			}
			default:
			case CENTER: {
				return renderer.getLeftX() + (renderer.getWidth() - textWidth) / 2;
			}
		}
	}

	private int getTextY() {
		return renderer.getTopY() + (renderer.getHeight() - textLineHeight) / 2;
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
		if (multiline && allowMultiLine) {
			int controlWidth = renderer.getWidth();

			//check if cachedBrokenLines need to be updated
			if (this.cachedBrokenLines == null
					|| this.lastControlX != renderer.getX1()
					|| this.lastControlY != renderer.getY1()
					|| this.lastControlArea != renderer.getArea()) {

				this.lastControlX = renderer.getX1();
				this.lastControlY = renderer.getY1();
				this.lastControlArea = renderer.getArea();

				//update cachedBrokenLines
				String[] words = textInForcedCase.split("\\s"); //split by space
				StringBuilder lineBuilder = new StringBuilder();

				ArrayList<String> linesList = new ArrayList<>(words.length);
				final int spaceWidth = (int) fontMetrics.computeStringWidth(" ");
				final int textPadding = (int) (renderer.getWidth() * TEXT_PADDING);
				if (words.length > 1) {
					int curWidth = textPadding;
					for (String word : words) {
						int wordWidth = (int) fontMetrics.computeStringWidth(word) + spaceWidth;
						if (curWidth + wordWidth >= controlWidth) {
							linesList.add(lineBuilder.toString());
							lineBuilder = new StringBuilder();
							curWidth = wordWidth;
							lineBuilder.append(word);
							lineBuilder.append(' ');
						} else {
							curWidth += wordWidth;
							lineBuilder.append(word);
							lineBuilder.append(' ');
						}
					}
					linesList.add(lineBuilder.toString()); //append any remaining text
					this.cachedBrokenLines = linesList.toArray(new String[linesList.size()]);
				} else {
					this.cachedBrokenLines = words;
				}
			}

			//paint the text as multiple cachedBrokenLines
			int lineNum = 0;
			textX = renderer.getLeftX();
			textY = renderer.getTopY();
			for (String line : cachedBrokenLines) {
				TextHelper.paintText(
						gc, textX, textY + lineNum * textLineHeight, font, line, textColor, textShadow, Color.BLACK
				);
				lineNum++;
			}
		} else {
			//paint all of the text as a single line

			TextHelper.paintText(
					gc, textX, textY, font, getText(), textColor, textShadow, Color.BLACK
			);
		}
	}

	public void setText(@NotNull String text) {
		this.textInOriginalCase = text;
		switch (textCase) {
			case Lower: {
				this.textInForcedCase = text.toLowerCase();
				break;
			}
			case Upper: {
				this.textInForcedCase = text.toUpperCase();
				break;
			}
			case Default: {
				this.textInForcedCase = text;
				break;
			}
		}
		textWidth = Math.round(fontMetrics.computeStringWidth(this.textInForcedCase));
		textLineHeight = Math.round(fontMetrics.getLineHeight());
		clearCachedBrokenLines();
	}

	@NotNull
	public String getText() {
		return textInForcedCase;
	}

	@NotNull
	public String getTextInOriginalCase() {
		return this.textInOriginalCase;
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
		this.fontMetrics = new FontMetrics(font);
		this.setText(this.textInOriginalCase); //update text width and line height
		clearCachedBrokenLines();
	}

	private void updateFontSize(@NotNull SVNumericValue sizeEx) {
		setFont(TextHelper.getFont(renderer.getResolution(), sizeEx.toDouble()));
	}

	/**
	 Set if the text renderer will render the text in multiple lines.
	 <p>
	 If set to true, this will also force the text to be initially placed at the top left corner of the control and
	 then have line breaks automatically inserted (this will not affect {@link #getText()}).

	 @param multiline true if to use multiple lines, false otherwise
	 */
	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
		clearCachedBrokenLines();
	}

	private void clearCachedBrokenLines() {
		this.cachedBrokenLines = null;
	}

	public void setAllowMultiLine(boolean allowMultiline) {
		this.allowMultiLine = allowMultiline;
		clearCachedBrokenLines();
	}

	public interface UpdateCallback {
		default void textUpdate(@Nullable SerializableValue newValue) {
		}

		default void textColorUpdate(@Nullable SerializableValue newValue) {
		}

		default void fontUpdate(@Nullable SerializableValue newValue) {
		}

		default void multilineUpdate(@Nullable SerializableValue newValue) {
		}

		default void styleUpdate(@Nullable SerializableValue newValue) {
		}

		default void textShadowUpdate(@Nullable SerializableValue newValue) {
		}
	}
}
