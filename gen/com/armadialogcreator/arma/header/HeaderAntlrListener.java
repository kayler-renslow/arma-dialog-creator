// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/armadialogcreator/arma/header\HeaderAntlr.g4 by ANTLR 4.7
package com.armadialogcreator.arma.header;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HeaderAntlrParser}.
 */
public interface HeaderAntlrListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#root_class}.
	 * @param ctx the parse tree
	 */
	void enterRoot_class(HeaderAntlrParser.Root_classContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#root_class}.
	 * @param ctx the parse tree
	 */
	void exitRoot_class(HeaderAntlrParser.Root_classContext ctx);
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#header_class}.
	 * @param ctx the parse tree
	 */
	void enterHeader_class(HeaderAntlrParser.Header_classContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#header_class}.
	 * @param ctx the parse tree
	 */
	void exitHeader_class(HeaderAntlrParser.Header_classContext ctx);
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#header_class_helper}.
	 * @param ctx the parse tree
	 */
	void enterHeader_class_helper(HeaderAntlrParser.Header_class_helperContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#header_class_helper}.
	 * @param ctx the parse tree
	 */
	void exitHeader_class_helper(HeaderAntlrParser.Header_class_helperContext ctx);
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(HeaderAntlrParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(HeaderAntlrParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#arr_assignment}.
	 * @param ctx the parse tree
	 */
	void enterArr_assignment(HeaderAntlrParser.Arr_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#arr_assignment}.
	 * @param ctx the parse tree
	 */
	void exitArr_assignment(HeaderAntlrParser.Arr_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(HeaderAntlrParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(HeaderAntlrParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#array_helper}.
	 * @param ctx the parse tree
	 */
	void enterArray_helper(HeaderAntlrParser.Array_helperContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#array_helper}.
	 * @param ctx the parse tree
	 */
	void exitArray_helper(HeaderAntlrParser.Array_helperContext ctx);
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(HeaderAntlrParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(HeaderAntlrParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link HeaderAntlrParser#equation}.
	 * @param ctx the parse tree
	 */
	void enterEquation(HeaderAntlrParser.EquationContext ctx);
	/**
	 * Exit a parse tree produced by {@link HeaderAntlrParser#equation}.
	 * @param ctx the parse tree
	 */
	void exitEquation(HeaderAntlrParser.EquationContext ctx);
}
