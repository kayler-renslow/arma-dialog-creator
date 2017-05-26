package com.kaylerrenslow.armaDialogCreator.expression;

/**
 Used for when {@link ExpressionEvaluator#terminate()} in invoked

 @author Kayler
 @since 05/26/2017 */
public class TerminateEvaluationException extends ExpressionEvaluationException {
	public TerminateEvaluationException() {
	}

	public TerminateEvaluationException(String message) {
		super(message);
	}

	public TerminateEvaluationException(String message, Throwable cause) {
		super(message, cause);
	}
}
