package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.util.ArmaPrecision;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/**
 Specifies a sound

 @author Kayler
 @since 05/22/2016. */
public class SVSound extends SerializableValue {
	public static final ValueConverter<SVSound> CONVERTER = new ValueConverter<SVSound>() {
		@Override
		public SVSound convert(DataContext context, @NotNull String... values) {
			return new SVSound(values);
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
	public SVSound(String[] values) throws NumberFormatException, IndexOutOfBoundsException {
		init(values[0], Double.valueOf(values[1]), Double.valueOf(values[2]));
	}

	/**
	 Creates a sound

	 @param soundName sound name
	 @param db db value
	 @param pitch pitch (ranged 0.0 - 1.0)
	 @throws IllegalArgumentException when pitch is less than 0 or greater than 1
	 */
	public SVSound(String soundName, double db, double pitch) {
		init(soundName, db, pitch);
	}

	private void init(String soundName, double db, double pitch) {
		this.soundName = soundName;
		this.db = db;
		setPitch(pitch);
	}

	@NotNull
	public String getSoundName() {
		return soundName;
	}

	public double getDb() {
		return db;
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

	/**
	 @return the sound array as a String.
	 Example: "hello.ogg", 0 db, and 1 pitch alpha becomes "{"hello",db-0,1}"
	 */
	@NotNull
	public String toArrayString() {
		String str = "{\"" + soundName + "\"";
		str += "db-" + db;
		str += pitch;
		return str + "}";
	}

	/** Get the colors as a string array formatted like so: {soundName, db, pitch} */
	@NotNull
	public String[] getAsStringArray() {
		return new String[]{soundName, db + "", pitch + ""};
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVSound(soundName, db, pitch);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Sound;
	}

	@Override
	public String toString() {
		return toArrayString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVSound) {
			SVSound other = (SVSound) o;
			return ArmaPrecision.isEqualTo(this.db, other.db)
					&& ArmaPrecision.isEqualTo(this.pitch, other.pitch)
					&& this.soundName.equals(other.soundName);
		}
		return false;
	}
}
