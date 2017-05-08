package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import org.jetbrains.annotations.NotNull;

/**
 Interface for values.
 @author Kayler
 @since 07/14/2016
 @see ExpressionInterpreter
 */
public interface Value {

	class StringLiteral implements Value {
		private String value;

		public StringLiteral(@NotNull String value) {
			this.value = value;
		}

		@NotNull
		public String getValue() {
			return value;
		}

		/** @return {@link #getValue()} */
		@Override
		public String toString() {
			return value;
		}
	}

	class NumVal implements Value{
		private double val;

		public NumVal(double v) {
			val = v;
		}

		public double v() {
			return val;
		}

		/**@return {@link ArmaPrecision#format(double)} of {@link #v()}*/
		@Override
		public String toString() {
			return ArmaPrecision.format(val);
		}
	}
}
