package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionEvaluationException;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionInterpreter;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 An {@link InputFieldDataChecker} implementation for expressions to be evaluated with {@link ExpressionInterpreter}

 @author Kayler
 @since 07/15/2016. */
public class ExpressionChecker implements InputFieldDataChecker<Expression> {
	public static final int TYPE_INT = 0;
	public static final int TYPE_FLOAT = 1;
	private final Env env;
	private final int type;

	public ExpressionChecker(@NotNull Env env, int type) {
		this.env = env;
		this.type = type;
	}

	@Override
	public String validData(@NotNull String data) {
		try {
			ExpressionInterpreter.getInstance().evaluate(data, env);
			return null;
		} catch (ExpressionEvaluationException ex) {
			return (ex.getMessage() == null || ex.getMessage().length() == 0) ? Lang.FxControlBundle().getString("InputField.DataCheckers.Expression.unknown_error") : ex.getMessage();
		}
	}

	@Nullable
	@Override
	public Expression parse(@NotNull String data) {
		return new Expression(data, env);
	}

	@Override
	public String getTypeName() {
		switch (type) {
			case TYPE_INT:
				return Lang.FxControlBundle().getString("InputField.DataCheckers.Expression.Int.type_name");
			default:
			case TYPE_FLOAT:
				return Lang.FxControlBundle().getString("InputField.DataCheckers.Expression.FPN.type_name");
		}
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}

}
