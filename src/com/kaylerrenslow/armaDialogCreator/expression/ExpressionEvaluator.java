package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 Created by Kayler on 07/14/2016.
 */
class ExpressionEvaluator implements AST.Visitor<Value> {

	private final ResourceBundle bundle = Lang.getBundle("ExpressionBundle");

	/**
	 Returns the value for the given expression in the given environment.

	 @throws ExpressionEvaluationException If the expression is invalid
	 */
	@NotNull
	public Value evaluate(AST.Expr e, Env env) throws ExpressionEvaluationException {
		try {
			return (Value) e.accept(this, env);
		} catch (Exception ex) {
			throw new ExpressionEvaluationException(ex.getMessage(), ex);
		}
	}

	@Override
	public Value visit(@NotNull AST.MaxExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(Math.max(leftN, getNumValValue(right)));
			}
			return unexpectedValueException(right, numberTypeName());
		}
		return unexpectedValueException(left, numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.MinExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(Math.min(leftN, getNumValValue(right)));
			}
			return unexpectedValueException(right, numberTypeName());
		}
		return unexpectedValueException(left, numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.AddExpr expr, @NotNull Env env) {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(leftN + getNumValValue(right));
			}
			if (right instanceof Value.StringLiteral) {
				return new Value.StringLiteral(ArmaPrecision.format(leftN) + right.toString());
			}
			return unexpectedValueException(right, numberTypeName() + "," + stringTypeName());
		} else if (left instanceof Value.StringLiteral) {
			if (right instanceof Value.NumVal) {
				return new Value.StringLiteral(left.toString() + ArmaPrecision.format(getNumValValue(right)));
			}
			if (right instanceof Value.StringLiteral) {
				return new Value.StringLiteral(left.toString() + right.toString());
			}
			return unexpectedValueException(right, numberTypeName() + "," + stringTypeName());

		}
		return unexpectedValueException(left, numberTypeName() + "," + stringTypeName());
	}

	@Override
	public Value visit(@NotNull AST.SubExpr expr, @NotNull Env env) {
		Value.NumVal left = (Value.NumVal) expr.getLeft().accept(this, env);
		Value.NumVal right = (Value.NumVal) expr.getRight().accept(this, env);
		return new Value.NumVal(left.v() - right.v());
	}

	@Override
	public Value visit(@NotNull AST.MultExpr expr, @NotNull Env env) {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(leftN * getNumValValue(right));
			}
			return unexpectedValueException(right, numberTypeName());
		}
		return unexpectedValueException(left, numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.DivExpr expr, @NotNull Env env) {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(leftN / getNumValValue(right));
			}
			return unexpectedValueException(right, numberTypeName());
		}
		return unexpectedValueException(left, numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.UnaryExpr expr, @NotNull Env env) {
		Value val = (Value) expr.getExpr().accept(this, env);
		if (val instanceof Value.NumVal) {
			Value.NumVal v = (Value.NumVal) val;
			if (expr.isAdd()) {
				return new Value.NumVal(+v.v());
			}
			return new Value.NumVal(-v.v());
		}
		return unexpectedValueException(val, numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.ParenExpr expr, @NotNull Env env) {
		return (Value) expr.getExp().accept(this, env);
	}

	@Override
	public Value visit(@NotNull AST.IdentifierExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		Value v = env.getValue(expr.getIdentifier());
		if (v == null) {
			throw new ExpressionEvaluationException(String.format(Lang.ApplicationBundle().getString("Expression.identifier_not_set_f"), expr.getIdentifier()));
		}
		return v;
	}

	@Override
	public Value visit(@NotNull AST.IntegerExpr expr, @NotNull Env env) {
		return new Value.NumVal(expr.getValue());
	}

	@Override
	public Value visit(@NotNull AST.FloatExpr expr, @NotNull Env env) {
		return new Value.NumVal(expr.getValue());
	}

	@Override
	public Value visit(@NotNull AST.StringExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		//this method will cut off the surrounded quotes given by the lexer
		//then, it will convert "" and '' to single " and then return the Java string in a class

		String truncated = expr.getValue().substring(1, expr.getValue().length() - 1);

		return new Value.StringLiteral(
				stringPattern.matcher(truncated).replaceAll("\"")
		);
	}

	private double getNumValValue(@NotNull Value v) {
		return ((Value.NumVal) v).v();
	}

	@NotNull
	private String stringTypeName() {
		return bundle.getString("string");
	}

	@NotNull
	private String numberTypeName() {
		return bundle.getString("number");
	}

	private Value unexpectedValueException(@NotNull Value v, @NotNull String expected) {
		throw new IllegalArgumentException(String.format(bundle.getString("unexpected_value_expected_f"), v.toString(), expected));
	}

	private final Pattern stringPattern = Pattern.compile("(\"\")|('')");
}
