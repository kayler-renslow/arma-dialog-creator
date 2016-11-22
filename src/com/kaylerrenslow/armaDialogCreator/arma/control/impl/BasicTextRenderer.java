/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
	protected static final Font DEFAULT_FX_FONT = Font.font(20d);
	private final ArmaControl control;
	private final ArmaControlRenderer renderer;

	private static final int ALIGN_LEFT = 0;
	private static final int ALIGN_CENTER = 1;
	private static final int ALIGN_RIGHT = 2;

	private int alignment = ALIGN_CENTER;
	protected Color textColor;

	private Text textObj = new Text();

	public BasicTextRenderer(ArmaControl control, ArmaControlRenderer renderer, ControlPropertyLookupConstant text, ControlPropertyLookupConstant colorText, ControlPropertyLookupConstant style) {
		this.control = control;
		this.renderer = renderer;
		textColor = renderer.getBackgroundColor().invert();
		init(text, colorText, style);
	}

	private void init(ControlPropertyLookupConstant text, ControlPropertyLookupConstant colorText, ControlPropertyLookupConstant style) {
		textObj.setFont(DEFAULT_FX_FONT);
		control.findProperty(text).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue == null) {
					setText("");
				} else {
					setText(newValue.toString().replaceAll("\"\"", "\""));
				}
				renderer.render();
			}
		});
		control.findProperty(colorText).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof AColor) {
					setTextColor(((AColor) newValue).toJavaFXColor());
				}
				renderer.render();
			}
		});
		control.findProperty(style).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof ControlStyleGroup) {
					ControlStyleGroup group = (ControlStyleGroup) newValue;
					for (ControlStyle style : group.getValues()) {
						if (style == ControlStyle.LEFT) {
							alignment = ALIGN_LEFT;
							break;
						} else if (style == ControlStyle.CENTER) {
							alignment = ALIGN_CENTER;
							break;
						} else if (style == ControlStyle.RIGHT) {
							alignment = ALIGN_RIGHT;
							break;
						}
					}
					renderer.render();
				}
			}
		});
	}

	private int getTextX() {
		int textWidth = (int) textObj.getLayoutBounds().getWidth();
		switch (alignment) {
			case ALIGN_LEFT: {
				return renderer.getLeftX() + (int) (renderer.getWidth() * 0.01);
			}
			case ALIGN_RIGHT: {
				return renderer.getRightX() - textWidth - (int) (renderer.getWidth() * 0.01);
			}
			default:
			case ALIGN_CENTER: {
				return renderer.getLeftX() + (renderer.getWidth() - textWidth) / 2;
			}
		}
	}

	private int getTextY() {
		int textHeight = (int) (textObj.getLayoutBounds().getHeight() * 0.25);
		return renderer.getTopY() + (renderer.getHeight() - textHeight) / 2;
	}

	public void paint(GraphicsContext gc) {
		gc.beginPath();
		gc.rect(renderer.getLeftX(), renderer.getTopY(), renderer.getWidth(), renderer.getHeight());
		gc.clip();
		gc.setFont(getFont());
		gc.setFill(textColor);
		gc.fillText(getText(), getTextX(), getTextY());
		gc.closePath();
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
}
