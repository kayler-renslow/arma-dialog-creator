package com.armadialogcreator.core.sv;

/**
 Use this interface when a {@link SerializableValue} contains a numeric value.

 @author Kayler
 @see SVInteger
 @see SVDouble
 @see SVExpression
 @since 07/04/2017 */
public interface SVNumericValue {
	int toInt();

	double toDouble();
}
