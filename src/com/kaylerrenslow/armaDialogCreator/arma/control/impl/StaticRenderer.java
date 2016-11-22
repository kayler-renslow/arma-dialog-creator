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
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BasicTextRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.AFont;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/25/2016.
 */
public class StaticRenderer extends ArmaControlRenderer {

	private final BlinkControlHandler blinkControlHandler;
	private BasicTextRenderer textRenderer;

	public StaticRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this, ControlPropertyLookup.TEXT, ControlPropertyLookup.COLOR_TEXT, ControlPropertyLookup.STYLE);

		myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				getBackgroundColorObserver().updateValue((AColor) newValue);
			}
		});
		myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND).setDefaultValue(true, new AColor(getBackgroundColor()));
		myControl.findProperty(ControlPropertyLookup.COLOR_TEXT).setDefaultValue(true, new AColor(getTextColor()));
		myControl.findProperty(ControlPropertyLookup.TEXT).setDefaultValue(true, "");
		myControl.findProperty(ControlPropertyLookup.FONT).setDefaultValue(true, AFont.DEFAULT);
		blinkControlHandler = new BlinkControlHandler(myControl.findProperty(ControlPropertyLookup.BLINKING_PERIOD));
	}


	public void paint(@NotNull GraphicsContext gc, @NotNull DataContext dataContext) {
		if (paintPreview(dataContext)) {
			blinkControlHandler.paint(gc, dataContext);
		}

		super.paint(gc, dataContext);
		textRenderer.paint(gc);
	}

	@Override
	public void setTextColor(@NotNull Color color) {
		this.textRenderer.setTextColor(color);
	}

	@Override
	public Color getTextColor() {
		return textRenderer.getTextColor();
	}
}
