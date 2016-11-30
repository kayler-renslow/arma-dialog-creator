package com.kaylerrenslow.armaDialogCreator.expression;

/**
 Created by Kayler on 07/14/2016.
 */
interface AST {
	interface Visitor<T> {
		T visit(AST.MaxExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(AST.MinExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(AST.AddExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(AST.SubExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(AST.MultExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(AST.DivExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(UnaryExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(ParenExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(IdentifierExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(IntegerExpr expr, Env env) throws ExpressionEvaluationException;

		T visit(FloatExpr expr, Env env) throws ExpressionEvaluationException;
	}

	abstract class ASTNode implements AST {
		public abstract Object accept(Visitor visitor, Env env);
	}

	abstract class Expr extends ASTNode {

	}

	class MaxExpr extends CommandExpr {

		public MaxExpr(Expr left, Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}

	}

	class MinExpr extends CommandExpr {

		public MinExpr(Expr left, Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}

	}

	abstract class CommandExpr extends Expr {
		private final Expr left;
		private final Expr right;

		public CommandExpr(Expr left, Expr right) {
			this.left = left;
			this.right = right;
		}

		/** Get left expression (left COMMAND right) */
		public Expr getLeft() {
			return left;
		}

		/** Get right expression (left COMMAND right) */
		public Expr getRight() {
			return right;
		}
	}

	abstract class ArithExpr extends Expr {
		private final Expr left;
		private final Expr right;

		public ArithExpr(Expr left, Expr right) {
			this.left = left;
			this.right = right;
		}

		/** Get left expression (left OPERATOR right) */
		public Expr getLeft() {
			return left;
		}

		/** Get right expression (left OPERATOR right) */
		public Expr getRight() {
			return right;
		}

	}

	class AddExpr extends ArithExpr {
		public AddExpr(Expr left, Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}

	class SubExpr extends ArithExpr {
		public SubExpr(Expr left, Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}

	class MultExpr extends ArithExpr {
		public MultExpr(Expr left, Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}

	class DivExpr extends ArithExpr {
		public DivExpr(Expr left, Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}

	class UnaryExpr extends Expr {
		private final boolean isAdd;
		private final Expr expr;

		public UnaryExpr(boolean isAdd, Expr expr) {
			this.isAdd = isAdd;
			this.expr = expr;
		}

		/** Return true if the unary expression is for adding +expression. If false, used for subtracting: -expression */
		public boolean isAdd() {
			return isAdd;
		}

		/** Get the expression after the operator */
		public Expr getExpr() {
			return expr;
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}

	class ParenExpr extends Expr {
		private final Expr exp;

		public ParenExpr(Expr exp) {
			this.exp = exp;
		}

		/** Get the expression inside the parentheses: '(' expression ')' */
		public Expr getExp() {
			return exp;
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}

	abstract class LiteralExpr extends Expr {

	}

	class IdentifierExpr extends LiteralExpr {
		private final String identifier;

		public IdentifierExpr(String identifier) {
			this.identifier = identifier;
		}

		public String getIdentifier() {
			return identifier;
		}
		
		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}

	class IntegerExpr extends LiteralExpr {
		private final int i;

		public IntegerExpr(int i) {
			this.i = i;
		}

		public int getValue() {
			return i;
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}

	class FloatExpr extends LiteralExpr {
		private final double d;

		public FloatExpr(double d) {
			this.d = d;
		}

		public double getValue() {
			return d;
		}

		@Override
		public Object accept(Visitor visitor, Env env) {
			return visitor.visit(this, env);
		}
	}
}
