/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 Created by Kayler on 08/05/2016.
 */
public class ControlStyleGroup extends SerializableValue {
	private ControlStyle[] values;
	
	public static final String DEFAULT_DELIMITER = "+";
	
	public static final ValueConverter<ControlStyleGroup> CONVERTER = new ValueConverter<ControlStyleGroup>() {
		@Override
		public ControlStyleGroup convert(DataContext context, @NotNull String... values) throws Exception {
			String[] split = values[0].split("\\" + DEFAULT_DELIMITER);
			ControlStyle[] styles = new ControlStyle[split.length];
			int styleInd = 0;
			for (String s : split) {
				try {
					int id = Integer.parseInt(s);
					styles[styleInd++] = ControlStyle.findById(id);
				} catch (IllegalArgumentException e) { //will catch number format exception
					return null;
				}
			}
			return new ControlStyleGroup(styles);
		}
	};
	
	public ControlStyleGroup(@NotNull ControlStyle[] values) {
		super(toString(values));
		this.values = values;
	}
	
	@NotNull
	public ControlStyle[] getValues() {
		return values;
	}
	
	public void setValues(@NotNull ControlStyle[] values) {
		this.values = values;
		valuesAsArray[0] = toString(values);
	}
	
	@Override
	public SerializableValue deepCopy() {
		ControlStyle[] copy = new ControlStyle[values.length];
		System.arraycopy(values, 0, copy, 0, copy.length);
		return new ControlStyleGroup(copy);
	}
	
	public static String toString(ControlStyle[] values) {
		String s = "";
		for (int i = 0; i < values.length; i++) {
			s += values[i].styleValue + (i != values.length - 1 ? "+" : "");
		}
		return s;
	}
	
	@Override
	public String toString() {
		return toString(values);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof ControlStyleGroup) {
			ControlStyleGroup other = (ControlStyleGroup) o;
			return Arrays.equals(this.values, other.values);
		}
		return false;
	}
}
