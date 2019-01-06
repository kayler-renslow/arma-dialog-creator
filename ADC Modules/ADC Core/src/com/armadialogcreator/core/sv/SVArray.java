package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 Created by Kayler on 07/13/2016.
 */
public final class SVArray extends SerializableValue {

	public static final ValueConverter<SVArray> CONVERTER = new ValueConverter<SVArray>() {
		@Override
		public SVArray convert(DataContext context, @NotNull String... values) throws Exception {
			return new SVArray(values);
		}
	};
	private final String[] strings;

	public SVArray(@NotNull String... strings) {
		this.strings = strings;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder("{");
		for (int i = 0; i < strings.length; i++) {
			ret.append(strings[i]).append(i != strings.length - 1 ? ", " : "");
		}
		ret.append('}');
		return ret.toString();
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return strings;
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVArray(strings);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Array;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVArray) {
			SVArray other = (SVArray) o;
			return Arrays.equals(this.strings, other.strings);
		}
		return false;
	}
}
