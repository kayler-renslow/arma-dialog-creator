package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/25/2016.
 */
public class StaticRenderer extends ArmaControlRenderer {
	@Override
	protected void init() {
		myControl.findRequiredProperty(ControlPropertyLookup.COLOR_BACKGROUND).getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof AColor) {
					setBackgroundColor(((AColor) newValue).toJavaFXColor());
				}
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
}
