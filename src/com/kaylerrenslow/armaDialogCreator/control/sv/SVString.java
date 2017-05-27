package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A generic wrapper implementation for a String. */
public final class SVString extends SerializableValue {

	public static final ValueConverter<SVString> CONVERTER = new ValueConverter<SVString>() {
		@Override
		public SVString convert(DataContext context, @NotNull String... values) throws Exception {
			return new SVString(values[0]);
		}
	};

	/** If s==null, "" (empty string) will be used */
	public SVString(@Nullable String s) {
		super(s == null ? "" : s);
	}

	/** Involves {@link #SVString(String)} with "" (empty String) */
	public SVString() {
		this("");
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

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.STRING;
	}

	@Override
	public String toString() {
		return valuesAsArray[0];
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVString) {
			SVString other = (SVString) o;
			return this.valuesAsArray[0].equals(other.valuesAsArray[0]);
		}
		return false;
	}
}
