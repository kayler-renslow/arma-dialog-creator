package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import org.jetbrains.annotations.NotNull;

/**
 Interface for values.

 @author Kayler
 @see ExpressionInterpreter
 @since 07/14/2016 */
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

		@NotNull
		public String getAsDisplayableArmaString() {
			return "\"" + value.replaceAll("\"", "\"") + "\"";
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof StringLiteral) {
				StringLiteral other = (StringLiteral) o;
				return this.value.equals(other.value);
			}
			return false;
		}

		/** @return {@link #getValue()} */
		@Override
		public String toString() {
			return value;
		}
	}

	class NumVal implements Value {
		private double val;

		public NumVal(double v) {
			val = v;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof NumVal) {
				NumVal other = (NumVal) o;
				//see Double.equals()
				return Double.doubleToLongBits(this.val) == Double.doubleToLongBits(other.val);
			}
			return false;
		}

		public double v() {
			return val;
		}

		/** @return {@link ArmaPrecision#format(double)} of {@link #v()} */
		@Override
		public String toString() {
			return ArmaPrecision.format(val);
		}
	}

	Void Void = new Void();

	class Void implements Value {
		private Void() {
		}

		@Override
		public String toString() {
			return "";
		}
	}
}
