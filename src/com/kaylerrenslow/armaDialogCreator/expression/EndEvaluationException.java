package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.NotNull;

/**
 A cheaty way of preventing further expression evaluation when an exitWith command is used.

 @author Kayler
 @since 05/24/2017 */
public class EndEvaluationException extends ExpressionEvaluationException {
	private Value returnValue;

	public EndEvaluationException(@NotNull Value returnValue) {
		super(null, "");
		this.returnValue = returnValue;
	}

	@NotNull
	public Value getReturnValue() {
		return returnValue;
	}
}
