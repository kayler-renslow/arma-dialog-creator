package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.AHexColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.ASound;
import com.kaylerrenslow.armaDialogCreator.data.Macro;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.MathUtil;
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

	private final String name;
	private final PropertyType type;
	private final ControlPropertyLookup propertyLookup;
	private ValueObserver<String[]> valuesObserver;
	private String[] defaultValues;
	private boolean dataOverride = false;
	private String[] cacheValues;

	public enum PropertyType {
		/** Is a integer value. Current implementation is a 32 bit integer (java int) */
		INT(Lang.PropertyType.INT),
		/** Is a floating point value. The current implementation uses 32 bit floating point (java double) */
		FLOAT(Lang.PropertyType.FLOAT),
		/** Is a boolean (0 for false, 1 for true) */
		BOOLEAN(Lang.PropertyType.BOOLEAN),
		/** Is a String */
		STRING(Lang.PropertyType.STRING, true),
		/** Generic array property type */
		ARRAY(Lang.PropertyType.ARRAY, 2),
		/** Color array string ({r,g,b,a} where r,g,b,a are from 0 to 1 inclusively) */
		COLOR(Lang.PropertyType.COLOR, 4),
		/** Is an array that is formatted to fit a sound and its params */
		SOUND(Lang.PropertyType.SOUND, 3),
		/** Is font name */
		FONT(Lang.PropertyType.FONT, true),
		/** Denotes a file name inside a String */
		FILE_NAME(Lang.PropertyType.FILE_NAME, true),
		/** Denotes an image path inside a String */
		IMAGE(Lang.PropertyType.IMAGE, true),
		/** Color is set to a hex string like #ffffff or #ffffffff */
		HEX_COLOR_STRING(Lang.PropertyType.HEX_COLOR_STRING, true),
		/** example: #(argb,8,8,3)color(1,1,1,1) however there is more than one way to set texture */
		TEXTURE(Lang.PropertyType.TEXTURE, true),
		/** Is an SQF code string, but this propertyType is an easy way to categorize all event handlers. */
		EVENT(Lang.PropertyType.EVENT, true),
		/** SQF code String */
		SQF(Lang.PropertyType.SQF, true);

		/** Number of values used to represent the data */
		public final int propertyValuesSize;
		/** If true, when this control property is exported, the value should have quotes around it */
		public final boolean exportHasQuotes;
		public final String displayName;

		PropertyType(String displayName) {
			this(displayName, 1);
		}

		PropertyType(String displayName, boolean exportHasQuotes) {
			this.propertyValuesSize = 1;
			this.displayName = displayName;
			this.exportHasQuotes = exportHasQuotes;
		}

		PropertyType(String displayName, int propertyValueSize) {
			if (propertyValueSize <= 0) {
				throw new IllegalArgumentException("Number of values must be >= 1");
			}
			this.displayName = displayName;
			propertyValuesSize = propertyValueSize;
			exportHasQuotes = false;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}

	/**
	 A control property is something like "idc" or "colorBackground". The current implementation puts all values inside a String array so that array serialization isn't needed.<br>
	 For types that aren't array, only 1 entry will be inside the array and that is the value.

	 @param propertyLookup unique lookup for the property.
	 @param name name of the property
	 @param type type of the property (integer, float, array, String)
	 @param values current values of the property. (if non-array, just create a String array of dimension 1 with value at index 0). If values are currently not set, use the constructor ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, @NotNull PropertyType type, int numValues)
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, @NotNull PropertyType type, @NotNull String[] values) {
		this.propertyLookup = propertyLookup;
		this.name = name;
		this.type = type;
		valuesObserver = new ValueObserver<>(values);
		defaultValues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			defaultValues[i] = values[i];
		}
	}

	/**
	 This constructor is used for when the values of the property are not set but the number of values stored is determined. For more information on this class, see constructor ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, @NotNull PropertyType type, @NotNull String[] values)

	 @param propertyLookup propertyLookup
	 @param name name of property
	 @param type type of property
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, @NotNull PropertyType type) {
		this(propertyLookup, name, type, new String[type.propertyValuesSize]);
	}

	/**
	 Creates a control property of type Object (the value will be the .toString() value of the object)<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, @NotNull PropertyType type, @NotNull Object value) {
		this(propertyLookup, name, type, new String[]{value.toString()});
	}

	/**
	 Creates a control property of type String<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, @NotNull String value) {
		this(propertyLookup, name, PropertyType.STRING, new String[]{value});
	}

	/**
	 Creates a control property of type String<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, @NotNull AHexColor value) {
		this(propertyLookup, name, PropertyType.STRING, new String[]{value.getHexColor()});
	}

	/**
	 Creates a control property of type Int<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, int value) {
		this(propertyLookup, name, PropertyType.INT, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Float<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, double value) {
		this(propertyLookup, name, PropertyType.FLOAT, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Boolean<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, boolean value) {
		this(propertyLookup, name, PropertyType.BOOLEAN, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Color<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, AColor value) {
		this(propertyLookup, name, PropertyType.COLOR, value.getAsStringArray());
	}

	/**
	 Creates a control property of type Sound<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, ASound value) {
		this(propertyLookup, name, PropertyType.SOUND, value.getAsStringArray());
	}

	/**
	 Creates a control property of type Font<br>
	 See constructor ControlProperty(ControlPropertyLookup propertyLookup, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(ControlPropertyLookup propertyLookup, @NotNull String name, AFont value) {
		this(propertyLookup, name, PropertyType.FONT, new String[]{value.name()});
	}

	@NotNull
	public ControlPropertyLookup getPropertyLookup() {
		return propertyLookup;
	}

	/** Return true if the data may not match the type of the control property. This is set by invoking {@link #setDataOverride(boolean)} */
	public boolean isDataOverride() {
		return dataOverride;
	}

	/** @see #isDataOverride() */
	public void setDataOverride(boolean dataOverride) {
		this.dataOverride = dataOverride;
	}

	/** Get whether or not all values are set inside the property. */
	public boolean valuesAreSet() {
		for (String s : valuesObserver.getValue()) {
			if (s == null) {
				return false;
			}
		}
		return true;
	}

	@NotNull
	public String getName() {
		return name;
	}

	/** Return true if the given type is equal to this instance's property type, false otherwise. (This is effectively doing the same thing as getType() == PropertyType.something) */
	public boolean isType(PropertyType type) {
		return this.type == type;
	}

	@NotNull
	public PropertyType getType() {
		return type;
	}

	@NotNull
	public String[] getValues() {
		return valuesObserver.getValue();
	}

	/** Get the default values for the property (can be array full of nulls but the array reference won't be null) */
	@Nullable
	public String[] getDefaultValues() {
		return defaultValues;
	}

	/** Get the first default value for the property */
	@Nullable
	public String getFirstDefaultValue() {
		return defaultValues[0];
	}

	/** Set the default values for the property (can be array full of nulls). If setValue is true, the defaultValues given will also be placed in the control property value */
	public void setDefaultValues(boolean setValue, String... defaultValues) {
		this.defaultValues = defaultValues;
		if (setValue) {
			setValues(defaultValues);
		}
	}

	/** Sets the default values to only one integer (same thing as calling setDefaultValues(integer+"")) */
	public void setDefaultValue(boolean setValue, int defaultValue) {
		setDefaultValues(setValue, defaultValue + "");
	}

	/** Sets the default values to only one double (same thing as calling setDefaultValues(doubleNum+"")) */
	public void setDefaultValue(boolean setValue, double defaultValue) {
		setDefaultValues(setValue, defaultValue + "");
	}

	public void setDefaultValue(boolean setValue, AColor defaultValue) {
		setDefaultValues(setValue, defaultValue.getAsStringArray());
	}

	public void setDefaultValue(boolean setValue, AFont defaultValue) {
		setDefaultValues(setValue, defaultValue.name());
	}

	/**
	 Set the control property's values equal to a macro. The properties prior to being set to the macro will be preserved.
	 If this method is invoked again with the macro=null, the preserved values will be inserted. In either scenario, the values observer will be notified of the change.

	 @param m the macro to set to, or null if not to set to macro
	 */
	public void setValueToMacro(@Nullable Macro m) {
		if (m == null) {
			valuesObserver.updateValue(cacheValues);
		} else {
			cacheValues = new String[valuesObserver.getValue().length];
			for (int i = 0; i < cacheValues.length; i++) {
				cacheValues[i] = valuesObserver.getValue()[i];
			}
			throw new IllegalStateException("todo");//todo need to update the valuesObserver. However, how do we extract each index from the macro String array?
		}
	}

	/** Get the first and only value and return it as a String (This can be used for any type, however, it is recommend to not use it on types where there are more than one value (ARRAY, FONT, COLOR, etc)) */
	public String getFirstValue() {
		return valuesObserver.getValue()[0];
	}

	/** Get the first and only int value if the property type is INT */
	public int getIntValue() {
		try {
			return Integer.valueOf(valuesObserver.getValue()[0]);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Incompatible type fetching. My property type=" + type);
		}
	}

	/** Get the first and only float value if the property type is FLOAT */
	public double getFloatValue() {
		try {
			return Double.valueOf(valuesObserver.getValue()[0]);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Incompatible type fetching. My property type=" + type);
		}
	}

	/** Get the first and only boolean value if the property type is BOOLEAN */
	public boolean getBooleanValue() {
		try {
			return Boolean.valueOf(valuesObserver.getValue()[0]);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Incompatible type fetching. My property type=" + type);
		}
	}

	/** Return a String with all the value(s) formatted for header export. If there is more than 1 value in this control property, the curly braces ('{','}') will be prepended and appended before the values */
	public String getValuesForExport() {
		String[] arr = valuesObserver.getValue();
		if (arr.length == 1) {
			if (type.exportHasQuotes) {
				return "\"" + arr[0] + "\"";
			}
			return arr[0];
		}
		String ret = "{";
		String v;
		for (int i = 0; i < arr.length; i++) {
			v = arr[i];
			if (type.exportHasQuotes) {
				v = "\"" + v + "\"";
			}
			ret += v + (i != arr.length - 1 ? "," : "");
		}
		return ret + "}";
	}


	/** Get the observer that observers the values inside this property. Whenever the values get updated, the observer and it's listener will be told so. */
	@NotNull
	public ValueObserver<String[]> getValuesObserver() {
		return valuesObserver;
	}

	/** Sets the first value equal to object.toString() */
	public void setFirstValue(@Nullable Object object) {
		valuesObserver.getValue()[0] = object == null ? null : object.toString();
		valuesObserver.updateValue(valuesObserver.getValue());
	}

	/** Set all values (if a value is intended to be empty (need to be filled), use null) */
	public void setValues(String... values) {
		valuesObserver.updateValue(values);
	}

	/** Update values but only set the value at index valueInd */
	public void setValue(String v, int valueInd) {
		valuesObserver.getValue()[valueInd] = v;
		valuesObserver.updateValue(valuesObserver.getValue());
	}

	/** Set the first value to String (use this whenever the type has values length == 1 (e.g. STRING, INT, FONT but not ARRAY or SOUND)) */
	public void setValue(String v) {
		valuesObserver.getValue()[0] = v;
		valuesObserver.updateValue(valuesObserver.getValue());
	}

	/** Set the first value to int (use this if type==INT) */
	public void setValue(int v) {
		valuesObserver.getValue()[0] = v + "";
		valuesObserver.updateValue(valuesObserver.getValue());
	}

	/** Set the first value to double (use this if type==FLOAT) */
	public void setValue(double v) {
		valuesObserver.getValue()[0] = truncate(v) + "";
		valuesObserver.updateValue(valuesObserver.getValue());
	}

	/** Set the first value to boolean (use this if type==BOOLEAN) */
	public void setValue(boolean v) {
		valuesObserver.getValue()[0] = v + "";
		valuesObserver.updateValue(valuesObserver.getValue());
	}

	/** Set the first value to a color (use this if type==COLOR or HEX_COLOR) */
	public void setValue(AColor color) {
		valuesObserver.updateValue(color.getAsStringArray());
	}

	/** Sets the first value equal to object.toString(). The value is set without notifying listeners */
	public void setFirstValueSilent(@Nullable Object object) {
		valuesObserver.getValue()[0] = object == null ? null : object.toString();
		valuesObserver.updateValueSilent(valuesObserver.getValue());
	}

	/** Set all values (if a value is intended to be empty (need to be filled), use null). The value is set without notifying listeners */
	public void setValuesSilent(String[] values) {
		valuesObserver.updateValueSilent(values);
	}

	/** Update values but only set the value at index valueInd. The value is set without notifying listeners */
	public void setValueSilent(String v, int valueInd) {
		valuesObserver.getValue()[valueInd] = v;
		valuesObserver.updateValueSilent(valuesObserver.getValue());
	}

	/** Set the first value to String (use this whenever the type has values length == 1 (e.g. STRING, INT, FONT but not ARRAY or SOUND)). The value is set without notifying listeners */
	public void setValueSilent(String v) {
		valuesObserver.getValue()[0] = v;
		valuesObserver.updateValueSilent(valuesObserver.getValue());
	}

	/** Set the first value to int (use this if type==INT). The value is set without notifying listeners */
	public void setValueSilent(int v) {
		valuesObserver.getValue()[0] = v + "";
		valuesObserver.updateValueSilent(valuesObserver.getValue());
	}

	/** Set the first value to double (use this if type==FLOAT). The value is set without notifying listeners */
	public void setValueSilent(double v) {
		valuesObserver.getValue()[0] = v + "";
		valuesObserver.updateValueSilent(valuesObserver.getValue());
	}

	/** Set the first value to boolean (use this if type==BOOLEAN). The value is set without notifying listeners */
	public void setValueSilent(boolean v) {
		valuesObserver.getValue()[0] = v + "";
		valuesObserver.updateValueSilent(valuesObserver.getValue());
	}

	/** Set the first value to a color (use this if type==COLOR or HEX_COLOR). The value is set without notifying listeners */
	public void setValueSilent(AColor color) {
		valuesObserver.updateValueSilent(color.getAsStringArray());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ControlProperty)) {
			return false;
		}
		ControlProperty other = (ControlProperty) o;
		return getName().equals(other.getName()) && type == other.type;
	}

	@Override
	public String toString() {
		return "ControlProperty{" +
				"name='" + name + '\'' +
				", type=" + type +
				", propertyLookup=" + propertyLookup +
				", values=" + Arrays.toString(getValues()) +
				'}';
	}
}
