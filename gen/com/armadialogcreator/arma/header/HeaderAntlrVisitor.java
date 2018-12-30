// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/armadialogcreator/arma/header\HeaderAntlr.g4 by ANTLR 4.6
package com.armadialogcreator.arma.header;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link HeaderAntlrParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface HeaderAntlrVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#root_class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot_class(HeaderAntlrParser.Root_classContext ctx);
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#header_class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader_class(HeaderAntlrParser.Header_classContext ctx);
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#header_class_helper}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader_class_helper(HeaderAntlrParser.Header_class_helperContext ctx);
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(HeaderAntlrParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#arr_assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArr_assignment(HeaderAntlrParser.Arr_assignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(HeaderAntlrParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#array_helper}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_helper(HeaderAntlrParser.Array_helperContext ctx);
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(HeaderAntlrParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link HeaderAntlrParser#equation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquation(HeaderAntlrParser.EquationContext ctx);
}
