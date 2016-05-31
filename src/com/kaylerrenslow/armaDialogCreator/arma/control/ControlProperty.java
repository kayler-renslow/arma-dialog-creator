package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.AHexColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.ASound;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/22/2016.
 */
public class ControlProperty {
	public static final ControlProperty[] EMPTY = new ControlProperty[0];

	private final String name;
	private final PropertyType type;
	private String[] values;
	private final int propertyId;

	public enum PropertyType {
		/** Is a integer value. Current implementation is a 32 bit integer (java int) */
		INT,
		/** Is a floating point value. The current implementation uses 32 bit floating point (java double) */
		FLOAT,
		/** Is a boolean (0 for false, 1 for true) */
		BOOLEAN,
		/** Is a String */
		STRING,
		/** Generic array property type */
		ARRAY,
		/** Color array string ({r,g,b,a} where r,g,b,a are from 0 to 1 inclusively) */
		COLOR,
		/** Is an array that is formatted to fit a sound and its params */
		SOUND,
		/** Is font name */
		FONT,
		/** Denotes a file name inside a String */
		FILE_NAME,
		/** Denotes an image path inside a String */
		IMAGE,
		/** Color is set to a hex string like #ffffff or #ffffffff */
		HEX_COLOR_STRING,
		/** example: #(argb,8,8,3)color(1,1,1,1) */
		TEXTURE,
		/** Is an SQF code string, but this propertyType is an easy way to categorize all event handlers. */
		EVENT,
		/** SQF code String */
		SQF,
		/** The type can be anything and/or can vary */
		CUSTOM
	}

	/**
	 A control property is something like "idc" or "colorBackground". The current implementation puts all values inside a String array so that array serialization isn't needed.<br>
	 For types that aren't array, only 1 entry will be inside the array and that is the value.

	 @param propertyId unique id for the property. This comes from ControlPropertiesLookup.propertyId
	 @param name name of the property
	 @param type type of the property (integer, float, array, String)
	 @param values current values of the property. (if non-array, just create a String array of dimension 1 with value at index 0). If values are currently not set, use the constructor ControlProperty(int propertyId, @NotNull String name, @NotNull PropertyType type, int numValues)
	 */
	public ControlProperty(int propertyId, @NotNull String name, @NotNull PropertyType type, @NotNull String[] values) {
		this.propertyId = propertyId;
		this.name = name;
		this.type = type;
		this.values = values;
	}

	/**
	 This constructor is used for when the values of the property are not set but the number of values stored is determined. For more information on this class, see constructor ControlProperty(int propertyId, @NotNull String name, @NotNull PropertyType type, @NotNull String[] values)

	 @param propertyId propertyId
	 @param name name of property
	 @param type type of property
	 @param numValues number of values that will be stored in the property (must be >=1).
	 */
	public ControlProperty(int propertyId, @NotNull String name, @NotNull PropertyType type, int numValues) {
		if (numValues <= 0) {
			throw new IllegalArgumentException("Number of values must be >= 1");
		}
		this.propertyId = propertyId;
		this.name = name;
		this.type = type;
		this.values = new String[numValues];
	}

	/**
	 Creates a control property of type Object (the value will be the .toString() value of the object)<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, @NotNull PropertyType type, @NotNull Object value) {
		this(propertyId, name, type, new String[]{value.toString()});
	}

	/**
	 Creates a control property of type Object (the value will be the .toString() value of the object)<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, @NotNull Object value) {
		this(propertyId, name, PropertyType.CUSTOM, new String[]{value.toString()});
	}

	/**
	 Creates a control property of type String<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, @NotNull String value) {
		this(propertyId, name, PropertyType.STRING, new String[]{value});
	}

	/**
	 Creates a control property of type String<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, @NotNull AHexColor value) {
		this(propertyId, name, PropertyType.STRING, new String[]{value.getHexColor()});
	}

	/**
	 Creates a control property of type Int<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, int value) {
		this(propertyId, name, PropertyType.INT, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Float<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, double value) {
		this(propertyId, name, PropertyType.FLOAT, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Boolean<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, boolean value) {
		this(propertyId, name, PropertyType.BOOLEAN, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Color<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, AColor value) {
		this(propertyId, name, PropertyType.COLOR, value.getAsStringArray());
	}

	/**
	 Creates a control property of type Sound<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, ASound value) {
		this(propertyId, name, PropertyType.SOUND, value.getAsStringArray());
	}

	/**
	 Creates a control property of type Font<br>
	 See constructor ControlProperty(int propertyId, String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(int propertyId, @NotNull String name, AFont value) {
		this(propertyId, name, PropertyType.FONT, new String[]{value.name()});
	}

	public int getPropertyId() {
		return propertyId;
	}

	/** Get whether or not all values are set inside the property. */
	public boolean valuesSet() {
		for (String s : values) {
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
		return values;
	}

	public String getStringValue() {
		if (type != PropertyType.STRING) {
			throw new IllegalStateException("Incompatible type fetching. My property type=" + type);
		}
		return values[0];
	}

	public int getIntValue() {
		try {
			return Integer.valueOf(values[0]);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Incompatible type fetching. My property type=" + type);
		}
	}

	public double getFloatValue() {
		try {
			return Double.valueOf(values[0]);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Incompatible type fetching. My property type=" + type);
		}
	}

	public boolean getBooleanValue() {
		try {
			return Boolean.valueOf(values[0]);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Incompatible type fetching. My property type=" + type);
		}
	}

	protected void setValues(String[] values) {
		this.values = values;
	}

	protected void setValue(String v) {
		this.values[0] = v;
	}

	protected void setValue(int v) {
		this.values[0] = v + "";
	}

	protected void setValue(double v) {
		this.values[0] = v + "";
	}

	protected void setValue(boolean v) {
		this.values[0] = v + "";
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

}
