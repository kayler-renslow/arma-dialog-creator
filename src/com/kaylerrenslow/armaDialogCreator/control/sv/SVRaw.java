package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A Raw value is literally anything. A Raw value should be thought of as a "substitute" for
 a different value.
 <p>
 A Raw value can't be converted (via {@link SerializableValue#convert(DataContext, SerializableValue, PropertyType)})
 into any {@link PropertyType}, except it's {@link #getSubstituteType()} (if specified).
 */
public final class SVRaw extends SerializableValue {

	public static final ValueConverter<SVRaw> CONVERTER = new ValueConverter<SVRaw>() {
		@Override
		public SVRaw convert(DataContext context, @NotNull String... values) throws Exception {
			//This converter is a placeholder to adhere to PropertyType specifications
			//and shouldn't ever be used. Reason is because it doesn't allow for passing in a revert type (type to convert Raw back into)
			throw new IllegalStateException();
		}
	};
	private final String s;
	private final PropertyType substituteType;

	/** If s==null, "" (empty string) will be used */
	public SVRaw(@Nullable String s, @Nullable PropertyType substituteType) {
		this.s = s;
		this.substituteType = substituteType;
	}

	@NotNull
	public String getString() {
		return s;
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVRaw(s, substituteType);
	}

	/**
	 @return the {@link PropertyType} that the value can convert into, or null if can't convert into anything
	 */
	@Nullable
	public PropertyType getSubstituteType() {
		return substituteType;
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Raw;
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
		if (o instanceof SVRaw) {
			SVRaw other = (SVRaw) o;
			return this.s.equals(other.s);
		}
		return false;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{s};
	}

	/**
	 @return null if {@link #getSubstituteType()} is null,
	 or new instance of {@link SerializableValue} that will have a {@link SerializableValue#getPropertyType()}
	 equal to {@link #getSubstituteType()}
	 @throws Exception when the new value couldn't be created
	 */
	@Nullable
	public SerializableValue newSubstituteTypeValue(@NotNull DataContext context) throws Exception {
		if (substituteType == null) {
			return null;
		}
		return substituteType.getConverter().convert(context, getAsStringArray());
	}

}
