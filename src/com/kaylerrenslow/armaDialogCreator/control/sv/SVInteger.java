package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/** A generic wrapper implementation for an int. */
public class SVInteger extends SVNumber {

	public static final ValueConverter<SVInteger> CONVERTER = new ValueConverter<SVInteger>() {
		@Override
		public SVInteger convert(DataContext context, @NotNull String... values) throws Exception {
			return new SVInteger(Integer.parseInt(values[0]));
		}
	};

	private int i;

	public SVInteger(int i) {
		super(i);
		this.i = i;
	}

	public void setInt(int i) {
		this.i = i;
	}

	public int getInt() {
		return i;
	}

	@NotNull
	@Override
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
	protected void setValue(String value) {
		setInt(Integer.parseInt(value));
	}

	@Override
	public double getNumber() {
		return i;
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

}
