package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.ResourceBundle;

/**
 A wrapper {@link Env} that simply checks if an identifier is equal to a command while assigning a value to said identifier.
 This is also the place where commands "true" and "false" are set to their values.

 @author Kayler
 @since 05/24/2017 */
public class EvaluatorWrapperEnv implements Env {
	private final ResourceBundle bundle = Lang.getBundle("ExpressionBundle");
	private Env env;

	public EvaluatorWrapperEnv(@NotNull Env env) {
		this.env = env;
	}

	@Override
	public Value put(@NotNull String identifier, Value v) {
		for (String s : ExpressionInterpreter.getSupportedCommands()) {
			if (s.equals(identifier)) {
				throw new ExpressionEvaluationException(String.format(bundle.getString("assigning_to_command_error_f"), s));
			}
		}
		return env.put(identifier, v);
	}

	@Override
	public Value remove(@NotNull String identifier) {
		return env.remove(identifier);
	}

	@Override
	@Nullable
	public Value getValue(@NotNull String identifier) {
		if (identifier.equals("true")) {
			return Value.True;
		}
		if (identifier.equals("false")) {
			return Value.False;
		}

		return env.getValue(identifier);
	}

	@Override
	@NotNull
	public Iterator<KeyValue<String, Value>> iterator() {
		return env.iterator();
	}
}
