package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/** A generic wrapper implementation for a String. */
public final class SVString extends SerializableValue {

	public static final ValueConverter<SVString> CONVERTER = new ValueConverter<SVString>() {
		@Override
		public SVString convert(DataContext context, @NotNull String... values) throws Exception {
			return new SVString(values[0]);
		}
	};
	
	public SVString(String s) {
		super(s);
	}

	public String getString() {
		return valuesAsArray[0];
	}

	public void setString(String s) {
		this.valuesAsArray[0] = s;
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVString(valuesAsArray[0]);
	}

	@Override
	public String toString() {
		return valuesAsArray[0];
	}
}
