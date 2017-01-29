package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 Base class that specifies that a value can be converted into a String[]. For primitives or other values that don't have multiple attributes, the String[] is length 1 with the attribute at index 0.

 @author Kayler
 @since 07/13/2016. */
public abstract class SerializableValue {


	/**
	 A safe way of converting a {@link SerializableValue} into a different one, i.e. {@link SVString} to {@link SVInteger}.

	 @param dataContext context to use
	 @param convertMe value to convert
	 @param newPropertyType property type to convert to
	 @return the new value
	 @throws SerializableValueConversionException when the value couldn't be converted successfully
	 */
	@NotNull
	public static SerializableValue convert(@Nullable DataContext dataContext, @NotNull SerializableValue convertMe, @NotNull PropertyType newPropertyType) throws SerializableValueConversionException {
		try {
			return constructNew(dataContext, newPropertyType, convertMe.getAsStringArray());
		} catch (SerializableValueConstructionException e) {
			throw new SerializableValueConversionException(e.getMessage());
		}
	}

	/**
	 Get the appropriate {@link SerializableValue} instance for the given {@link PropertyType}

	 @param dataContext context to use
	 @param type type
	 @param values values to use
	 @return the new instance
	 @throws SerializableValueConstructionException when the value could not be created
	 */
	@NotNull
	public static SerializableValue constructNew(@Nullable DataContext dataContext, @NotNull PropertyType type, @NotNull String... values) {
		Exception e = null;
		try {
			SerializableValue value = doConstructNew(dataContext, type, values);
			if (value != null) {
				return value;
			}
		} catch (Exception e1) {
			e = e1;
			e1.printStackTrace(System.out);
		}
		throw new SerializableValueConstructionException(e != null ? e.getMessage() : "");
	}

	private static SerializableValue doConstructNew(@Nullable DataContext dataContext, @NotNull PropertyType type, @NotNull String[] values) throws Exception {
		return type.getConverter().convert(dataContext, values);
	}

	protected final String[] valuesAsArray;

	/**
	 Equivalent of doing:<code>new SerializableValue(new String[]{onlyValue})</code>
	 */
	public SerializableValue(@NotNull String onlyValue) {
		this(new String[]{onlyValue});
	}

	/** Construct the SerializableValue from the given String values. Each value corresponds to a value in {@link #getAsStringArray()} */
	public SerializableValue(@NotNull String[] values) {
		this.valuesAsArray = values;
	}

	/** Return the value as a String array */
	public String[] getAsStringArray() {
		return valuesAsArray;
	}

	/** Returns {@link Arrays#toString(Object[])} with {@link #getAsStringArray()} as the parameter */
	public String toStringDebug() {
		return Arrays.toString(valuesAsArray);
	}

	/** Return the instance as a deep copy */
	public abstract SerializableValue deepCopy();


	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SerializableValue) {
			SerializableValue other = (SerializableValue) o;
			return Arrays.equals(this.valuesAsArray, other.valuesAsArray);
		}
		return false;
	}
}
