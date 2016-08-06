package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/15/2016.
 */
public enum PropertyType {
	/** Is a integer value. Current implementation is a 32 bit integer (java int) */
	INT(SVInteger.CONVERTER, Lang.PropertyType.INT),
	/** Is a floating point value. The current implementation uses 64 bit floating point (java double) */
	FLOAT(SVDouble.CONVERTER, Lang.PropertyType.FLOAT),
	/** Unique property type to {@link ControlPropertyLookup#STYLE} */
	CONTROL_STYLE(ControlStyleGroup.CONVERTER, Lang.PropertyType.CONTROL_STYLE),
	/** Is an expression */
	EXP(Expression.CONVERTER, Lang.PropertyType.EXP),
	/** Is a boolean (0 for false, 1 for true) */
	BOOLEAN(SVBoolean.CONVERTER, Lang.PropertyType.BOOLEAN),
	/** Is a String */
	STRING(SVString.CONVERTER, Lang.PropertyType.STRING, true),
	/** Generic array property type */
	ARRAY(SVStringArray.CONVERTER, Lang.PropertyType.ARRAY, 2),
	/** Color array string ({r,g,b,a} where r,g,b,a are from 0 to 1 inclusively) */
	COLOR(AColor.CONVERTER, Lang.PropertyType.COLOR, 4),
	/** Is an array that is formatted to fit a sound and its params */
	SOUND(ASound.CONVERTER, Lang.PropertyType.SOUND, 3),
	/** Is font name */
	FONT(AFont.CONVERTER, Lang.PropertyType.FONT, true),
	/** Denotes a file name inside a String */
	FILE_NAME(SVString.CONVERTER, Lang.PropertyType.FILE_NAME, true),
	/** Denotes an image path inside a String */
	IMAGE(SVImage.CONVERTER, Lang.PropertyType.IMAGE, true),
	/** Color is set to a hex string like #ffffff or #ffffffff */
	HEX_COLOR_STRING(AHexColor.CONVERTER, Lang.PropertyType.HEX_COLOR_STRING, true),
	/** example: #(argb,8,8,3)color(1,1,1,1) however there is more than one way to set texture */
	TEXTURE(SVString.CONVERTER, Lang.PropertyType.TEXTURE, true),
	/** Is an SQF code string, but this propertyType is an easy way to categorize all event handlers. */
	EVENT(SVString.CONVERTER, Lang.PropertyType.EVENT, true),
	/** SQF code String */
	SQF(SVString.CONVERTER, Lang.PropertyType.SQF, true);
	
	/** Number of values used to represent the data */
	public final int propertyValuesSize;
	/** If true, when this control property is exported, the value should have quotes around it */
	public final boolean exportHasQuotes;
	public final String displayName;
	public final ValueConverter<? extends SerializableValue> converter;
	
	PropertyType(ValueConverter<? extends SerializableValue> converter, String displayName) {
		this(converter, displayName, 1);
	}
	
	PropertyType(ValueConverter<? extends SerializableValue> converter, String displayName, boolean exportHasQuotes) {
		this.converter = converter;
		this.propertyValuesSize = 1;
		this.displayName = displayName;
		this.exportHasQuotes = exportHasQuotes;
	}
	
	PropertyType(ValueConverter<? extends SerializableValue> converter, String displayName, int propertyValueSize) {
		if (propertyValueSize <= 0) {
			throw new IllegalArgumentException("Number of values must be >= 1");
		}
		this.converter = converter;
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
