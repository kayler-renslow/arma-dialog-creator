package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.ASound;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/22/2016.
 */
public class ControlProperty {
	private final String name;
	private final PropertyType type;
	private String[] values;

	public enum PropertyType {
		INT, FLOAT, BOOLEAN, STRING, ARRAY, COLOR, SOUND, FONT
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
	 Creates a control property of type String<br>
	 See constructor ControlProperty(String name, PropertyType type, String[] values) for more information
	 */
	public ControlProperty(@NotNull String name, @NotNull String value) {
		this(name, PropertyType.STRING, new String[]{value});
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

	void setValues(String[] values) {
		this.values = values;
	}

	void setValues(String v) {
		this.values[0] = v;
	}

	void setValues(int v) {
		this.values[0] = v + "";
	}

	void setValues(double v) {
		this.values[0] = v + "";
	}

	void setValues(boolean v) {
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
		return getName().equals(other.getName());
	}

	ControlProperty deepCopy() {
		String[] values = new String[this.values.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = this.values[i]; //strings are immutable
		}
		return new ControlProperty(this.name, this.type, values);
	}
}
