package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.SimpleEnv;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A wrapper {@link Env} for {@link Preprocessor}. This environment takes a provided {@link Env} and will <b>never</b> modify it.
 All changes (e.g. {@link #put(String, Value)}) will be done to {@link ParserTimeEnv}'s own internal data structure. Also, {@link #getValue(String)} will
 first check the provided {@link Env} before checking in {@link ParserTimeEnv}'s internal data structure.<br>
 <p>
 This environment is for handling __EXEC for the preprocessor.

 @author Kayler
 @since 05/24/2017 */
public class ParserTimeEnv extends SimpleEnv {
	private Env providedPreprocessorEnv;

	public ParserTimeEnv(@NotNull Env providedPreprocessorEnv) {
		this.providedPreprocessorEnv = providedPreprocessorEnv;
	}

	@Override
	public Value put(@NotNull String identifier, Value v) {
		return super.put(identifier, v);
	}

	@Override
	public Value remove(@NotNull String identifier) {
		return super.remove(identifier);
	}

	@Override
	@Nullable
	public Value getValue(@NotNull String identifier) {
		Value v = providedPreprocessorEnv.getValue(identifier);
		if (v != null) {
			return v;
		}
		return super.getValue(identifier);
	}
}
