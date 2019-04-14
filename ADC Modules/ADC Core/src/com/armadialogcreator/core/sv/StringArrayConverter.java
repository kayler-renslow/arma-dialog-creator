package com.armadialogcreator.core.sv;

import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/07/2019 */
public interface StringArrayConverter<T> {
	/** convert a String array into type T */
	T convert(@NotNull Env env, @NotNull String[] values) throws Exception;
}
