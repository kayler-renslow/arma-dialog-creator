package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/15/2016.
 */
public enum PropertyType {
	/** Is a integer value. Current implementation is a 32 bit integer (java int) */
	INT(Lang.PropertyType.INT),
	/** Is a floating point value. The current implementation uses 64 bit floating point (java double) */
	FLOAT(Lang.PropertyType.FLOAT),
	/** Is an expression */
	EXP(Lang.PropertyType.EXP),
	/** Is a boolean (0 for false, 1 for true) */
	BOOLEAN(Lang.PropertyType.BOOLEAN),
	/** Is a String */
	STRING(Lang.PropertyType.STRING, true),
	/** Generic array property type */
	ARRAY(Lang.PropertyType.ARRAY, 2),
	/** Color array string ({r,g,b,a} where r,g,b,a are from 0 to 1 inclusively) */
	COLOR(Lang.PropertyType.COLOR, 4),
	/** Is an array that is formatted to fit a sound and its params */
	SOUND(Lang.PropertyType.SOUND, 3),
	/** Is font name */
	FONT(Lang.PropertyType.FONT, true),
	/** Denotes a file name inside a String */
	FILE_NAME(Lang.PropertyType.FILE_NAME, true),
	/** Denotes an image path inside a String */
	IMAGE(Lang.PropertyType.IMAGE, true),
	/** Color is set to a hex string like #ffffff or #ffffffff */
	HEX_COLOR_STRING(Lang.PropertyType.HEX_COLOR_STRING, true),
	/** example: #(argb,8,8,3)color(1,1,1,1) however there is more than one way to set texture */
	TEXTURE(Lang.PropertyType.TEXTURE, true),
	/** Is an SQF code string, but this propertyType is an easy way to categorize all event handlers. */
	EVENT(Lang.PropertyType.EVENT, true),
	/** SQF code String */
	SQF(Lang.PropertyType.SQF, true);
	
	/** Number of values used to represent the data */
	public final int propertyValuesSize;
	/** If true, when this control property is exported, the value should have quotes around it */
	public final boolean exportHasQuotes;
	public final String displayName;
	
	PropertyType(String displayName) {
		this(displayName, 1);
	}
	
	PropertyType(String displayName, boolean exportHasQuotes) {
		this.propertyValuesSize = 1;
		this.displayName = displayName;
		this.exportHasQuotes = exportHasQuotes;
	}
	
	PropertyType(String displayName, int propertyValueSize) {
		if (propertyValueSize <= 0) {
			throw new IllegalArgumentException("Number of values must be >= 1");
		}
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
