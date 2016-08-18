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
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/25/2016.
 */
public class StaticRenderer extends ArmaControlRenderer {

	protected static final Font DEFAULT_FX_FONT = Font.font(20d);

	protected Color textColor = backgroundColor.invert();

	private Text textObj = new Text();

	public StaticRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		init();
	}

	private void init() {
		textObj.setFont(DEFAULT_FX_FONT);
		myControl.findRequiredProperty(ControlPropertyLookup.COLOR_BACKGROUND).getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				globalBackgroundColorObserver.updateValue((AColor) newValue);
			}
		});
		myControl.findRequiredProperty(ControlPropertyLookup.TEXT).getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue == null) {
					setText("");
				} else {
					setText(newValue.toString().replaceAll("\"\"", "\""));
				}
			}
		});
		myControl.findRequiredProperty(ControlPropertyLookup.COLOR_TEXT).getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof AColor) {
					setTextColor(((AColor) newValue).toJavaFXColor());
				}
			}
		});

	}

	private int getTextX() {
		int textWidth = (int) textObj.getLayoutBounds().getWidth();
		return getLeftX() + (getWidth() - textWidth) / 2;
	}

	private int getTextY() {
		int textHeight = (int) (textObj.getLayoutBounds().getHeight() * 0.25);
		return getTopY() + (getHeight() - textHeight) / 2;
	}

	public void paint(GraphicsContext gc) {
		super.paint(gc);
		gc.setFont(getFont());
		gc.setFill(textColor);
		gc.fillText(getText(), getTextX(), getTextY());
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
