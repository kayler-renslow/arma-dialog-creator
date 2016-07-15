package com.kaylerrenslow.armaDialogCreator.expression;

/**
 Created by Kayler on 07/14/2016.
 */
class ExpressionEvaluator implements AST.Visitor {

	public Value evaluate(AST.Expr e) {
		Env env = new Env();
		return (Value) e.accept(this, env);
	}

	@Override
	public Object visit(AST.AddExpr expr, Env env) {
		return null;
	}

	@Override
	public Object visit(AST.SubExpr expr, Env env) {
		return null;
	}

	@Override
	public Object visit(AST.MultExpr expr, Env env) {
		return null;
	}

	@Override
	public Object visit(AST.DivExpr expr, Env env) {
		return null;
	}

	@Override
	public Object visit(AST.UnaryExpr expr, Env env) {
		return null;
	}

	@Override
	public Object visit(AST.ParenExpr expr, Env env) {
		return null;
	}

	@Override
	public Object visit(AST.IdentifierExpr expr, Env env) {
		return null;
	}

	@Override
	public Object visit(AST.IntegerExpr expr, Env env) {
		return null;
	}

	@Override
	public Object visit(AST.FloatExpr expr, Env env) {
		return null;
	}
}
