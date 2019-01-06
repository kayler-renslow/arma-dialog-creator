package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/** A generic wrapper implementation for an int. */
public class SVInteger extends SerializableValue implements SVNumericValue {

	public static final ValueConverter<SVInteger> CONVERTER = new ValueConverter<SVInteger>() {
		@Override
		public SVInteger convert(DataContext context, @NotNull String... values) throws Exception {
			return new SVInteger(Integer.parseInt(values[0]));
		}
	};

	private final int i;

	public SVInteger(int i) {
		this.i = i;
	}

	public int getInt() {
		return i;
	}

	@NotNull
	public String[] getAsStringArray() {
		return new String[]{i + ""};
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVInteger(i);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Int;
	}

	@Override
	public String toString() {
		return i + "";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVInteger) {
			SVInteger other = (SVInteger) o;
			return this.i == other.i;
		}
		return false;
	}

	@Override
	public int toInt() {
		return i;
	}

	@Override
	public double toDouble() {
		return i;
	}
}
