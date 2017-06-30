package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.CharSequenceReader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 Evaluates an expression AST and returns the result via {@link #evaluate(AST.Expr, Env)}.

 @author Kayler
 @since 07/14/2016 */
class ExpressionEvaluator implements AST.Visitor<Value> {

	private final ResourceBundle bundle = Lang.getBundle("ExpressionBundle");
	private final AtomicBoolean terminated = new AtomicBoolean(false);

	/**
	 Terminate this evaluator immediately. This method is thread-safe.
	 <br>Will cause the evaluator to throw a {@link TerminateEvaluationException}
	 */
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
	public Value evaluate(@NotNull AST.Expr e, @NotNull Env env) {
		try {
			return (Value) e.accept(this, env);
		} catch (Exception ex) {
			if (ex instanceof ExpressionEvaluationException) {
				if (ex instanceof EndEvaluationException) {
					return ((EndEvaluationException) ex).getReturnValue();
				}
				throw ex;
			}
			throw new ExpressionEvaluationException(null, ex.getMessage(), ex);
		}
	}

	/**
	 Each statement provided will be evaluated. If <code>statements.size()==0</code>, then {@link Value.Void} will be returned

	 @return the last {@link AST.Statement} value for the given statement list in the given environment.
	 @throws ExpressionEvaluationException If one of the statements is invalid
	 */
	@NotNull
	public Value evaluate(@NotNull List<AST.Statement> statements, @NotNull Env env) {
		try {
			Value last = Value.Void;
			//In the case that we are in a loop evaluating nothing (for loop with empty code block, statements.size() == 0),
			//we must check if we terminated here since it won't be triggered inside a visitor method.
			checkIfTerminated();
			for (AST.Statement s : statements) {
				checkIfTerminated();
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
			throw new ExpressionEvaluationException(null, ex.getMessage(), ex);
		}
	}

	@Override
	public Value visit(@NotNull AST.MaxExpr expr, @NotNull Env env) {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(Math.max(leftN, getNumValValue(right)));
			}
			return unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
		}
		return unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.MinExpr expr, @NotNull Env env) {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(Math.min(leftN, getNumValValue(right)));
			}
			return unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
		}
		return unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
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
			return unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
		} else if (left instanceof Value.StringLiteral) {
			if (right instanceof Value.StringLiteral) {
				return new Value.StringLiteral(((Value.StringLiteral) left).getValue() + ((Value.StringLiteral) right).getValue());
			}
			return unexpectedValueException(expr, right, expr.getRight(), stringTypeName());
		} else if (left instanceof Value.Array) {
			if (right instanceof Value.Array) {
				((Value.Array) left).getItems().addAll(((Value.Array) right).getItems());
				return left;
			}
			return unexpectedValueException(expr, right, expr.getRight(), arrayTypeName());
		}
		return unexpectedValueException(expr, left, expr.getLeft(), numberTypeName() + "," + stringTypeName() + "," + arrayTypeName());
	}

	@Override
	public Value visit(@NotNull AST.SubExpr expr, @NotNull Env env) {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);

		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(leftN - getNumValValue(right));
			}
			return unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
		} else if (left instanceof Value.Array) {
			if (right instanceof Value.Array) {
				((Value.Array) left).getItems().removeAll(((Value.Array) right).getItems());
				return left;
			}
			return unexpectedValueException(expr, right, expr.getRight(), arrayTypeName());
		}
		return unexpectedValueException(expr, left, expr.getLeft(), numberTypeName() + "," + arrayTypeName());
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
			return unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
		}
		return unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
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
			return unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
		}
		return unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.ModExpr expr, @NotNull Env env) {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.NumVal) {
			double leftN = getNumValValue(left);
			if (right instanceof Value.NumVal) {
				return new Value.NumVal(leftN % getNumValValue(right));
			}
			return unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
		}
		return unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.ExponentExpr expr, @NotNull Env env) {
		double result = 0;
		boolean didFirst = false;

		for (AST.Expr e : expr.getExprs()) {
			Value v = (Value) e.accept(this, env);
			if (v instanceof Value.NumVal) {
				if (!didFirst) {
					result = ((Value.NumVal) v).v();
					didFirst = true;
				} else {
					result = Math.pow(result, ((Value.NumVal) v).v());
				}
			} else {
				return unexpectedValueException(expr, v, e, numberTypeName());
			}
		}

		return new Value.NumVal(result);
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
		return unexpectedValueException(expr, val, expr.getExpr(), numberTypeName());
	}

	@Override
	public Value visit(@NotNull AST.ParenExpr expr, @NotNull Env env) {
		return (Value) expr.getExp().accept(this, env);
	}

	@Override
	public Value visit(@NotNull AST.IdentifierExpr expr, @NotNull Env env) {
		String var = expr.getIdentifier();
		if (var.equalsIgnoreCase("true")) {
			return Value.True;
		}
		if (var.equalsIgnoreCase("false")) {
			return Value.False;
		}
		Value v = getValueForIdentifier(expr, env, var);
		return v;
	}

	@NotNull
	private Value getValueForIdentifier(@NotNull AST.Expr expr, @NotNull Env env, @NotNull String var) {
		Value v = env.getValue(var);
		if (v == null) {
			throw new ExpressionEvaluationException(
					expr,
					String.format(Lang.ApplicationBundle().getString("Expression.identifier_not_set_f"), var)
			);
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
	public Value visit(@NotNull AST.StringExpr expr, @NotNull Env env) {
		//this method will cut off the surrounded quotes given by the lexer
		//then, it will convert "" and '' to single "

		String truncated = expr.getValue().substring(1, expr.getValue().length() - 1);

		return new Value.StringLiteral(
				stringPattern.matcher(truncated).replaceAll("\"")
		);
	}

	@Override
	public Value visit(@NotNull AST.Statement statement, @NotNull Env env) {
		if (statement.getAssignment() != null) {
			return (Value) statement.getAssignment().accept(this, env);
		}
		if (statement.getExpr() != null) {
			return (Value) statement.getExpr().accept(this, env);
		}
		throw new IllegalStateException("didn't match an assignment or expression");
	}

	@Override
	public Value visit(@NotNull AST.Assignment assignment, @NotNull Env env) {
		String var = assignment.getVar();
		for (String s : ExpressionInterpreter.getSupportedCommands()) {
			if (s.equals(var)) {
				throw new ExpressionEvaluationException(assignment, String.format(bundle.getString("assigning_to_command_error_f"), s));
			}
		}
		env.put(var, (Value) assignment.getExpr().accept(this, env));

		return Value.Void;
	}

	@Override
	public Value visit(@NotNull AST.Code code, @NotNull Env env) {
		return new Value.Code(code.getStatements(), this);
	}

	@Override
	public Value visit(@NotNull AST.CodeExpr expr, @NotNull Env env) {
		return (Value) expr.getCode().accept(this, env);
	}

	@Override
	public Value visit(@NotNull AST.IfExpr expr, @NotNull Env env) {
		Value cond = (Value) expr.getCondition().accept(this, env);
		if (cond != Value.True && cond != Value.False) {
			unexpectedValueException(expr, cond, expr.getCondition(), boolTypeName());
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
					unexpectedValueException(expr, ret, expr.getTrueCond(), codeTypeName());
				}
				//nothing left to do
				return Value.Void;
			}
			case IfThen: {
				if (cond == Value.True) {
					if (expr.getArr() != null) {
						Value.Array array = (Value.Array) expr.getArr().accept(this, env);
						if (array.length() < 2) {
							badArrayLength(expr, array, 2, "if condition then []");
						}
						Value v = array.get(0);
						if (v instanceof Value.Code) {
							return ((Value.Code) v).exec(env);
						}
						unexpectedValueException(expr, v, expr.getArr(), codeTypeName());
					} else {
						if (expr.getTrueCond() == null) {
							throw new IllegalStateException("getTrueCond() is null");
						}
						Value v = (Value) expr.getTrueCond().accept(this, env);
						if (v instanceof Value.Code) {
							return ((Value.Code) v).exec(env);
						}
						unexpectedValueException(expr, v, expr.getTrueCond(), codeTypeName());
					}
				}
				if (expr.getArr() != null) {
					Value.Array array = (Value.Array) expr.getArr().accept(this, env);
					if (array.length() < 2) {
						badArrayLength(expr, array, 2, "if condition then [{},{}]");
					}
					Value v = array.get(1);
					if (v instanceof Value.Code) {
						return ((Value.Code) v).exec(env);
					}
					unexpectedValueException(expr, v, expr.getArr(), codeTypeName());
				} else {
					if (expr.getFalseCond() == null) {
						return Value.Void;
					}
					Value v = (Value) expr.getFalseCond().accept(this, env);
					if (v instanceof Value.Code) {
						return ((Value.Code) v).exec(env);
					}
					unexpectedValueException(expr, v, expr.getFalseCond(), codeTypeName());
				}
			}
			default: {
				throw new IllegalStateException("unhandled type: " + expr.getType());
			}
		}
	}

	@Override
	public Value visit(@NotNull AST.Array array, @NotNull Env env) {
		List<Value> values = new ArrayList<>();
		for (AST.Expr e : array.getItems()) {
			values.add((Value) e.accept(this, env));
		}
		return new Value.Array(values);
	}

	@Override
	public Value visit(@NotNull AST.SelectExpr expr, @NotNull Env env) {
		Value left = (Value) expr.getLeft().accept(this, env);
		Value right = (Value) expr.getRight().accept(this, env);
		if (left instanceof Value.StringLiteral) {
			Value.StringLiteral string = (Value.StringLiteral) left;
			if (right instanceof Value.Array) {
				//string select [start,length]
				Value.Array rightArr = (Value.Array) right;
				if (rightArr.length() < 1) {
					badArrayLength(expr, rightArr, 1, "\"string\" select [start,length]");
				}
				Value startVal = rightArr.get(0);
				if (!(startVal instanceof Value.NumVal)) {
					unexpectedValueException(expr, startVal, expr.getRight(), numberTypeName());
				}
				int start = (int) Math.round(getNumValValue(startVal));
				if (start < 0 || start >= string.length()) {
					indexOutOfBounds(expr, string.getValue(), "start", start, 0, string.length() - 1);
				}
				if (rightArr.length() > 1) {
					int length;
					Value lengthVal = rightArr.get(1);
					if (!(lengthVal instanceof Value.NumVal)) {
						unexpectedValueException(expr, lengthVal, expr.getRight(), numberTypeName());
					}
					length = (int) Math.round(getNumValValue(lengthVal));
					if (length < 0 || start + length >= string.length()) {
						indexOutOfBounds(expr, string.getValue(), "length", length, 0, string.length() - 1);
					}

					return new Value.StringLiteral(string.getValue().substring(start, start + length));
				}

				return new Value.StringLiteral(string.getValue().substring(start));
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
						indexOutOfBounds(expr, leftArr.toString(), "boolean", 1, 0, 2);
					}
					return leftArr.get(1);
				} else {
					if (leftArr.length() < 2) {
						indexOutOfBounds(expr, leftArr.toString(), "boolean", 1, 0, 2);
					}
					return leftArr.get(0);
				}
			}
			if (right instanceof Value.Array) {
				//[] select [start, count]
				Value.Array rightArr = (Value.Array) right;

				if (rightArr.length() < 2) {
					badArrayLength(expr, rightArr, 2, "[] select [start, count]");
				}

				Value startVal = rightArr.get(0);
				if (!(startVal instanceof Value.NumVal)) {
					unexpectedValueException(expr, startVal, expr.getRight(), numberTypeName());
				}
				int start = (int) Math.round(getNumValValue(startVal));

				Value countVal = rightArr.get(1);
				if (!(countVal instanceof Value.NumVal)) {
					unexpectedValueException(expr, countVal, expr.getRight(), numberTypeName());
				}
				int count = (int) Math.round(getNumValValue(countVal));

				if (start < 0 || start >= leftArr.length()) {
					indexOutOfBounds(expr, leftArr.toString(), "start", start, 0, leftArr.length() - 1);
				}

				if (count < 0) {
					indexOutOfBounds(expr, leftArr.toString(), "count", count, 0, 10000000);
				}

				return new Value.Array(leftArr.getItems().subList(start, Math.min(count, leftArr.length())));
			}
			if (right instanceof Value.NumVal) {
				int rightIndex = (int) Math.round(((Value.NumVal) right).v());
				if (rightIndex < 0 || rightIndex >= leftArr.length()) {
					indexOutOfBounds(expr, leftArr.toString(), "index", rightIndex, 0, leftArr.length() - 1);
				}
				return leftArr.getItems().get(rightIndex);
			}
			unexpectedValueException(expr, right, expr.getRight(),
					codeTypeName() + "," + boolTypeName() + "," + arrayTypeName() + "," + numberTypeName()
			);
		}
		return unexpectedValueException(expr, left, expr.getLeft(), stringTypeName() + "," + arrayTypeName());
	}

	@Override
	public Value visit(@NotNull AST.CompExpr expr, @NotNull Env env) {
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
					unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
				}
				if (!(right instanceof Value.NumVal)) {
					unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
				}
				return Value.BoolVal.get(getNumValValue(left) < getNumValValue(right));
			}
			case LessThanOrEqual: {
				if (!(left instanceof Value.NumVal)) {
					unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
				}
				if (!(right instanceof Value.NumVal)) {
					unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
				}
				double leftN = getNumValValue(left);
				double rightN = getNumValValue(right);
				return Value.BoolVal.get(leftN < rightN || ArmaPrecision.isEqualTo(leftN, rightN));
			}
			case GreaterThan: {
				if (!(left instanceof Value.NumVal)) {
					unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
				}
				if (!(right instanceof Value.NumVal)) {
					unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
				}
				double leftN = getNumValValue(left);
				double rightN = getNumValValue(right);
				return Value.BoolVal.get(leftN > rightN);
			}
			case GreaterThanOrEqual: {
				if (!(left instanceof Value.NumVal)) {
					unexpectedValueException(expr, left, expr.getLeft(), numberTypeName());
				}
				if (!(right instanceof Value.NumVal)) {
					unexpectedValueException(expr, right, expr.getRight(), numberTypeName());
				}

				double leftN = getNumValValue(left);
				double rightN = getNumValValue(right);
				return Value.BoolVal.get(leftN > rightN || ArmaPrecision.isEqualTo(leftN, rightN));
			}
		}
		throw new IllegalStateException("unhandled operator: " + expr.getOperator());
	}

	@Override
	public Value visit(@NotNull AST.ForVarExpr expr, @NotNull Env env) {
		Value varVal = (Value) expr.getVarExpr().accept(this, env);
		if (!(varVal instanceof Value.StringLiteral)) {
			unexpectedValueException(expr, varVal, expr.getVarExpr(), stringTypeName());
		}
		String var = ((Value.StringLiteral) varVal).getValue();

		Value fromVal = (Value) expr.getFromExpr().accept(this, env);
		if (!(fromVal instanceof Value.NumVal)) {
			unexpectedValueException(expr, fromVal, expr.getFromExpr(), numberTypeName());
		}
		double from = getNumValValue(fromVal);

		Value toVal = (Value) expr.getToExpr().accept(this, env);
		if (!(toVal instanceof Value.NumVal)) {
			unexpectedValueException(expr, toVal, expr.getToExpr(), numberTypeName());
		}

		double to = getNumValValue(toVal);

		double step = 1;
		if (expr.getStepExpr() != null) {
			Value stepVal = (Value) expr.getStepExpr().accept(this, env);
			if (!(stepVal instanceof Value.NumVal)) {
				unexpectedValueException(expr, stepVal, expr.getStepExpr(), numberTypeName());
			}
			step = getNumValValue(stepVal);
		}

		Value.Code code;
		{
			Value doCodeVal = (Value) expr.getDoCode().accept(this, env);
			if (!(doCodeVal instanceof Value.Code)) {
				unexpectedValueException(expr, doCodeVal, expr.getDoCode(), codeTypeName());
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
	public Value visit(@NotNull AST.ForArrExpr expr, @NotNull Env env) {
		//for [{},{},{}] do {};

		Value value = (Value) expr.getArray().accept(this, env);
		if (!(value instanceof Value.Array)) {
			unexpectedValueException(expr, value, expr.getArray(), arrayTypeName());
		}
		Value.Array array = (Value.Array) value;
		if (array.length() < 3) {
			badArrayLength(expr, array, 3, "forspec");
		}

		if (!(array.get(0) instanceof Value.Code)) {
			unexpectedValueException(expr, array.get(0), expr.getArray(), codeTypeName());
		}

		if (!(array.get(1) instanceof Value.Code)) {
			unexpectedValueException(expr, array.get(1), expr.getArray(), codeTypeName());
		}

		if (!(array.get(2) instanceof Value.Code)) {
			unexpectedValueException(expr, array.get(2), expr.getArray(), codeTypeName());
		}

		Value.Code initCode = (Value.Code) array.get(0);
		Value.Code conditionCode = (Value.Code) array.get(1);
		Value.Code iterCompleteCode = (Value.Code) array.get(2);

		Value valueDo = (Value) expr.getDoCode().accept(this, env);
		if (!(valueDo instanceof Value.Code)) {
			unexpectedValueException(expr, valueDo, expr.getDoCode(), codeTypeName());
		}
		Value.Code doCode = (Value.Code) valueDo;


		initCode.exec(env);

		while (conditionCode.exec(env) == Value.True) {
			doCode.exec(env);
			iterCompleteCode.exec(env);
		}

		return Value.Void;
	}

	@Override
	public Value visit(@NotNull AST.CountExpr expr, @NotNull Env env) {
		if (expr.getLeft() != null) {
			Value left = (Value) expr.getLeft().accept(this, env);
			if (left instanceof Value.Code) {
				//{} count []
				Value.Code condition = (Value.Code) left;
				Value right = (Value) expr.getRight().accept(this, env);
				if (!(right instanceof Value.Array)) {
					unexpectedValueException(expr, right, expr.getRight(), arrayTypeName());
				}
				Value.Array array = (Value.Array) right;

				int count = 0;
				for (Value v : array) {
					env.put("_x", v);
					Value ret = condition.exec(env);
					if (ret == Value.True) {
						count++;
					}
				}

				return new Value.NumVal(count);
			} else {
				unexpectedValueException(expr, left, expr.getLeft(), codeTypeName());
			}
		}

		int count = 0;
		Value right = (Value) expr.getRight().accept(this, env);
		if (right instanceof Value.StringLiteral) {
			//count ""
			Value.StringLiteral string = (Value.StringLiteral) right;
			count = string.length();
		} else if (right instanceof Value.Array) {
			//count []
			Value.Array array = (Value.Array) right;
			count = array.length();
		} else {
			unexpectedValueException(expr, right, expr.getRight(), stringTypeName() + "," + arrayTypeName());
		}

		return new Value.NumVal(count);
	}

	@Override
	public Value visit(@NotNull AST.StrExpr expr, @NotNull Env env) {
		Value v = (Value) expr.getExpr().accept(this, env);
		if (v instanceof Value.StringLiteral) {
			return v;
		}
		return new Value.StringLiteral(v.toString());
	}

	@Override
	public Value visit(@NotNull AST.UnaryCommand expr, @NotNull Env env) {
		UnaryCommandValueProvider provider = env.getUnaryCommandValueProvider();
		if (provider == null) {
			return getValueForIdentifier(expr, env, expr.getCommand());
		}
		return UnaryCommandTranslator.executeUnaryCommand(expr.getCommand(), provider);
	}

	@Override
	public Value visit(@NotNull AST.BinLogicalExpr expr, @NotNull Env env) {
		//This function is used to evaluate only whats needed (short circuit the expression).
		//For instance, false && true is short circuited because the expression is immediately false
		//do to the left predicate being false
		Function<AST.ASTNode, Value.BoolVal> boolFunc = boolExpr -> {
			Value v = (Value) boolExpr.accept(this, env);
			if (v instanceof Value.Code) {
				Value.Code code = (Value.Code) v;
				v = code.exec(env);
			}
			if (!(v instanceof Value.BoolVal)) {
				unexpectedValueException(expr, v, boolExpr, boolTypeName());
			}
			return (Value.BoolVal) v;
		};

		if (expr.getType() == AST.BinLogicalExpr.Type.And) {
			Value left = boolFunc.apply(expr.getLeft());
			if (left == Value.False) {
				return Value.False;
			}
			return Value.BoolVal.get(boolFunc.apply(expr.getRight()) == Value.True);
		} else if (expr.getType() == AST.BinLogicalExpr.Type.Or) {
			Value left = boolFunc.apply(expr.getLeft());
			if (left == Value.True) {
				return Value.True;
			}
			return Value.BoolVal.get(boolFunc.apply(expr.getRight()) == Value.True);

		} else {
			throw new IllegalStateException("unknown type: " + expr.getType());
		}
	}

	@Override
	public Value visit(@NotNull AST.NotExpr expr, @NotNull Env env) {
		Value v = (Value) expr.getExpr().accept(this, env);
		if (!(v instanceof Value.BoolVal)) {
			unexpectedValueException(expr, v, expr.getExpr(), boolTypeName());
		}
		return ((Value.BoolVal) v).not();
	}

	@Override
	public Value visit(@NotNull AST.AbsExpr expr, @NotNull Env env) {
		Value v = (Value) expr.getExpr().accept(this, env);
		if (!(v instanceof Value.NumVal)) {
			unexpectedValueException(expr, v, expr.getExpr(), numberTypeName());
		}
		double absV = Math.abs(((Value.NumVal) v).v());
		return new Value.NumVal(absV);
	}

	@Override
	public Value visit(@NotNull AST.FormatExpr expr, @NotNull Env env) {
		Value v = (Value) expr.getExpr().accept(this, env);
		if (!(v instanceof Value.Array)) {
			unexpectedValueException(expr, v, expr.getExpr(), arrayTypeName());
		}
		Value.Array array = (Value.Array) v;
		if (array.length() == 0) {
			badArrayLength(expr, array, 1, "format [\"\", arg, ...]");
		}

		if (!(array.get(0) instanceof Value.StringLiteral)) {
			unexpectedValueException(expr, array.get(0), expr.getExpr(), stringTypeName());
		}

		Value.StringLiteral formatString = (Value.StringLiteral) array.get(0);

		String result = getStringFromFormat(expr, formatString.getValue(), array.getItems());

		return new Value.StringLiteral(result);
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

	@NotNull
	private String getStringFromFormat(@NotNull AST.ASTNode requesterNode, @NotNull String armaFormatString,
									   @NotNull List<Value> args) {

		if (!armaFormatString.contains("%")) {
			return armaFormatString;
		}
		StringBuilder sb = new StringBuilder(armaFormatString.length());
		CharSequenceReader reader = new CharSequenceReader(armaFormatString);
		boolean inArg = false;
		while (reader.hasAvailable()) {
			char c = reader.read();
			if (inArg) {
				int start = reader.getIndex();
				int end = start + 1;
				while (reader.canPeekAhead(1)) {
					char cc = reader.peekAhead(1);
					if (!Character.isDigit(cc)) {
						end = reader.getPos();
						break;
					}
					reader.read();
				}
				inArg = false;
				if (armaFormatString.charAt(end - 1) == '%') {
					//is something like this: %%1
					sb.append('%');
					continue;
				}

				String argString = armaFormatString.substring(start, end);
				int arg = -1;
				try {
					arg = Integer.parseInt(argString);
				} catch (NumberFormatException ignore) {
					formatStringInvalidException(requesterNode, armaFormatString,
							String.format(bundle.getString("format_string_invalid_arg_f"), argString)
					);
				}
				if (arg < 0 || arg >= args.size()) {
					formatStringInvalidException(requesterNode, armaFormatString,
							String.format(bundle.getString("format_string_arg_out_of_bounds_f"), argString)
					);
				}
				Value argValue = args.get(arg);
				if (argValue instanceof Value.StringLiteral) {
					sb.append(((Value.StringLiteral) argValue).getValue());
				} else {
					sb.append(argValue.toString());
				}
				if (reader.canPeekAhead(1) && reader.peekAhead(1) == '%') {
					sb.append('%');
					reader.read();
					continue;
				}
				continue;
			} else {
				if (c == '%') {
					inArg = true;
					continue;
				}
				sb.append(c);
			}
		}

		return sb.toString();
	}

	private void formatStringInvalidException(@NotNull AST.@NotNull ASTNode requesterNode,
											  @NotNull String armaFormatString, @NotNull String errorReason) {
		throw new ExpressionEvaluationException(requesterNode,
				String.format(bundle.getString("format_string_invalid_f"), armaFormatString, errorReason)
		);
	}

	/**
	 Create and throw {@link ExpressionEvaluationException} with the given information

	 @param errorNode the node (often the visitor parameter) in which the error occurred
	 @param v the value that was invalid/unexpected
	 @param valueNode the node that contains the invalid/unexpected value
	 @param expected the value type that was expected
	 @return nothing since will throw an exception
	 */
	private Value unexpectedValueException(@NotNull AST.ASTNode errorNode, @NotNull Value v, @NotNull AST.ASTNode valueNode, @NotNull String expected) {
		throw new ExpressionEvaluationException(errorNode, String.format(bundle.getString("unexpected_value_expected_f"), v.toString(), valueNode.toString(), expected));
	}

	private void badArrayLength(@NotNull AST.ASTNode errorNode, @NotNull Value.Array arr, int reqSize, @NotNull String from) {
		throw new ExpressionEvaluationException(errorNode, String.format(bundle.getString("bad_array_size_f"), arr.length(), reqSize, from));
	}

	private void indexOutOfBounds(@NotNull AST.ASTNode errorNode, @NotNull String source, @NotNull String varName, int index, int lowerBound, int upperBound) {
		throw new ExpressionEvaluationException(errorNode, String.format(bundle.getString("index_out_of_bounds_f"), varName, source, index, lowerBound, upperBound));
	}

	private final Pattern stringPattern = Pattern.compile("(\"\")|('')");

}
