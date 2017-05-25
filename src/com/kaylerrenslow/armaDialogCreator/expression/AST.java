package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 Created by Kayler on 07/14/2016.
 */
interface AST {
	interface Visitor<T> {
		T visit(@NotNull AST.MaxExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull AST.MinExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull AST.AddExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull AST.SubExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull AST.MultExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull AST.DivExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull UnaryExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull ParenExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull IdentifierExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull IntegerExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull FloatExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull StringExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull Statement statement, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull Assignment assignment, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull Code code, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull CodeExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull IfExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull Array array, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull SelectExpr expr, @NotNull Env env) throws ExpressionEvaluationException;

		T visit(@NotNull CompExpr expr, @NotNull Env env) throws ExpressionEvaluationException;
	}

	abstract class ASTNode implements AST {
		public abstract Object accept(@NotNull Visitor visitor, @NotNull Env env);
	}


	class Code extends ASTNode {

		private final List<Statement> statements;

		public Code(@NotNull List<Statement> statements) {
			this.statements = statements;
		}

		@NotNull
		public List<Statement> getStatements() {
			return statements;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	abstract class Expr extends ASTNode {

	}

	class MaxExpr extends CommandExpr {

		public MaxExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

	}

	class MinExpr extends CommandExpr {

		public MinExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

	}

	abstract class CommandExpr extends Expr {
		private final Expr left;
		private final Expr right;

		public CommandExpr(@NotNull Expr left, @NotNull Expr right) {
			this.left = left;
			this.right = right;
		}

		/** Get left expression (left COMMAND right) */
		@NotNull
		public Expr getLeft() {
			return left;
		}

		/** Get right expression (left COMMAND right) */
		@NotNull
		public Expr getRight() {
			return right;
		}
	}

	abstract class BinaryExpr extends Expr {
		private final Expr left;
		private final Expr right;

		public BinaryExpr(@NotNull Expr left, @NotNull Expr right) {
			this.left = left;
			this.right = right;
		}

		/** Get left expression (left OPERATOR right) */
		@NotNull
		public Expr getLeft() {
			return left;
		}

		/** Get right expression (left OPERATOR right) */
		@NotNull
		public Expr getRight() {
			return right;
		}

	}

	class AddExpr extends BinaryExpr {
		public AddExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class SubExpr extends BinaryExpr {
		public SubExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class MultExpr extends BinaryExpr {
		public MultExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class DivExpr extends BinaryExpr {
		public DivExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class CompExpr extends BinaryExpr {
		public enum Operator {
			Equal, NotEqual, LessThan, LessThanOrEqual, GreaterThan, GreaterThanOrEqual
		}

		private final Operator operator;

		public CompExpr(@NotNull Expr left, @NotNull Expr right, @NotNull String operatorText) {
			super(left, right);
			if (operatorText.equals("==")) {
				operator = Operator.Equal;
			} else if (operatorText.equals("!=")) {
				operator = Operator.NotEqual;
			} else if (operatorText.equals("<")) {
				operator = Operator.LessThan;
			} else if (operatorText.equals("<=")) {
				operator = Operator.LessThanOrEqual;
			} else if (operatorText.equals(">")) {
				operator = Operator.GreaterThan;
			} else if (operatorText.equals(">=")) {
				operator = Operator.GreaterThanOrEqual;
			} else {
				throw new IllegalArgumentException("unknown operator:" + operatorText);
			}
		}

		@NotNull
		public Operator getOperator() {
			return operator;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class UnaryExpr extends Expr {
		private final boolean isAdd;
		private final Expr expr;

		public UnaryExpr(boolean isAdd, @NotNull Expr expr) {
			this.isAdd = isAdd;
			this.expr = expr;
		}

		/** Return true if the unary expression is for adding +expression. If false, used for subtracting: -expression */
		public boolean isAdd() {
			return isAdd;
		}

		/** Get the expression after the operator */
		@NotNull
		public Expr getExpr() {
			return expr;
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class ParenExpr extends Expr {
		private final Expr exp;

		public ParenExpr(Expr exp) {
			this.exp = exp;
		}

		/** Get the expression inside the parentheses: '(' expression ')' */
		@NotNull
		public Expr getExp() {
			return exp;
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class IfExpr extends Expr {

		public enum Type {
			IfThen, ExitWith
		}

		private final Expr condition;
		private final Type type;
		private Array arr;
		private Expr trueCond;
		private Expr falseCond;

		public IfExpr(@NotNull Expr condition, @NotNull Expr trueCond, @Nullable Expr falseCond, @NotNull Type type) {
			this.condition = condition;
			this.trueCond = trueCond;
			this.falseCond = falseCond;
			this.type = type;
		}

		public IfExpr(@NotNull Expr condition, @NotNull Array arr) {
			this.condition = condition;
			this.arr = arr;
			this.type = Type.IfThen;
		}

		@NotNull
		public Expr getCondition() {
			return condition;
		}

		@NotNull
		public Type getType() {
			return type;
		}

		@Nullable
		public Array getArr() {
			return arr;
		}

		@Nullable
		public Expr getTrueCond() {
			return trueCond;
		}

		@Nullable
		public Expr getFalseCond() {
			return falseCond;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class SelectExpr extends BinaryExpr {

		public SelectExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class CodeExpr extends Expr {

		private Code code;

		public CodeExpr(@NotNull Code code) {
			this.code = code;
		}

		@NotNull
		public Code getCode() {
			return code;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	abstract class LiteralExpr extends Expr {

	}

	class IdentifierExpr extends LiteralExpr {
		private final String identifier;

		public IdentifierExpr(@NotNull String identifier) {
			this.identifier = identifier;
		}

		@NotNull
		public String getIdentifier() {
			return identifier;
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
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
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
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
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class StringExpr extends LiteralExpr {
		private String s;

		public StringExpr(@NotNull String s) {
			this.s = s;
		}

		@NotNull
		public String getValue() {
			return s;
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class Array extends LiteralExpr {

		private final List<Expr> items;

		public Array(@NotNull List<Expr> items) {
			this.items = items;
		}

		@NotNull
		public List<Expr> getItems() {
			return items;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class Assignment extends ASTNode {

		private final String var;
		private final Expr expr;

		public Assignment(@NotNull String var, @NotNull Expr e) {
			this.var = var;
			this.expr = e;
		}

		@NotNull
		public String getVar() {
			return var;
		}

		@NotNull
		public Expr getExpr() {
			return expr;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class Statement extends ASTNode {

		private Expr expr;
		private Assignment assign;

		public Statement(@NotNull Expr expr) {
			this.expr = expr;
		}

		public Statement(@NotNull Assignment assign) {
			this.assign = assign;
		}

		@Nullable
		public Expr getExpr() {
			return expr;
		}

		@Nullable
		public Assignment getAssignment() {
			return assign;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}
}
