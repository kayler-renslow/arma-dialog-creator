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

/** A generic wrapper implementation for an int. */
public class SVInteger extends SVNumber {
	
	public static final ValueConverter<SVInteger> CONVERTER = new ValueConverter<SVInteger>() {
		@Override
		public SVInteger convert(DataContext context, @NotNull String... values) throws Exception {
			return new SVInteger(Integer.parseInt(values[0]));
		}
	};
	
	private int i;

	public SVInteger(int i) {
		super(i);
		this.i = i;
	}

	public void setInt(int i) {
		this.i = i;
		valuesAsArray[0] = this.i + "";
	}

	public int getInt() {
		return i;
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVInteger(i);
	}

	@Override
	public String toString() {
		return valuesAsArray[0];
	}
	
	@Override
	protected void setValue(String value) {
		setInt(Integer.parseInt(value));
	}
	
	@Override
	public double getNumber() {
		return i;
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof SVInteger){
			SVInteger other = (SVInteger) o;
			return this.i == other.i;
		}
		return false;
	}

}
