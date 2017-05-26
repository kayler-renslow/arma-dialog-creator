package com.kaylerrenslow.armaDialogCreator.expression;

/**
 Created by Kayler on 07/15/2016.
 */
public class ExpressionEvaluationException extends RuntimeException {
	public ExpressionEvaluationException() {
	}

	public ExpressionEvaluationException(String message) {
		super(message);
	}

	public ExpressionEvaluationException(String message, Throwable cause) {
		super(message, cause);
	}
}
