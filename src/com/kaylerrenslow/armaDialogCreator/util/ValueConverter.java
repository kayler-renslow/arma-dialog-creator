package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used for converting a String[] into a given type.

 @author Kayler
 @since 07/29/2016. */
public interface ValueConverter<T> {
	/** Convert a String array into type T */
	T convert(@Nullable DataContext context, @NotNull String... values) throws Exception;
}
