package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 Evaluates an expression AST and returns the result via {@link #evaluate(AST.Expr, Env)}.

 @author Kayler
 @since 07/14/2016 */
class ExpressionEvaluator implements AST.Visitor<Value> {

	private final ResourceBundle bundle = Lang.getBundle("ExpressionBundle");
	private final ExpressionLexer lexer;
	private final ExpressionParser parser;

	/**
	 Construct a new evaluator with the given lexer and parser. The lexer and parser given is what will be
	 passed into any {@link Value.Code} instances. The error reporters for the lexer and parser will be used any time {@link Value.Code#exec()} is invoked.

	 @param lexer the lexer
	 @param parser the parser
	 */
	public ExpressionEvaluator(@NotNull ExpressionLexer lexer, @NotNull ExpressionParser parser) {
		this.lexer = lexer;
		this.parser = parser;
	}

	/**
	 Returns the value for the given expression in the given environment.

	 @throws ExpressionEvaluationException If the expression is invalid
	 */
	@NotNull
	public Value evaluate(@NotNull AST.Expr e, @NotNull Env env) throws ExpressionEvaluationException {
		try {
			return (Value) e.accept(this, new EvaluatorWrapperEnv(env));
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
			for (AST.Statement s : statements) {
				last = (Value) s.accept(this, new EvaluatorWrapperEnv(env));
			}
			return last;
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

	@Override
	public Value visit(@NotNull AST.Statement statement, @NotNull Env env) throws ExpressionEvaluationException {
		if (statement.getAssignment() != null) {
			AST.Assignment assignment = statement.getAssignment();
			Value v = (Value) assignment.getExpr().accept(this, env);
			env.put(assignment.getVar(), v);
			return Value.Void;
		}
		if (statement.getExpr() != null) {
			AST.Expr expr = statement.getExpr();

			return (Value) expr.accept(this, env);
		}
		throw new IllegalStateException("didn't match an assignment or expression");
	}

	@Override
	public Value visit(@NotNull AST.Assignment assignment, @NotNull Env env) throws ExpressionEvaluationException {
		return Value.Void;
	}

	@Override
	public Value visit(@NotNull AST.Code code, @NotNull Env env) throws ExpressionEvaluationException {
		return new Value.Code(code.getStatements(), env, this);
	}

	@Override
	public Value visit(@NotNull AST.CodeExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
		return (Value) expr.getCode().accept(this, env);
	}

	@Override
	public Value visit(@NotNull AST.IfExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
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
						throw new EndEvaluationException(((Value.Code) ret).exec());
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
							return ((Value.Code) v).exec();
						}
						unexpectedValueException(v, codeTypeName());
					} else {
						if (expr.getTrueCond() == null) {
							throw new IllegalStateException("getTrueCond() is null");
						}
						Value v = (Value) expr.getTrueCond().accept(this, env);
						if (v instanceof Value.Code) {
							return ((Value.Code) v).exec();
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
						return ((Value.Code) v).exec();
					}
					unexpectedValueException(v, codeTypeName());
				} else {
					if (expr.getFalseCond() == null) {
						throw new IllegalStateException("getFalseCond() is null");
					}
					Value v = (Value) expr.getFalseCond().accept(this, env);
					if (v instanceof Value.Code) {
						return ((Value.Code) v).exec();
					}
					unexpectedValueException(v, codeTypeName());
				}
			}
			default: {
				throw new IllegalStateException("unhandled type: " + expr.getType());
			}
		}
		throw new IllegalStateException("should have returned something before this point");
	}

	@Override
	public Value visit(@NotNull AST.Array array, @NotNull Env env) throws ExpressionEvaluationException {
		List<Value> values = new ArrayList<>();
		for (AST.Expr e : array.getItems()) {
			values.add((Value) e.accept(this, env));
		}
		return new Value.Array(values);
	}

	@Override
	public Value visit(@NotNull AST.SelectExpr expr, @NotNull Env env) throws ExpressionEvaluationException {
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
					if (rightCode.exec() == Value.True) {
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
