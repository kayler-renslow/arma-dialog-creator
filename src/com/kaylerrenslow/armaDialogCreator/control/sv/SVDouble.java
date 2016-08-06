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

/** A generic wrapper implementation for a double. */
public final class SVDouble extends SVNumber {
	public static final ValueConverter<SVDouble> CONVERTER = new ValueConverter<SVDouble>() {
		@Override
		public SVDouble convert(DataContext context, @NotNull String... values) throws Exception {
			return new SVDouble(Double.parseDouble(values[0]));
		}
	};
	
	private double d;
	
	public SVDouble(double d) {
		super(d);
		this.d = d;
	}
	
	public void setDouble(double d) {
		this.d = d;
		valuesAsArray[0] = d + "";
	}
	
	public double getDouble() {
		return d;
	}
	
	@Override
	public SerializableValue deepCopy() {
		return new SVDouble(d);
	}
	
	@Override
	public String toString() {
		return valuesAsArray[0];
	}
	
	@Override
	protected void setValue(String value) {
		setDouble(Double.parseDouble(value));
	}
	
	@Override
	public double getNumber() {
		return d;
	}
}
