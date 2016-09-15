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

import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.util.MathUtil;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 Created by Kayler on 05/22/2016.
 */
public class ControlProperty {
	public static final ControlProperty[] EMPTY = new ControlProperty[0];

	/** Truncates the double to remove insignificant decimal places */
	public static double truncate(double x) {
		return MathUtil.truncate(x, 8);
	}

	private final ValueListener<?> macroListener = new ValueListener<Object>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
			if (myMacro == null) {
				throw new IllegalStateException("myMacro shouldn't be null");
			}
			setValue(myMacro.getValue());
		}
	};

	private final ControlPropertyLookupConstant propertyLookup;
	private final ValueObserver<SerializableValue> valueObserver;
	private SerializableValue defaultValue;
	private boolean dataOverride = false;
	/** Value to switch to when the set macro becomes null. */
	private SerializableValue beforeMacroValue;
	private @Nullable Macro myMacro;

	/**
	 A control property is something like "idc" or "colorBackground". The current implementation has all values a {@link SerializableValue}. This constructor also sets the default value (retrievable via {@link #getDefaultValue()}) equal to null.

	 @param propertyLookup unique lookup for the property.
	 @param value current value of the property
	 */
	public ControlProperty(ControlPropertyLookupConstant propertyLookup, @Nullable SerializableValue value) {
		this.propertyLookup = propertyLookup;
		valueObserver = new ValueObserver<>(value);
		defaultValue = null;
		beforeMacroValue = value;
	}

	/**
	 This constructor is used for when the values of the property are not set but the number of values stored is determined.
	 For more information on this class, see constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}

	 @param propertyLookup propertyLookup
	 */
	public ControlProperty(ControlPropertyLookupConstant propertyLookup) {
		this(propertyLookup, (SerializableValue) null);
	}

	/**
	 Creates a control property of type {@link SVInteger}<br>
	 See constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 */
	public ControlProperty(ControlPropertyLookupConstant propertyLookup, int value) {
		this(propertyLookup, new SVInteger(value));
	}

	/**
	 Creates a control property of type {@link SVDouble}<br>
	 See constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 */
	public ControlProperty(ControlPropertyLookupConstant propertyLookup, double value) {
		this(propertyLookup, new SVDouble(value));
	}

	/**
	 Creates a control property of type {@link SVBoolean}<br>
	 See constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 */
	public ControlProperty(ControlPropertyLookupConstant propertyLookup, boolean value) {
		this(propertyLookup, SVBoolean.get(value));
	}

	/**
	 Creates a control property of type {@link SVString}<br>
	 See constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 */
	public ControlProperty(ControlPropertyLookupConstant propertyLookup, String value) {
		this(propertyLookup, new SVString(value));
	}

	@NotNull
	public ControlPropertyLookup getPropertyLookup() {
		return (ControlPropertyLookup) propertyLookup;
	}

	/** Return true if the data may not match the type of the control property. This is set by invoking {@link #setDataOverride(boolean)} */
	public boolean isDataOverride() {
		return dataOverride;
	}

	/** @see #isDataOverride() */
	public void setDataOverride(boolean dataOverride) {
		this.dataOverride = dataOverride;
	}

	@NotNull
	public String getName() {
		return propertyLookup.getPropertyName();
	}

	/** Return true if the given type is equal to this instance's property type, false otherwise. (This is effectively doing the same thing as getPropertyType() == PropertyType.something) */
	public boolean isPropertyType(PropertyType type) {
		return getPropertyType() == type;
	}

	@NotNull
	public PropertyType getPropertyType() {
		return propertyLookup.getPropertyType();
	}

	@Nullable
	public SerializableValue getValue() {
		return valueObserver.getValue();
	}

	/** Get the default value for the property */
	@Nullable
	public SerializableValue getDefaultValue() {
		return defaultValue;
	}

	/**
	 Set the default value.

	 @param setValue if true, the ControlProperty value will also be set to the given defaultValue
	 @param defaultValue new default value
	 */
	public void setDefaultValue(boolean setValue, SerializableValue defaultValue) {
		this.defaultValue = defaultValue;
		if (setValue) {
			setValue(defaultValue);
		}
	}

	/**
	 Sets the default value to an integer (uses {@link SVInteger}) (same thing as calling setDefaultValue(new SVInteger(defaultValue)))

	 @see #setDefaultValue(boolean, SerializableValue)
	 */
	public void setDefaultValue(boolean setValue, int defaultValue) {
		setDefaultValue(setValue, new SVInteger(defaultValue));
	}

	/**
	 Sets the default value to an integer (uses {@link SVDouble}) (same thing as calling setDefaultValue(new SVDouble(defaultValue)))

	 @see #setDefaultValue(boolean, SerializableValue)
	 */
	public void setDefaultValue(boolean setValue, double defaultValue) {
		setDefaultValue(setValue, new SVDouble(defaultValue));
	}

	/**
	 Sets the default value to a String (uses {@link SVString}) (same thing as calling setDefaultValue(new SVString(defaultValue)))

	 @see #setDefaultValue(boolean, SerializableValue)
	 */
	public void setDefaultValue(boolean setValue, String defaultValue) {
		setDefaultValue(setValue, new SVString(defaultValue));
	}

	/**
	 Set the control property's values equal to a macro. The properties prior to being set to the macro will be preserved.
	 If this method is invoked again with the macro=null, the preserved values will be inserted. In either scenario, the values observer will be notified of the change.

	 @param m the macro to set to, or null if not to set to macro
	 */
	public void setValueToMacro(@Nullable Macro m) {
		if (m == null) {
			if (this.myMacro != null) {
				myMacro.getValueObserver().removeListener(macroListener);
			}
			this.myMacro = null;
			setValue(beforeMacroValue);
		} else {
			this.myMacro = m;
			this.myMacro.getValueObserver().addValueListener(macroListener);
			beforeMacroValue = valueObserver.getValue();
			setValue(this.myMacro.getValue());
		}
	}

	/** Get the macro that the control property is using, or null if not using a macro */
	@Nullable
	public Macro getMacro() {
		return myMacro;
	}

	/**
	 Get the ControlProperty's value as an int. If the value is of type {@link SVInteger}, this method will succeed.

	 @throws IllegalStateException when ControlProperty's value isn't of type {@link SVInteger}
	 */
	public int getIntValue() {
		if (getValue() == null) {
			throw new NullPointerException("value is null");
		}
		if (getValue() instanceof SVInteger) {
			return ((SVInteger) getValue()).getInt();
		}
		throw new IllegalStateException("Incompatible type fetching. My serializable value class name=" + getValue().getClass().getName());
	}

	/**
	 Get the ControlProperty's value as an double. If the value is of type {@link SVDouble}, this method will succeed.

	 @throws IllegalStateException when ControlProperty's value isn't of type {@link SVDouble}
	 */
	public double getFloatValue() {
		if (getValue() == null) {
			throw new NullPointerException("value is null");
		}
		if (getValue() instanceof SVDouble) {
			return ((SVDouble) getValue()).getDouble();
		}
		throw new IllegalStateException("Incompatible type fetching. My serializable value class name=" + getValue().getClass().getName());
	}

	/**
	 Get the ControlProperty's value as an boolean. If the value is of type {@link SVBoolean}, this method will succeed.

	 @throws IllegalStateException when ControlProperty's value isn't of type {@link SVBoolean}
	 */
	public boolean getBooleanValue() {
		if (getValue() == null) {
			throw new NullPointerException("value is null");
		}
		if (getValue() instanceof SVBoolean) {
			return ((SVBoolean) getValue()).isTrue();
		}
		throw new IllegalStateException("Incompatible type fetching. My serializable value class name=" + getValue().getClass().getName());
	}


	/** Get the observer that observers the values inside this property. Whenever the values get updated, the observer and it's listener will be told so. */
	@NotNull
	public ValueObserver<SerializableValue> getValueObserver() {
		return valueObserver;
	}

	/** Set ControlProperty's value. If a macro is set to the control property, the macro's value will be undisturbed, however, the control property's value will be set to this value. */
	public void setValue(@Nullable SerializableValue v) {
		beforeMacroValue = v;
		valueObserver.updateValue(v);
	}

	/** Set the first value to int. This will just wrap the int in {@link SVInteger} */
	public void setValue(int v) {
		setValue(new SVInteger(v));
	}

	/** Set the first value to int. This will just wrap the int in {@link SVDouble} */
	public void setValue(double v) {
		setValue(new SVDouble(v));
	}

	/** Set the first value to int. This will just wrap the int in {@link SVBoolean} */
	public void setValue(boolean v) {
		setValue(SVBoolean.get(v));
	}

	/** Set the first value to String. This will just wrap the String in {@link SVString} */
	public void setValue(String v) {
		setValue(new SVString(v));
	}

	/** Return true if instanceof {@link ControlProperty} and {@link #getPropertyLookup()} is reference-equivalent. */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ControlProperty)) {
			return false;
		}
		ControlProperty other = (ControlProperty) o;
		return this.propertyLookup == other.propertyLookup;
	}

	@Override
	public String toString() {
		return "ControlProperty{" +
				"propertyLookup=" + propertyLookup +
				", value=" + (getValue() != null ? Arrays.toString(getValue().getAsStringArray()) : "null") +
				'}';
	}
}
