package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.old.PropertyType;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
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
	private String s;

	/** If s==null, "" (empty string) will be used */
	public SVString(@Nullable String s) {
		this.s = s == null ? "" : s;
	}

	/** Involves {@link #SVString(String)} with "" (empty String) */
	public SVString() {
		this("");
	}

	@NotNull
	public String getString() {
		return s;
	}

	public void setString(@Nullable String s) {
		this.s = s == null ? "" : s;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{s};
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVString(s);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.String;
	}

	@Override
	public String toString() {
		return s;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVString) {
			SVString other = (SVString) o;
			return this.s.equals(other.s);
		}
		return false;
	}

	/** @return a new {@link SVString} instance with "" as the value */
	@NotNull
	public static SVString newEmptyString() {
		return new SVString("");
	}
}
