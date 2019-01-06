package com.armadialogcreator.expression;

/**
 Used for when {@link ExpressionEvaluator#terminate()} in invoked

 @author Kayler
 @since 05/26/2017 */
public class TerminateEvaluationException extends ExpressionEvaluationException {
	protected TerminateEvaluationException() {
		super(null);
	}

	public TerminateEvaluationException(String message) {
		super(null, message);
	}

	public TerminateEvaluationException(String message, Throwable cause) {
		super(null, message, cause);
	}
}
