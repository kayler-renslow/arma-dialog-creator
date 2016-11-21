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

	private ControlPropertyLookup lookup;
	private SerializableValue value;
	private String macroKey;
	private Object customData;
	private boolean usingCustomData;

	/**
	 Construct a specification with the given lookup, value, and macro key

	 @param lookup lookup to use
	 @param value value to use (NOTE: will not deepy via {@link SerializableValue#deepCopy()})
	 @param macroKey key to use
	 */
	public ControlPropertySpecification(@NotNull ControlPropertyLookup lookup, @Nullable SerializableValue value, @Nullable String macroKey) {
		this.lookup = lookup;
		this.value = value;
		if (macroKey == null || macroKey.trim().length() == 0) {
			this.macroKey = null;
		} else {
			this.macroKey = macroKey.trim();
		}
	}

	/**
	 Similar to {@link ControlPropertySpecification#ControlPropertySpecification(ControlPropertyLookup, SerializableValue, String)} with value=null and macroKey=null
	 */
	public ControlPropertySpecification(@NotNull ControlPropertyLookup lookup) {
		this(lookup, null, null);
	}

	/**
	 Similar to {@link ControlPropertySpecification#ControlPropertySpecification(ControlProperty, boolean)} with deepCopy=true
	 */
	public ControlPropertySpecification(@NotNull ControlProperty property) {
		this(property, true);
	}

	/**
	 Construct a specification with the given property. If <code>deepCopy</code> is true, the value returned from {@link ControlProperty#getValue()} will be deep copied via
	 {@link SerializableValue#deepCopy()}. If <code>deepCopy</code> is false, will simply use the same value.

	 @param property property to use
	 @param deepCopy true to deep copy {@link ControlProperty#getValue()}, false otherwise
	 */
	public ControlPropertySpecification(@NotNull ControlProperty property, boolean deepCopy) {
		this.lookup = (ControlPropertyLookup) property.getPropertyLookup();
		if (property.getValue() == null) {
			this.value = null;
		} else if (deepCopy) {
			this.value = property.getValue().deepCopy();
		} else {
			this.value = property.getValue();
		}
		if (property.getMacro() != null) {
			this.macroKey = property.getMacro().getKey();
		} else {
			this.macroKey = null;
		}
		this.customData = property.getCustomData();
		this.usingCustomData = property.isUsingCustomData();
	}

	/**
	 Set the custom data (will not deep copy!).

	 @param customData custom data to use
	 */
	public void setCustomData(@Nullable Object customData) {
		this.customData = customData;
	}

	public void setUsingCustomData(boolean usingCustomData) {
		this.usingCustomData = usingCustomData;
	}

	/**
	 Set the value of the specification

	 @param value value to use (will not deep copy!)
	 */
	public void setValue(@Nullable SerializableValue value) {
		this.value = value;
	}

	/**
	 Set the macro key (from {@link Macro#getKey()})

	 @param macroKey key to use, or null if not to use a macro
	 */
	public void setMacroKey(@Nullable String macroKey) {
		this.macroKey = macroKey;
	}

	@Nullable
	public Object getCustomData() {
		return customData;
	}

	public boolean isCustomData() {
		return usingCustomData;
	}

	@NotNull
	public ControlPropertyLookup getPropertyLookup() {
		return lookup;
	}

	@Nullable
	public SerializableValue getValue() {
		return value;
	}

	/** Get the name of the {@link Macro} to be used, or null if no macro should be used. */
	@Nullable
	public String getMacroKey() {
		return macroKey;
	}

	/** Returns a new {@link ControlProperty} instance. Equivalent to invoking {@link ControlProperty#ControlProperty(ControlPropertySpecification, MacroRegistry)} */
	public ControlProperty constructNewControlProperty(@NotNull MacroRegistry registry) {
		return new ControlProperty(this, registry);
	}
}
