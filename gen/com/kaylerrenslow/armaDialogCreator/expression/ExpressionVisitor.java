// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/kaylerrenslow/armaDialogCreator/expression\Expression.g4 by ANTLR 4.5.3
package com.kaylerrenslow.armaDialogCreator.expression;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ExpressionParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#unary_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_expression(ExpressionParser.Unary_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#paren_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParen_expression(ExpressionParser.Paren_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#literal_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral_expression(ExpressionParser.Literal_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#int_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt_value(ExpressionParser.Int_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#float_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat_value(ExpressionParser.Float_valueContext ctx);
}