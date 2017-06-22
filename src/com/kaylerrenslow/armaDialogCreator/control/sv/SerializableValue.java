package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 Base class that specifies that a value can be converted into a String[].
 For primitives or other values that don't have multiple attributes, the String[] is length 1 with the attribute at index 0.

 @author Kayler
 @since 07/13/2016. */
public abstract class SerializableValue {

	/**
	 Get a list that the given {@link PropertyType} can convert to

	 @param type the type to check
	 @return the list of types, or a list of size 1 if can't convert into anything, except itself
	 @see #isConvertible(PropertyType, PropertyType)
	 */
	@NotNull
	public static List<PropertyType> getTypesCanConvertTo(@NotNull PropertyType type) {
		List<PropertyType> list = new ArrayList<>(PropertyType.values().length);
		for (PropertyType propertyType : PropertyType.values()) {
			if (isConvertible(type, propertyType)) {
				list.add(propertyType);
			}
		}
		return list;
	}

	/**
	 Check if one {@link PropertyType} is convertible to another.<p>
	 <p>
	 In the case that <code>fromType</code> is {@link PropertyType#Raw},
	 will always return false if <code>toType</code> is not {@link PropertyType} itself.
	 To convert {@link PropertyType#Raw}, use {@link SVRaw#newSubstituteTypeValue(DataContext)}.

	 @param fromType from
	 @param toType to
	 @return true if convertible, false if not
	 @see #convert(DataContext, SerializableValue, PropertyType)
	 */
	public static boolean isConvertible(@NotNull PropertyType fromType, @NotNull PropertyType toType) {
		if (fromType == PropertyType.Raw) {
			return false;
		}
		if (fromType == toType) {
			return true;
		}
		switch (fromType) {
			case Int: {
				return false;
			}
			case Array: {
				return false;
			}
			case SQF: {
				return toType == PropertyType.String;
			}
			case Color: {
				return false;
			}
			case Float: {
				return false;
			}
			case Sound: {
				return false;
			}
			case ControlStyle: {
				return false;
			}
			case Texture: {
				return toType == PropertyType.String;
			}
			case String: {
				return toType == PropertyType.Texture
						|| toType == PropertyType.FileName
						|| toType == PropertyType.Image
						|| toType == PropertyType.Font
						|| toType == PropertyType.SQF
						;
			}
			case FileName: {
				return toType == PropertyType.String
						|| toType == PropertyType.Image
						;
			}
			case Image: {
				return toType == PropertyType.String
						|| toType == PropertyType.FileName
						;
			}
			case Font: {
				return toType == PropertyType.String;
			}
			case Boolean: {
				return false;
			}
		}
		return false;
	}

	/**
	 A safe way of converting a {@link SerializableValue} into a different one, i.e. {@link SVString} to {@link SVFont}.
	 If <code>newPropertyType</code> is equal to the current type, <code>convertMe</code> will be returned.

	 @param dataContext context to use
	 @param convertMe value to convert
	 @param newPropertyType property type to convert to
	 @return the new value
	 @throws SerializableValueConversionException when the value couldn't be converted successfully
	 @throws IllegalArgumentException             when {@link #isConvertible(PropertyType, PropertyType)}
	 returns false on {@link SerializableValue#getPropertyType()} and <code>newPropertyType</code>
	 */
	@NotNull
	public static SerializableValue convert(@Nullable DataContext dataContext, @NotNull SerializableValue convertMe, @NotNull PropertyType newPropertyType) throws SerializableValueConversionException {
		if (convertMe.getPropertyType() == newPropertyType) {
			return convertMe;
		}
		if (!isConvertible(convertMe.getPropertyType(), newPropertyType)) {
			throw new IllegalArgumentException("types are not convertible:" + convertMe.getPropertyType() + ", " + newPropertyType);
		}
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

	//
	//end static
	//

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
	@NotNull
	public String[] getAsStringArray() {
		return valuesAsArray;
	}

	/**
	 Returns {@link Arrays#toString(Object[])} with {@link #getAsStringArray()} as the parameter.
	 Also concatenates the {@link #getPropertyType()}
	 */
	@NotNull
	public String toStringDebug() {
		return Arrays.toString(valuesAsArray) + ", propertyType:" + getPropertyType();
	}

	/** Return the instance as a deep copy */
	@NotNull
	public abstract SerializableValue deepCopy();

	/**
	 Gets the constant {@link PropertyType} for this value

	 @return the {@link PropertyType} of this value
	 */
	@NotNull
	public abstract PropertyType getPropertyType();

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SerializableValue) {
			SerializableValue other = (SerializableValue) o;
			if (this.getClass() == other.getClass()) {
				 /*
				 We DO NOT want different SerializableValue classes saying they are equal just because their values are equal.
				 There can be more to a SerializableValue than just the values in the array.
				 Some SerializableValue instances may be exported with quotes and we don't want those to be equal to those without quotes, even if the values are the same.
				 Example: 1 != "1"
				 */
				return Arrays.equals(this.valuesAsArray, other.valuesAsArray);
			}
		}
		return false;
	}

}
