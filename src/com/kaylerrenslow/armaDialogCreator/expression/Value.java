package com.kaylerrenslow.armaDialogCreator.expression;

/**
 Created by Kayler on 07/14/2016.
 */
public interface Value {
	@Override
	String toString();

	class NumVal implements Value{
		private double val;

		public NumVal(double v) {
			val = v;
		}

		public double v() {
			return val;
		}

		@Override
		public String toString() {
			int tmp = (int) val;
			if (tmp == val) {
				return "" + tmp; //get rid of trailing zeroes
			}
			return "" + val;
		}
	}
}
