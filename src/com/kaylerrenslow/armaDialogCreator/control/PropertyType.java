/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;

import java.util.LinkedList;

/**
 @author Kayler
 Created on 07/15/2016. */
public enum PropertyType {
	/** Is a integer value. Current implementation is a 32 bit integer (java int) */
	INT(0, SVInteger.CONVERTER, getString("PropertyType.int")),
	/** Is a floating point value. The current implementation uses 64 bit floating point (java double) */
	FLOAT(1, SVDouble.CONVERTER, getString("PropertyType.float")),
	/** Unique property type to {@link ControlPropertyLookup#STYLE} */
	CONTROL_STYLE(2, ControlStyleGroup.CONVERTER, getString("PropertyType.control_style")),
	/** Is an expression */
	EXP(3, Expression.CONVERTER, getString("PropertyType.exp")),
	/** Is a boolean (0 for false, 1 for true) */
	BOOLEAN(4, SVBoolean.CONVERTER, getString("PropertyType.boolean")),
	/** Is a String */
	STRING(5, SVString.CONVERTER, getString("PropertyType.string"), true),
	/** Generic array property type */
	ARRAY(6, SVStringArray.CONVERTER, getString("PropertyType.array"), 2),
	/** Color array string ({r,g,b,a} where r,g,b,a are from 0 to 1 inclusively) */
	COLOR(7, AColor.CONVERTER, getString("PropertyType.color"), 4),
	/** Is an array that is formatted to fit a sound and its params */
	SOUND(8, ASound.CONVERTER, getString("PropertyType.sound"), 3),
	/** Is font name */
	FONT(9, AFont.CONVERTER, getString("PropertyType.font"), true),
	/** Denotes a file name inside a String */
	FILE_NAME(10, SVString.CONVERTER, getString("PropertyType.file_name"), true),
	/** Denotes an image path inside a String */
	IMAGE(11, SVImage.CONVERTER, getString("PropertyType.image"), true),
	/** Color is set to a hex string like #ffffff or #ffffffff */
	HEX_COLOR_STRING(12, AHexColor.CONVERTER, getString("PropertyType.hex_color_string"), true),
	/** example: #(argb,8,8,3)color(1,1,1,1) however there is more than one way to set texture */
	TEXTURE(13, SVString.CONVERTER, getString("PropertyType.texture"), true),
	/** SQF code String */
	SQF(14, SVString.CONVERTER, getString("PropertyType.sqf"), true);
	
	/** Number of values used to represent the data */
	public final int propertyValuesSize;
	/** If true, when this control property is exported, the value should have quotes around it */
	public final boolean exportHasQuotes;
	public final String displayName;
	public final ValueConverter<? extends SerializableValue> converter;
	/** Unique id. Used when saving for file. Do not change when assigned. */
	public final int id;
	
	PropertyType(int id, ValueConverter<? extends SerializableValue> converter, String displayName) {
		this(id, converter, displayName, 1);
	}
	
	PropertyType(int id, ValueConverter<? extends SerializableValue> converter, String displayName, boolean exportHasQuotes) {
		if (IdChecker.usedIds.contains(id)) {
			throw new IllegalStateException("used id:" + id);
		}
		IdChecker.usedIds.add(id);
		this.id = id;
		this.converter = converter;
		this.propertyValuesSize = 1;
		this.displayName = displayName;
		this.exportHasQuotes = exportHasQuotes;
	}
	
	PropertyType(int id, ValueConverter<? extends SerializableValue> converter, String displayName, int propertyValueSize) {
		if (IdChecker.usedIds.contains(id)) {
			throw new IllegalStateException("used id:" + id);
		}
		IdChecker.usedIds.add(id);
		this.id = id;
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
	
	/** @throws IllegalArgumentException when id couldn't be matched */
	public static PropertyType findById(int id) {
		for (PropertyType propertyType : values()) {
			if (propertyType.id == id) {
				return propertyType;
			}
		}
		throw new IllegalArgumentException("id " + id + " couldn't be matched");
	}

	private static String getString(String s) {
		return Lang.LookupBundle().getString(s);
	}

	private static class IdChecker {
		static final LinkedList<Integer> usedIds = new LinkedList<>();
	}
}
