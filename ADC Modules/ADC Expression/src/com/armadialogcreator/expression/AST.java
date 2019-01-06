package com.armadialogcreator.expression;

import com.armadialogcreator.util.ArmaPrecision;
import com.armadialogcreator.util.IndentedStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 Created by Kayler on 07/14/2016.
 */
interface AST {
	interface Visitor<T> {
		T visit(@NotNull AST.MaxExpr expr, @NotNull Env env);

		T visit(@NotNull AST.MinExpr expr, @NotNull Env env);

		T visit(@NotNull AST.AddExpr expr, @NotNull Env env);

		T visit(@NotNull AST.SubExpr expr, @NotNull Env env);

		T visit(@NotNull AST.MultExpr expr, @NotNull Env env);

		T visit(@NotNull AST.DivExpr expr, @NotNull Env env);

		T visit(@NotNull ModExpr expr, @NotNull Env env);

		T visit(@NotNull ExponentExpr expr, @NotNull Env env);

		T visit(@NotNull UnaryExpr expr, @NotNull Env env);

		T visit(@NotNull ParenExpr expr, @NotNull Env env);

		T visit(@NotNull IdentifierExpr expr, @NotNull Env env);

		T visit(@NotNull IntegerExpr expr, @NotNull Env env);

		T visit(@NotNull FloatExpr expr, @NotNull Env env);

		T visit(@NotNull StringExpr expr, @NotNull Env env);

		T visit(@NotNull Statement statement, @NotNull Env env);

		T visit(@NotNull Assignment assignment, @NotNull Env env);

		T visit(@NotNull Code code, @NotNull Env env);

		T visit(@NotNull CodeExpr expr, @NotNull Env env);

		T visit(@NotNull IfExpr expr, @NotNull Env env);

		T visit(@NotNull Array array, @NotNull Env env);

		T visit(@NotNull SelectExpr expr, @NotNull Env env);

		T visit(@NotNull CompExpr expr, @NotNull Env env);

		T visit(@NotNull ForVarExpr expr, @NotNull Env env);

		T visit(@NotNull ForArrExpr expr, @NotNull Env env);

		T visit(@NotNull CountExpr expr, @NotNull Env env);

		T visit(@NotNull StrExpr expr, @NotNull Env env);

		T visit(@NotNull UnaryCommand expr, @NotNull Env env);

		T visit(@NotNull AST.BinLogicalExpr expr, @NotNull Env env);

		T visit(@NotNull NotExpr expr, @NotNull Env env);

		T visit(@NotNull AbsExpr expr, @NotNull Env env);

		T visit(@NotNull FormatExpr expr, @NotNull Env env);

	}

	abstract class ASTNode implements AST {
		public abstract Object accept(@NotNull Visitor visitor, @NotNull Env env);

		abstract void toString(@NotNull IndentedStringBuilder sb);

		@Override
		public String toString() {
			IndentedStringBuilder sb = new IndentedStringBuilder(4);
			toString(sb);
			return sb.toString();
		}
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

		@Override
		public void toString(@NotNull IndentedStringBuilder sb) {
			sb.append("{ ");
			sb.incrementTabCount();
			final boolean appendNewline = statements.size() > 1;
			if (appendNewline) {
				sb.append('\n');
			}
			for (Statement s : statements) {
				s.toString(sb);
				if (appendNewline) {
					sb.append('\n');
				}
			}
			sb.decrementTabCount();
			sb.append(" }");
		}
	}

	abstract class Expr extends ASTNode {

	}

	class AbsExpr extends Expr {

		private final Expr expr;

		public AbsExpr(@NotNull Expr expr) {
			this.expr = expr;
		}

		@NotNull
		public Expr getExpr() {
			return expr;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append("abs ");
			expr.toString(sb);
		}
	}

	class MaxExpr extends BinaryCommandExpr {

		public MaxExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@NotNull
		protected String commandNameForToString() {
			return "max";
		}

	}

	class MinExpr extends BinaryCommandExpr {

		public MinExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		@NotNull
		protected String commandNameForToString() {
			return "min";
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

	}

	abstract class BinaryCommandExpr extends Expr {
		private final Expr left;
		private final Expr right;

		public BinaryCommandExpr(@NotNull Expr left, @NotNull Expr right) {
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

		@NotNull
		protected abstract String commandNameForToString();

		@Override
		public void toString(@NotNull IndentedStringBuilder sb) {
			getLeft().toString(sb);
			sb.append(" ");
			sb.append(commandNameForToString());
			sb.append(" ");
			getRight().toString(sb);
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

		/** Return an operator for {@link #toString()}. */
		@NotNull
		protected abstract String operatorForToString();

		@Override
		public void toString(@NotNull IndentedStringBuilder sb) {
			getLeft().toString(sb);
			sb.append(" ");
			sb.append(operatorForToString());
			sb.append(" ");
			getRight().toString(sb);
		}

	}

	class BinLogicalExpr extends BinaryExpr {
		public enum Type {
			And, Or
		}

		private final Type type;

		public BinLogicalExpr(@NotNull Type type, @NotNull Expr left, @NotNull Expr right) {
			super(left, right);
			this.type = type;
		}

		@NotNull
		public Type getType() {
			return type;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		protected @NotNull String operatorForToString() {
			return type == Type.And ? "&&" : "||";
		}
	}

	class NotExpr extends Expr {

		private final Expr expr;

		public NotExpr(@NotNull Expr expr) {
			this.expr = expr;
		}

		@NotNull
		public Expr getExpr() {
			return expr;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append("!");
			expr.toString(sb);
		}
	}

	class AddExpr extends BinaryExpr {
		public AddExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		@NotNull
		protected String operatorForToString() {
			return "+";
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
		@NotNull
		protected String operatorForToString() {
			return "-";
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
		@NotNull
		protected String operatorForToString() {
			return "*";
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
		@NotNull
		protected String operatorForToString() {
			return "/";
		}

		@Override
		public Object accept(@NotNull Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class CompExpr extends BinaryExpr {
		public enum Operator {
			Equal("=="), NotEqual("!="), LessThan("<"), LessThanOrEqual("<="), GreaterThan(">"), GreaterThanOrEqual(">=");

			private final String operatorText;

			Operator(String operatorText) {
				this.operatorText = operatorText;
			}
		}

		private Operator operator;

		public CompExpr(@NotNull Expr left, @NotNull Expr right, @NotNull String operatorText) {
			super(left, right);

			for (Operator o : Operator.values()) {
				if (o.operatorText.equals(operatorText)) {
					operator = o;
					break;
				}
			}
			if (operator == null) {
				throw new IllegalArgumentException("unknown operator:" + operatorText);
			}
		}

		@Override
		@NotNull
		protected String operatorForToString() {
			return getOperator().operatorText;
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

	class ModExpr extends BinaryExpr {
		public ModExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		@NotNull
		protected String operatorForToString() {
			return "%";
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class ExponentExpr extends Expr {

		private final List<Expr> exprs = new ArrayList<>();

		public ExponentExpr(@NotNull Expr expr) {
			exprs.add(expr);
		}

		@NotNull
		public List<Expr> getExprs() {
			return exprs;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			for (Expr e : exprs) {
				e.toString(sb);
				sb.append('^');
			}
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

		@Override
		public void toString(@NotNull IndentedStringBuilder sb) {
			sb.append(isAdd() ? "+" : "-");
			getExpr().toString(sb);
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

		@Override
		public void toString(@NotNull IndentedStringBuilder sb) {
			sb.append("(");
			getExp().toString(sb);
			sb.append(")");
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

		@Override
		public void toString(@NotNull IndentedStringBuilder sb) {
			sb.append("if ");
			getCondition().toString(sb);
			switch (getType()) {
				case ExitWith: {
					sb.append(" exitWith ");
					getTrueCond().toString(sb);
					break;
				}
				case IfThen: {
					sb.append(" then ");
					if (getArr() == null) {
						getTrueCond().toString(sb);
						if (getFalseCond() != null) {
							sb.append(" else ");
							getFalseCond().toString(sb);
						}

					} else {
						getArr().toString(sb);
					}
					break;
				}
				default: {
					throw new IllegalStateException("unhandled type " + getType());
				}
			}

		}
	}

	abstract class ForExpr extends Expr {
		private final Expr doCode;

		public ForExpr(@NotNull Expr doCode) {
			this.doCode = doCode;
		}

		@NotNull
		public Expr getDoCode() {
			return doCode;
		}
	}

	/** Example: for "_i" from 9 to 1 step -2 do {debugLog _i;}; */
	class ForVarExpr extends ForExpr {

		private final Expr varExpr;
		private final Expr fromExpr;
		private final Expr toExpr;
		private final Expr stepExpr;

		public ForVarExpr(@NotNull Expr varExpr, @NotNull Expr fromExpr, @NotNull Expr toExpr, @Nullable Expr stepExpr, @NotNull Expr doCode) {
			super(doCode);
			this.varExpr = varExpr;
			this.fromExpr = fromExpr;
			this.toExpr = toExpr;
			this.stepExpr = stepExpr;
		}

		@NotNull
		public Expr getVarExpr() {
			return varExpr;
		}

		@NotNull
		public Expr getFromExpr() {
			return fromExpr;
		}

		@NotNull
		public Expr getToExpr() {
			return toExpr;
		}

		@Nullable
		public Expr getStepExpr() {
			return stepExpr;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			if (getStepExpr() == null) {
				sb.append("for ");
				getVarExpr().toString(sb);
				sb.append(" from ");
				getFromExpr().toString(sb);
				sb.append(" to ");
				getToExpr().toString(sb);
				sb.append(" do ");
				getDoCode().toString(sb);
			} else {
				sb.append("for ");
				getVarExpr().toString(sb);
				sb.append(" from ");
				getFromExpr().toString(sb);
				sb.append(" to ");
				getToExpr().toString(sb);
				sb.append(" step ");
				getStepExpr().toString(sb);
				sb.append(" do ");
				getDoCode().toString(sb);
			}
		}
	}

	/** Example: for [{_i=0}, {_i < 10}, {_i = _i + 1}] do {hint str _i}; */
	class ForArrExpr extends ForExpr {

		private final Expr array;

		public ForArrExpr(@NotNull AST.Expr array, @NotNull Expr doCode) {
			super(doCode);
			this.array = array;
		}

		@NotNull
		public Expr getArray() {
			return array;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append("for ");
			getArray().toString(sb);
			sb.append(" do ");
			getDoCode().toString(sb);
		}
	}

	class StrExpr extends Expr {

		private Expr expr;

		public StrExpr(@NotNull Expr expr) {
			this.expr = expr;
		}

		@NotNull
		public Expr getExpr() {
			return expr;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append("str ");
			expr.toString(sb);
		}
	}

	class FormatExpr extends Expr {

		private final Expr expr;

		public FormatExpr(@NotNull Expr expr) {
			this.expr = expr;
		}

		@NotNull
		public Expr getExpr() {
			return expr;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append("format ");
			expr.toString(sb);
		}
	}

	class SelectExpr extends BinaryCommandExpr {

		public SelectExpr(@NotNull Expr left, @NotNull Expr right) {
			super(left, right);
		}

		@Override
		@NotNull
		protected String commandNameForToString() {
			return "select";
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}
	}

	class CountExpr extends Expr {
		private final Expr left;
		private final Expr right;

		public CountExpr(@Nullable Expr left, @NotNull Expr right) {
			this.left = left;
			this.right = right;
		}

		/** Get left expression (left count right) */
		@Nullable
		public Expr getLeft() {
			return left;
		}

		/** Get right expression (left count right) */
		@NotNull
		public Expr getRight() {
			return right;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			if (getLeft() != null) {
				getLeft().toString(sb);
			}
			sb.append(" count ");
			getRight().toString(sb);
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

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			code.toString(sb);
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

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append(identifier);
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

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append(i + "");
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

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append(ArmaPrecision.format(d));
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

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append(s);
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

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append('[');
			int i = 0;
			for (Expr e : getItems()) {
				e.toString(sb);
				if (i != getItems().size() - 1) {
					sb.append("', '");
				}
			}
			sb.append(']');
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

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append(var);
			sb.append(" = ");
			getExpr().toString(sb);
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

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			if (getExpr() != null) {
				getExpr().toString(sb);
			} else if (getAssignment() != null) {
				getAssignment().toString(sb);
			}
			sb.append(";");
		}
	}

	class UnaryCommand extends Expr {

		private final String command;

		public UnaryCommand(@NotNull String command) {
			this.command = command;
		}

		@NotNull
		public String getCommand() {
			return command;
		}

		@Override
		public Object accept(@NotNull AST.Visitor visitor, @NotNull Env env) {
			return visitor.visit(this, env);
		}

		@Override
		void toString(@NotNull IndentedStringBuilder sb) {
			sb.append(command);
		}
	}
}
