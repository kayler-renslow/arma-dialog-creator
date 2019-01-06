package com.armadialogcreator.expression;

import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 Used with {@link ExpressionInterpreter}.

 @author Kayler
 @since 07/15/2016 */
public class ExpressionEvaluationException extends RuntimeException {
	private final AST.ASTNode errorNode;

	/**
	 @param errorNode the nde that caused the exception
	 */
	protected ExpressionEvaluationException(@Nullable AST.ASTNode errorNode) {
		this.errorNode = errorNode;
	}

	/**
	 @param errorNode the node that caused the exception
	 @param message message about the error
	 */
	public ExpressionEvaluationException(@Nullable AST.ASTNode errorNode, String message) {
		super(message);
		this.errorNode = errorNode;
	}

	/**
	 @param errorNode the nde that caused the exception
	 @param message message about the error
	 @param cause another Throwable that this exception wraps
	 */
	public ExpressionEvaluationException(@Nullable AST.ASTNode errorNode, String message, Throwable cause) {
		super(message, cause);
		this.errorNode = errorNode;
	}

	/** @return the node that caused the issue, or null if couldn't be determined */
	@Nullable
	protected AST.ASTNode getErrorNode() {
		return errorNode;
	}

	/** @return the node that caused the issue as a String, or null if couldn't be determined */
	@Nullable
	public String getErrorNodeAsString() {
		return errorNode == null ? null : errorNode.toString();
	}

	@Override
	public void printStackTrace(PrintStream s) {
		if (s != null) {
			s.append("Error node: ");
			s.append(getErrorNodeAsString());
			s.append('\n');
		}
		super.printStackTrace(s);
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		if (s != null) {
			s.append("Error node: ");
			s.append(getErrorNodeAsString());
			s.append('\n');
		}
		super.printStackTrace(s);
	}
}
