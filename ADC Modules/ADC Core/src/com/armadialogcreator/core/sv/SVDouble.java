package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/** A generic wrapper implementation for a double. */
public class SVDouble extends SerializableValue implements SVNumericValue {
	public static final StringArrayConverter<SVDouble> CONVERTER = new StringArrayConverter<SVDouble>() {
		@Override
		public SVDouble convert(@NotNull Env env, @NotNull String[] values) throws Exception {
			return new SVDouble(Double.parseDouble(values[0]));
		}
	};
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.########");

	static {
		DECIMAL_FORMAT.setRoundingMode(RoundingMode.CEILING);
	}

	public static String format(double d) {
		return DECIMAL_FORMAT.format(d);
	}

	private final double d;
	
	public SVDouble(double d) {
		this.d = d;
	}
	
	public double getDouble() {
		return d;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{format(d)};
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVDouble(d);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Float;
	}

	@Override
	public String toString() {
		return format(d);
	}

	/** use the equals method in {@link SerializableValue}*/
	@Override
	public boolean equals(Object o){
		return super.equals(o);
	}

	@Override
	public int toInt() {
		return (int) Math.round(d);
	}

	@Override
	public double toDouble() {
		return d;
	}
}
