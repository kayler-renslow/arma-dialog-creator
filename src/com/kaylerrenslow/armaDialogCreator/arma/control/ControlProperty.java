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

	public enum PropertyType {
		INT,
		FLOAT,
		/** Float between 0 and 1 inclusively */
		FLOAT_RANGE,
		BOOLEAN,
		STRING,
		ARRAY,
		COLOR,
		SOUND,
		FONT,
		/**Denotes a file name inside a String*/
		FILE_NAME,
		/**Denotes an image path inside a String*/
		IMAGE,
		/** Color is set to a hex string like #ffffff or #ffffffff */
		HEX_COLOR_STRING,
		/**example: #(argb,8,8,3)color(1,1,1,1)*/
		TEXTURE,
		CUSTOM /** The type can be anything and/or can vary */
	}

	/**
	 A control property is something like "idc" or "colorBackground". The current implementation puts all values inside a String array so that array serialization isn't needed.<br>
	 For types that aren't array, only 1 entry will be inside the array and that is the value.

	 @param name name of the property
	 @param type type of the property (integer, float, array, String)
	 @param values values of the property. (if non-array, just create a String array of dimension 1 with value at index 0)
	 */
	public ControlProperty(@NotNull String name, @NotNull PropertyType type, @NotNull String[] values) {
		this.name = name;
		this.type = type;
		this.values = values;
	}

	/**
	 Creates a control property of type Object (the value will be the .toString() value of the object)<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, @NotNull PropertyType type, @NotNull Object value) {
		this(name, type, new String[]{value.toString()});
	}

	/**
	 Creates a control property of type Object (the value will be the .toString() value of the object)<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, @NotNull Object value) {
		this(name, PropertyType.CUSTOM, new String[]{value.toString()});
	}

	/**
	 Creates a control property of type String<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, @NotNull String value) {
		this(name, PropertyType.STRING, new String[]{value});
	}

	/**
	 Creates a control property of type String<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, @NotNull AHexColor value) {
		this(name, PropertyType.STRING, new String[]{value.getHexColor()});
	}

	/**
	 Creates a control property of type Int<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, int value) {
		this(name, PropertyType.INT, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Float<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, double value) {
		this(name, PropertyType.FLOAT, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Boolean<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, boolean value) {
		this(name, PropertyType.BOOLEAN, new String[]{value + ""});
	}

	/**
	 Creates a control property of type Color<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, AColor value) {
		this(name, PropertyType.COLOR, value.getAsStringArray());
	}

	/**
	 Creates a control property of type Sound<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, ASound value) {
		this(name, PropertyType.SOUND, value.getAsStringArray());
	}

	/**
	 Creates a control property of type Font<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, AFont value) {
		this(name, PropertyType.FONT, new String[]{value.name()});
	}

	@NotNull
	public String getName() {
		return name;
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

	ControlProperty deepCopy() {
		String[] values = new String[this.values.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = this.values[i]; //strings are immutable
		}
		return new ControlProperty(this.name, this.type, values);
	}
}
