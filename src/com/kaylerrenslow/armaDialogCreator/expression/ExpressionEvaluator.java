/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
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
			throw new ExpressionEvaluationException(String.format(Lang.Expression.IDENTIFIER_NOT_SET_F, expr.getIdentifier()));
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
