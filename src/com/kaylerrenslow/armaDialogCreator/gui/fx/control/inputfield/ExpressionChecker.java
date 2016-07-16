package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionEvaluationException;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionInterpreter;
import com.kaylerrenslow.armaDialogCreator.main.FXControlLang;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 An {@link InputFieldDataChecker} implementation for expressions to be evaluated with {@link ExpressionInterpreter}
 Created on 07/15/2016. */
public class ExpressionChecker implements InputFieldDataChecker<Expression> {
	private final Env env;

	public ExpressionChecker(Env env) {
		this.env = env;
	}

	@Override
	public String validData(String data) {
		try {
			ExpressionInterpreter.getInstance().evaluate(data, env);
			return null;
		} catch (ExpressionEvaluationException ex) {
			return ex.getMessage();
		}
	}

	@Nullable
	@Override
	public Expression parse(String data) {
		return new Expression(data, env);
	}

	@Override
	public String getTypeName() {
		return FXControlLang.InputField.DataCheckers.Expression.TYPE_NAME;
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
