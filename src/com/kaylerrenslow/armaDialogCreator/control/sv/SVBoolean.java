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

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/** A generic wrapper implementation for a boolean. */
public final class SVBoolean extends SerializableValue {
	private static final String S_TRUE = "true";
	private static final String S_FALSE = "false";
	
	public static final ValueConverter<SVBoolean> CONVERTER = new ValueConverter<SVBoolean>() {
		@Override
		public SVBoolean convert(DataContext context, @NotNull String... values) throws Exception {
			return values[0].trim().equalsIgnoreCase(S_TRUE) ? TRUE : FALSE;
		}
	};
	
	private boolean b;
	
	public static SVBoolean TRUE = new SVBoolean(true);
	public static SVBoolean FALSE = new SVBoolean(false);
	
	private SVBoolean(boolean b) {
		super(b + "");
		this.b = b;
	}
	
	public boolean isTrue() {
		return b;
	}
	
	@Override
	public SerializableValue deepCopy() {
		return get(b);
	}
	
	@Override
	public String toString() {
		return b ? S_TRUE : S_FALSE;
	}
	
	/** Return {@link #TRUE} if value==true. Return {@link #FALSE} if value==false */
	public static SerializableValue get(boolean value) {
		return value ? TRUE : FALSE;
	}
}
