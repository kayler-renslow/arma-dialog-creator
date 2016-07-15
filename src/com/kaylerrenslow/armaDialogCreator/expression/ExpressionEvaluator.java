package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/14/2016.
 */
class ExpressionEvaluator implements AST.Visitor<Value> {

	/** Returns the value for the given expression in the given environment. If the expression is invalid, will return null. */
	@Nullable
	public Value evaluate(AST.Expr e, Env env) {
		try {
			return (Value) e.accept(this, env);
		} catch (Exception ex) {
			return null;
		}
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
	public Value visit(AST.IdentifierExpr expr, Env env) {
		return env.getValue(expr.getIdentifier());
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
