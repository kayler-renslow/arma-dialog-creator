// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/kaylerrenslow/armaDialogCreator/expression\Expression.g4 by ANTLR 4.7
package com.kaylerrenslow.armaDialogCreator.expression;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExpressionParser}.
 */
public interface ExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(ExpressionParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(ExpressionParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ExpressionParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ExpressionParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(ExpressionParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(ExpressionParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ExpressionParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ExpressionParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expression(ExpressionParser.Unary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expression(ExpressionParser.Unary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#paren_expression}.
	 * @param ctx the parse tree
	 */
	void enterParen_expression(ExpressionParser.Paren_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#paren_expression}.
	 * @param ctx the parse tree
	 */
	void exitParen_expression(ExpressionParser.Paren_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_expression(ExpressionParser.Literal_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_expression(ExpressionParser.Literal_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#int_value}.
	 * @param ctx the parse tree
	 */
	void enterInt_value(ExpressionParser.Int_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#int_value}.
	 * @param ctx the parse tree
	 */
	void exitInt_value(ExpressionParser.Int_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#float_value}.
	 * @param ctx the parse tree
	 */
	void enterFloat_value(ExpressionParser.Float_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#float_value}.
	 * @param ctx the parse tree
	 */
	void exitFloat_value(ExpressionParser.Float_valueContext ctx);
}