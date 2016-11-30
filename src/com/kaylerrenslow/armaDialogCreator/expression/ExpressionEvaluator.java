package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/14/2016.
 */
class ExpressionEvaluator implements AST.Visitor<Value> {

	/**
	 Returns the value for the given expression in the given environment.

	 @throws ExpressionEvaluationException If the expression is invalid
	 */
	@NotNull
	public Value evaluate(AST.Expr e, Env env) throws ExpressionEvaluationException {
		try {
			return (Value) e.accept(this, env);
		} catch (Exception ex) {
			throw new ExpressionEvaluationException(ex.getMessage());
		}
	}

	@Override
	public Value visit(AST.MaxExpr expr, Env env) throws ExpressionEvaluationException {
		Value.NumVal left = (Value.NumVal) expr.getLeft().accept(this, env);
		Value.NumVal right = (Value.NumVal) expr.getRight().accept(this, env);
		return new Value.NumVal(Math.max(left.v(), right.v()));
	}

	@Override
	public Value visit(AST.MinExpr expr, Env env) throws ExpressionEvaluationException {
		Value.NumVal left = (Value.NumVal) expr.getLeft().accept(this, env);
		Value.NumVal right = (Value.NumVal) expr.getRight().accept(this, env);
		return new Value.NumVal(Math.min(left.v(), right.v()));
	}

	@Override
	public Value visit(AST.AddExpr expr, Env env) {
		Value.NumVal left = (Value.NumVal) expr.getLeft().accept(this, env);
		Value.NumVal right = (Value.NumVal) expr.getRight().accept(this, env);
		return new Value.NumVal(left.v() + right.v());
	}

	@Override
	public Value visit(AST.SubExpr expr, Env env) {
		Value.NumVal left = (Value.NumVal) expr.getLeft().accept(this, env);
		Value.NumVal right = (Value.NumVal) expr.getRight().accept(this, env);
		return new Value.NumVal(left.v() - right.v());
	}

	@Override
	public Value visit(AST.MultExpr expr, Env env) {
		Value.NumVal left = (Value.NumVal) expr.getLeft().accept(this, env);
		Value.NumVal right = (Value.NumVal) expr.getRight().accept(this, env);
		return new Value.NumVal(left.v() * right.v());
	}

	@Override
	public Value visit(AST.DivExpr expr, Env env) {
		Value.NumVal left = (Value.NumVal) expr.getLeft().accept(this, env);
		Value.NumVal right = (Value.NumVal) expr.getRight().accept(this, env);
		return new Value.NumVal(left.v() / right.v());
	}

	@Override
	public Value visit(AST.UnaryExpr expr, Env env) {
		Value.NumVal v = (Value.NumVal) expr.getExpr().accept(this, env);
		if (expr.isAdd()) {
			return new Value.NumVal(+v.v());
		}
		return new Value.NumVal(-v.v());
	}

	@Override
	public Value visit(AST.ParenExpr expr, Env env) {
		return (Value) expr.getExp().accept(this, env);
	}

	@Override
	public Value visit(AST.IdentifierExpr expr, Env env) throws ExpressionEvaluationException {
		Value v = env.getValue(expr.getIdentifier());
		if (v == null) {
			throw new ExpressionEvaluationException(String.format(Lang.ApplicationBundle().getString("Expression.identifier_not_set_f"), expr.getIdentifier()));
		}
		return v;
	}

	@Override
	public Value visit(AST.IntegerExpr expr, Env env) {
		return new Value.NumVal(expr.getValue());
	}

	@Override
	public Value visit(AST.FloatExpr expr, Env env) {
		return new Value.NumVal(expr.getValue());
	}
}
