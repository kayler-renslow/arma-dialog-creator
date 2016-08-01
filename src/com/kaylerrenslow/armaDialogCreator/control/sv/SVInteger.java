package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/** A generic wrapper implementation for an int. */
public final class SVInteger extends SVNumber {
	
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
		valuesAsArray[0] = this.i + "";
	}

	public int getInt() {
		return i;
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVInteger(i);
	}

	@Override
	public String toString() {
		return valuesAsArray[0];
	}
	
	@Override
	protected void setValue(String value) {
		setInt(Integer.parseInt(value));
	}
	
	@Override
	public double getNumber() {
		return i;
	}
}
