package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 Interface for values. A value should be immutable.

 @author Kayler
 @see ExpressionInterpreter
 @since 07/14/2016 */
public interface Value {

	class StringLiteral implements Value {
		private final String value;

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

		/** @return the value's length */
		public int length() {
			return value.length();
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

	/**
	 Used for executing a code String or code in {}.

	 @author Kayler
	 @since 5/24/2017
	 */
	class Code implements Value {
		private String codeString;
		private List<AST.Statement> statements;
		private ExpressionEvaluator evaluator;

		/**
		 Create a code instance with the given code String.
		 This code will be executed in a new evaluator via {@link ExpressionInterpreter#evaluateStatements(String, Env)}

		 @param codeString the code to execute, in a String
		 */
		public Code(@NotNull String codeString) {
			this.codeString = codeString;
		}

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

		/**
		 Execute the code.

		 @param env environment to execute in
		 @return the last statement's value in a code string or {}.
		 */
		@NotNull
		public Value exec(@NotNull Env env) throws ExpressionEvaluationException {
			if (codeString != null) {
				return ExpressionInterpreter.getInstance().evaluateStatements(codeString, env);
			}
			return ExpressionInterpreter.getInstance().evaluateStatements(statements, env, evaluator);
		}

		@Override
		public String toString() {
			return "{...}";
		}
	}

	/**
	 An array

	 @author Kayler
	 @since 5/24/2017
	 */
	class Array implements Value, Iterable<Value> {
		private final List<Value> items;

		public Array(@NotNull List<Value> items) {
			this.items = items;
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
			return "";
		}

		@Override
		public boolean equals(Object o) {
			return o == this;
		}
	}

	BoolVal True = new BoolVal(true);
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
	}
}
