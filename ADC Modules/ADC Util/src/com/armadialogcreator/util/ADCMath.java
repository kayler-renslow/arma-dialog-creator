package com.armadialogcreator.util;

/**
 @author K
 @since 4/2/19 */
public class ADCMath {
	public static boolean equals(double d, double d2, double eps) {
		return Math.abs(d - d2) < eps;
	}
}
