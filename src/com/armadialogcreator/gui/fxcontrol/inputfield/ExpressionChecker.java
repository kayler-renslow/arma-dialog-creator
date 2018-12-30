package com.armadialogcreator.gui.fxcontrol.inputfield;

import com.armadialogcreator.control.sv.SVExpression;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.expression.ExpressionEvaluationException;
import com.armadialogcreator.expression.ExpressionInterpreter;
import com.armadialogcreator.main.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 An {@link InputFieldDataChecker} implementation for expressions to be evaluated with {@link ExpressionInterpreter}

 @author Kayler
 @since 07/15/2016. */
public class ExpressionChecker implements InputFieldDataChecker<SVExpression> {
	public static final int TYPE_INT = 0;
	public static final int TYPE_FLOAT = 1;
	private final Env env;
	private final int type;

	public ExpressionChecker(@NotNull Env env, int type) {
		this.env = env;
		this.type = type;
	}

	@Override
	public String errorMsgOnData(@NotNull String data) {
		try {
			parse(data);
			return null;
		} catch (ExpressionEvaluationException ex) {
			return (ex.getMessage() == null || ex.getMessage().length() == 0) ? Lang.FxControlBundle().getString("InputField.DataCheckers.Expression.unknown_error") : ex.getMessage();
		}
	}

	@Nullable
	@Override
	public SVExpression parse(@NotNull String data) {
		return new SVExpression(data, env);
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
