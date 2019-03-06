package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import org.jetbrains.annotations.NotNull;

/**
 A Raw value that has no value (example: property=;) //notice nothing inbetween = and ;
 This value is used more as a placeholder value for when a null {@link SerializableValue} isn't allowed.
 */
public final class SVNull extends SerializableValue {
	public static final SVNull instance = new SVNull();

	private SVNull() {

	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return instance;
	}

	/**
	 @return {@link PropertyType#Raw}
	 */
	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Raw;
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public boolean equals(Object o) {
		return o == this;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{""};
	}

}
