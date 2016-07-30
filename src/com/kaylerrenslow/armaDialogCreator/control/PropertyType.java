package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/15/2016.
 */
public enum PropertyType {
	/** Is a integer value. Current implementation is a 32 bit integer (java int) */
	INT(SVInteger.class, Lang.PropertyType.INT),
	/** Is a floating point value. The current implementation uses 64 bit floating point (java double) */
	FLOAT(SVDouble.class, Lang.PropertyType.FLOAT),
	/** Is an expression */
	EXP(Expression.class, Lang.PropertyType.EXP),
	/** Is a boolean (0 for false, 1 for true) */
	BOOLEAN(SVBoolean.class, Lang.PropertyType.BOOLEAN),
	/** Is a String */
	STRING(SVString.class, Lang.PropertyType.STRING, true),
	/** Generic array property type */
	ARRAY(SVStringArray.class, Lang.PropertyType.ARRAY, 2),
	/** Color array string ({r,g,b,a} where r,g,b,a are from 0 to 1 inclusively) */
	COLOR(AColor.class, Lang.PropertyType.COLOR, 4),
	/** Is an array that is formatted to fit a sound and its params */
	SOUND(ASound.class, Lang.PropertyType.SOUND, 3),
	/** Is font name */
	FONT(AFont.class, Lang.PropertyType.FONT, true),
	/** Denotes a file name inside a String */
	FILE_NAME(SVString.class, Lang.PropertyType.FILE_NAME, true),
	/** Denotes an image path inside a String */
	IMAGE(SVImage.class, Lang.PropertyType.IMAGE, true),
	/** Color is set to a hex string like #ffffff or #ffffffff */
	HEX_COLOR_STRING(AHexColor.class, Lang.PropertyType.HEX_COLOR_STRING, true),
	/** example: #(argb,8,8,3)color(1,1,1,1) however there is more than one way to set texture */
	TEXTURE(SVString.class, Lang.PropertyType.TEXTURE, true),
	/** Is an SQF code string, but this propertyType is an easy way to categorize all event handlers. */
	EVENT(SVString.class, Lang.PropertyType.EVENT, true),
	/** SQF code String */
	SQF(SVString.class, Lang.PropertyType.SQF, true);
	
	/** Number of values used to represent the data */
	public final int propertyValuesSize;
	/** If true, when this control property is exported, the value should have quotes around it */
	public final boolean exportHasQuotes;
	public final String displayName;
	public final Class<? extends SerializableValue> valueClass;
	
	PropertyType(Class<? extends SerializableValue> valueClass, String displayName) {
		this(valueClass, displayName, 1);
	}
	
	PropertyType(Class<? extends SerializableValue> valueClass, String displayName, boolean exportHasQuotes) {
		this.valueClass = valueClass;
		this.propertyValuesSize = 1;
		this.displayName = displayName;
		this.exportHasQuotes = exportHasQuotes;
	}
	
	PropertyType(Class<? extends SerializableValue> valueClass, String displayName, int propertyValueSize) {
		if (propertyValueSize <= 0) {
			throw new IllegalArgumentException("Number of values must be >= 1");
		}
		this.valueClass = valueClass;
		this.displayName = displayName;
		propertyValuesSize = propertyValueSize;
		exportHasQuotes = false;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
	/** An alternative to {@link #valueOf(String)} that doesn't throw exceptions */
	@Nullable
	public static PropertyType get(String propertyName) {
		for (PropertyType propertyType : values()) {
			if (propertyType.name().equals(propertyName)) {
				return propertyType;
			}
		}
		return null;
	}
}
