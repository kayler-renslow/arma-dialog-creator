package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.old.PropertyType;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A generic wrapper implementation for a boolean. */
public final class SVBoolean extends SerializableValue {
	private static final String S_TRUE = "true";
	private static final String S_FALSE = "false";
	
	public static final ValueConverter<SVBoolean> CONVERTER = new ValueConverter<SVBoolean>() {
		@Override
		public SVBoolean convert(DataContext context, @NotNull String... values) throws Exception {
			return valueOf(values[0]);
		}
	};
	
	private boolean b;

	public static final SVBoolean TRUE = new SVBoolean(true);
	public static final SVBoolean FALSE = new SVBoolean(false);
	
	private SVBoolean(boolean b) {
		this.b = b;
	}
	
	public boolean isTrue() {
		return b;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{b ? "true" : "false"};
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return get(b);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Boolean;
	}

	@Override
	public String toString() {
		return b ? S_TRUE : S_FALSE;
	}
	
	/** Return {@link #TRUE} if value==true. Return {@link #FALSE} if value==false */
	public static SVBoolean get(@Nullable Boolean value) {
		return value == null ? null : value ? TRUE : FALSE;
	}
	
	@Override
	public boolean equals(Object o){
		return o == this;
	}

	/** Return a boolean of the given String value */
	@NotNull
	public static SVBoolean valueOf(@NotNull String value) {
		return value.trim().equalsIgnoreCase(S_TRUE) ? TRUE : FALSE;
	}
}
