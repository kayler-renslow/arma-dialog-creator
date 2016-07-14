package com.kaylerrenslow.armaDialogCreator.arma.util;

/**
 @author Kayler
 Specifies a sound
 Created on 05/22/2016. */
public class ASound {
	private String soundName;
	private double db;
	private double pitch;

	/**
	 Create a new sound from String array that is formatted like so: {name, db, pitch}. Name must be String, db must be a double, and pitch must be a double

	 @throws NumberFormatException     when the string array is not formatted correctly
	 @throws IndexOutOfBoundsException when string array is not of proper size (must be length 3)
	 */
	public ASound(String[] values) throws NumberFormatException, IndexOutOfBoundsException {
		this(values[0], Double.valueOf(values[1]), Double.valueOf(values[2]));
	}

	/**
	 Creates a sound

	 @param soundName sound name
	 @param db db value
	 @param pitch pitch (ranged 0.0 - 1.0)
	 @throws IllegalArgumentException when pitch is less than 0 or greater than 1
	 */
	public ASound(String soundName, double db, double pitch) {
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

	public void setDb(int db) {
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
		String[] s = new String[3];
		s[0] = soundName;
		s[1] = db + "";
		s[2] = pitch + "";
		return s;
	}

	@Override
	public String toString() {
		return toArrayString();
	}
}
