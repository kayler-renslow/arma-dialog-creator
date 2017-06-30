package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 Interface for values. A value should be immutable.

 @author Kayler
 @see ExpressionInterpreter
 @since 07/14/2016 */
public interface Value {

	class StringLiteral implements Value {
		private final String rawValue;
		private final String displayableValue;

		/**
		 A string literal.

		 @param value the String without surrounding quotes and
		 all inner cancelled quotes ("" ,'') are replaced with "
		 */
		public StringLiteral(@NotNull String value) {
			this.rawValue = value;
			this.displayableValue = "\"" + value.replaceAll("([\"'])", "\"\"") + "\"";
		}

		/**
		 Return the raw version of the String.
		 This will not be the String that will look like an arma string
		 (won't have surrounded quotes, no cancelling quotes like "")

		 @return raw value for use in {@link ExpressionInterpreter}
		 @see #toString()
		 */
		@NotNull
		public String getValue() {
			return rawValue;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof StringLiteral) {
				StringLiteral other = (StringLiteral) o;
				return this.rawValue.equals(other.rawValue);
			}
			return false;
		}

		/** @return the String as an Arma string. Will have surrounding quotes and all inner quotes (",') will be cancelled ("="", '='') */
		@Override
		public String toString() {
			return displayableValue;
		}

		/** @return the value's length */
		public int length() {
			return rawValue.length();
		}
	}

	class NumVal implements Value {
		private final double val;

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
				return ArmaPrecision.isEqualTo(this.val, other.val);
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

	/**
	 Used for executing code in {}.

	 @author Kayler
	 @since 5/24/2017
	 */
	class Code implements Value {
		private final List<AST.Statement> statements;
		private final ExpressionEvaluator evaluator;

		/**
		 Creates a code instance with the given list of {@link AST.Statement} and {@link ExpressionEvaluator}.
		 This constructor is primarily used in {@link Code#exec(Env)} for when the code was inside {} instead of a String.

		 @param statements list of {@link AST.Statement}
		 @param evaluator evaluator instance that the {@link Code} instance is being constructed in
		 */
		protected Code(@NotNull List<AST.Statement> statements, @NotNull ExpressionEvaluator evaluator) {
			this.statements = statements;
			this.evaluator = evaluator;
		}

		/** @return list of {@link AST.Statement} to run with {@link #exec(Env)} */
		@NotNull
		protected List<AST.Statement> getStatements() {
			return statements;
		}

		/**
		 Execute the code.

		 @param env environment to execute in
		 @return the last statement's value in a code string or {}.
		 */
		@NotNull
		public Value exec(@NotNull Env env) throws ExpressionEvaluationException {
			return evaluator.evaluate(statements, env);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append('{');
			for (AST.Statement s : statements) {
				sb.append(s.toString());
			}
			sb.append('}');
			return sb.toString();
		}
	}

	/**
	 An array with the underlying data being a list.

	 @author Kayler
	 @since 5/24/2017
	 */
	class Array implements Value, Iterable<Value> {
		private final List<Value> items;

		/** Construct an array with the internal data equal to items */
		public Array(@NotNull List<Value> items) {
			this.items = items;
		}

		/** Construct an array with the internal data equal to items */
		public Array(@NotNull Value... items) {
			this.items = Arrays.asList(items);
		}

		/** Construct an array with the internal data equal to a new, empty {@link ArrayList} */
		public Array() {
			items = new ArrayList<>();
		}

		@NotNull
		public List<Value> getItems() {
			return items;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof Array) {
				Array other = (Array) o;
				return this.items.equals(other.items);
			}
			return false;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			sb.append('[');
			for (Value v : items) {
				sb.append(v.toString());
				if (i != items.size() - 1) {
					sb.append(", ");
				}
				i++;
			}
			sb.append(']');
			return sb.toString();
		}

		@NotNull
		@Override
		public Iterator<Value> iterator() {
			return items.iterator();
		}

		public int length() {
			return items.size();
		}

		@NotNull
		public Value get(int i) {
			return items.get(i);
		}
	}

	/** Only instance of {@link Void} */
	Void Void = new Void();

	/**
	 Value for nothing or nil

	 @author Kayler
	 @since 5/23/2017
	 */
	class Void implements Value {
		private Void() {
		}

		@Override
		public String toString() {
			return "nil";
		}

		@Override
		public boolean equals(Object o) {
			return o == this;
		}
	}

	/** Only instance of {@link BoolVal} that is equal to true */
	BoolVal True = new BoolVal(true);
	/** Only instance of {@link BoolVal} that is equal to false */
	BoolVal False = new BoolVal(false);

	class BoolVal implements Value {
		private boolean bool;

		private BoolVal(boolean bool) {
			this.bool = bool;
		}

		public boolean isTrue() {
			return bool;
		}

		public boolean isFalse() {
			return !bool;
		}

		@Override
		public String toString() {
			return bool + "";
		}

		@Override
		public boolean equals(Object o) {
			//no need to cast and compare values since there are only 2 instance of BoolVal
			return o == this;
		}

		@NotNull
		public static BoolVal get(boolean b) {
			return b ? True : False;
		}

		/** @return if true, return false, otherwise return true */
		@NotNull
		public BoolVal not() {
			return bool ? False : True;
		}
	}
}
