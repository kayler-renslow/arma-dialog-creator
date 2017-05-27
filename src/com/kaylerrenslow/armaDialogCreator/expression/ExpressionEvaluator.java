package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 Evaluates an expression AST and returns the result via {@link #evaluate(AST.Expr, Env)}.

 @author Kayler
 @since 07/14/2016 */
class ExpressionEvaluator implements AST.Visitor<Value> {

	private final ResourceBundle bundle = Lang.getBundle("ExpressionBundle");
	private final ExpressionInterpreter interpreter;
	private final AtomicBoolean terminated = new AtomicBoolean(false);

	public ExpressionEvaluator(@NotNull ExpressionInterpreter interpreter) {
		this.interpreter = interpreter;
	}


	public void terminate() {
		terminated.set(true);
	}

	private void checkIfTerminated() {
		if (terminated.get()) {
			throw new TerminateEvaluationException();
		}
	}

	/**
	 Returns the value for the given expression in the given environment.

	 @throws ExpressionEvaluationException If the expression is invalid
	 */
	@NotNull
	public Value evaluate(@NotNull AST.Expr e, @NotNull Env env) throws ExpressionEvaluationException {
		try {
			return (Value) e.accept(this, env);
		} catch (Exception ex) {
			if (ex instanceof ExpressionEvaluationException) {
				if (ex instanceof EndEvaluationException) {
					return ((EndEvaluationException) ex).getReturnValue();
				}
				throw ex;
			}
			throw new ExpressionEvaluationException(ex.getMessage(), ex);
		}
	}

	/**
	 Each statement provided will be evaluated. If <code>statements.size()==0</code>, then {@link Value.Void} will be returned

	 @return the last {@link AST.Statement} value for the given statement list in the given environment.
	 @throws ExpressionEvaluationException If one of the statements is invalid
	 */
	@NotNull
	public Value evaluate(@NotNull List<AST.Statement> statements, @NotNull Env env) throws ExpressionEvaluationException {
		try {
			Value last = Value.Void;
			if (statements.size() == 0) {
				//In the case that we are in an infinite loop evaluating nothing (for loop with empty code block),
				//we must check if we terminated here since it won't be triggered inside a visitor method.
				checkIfTerminated();
			}
			for (AST.Statement s : statements) {
				last = (Value) s.accept(this, env);
			}
			return last;
		} catch (Exception ex) {
			if (ex instanceof ExpressionEvaluationException) {
				if (ex instanceof EndEvaluationException) {
					return ((EndEvaluationException) ex).getReturnValue();
				}
				throw ex;
			}
			throw new ExpressionEvaluationException(ex.getMessage(), ex);
		}
	}

	@Override
	public Value visit(@NotNull AST.MaxExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

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
		checkIfTerminated();

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
		checkIfTerminated();

		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(leftN + getNumValValue(right));
			}
			return unexpectedValueException(right, numberTypeName());
		} else if (left instanceof Value.StringLiteral) {
			if (right instanceof Value.StringLiteral) {
				return new Value.StringLiteral(((Value.StringLiteral) left).getValue() + ((Value.StringLiteral) right).getValue());
			}
			return unexpectedValueException(right, stringTypeName());
		} else if (left instanceof Value.Array) {
			if (right instanceof Value.Array) {
				((Value.Array) left).getItems().addAll(((Value.Array) right).getItems());
				return left;
			}
			return unexpectedValueException(right, arrayTypeName());
		}
		return unexpectedValueException(left, numberTypeName() + "," + stringTypeName() + "," + arrayTypeName());
	}

	@Override
	public Value visit(@NotNull AST.SubExpr expr, @NotNull Env env) {
		checkIfTerminated();

		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);

		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(leftN - getNumValValue(right));
			}
			return unexpectedValueException(right, numberTypeName());
		} else if (left instanceof Value.Array) {
			if (right instanceof Value.Array) {
				((Value.Array) left).getItems().removeAll(((Value.Array) right).getItems());
				return left;
			}
			return unexpectedValueException(right, arrayTypeName());
		}
		return unexpectedValueException(left, numberTypeName() + "," + arrayTypeName());
	}

	@Override
	public Value visit(@NotNull AST.MultExpr expr, @NotNull Env env) {
		checkIfTerminated();

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
		checkIfTerminated();

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
		checkIfTerminated();

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
		checkIfTerminated();

		return (Value) expr.getExp().accept(this, env);
	}

	@Override
	public Value visit(@NotNull AST.IdentifierExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		String var = expr.getIdentifier();
		if (var.equals("true")) {
			return Value.True;
		}
		if (var.equals("false")) {
			return Value.False;
		}
		Value v = env.getValue(var);
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

	@Override
	public Value visit(@NotNull AST.Statement statement, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		if (statement.getAssignment() != null) {
			return (Value) statement.getAssignment().accept(this, env);
		}
		if (statement.getExpr() != null) {
			return (Value) statement.getExpr().accept(this, env);
		}
		throw new IllegalStateException("didn't match an assignment or expression");
	}

	@Override
	public Value visit(@NotNull AST.Assignment assignment, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		String var = assignment.getVar();
		for (String s : ExpressionInterpreter.getSupportedCommands()) {
			if (s.equals(var)) {
				throw new ExpressionEvaluationException(String.format(bundle.getString("assigning_to_command_error_f"), s));
			}
		}
		env.put(var, (Value) assignment.getExpr().accept(this, env));

		return Value.Void;
	}

	@Override
	public Value visit(@NotNull AST.Code code, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		return new Value.Code(code.getStatements(), this);
	}

	@Override
	public Value visit(@NotNull AST.CodeExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		return (Value) expr.getCode().accept(this, env);
	}

	@Override
	public Value visit(@NotNull AST.IfExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		Value cond = (Value) expr.getCondition().accept(this, env);
		if (cond != Value.True && cond != Value.False) {
			unexpectedValueException(cond, boolTypeName());
		}
		switch (expr.getType()) {
			case ExitWith: {
				if (cond == Value.True) {
					if (expr.getTrueCond() == null) {
						throw new IllegalStateException("getTrueCond() is null");
					}
					Value ret = (Value) expr.getTrueCond().accept(this, env);
					if (ret instanceof Value.Code) {
						throw new EndEvaluationException(((Value.Code) ret).exec(env));
					}
					unexpectedValueException(ret, codeTypeName());
				}
				break;
			}
			case IfThen: {
				if (cond == Value.True) {
					if (expr.getArr() != null) {
						Value.Array array = (Value.Array) expr.getArr().accept(this, env);
						if (array.length() < 2) {
							badArrayLength(array, 2, "if condition then []");
						}
						Value v = array.get(0);
						if (v instanceof Value.Code) {
							return ((Value.Code) v).exec(env);
						}
						unexpectedValueException(v, codeTypeName());
					} else {
						if (expr.getTrueCond() == null) {
							throw new IllegalStateException("getTrueCond() is null");
						}
						Value v = (Value) expr.getTrueCond().accept(this, env);
						if (v instanceof Value.Code) {
							return ((Value.Code) v).exec(env);
						}
						unexpectedValueException(v, codeTypeName());
					}
				}
				if (expr.getArr() != null) {
					Value.Array array = (Value.Array) expr.getArr().accept(this, env);
					if (array.length() < 2) {
						badArrayLength(array, 2, "if condition then []");
					}
					Value v = array.get(1);
					if (v instanceof Value.Code) {
						return ((Value.Code) v).exec(env);
					}
					unexpectedValueException(v, codeTypeName());
				} else {
					if (expr.getFalseCond() == null) {
						throw new IllegalStateException("getFalseCond() is null");
					}
					Value v = (Value) expr.getFalseCond().accept(this, env);
					if (v instanceof Value.Code) {
						return ((Value.Code) v).exec(env);
					}
					unexpectedValueException(v, codeTypeName());
				}
			}
			default: {
				throw new IllegalStateException("unhandled type: " + expr.getType());
			}
		}
		return Value.Void;
	}

	@Override
	public Value visit(@NotNull AST.Array array, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		List<Value> values = new ArrayList<>();
		for (AST.Expr e : array.getItems()) {
			values.add((Value) e.accept(this, env));
		}
		return new Value.Array(values);
	}

	@Override
	public Value visit(@NotNull AST.SelectExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.StringLiteral) {
			Value.StringLiteral string = (Value.StringLiteral) left;
			if (right instanceof Value.Array) {
				//string select [start,length]
				Value.Array rightArr = (Value.Array) right;
				if (rightArr.length() < 1) {
					badArrayLength(rightArr, 1, "\"string\" select [start,length]");
				}
				Value startVal = rightArr.get(0);
				if (!(startVal instanceof Value.NumVal)) {
					unexpectedValueException(startVal, numberTypeName());
				}
				int start = (int) getNumValValue(startVal);
				if (start < 0 || start >= string.length()) {
					indexOutOfBounds(string.getValue(), "start", start, 0, string.length() - 1);
				}
				int length = string.length();
				if (rightArr.length() > 1) {
					Value lengthVal = rightArr.get(1);
					if (!(lengthVal instanceof Value.NumVal)) {
						unexpectedValueException(lengthVal, numberTypeName());
					}
					length = (int) getNumValValue(lengthVal);
				}
				if (length < 0 || length >= string.length()) {
					indexOutOfBounds(string.getValue(), "length", length, 0, string.length() - 1);
				}
				return new Value.StringLiteral(string.getValue().substring(start, length));
			}
		}
		if (left instanceof Value.Array) {
			Value.Array leftArr = (Value.Array) left;
			if (right instanceof Value.Code) {
				//[] select {}
				List<Value> newItems = new ArrayList<>();

				Value.Code rightCode = (Value.Code) right;
				for (Value v : leftArr) {
					env.put("_x", v);
					if (rightCode.exec(env) == Value.True) {
						newItems.add(v);
					}
				}
				return new Value.Array(newItems);
			}
			if (right instanceof Value.BoolVal) {
				//[] select boolean
				if (right == Value.True) {
					if (leftArr.length() < 2) {
						indexOutOfBounds(leftArr.toString(), "boolean", 1, 0, 2);
					}
					return leftArr.get(1);
				} else {
					if (leftArr.length() < 2) {
						indexOutOfBounds(leftArr.toString(), "boolean", 1, 0, 2);
					}
					return leftArr.get(0);
				}
			}
			if (right instanceof Value.Array) {
				//[] select [start, count]
				Value.Array rightArr = (Value.Array) right;

				if (rightArr.length() < 2) {
					badArrayLength(rightArr, 2, "[] select [start, count]");
				}

				Value startVal = rightArr.get(0);
				if (!(startVal instanceof Value.NumVal)) {
					unexpectedValueException(startVal, numberTypeName());
				}
				int start = (int) getNumValValue(startVal);

				Value countVal = rightArr.get(1);
				if (!(countVal instanceof Value.NumVal)) {
					unexpectedValueException(countVal, numberTypeName());
				}
				int count = (int) getNumValValue(countVal);

				if (start < 0 || start >= leftArr.length()) {
					indexOutOfBounds(leftArr.toString(), "start", start, 0, leftArr.length() - 1);
				}

				if (count < 0 || count >= leftArr.length()) {
					indexOutOfBounds(leftArr.toString(), "count", count, 0, leftArr.length() - 1);
				}

				return new Value.Array(leftArr.getItems().subList(start, count + 1));
			}
			unexpectedValueException(right, codeTypeName() + "," + boolTypeName() + "," + arrayTypeName());
		}
		return unexpectedValueException(left, stringTypeName() + "," + arrayTypeName());
	}

	@Override
	public Value visit(@NotNull AST.CompExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		switch (expr.getOperator()) {
			case Equal: {
				return Value.BoolVal.get(left.equals(right));
			}
			case NotEqual: {
				return Value.BoolVal.get(!left.equals(right));
			}
			case LessThan: {
				if (!(left instanceof Value.NumVal)) {
					unexpectedValueException(left, numberTypeName());
				}
				if (!(right instanceof Value.NumVal)) {
					unexpectedValueException(right, numberTypeName());
				}
				return Value.BoolVal.get(getNumValValue(left) < getNumValValue(right));
			}
			case LessThanOrEqual: {
				if (!(left instanceof Value.NumVal)) {
					unexpectedValueException(left, numberTypeName());
				}
				if (!(right instanceof Value.NumVal)) {
					unexpectedValueException(right, numberTypeName());
				}
				double leftN = getNumValValue(left);
				double rightN = getNumValValue(right);
				return Value.BoolVal.get(leftN < rightN || ArmaPrecision.isEqualTo(leftN, rightN));
			}
			case GreaterThan: {
				if (!(left instanceof Value.NumVal)) {
					unexpectedValueException(left, numberTypeName());
				}
				if (!(right instanceof Value.NumVal)) {
					unexpectedValueException(right, numberTypeName());
				}
				double leftN = getNumValValue(left);
				double rightN = getNumValValue(right);
				return Value.BoolVal.get(leftN > rightN);
			}
			case GreaterThanOrEqual: {
				if (!(left instanceof Value.NumVal)) {
					unexpectedValueException(left, numberTypeName());
				}
				if (!(right instanceof Value.NumVal)) {
					unexpectedValueException(right, numberTypeName());
				}

				double leftN = getNumValValue(left);
				double rightN = getNumValValue(right);
				return Value.BoolVal.get(leftN > rightN || ArmaPrecision.isEqualTo(leftN, rightN));
			}
		}
		throw new IllegalStateException("unhandled operator: " + expr.getOperator());
	}

	@Override
	public Value visit(@NotNull AST.ForVarExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		Value varVal = (Value) expr.getVarExpr().accept(this, env);
		if (!(varVal instanceof Value.StringLiteral)) {
			unexpectedValueException(varVal, stringTypeName());
		}
		String var = ((Value.StringLiteral) varVal).getValue();

		Value fromVal = (Value) expr.getFromExpr().accept(this, env);
		if (!(fromVal instanceof Value.NumVal)) {
			unexpectedValueException(fromVal, numberTypeName());
		}
		double from = getNumValValue(fromVal);

		Value toVal = (Value) expr.getToExpr().accept(this, env);
		if (!(toVal instanceof Value.NumVal)) {
			unexpectedValueException(toVal, numberTypeName());
		}

		double to = getNumValValue(toVal);

		double step = 1;
		if (expr.getStepExpr() != null) {
			Value stepVal = (Value) expr.getStepExpr().accept(this, env);
			if (!(stepVal instanceof Value.NumVal)) {
				unexpectedValueException(stepVal, numberTypeName());
			}
			step = getNumValValue(stepVal);
		}

		Value.Code code;
		{
			Value doCodeVal = (Value) expr.getDoCode().accept(this, env);
			if (!(doCodeVal instanceof Value.Code)) {
				unexpectedValueException(doCodeVal, codeTypeName());
			}
			code = (Value.Code) doCodeVal;
		}

		double i = from;

		while ((i <= to && step > 0) || i >= to && step < 0) {
			env.put(var, new Value.NumVal(i));
			code.exec(env);
			i += step;
		}

		return Value.Void;
	}

	@Override
	public Value visit(@NotNull AST.ForArrExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		checkIfTerminated();

		return Value.Void;
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

	@NotNull
	private String codeTypeName() {
		return bundle.getString("code");
	}

	@NotNull
	private String arrayTypeName() {
		return bundle.getString("array");
	}

	@NotNull
	private String boolTypeName() {
		return bundle.getString("boolean");
	}

	private Value unexpectedValueException(@NotNull Value v, @NotNull String expected) {
		throw new ExpressionEvaluationException(String.format(bundle.getString("unexpected_value_expected_f"), v.toString(), expected));
	}

	private void badArrayLength(@NotNull Value.Array arr, int reqSize, @NotNull String from) {
		throw new ExpressionEvaluationException(String.format(bundle.getString("bad_array_size_f"), arr.length(), reqSize, from));
	}

	private void indexOutOfBounds(@NotNull String source, @NotNull String varName, int index, int lowerBound, int upperBound) {
		throw new ExpressionEvaluationException(String.format(bundle.getString("index_out_of_bounds"), varName, source, index, lowerBound, upperBound));
	}

	private final Pattern stringPattern = Pattern.compile("(\"\")|('')");

}
