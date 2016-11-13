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
 Created by Kayler on 11/12/2016.
 */
public class ControlPropertySpecification {
	public static final ControlPropertySpecification[] EMPTY = new ControlPropertySpecification[0];

	private final ControlPropertyLookup lookup;
	private final SerializableValue value;
	private final String macroKey;
	private Object customData;
	private boolean usingCustomData;

	public ControlPropertySpecification(@NotNull ControlPropertyLookup lookup, @NotNull SerializableValue value, @Nullable String macroKey) {
		this.lookup = lookup;
		this.value = value;
		if (macroKey == null || macroKey.trim().length() == 0) {
			this.macroKey = null;
		} else {
			this.macroKey = macroKey.trim();
		}
	}

	public ControlPropertySpecification(@NotNull ControlProperty property) {
		this.lookup = (ControlPropertyLookup) property.getPropertyLookup();
		if (property.getValue() != null) {
			this.value = property.getValue().deepCopy();
		} else {
			this.value = null;
		}
		if (property.getMacro() != null) {
			this.macroKey = property.getMacro().getKey();
		} else {
			this.macroKey = null;
		}
		this.customData = property.getCustomData();
		this.usingCustomData = property.isCustomData();
	}

	public void setCustomData(@Nullable Object customData) {
		this.customData = customData;
	}

	public void setUsingCustomData(boolean usingCustomData) {
		this.usingCustomData = usingCustomData;
	}

	@Nullable
	public Object getCustomData() {
		return customData;
	}

	public boolean isUsingCustomData() {
		return usingCustomData;
	}

	@NotNull
	public ControlPropertyLookup getLookup() {
		return lookup;
	}

	@NotNull
	public SerializableValue getValue() {
		return value;
	}

	/** Get the name of the {@link Macro} to be used. */
	@Nullable
	public String getMacroKey() {
		return macroKey;
	}

	/** Returns a new {@link ControlProperty} instance. Equivalent to invoking {@link ControlProperty(ControlPropertySpecification} */
	public ControlProperty constructNewControlProperty() {
		return new ControlProperty(this);
	}
}
