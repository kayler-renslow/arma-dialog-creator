/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used with {@link ControlClass#getPropertyUpdateGroup()}

 @author Kayler
 @since 08/11/2016. */
public class ControlPropertyValueUpdate implements ControlPropertyUpdate {
	public enum ValueOrigin {
		/** Caused by {@link Macro} value update via {@link ControlProperty#setValueToMacro(Macro)} */
		MACRO,
		/** Caused by {@link ControlProperty#inherit(ControlProperty)} */
		INHERIT,
		/** By user or program */
		OTHER
	}

	private final ControlProperty controlProperty;
	private final SerializableValue oldValue;
	private final SerializableValue newValue;
	private final ValueOrigin origin;

	public ControlPropertyValueUpdate(@NotNull ControlProperty controlProperty, SerializableValue oldValue, SerializableValue newValue, @NotNull ValueOrigin origin) {
		this.controlProperty = controlProperty;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.origin = origin;
	}

	/** Get the ControlProperty that recieved an update */
	@NotNull
	public ControlProperty getControlProperty() {
		return controlProperty;
	}

	/** Get the ControlProperty's old value */
	@Nullable
	public SerializableValue getOldValue() {
		return oldValue;
	}

	/** Get the ControlProperty's new value */
	@Nullable
	public SerializableValue getNewValue() {
		return newValue;
	}

	/** Get the origin of the value update. */
	@NotNull
	public ValueOrigin getOrigin() {
		return origin;
	}
}
