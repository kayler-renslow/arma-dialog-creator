package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 @author Kayler
 @since 05/06/2017 */
public class ArmaPrecision {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.########");
	public static final double EPSILON = 0.00000001;

	/** Return true if d1 == d2 or Math.abs(d1 - d2) < {@link #EPSILON}. */
	public static boolean isEqualTo(double d1, double d2) {
		return d1 == d2 || Math.abs(d1 - d2) < EPSILON;
	}

	static {
		DECIMAL_FORMAT.setRoundingMode(RoundingMode.CEILING);

		//This is to ensure that numbers use periods instead of commas for decimals and use commas for thousands place.
		//Also, this is being initialized here because DECIMAL_FORMAT is static and may initialize before some testing methods
		DECIMAL_FORMAT.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
	}

	/** Get a String representation of the given double that is truncated and not rounded */
	@NotNull
	public static String format(double d) {
		return DECIMAL_FORMAT.format(d);
	}
}
