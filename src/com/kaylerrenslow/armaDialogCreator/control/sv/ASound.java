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

import java.util.Arrays;

/**
 @author Kayler
 Specifies a sound
 Created on 05/22/2016. */
public class ASound extends SerializableValue {
	public static final ValueConverter<ASound> CONVERTER = new ValueConverter<ASound>() {
		@Override
		public ASound convert(DataContext context, @NotNull String... values) {
			return new ASound(values);
		}
	};
	
	private String soundName;
	private double db;
	private double pitch;
	
	/**
	 Create a new sound from String array that is formatted like so: {name, db, pitch}. Name must be String, db must be a double, and pitch must be a double
	 
	 @throws NumberFormatException     when the string array is not formatted correctly
	 @throws IndexOutOfBoundsException when string array is not of proper size (must be length 3)
	 */
	public ASound(String[] values) throws NumberFormatException, IndexOutOfBoundsException {
		super(values);
		init(values[0], Double.valueOf(values[1]), Double.valueOf(values[2]));
	}
	
	/**
	 Creates a sound
	 
	 @param soundName sound name
	 @param db db value
	 @param pitch pitch (ranged 0.0 - 1.0)
	 @throws IllegalArgumentException when pitch is less than 0 or greater than 1
	 */
	public ASound(String soundName, double db, double pitch) {
		super(new String[]{soundName, db + "", pitch + ""});
		init(soundName, db, pitch);
	}
	
	private void init(String soundName, double db, double pitch) {
		this.soundName = soundName;
		this.db = db;
		setPitch(pitch);
	}
	
	public String getSoundName() {
		return soundName;
	}
	
	public void setSoundName(String soundName) {
		this.soundName = soundName;
	}
	
	public double getDb() {
		return db;
	}
	
	public void setDb(double db) {
		this.db = db;
	}
	
	public double getPitch() {
		return pitch;
	}
	
	/**
	 Set the pitch
	 
	 @throws IllegalArgumentException when pitch is less than 0 or greater than 1
	 */
	public void setPitch(double pitch) {
		if (pitch < 0 || pitch > 1) {
			throw new IllegalArgumentException("pitch must be between 0 and 1 inclusively");
		}
		this.pitch = pitch;
	}
	
	/** Serializes the sound array into a String. Example: "hello.ogg", 0 db, and 1 pitch alpha becomes "{"hello",db-0,1}" */
	public String toArrayString() {
		String str = "{\"" + soundName + "\"";
		str += "db-" + db;
		str += pitch;
		return str + "}";
	}
	
	/** Get the colors as a string array formatted like so: {soundName, db, pitch} */
	public String[] getAsStringArray() {
		valuesAsArray[0] = soundName;
		valuesAsArray[1] = db + "";
		valuesAsArray[2] = pitch + "";
		return valuesAsArray;
	}
	
	@Override
	public SerializableValue deepCopy() {
		return new ASound(soundName, db, pitch);
	}
	
	@Override
	public String toString() {
		return toArrayString();
	}
		
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof ASound){
			ASound other = (ASound) o;
			return Arrays.equals(this.valuesAsArray, other.valuesAsArray);
		}
		return false;
	}
}
