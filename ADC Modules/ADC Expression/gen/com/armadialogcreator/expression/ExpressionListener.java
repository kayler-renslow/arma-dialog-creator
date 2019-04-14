// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/ADC Modules/ADC Expression/src/com/armadialogcreator/expression\Expression.g4 by ANTLR 4.7
package com.armadialogcreator.expression;
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
	 * Enter a parse tree produced by {@link ExpressionParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(ExpressionParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(ExpressionParser.CodeContext ctx);
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
	 * Enter a parse tree produced by {@link ExpressionParser#caret_expression_helper}.
	 * @param ctx the parse tree
	 */
	void enterCaret_expression_helper(ExpressionParser.Caret_expression_helperContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#caret_expression_helper}.
	 * @param ctx the parse tree
	 */
	void exitCaret_expression_helper(ExpressionParser.Caret_expression_helperContext ctx);
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
	 * Enter a parse tree produced by {@link ExpressionParser#if_expression}.
	 * @param ctx the parse tree
	 */
	void enterIf_expression(ExpressionParser.If_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#if_expression}.
	 * @param ctx the parse tree
	 */
	void exitIf_expression(ExpressionParser.If_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#for_expression}.
	 * @param ctx the parse tree
	 */
	void enterFor_expression(ExpressionParser.For_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#for_expression}.
	 * @param ctx the parse tree
	 */
	void exitFor_expression(ExpressionParser.For_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(ExpressionParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(ExpressionParser.ArrayContext ctx);
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
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#unary_command}.
	 * @param ctx the parse tree
	 */
	void enterUnary_command(ExpressionParser.Unary_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#unary_command}.
	 * @param ctx the parse tree
	 */
	void exitUnary_command(ExpressionParser.Unary_commandContext ctx);
}