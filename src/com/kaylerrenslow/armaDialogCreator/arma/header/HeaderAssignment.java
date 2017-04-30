package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderAssignment extends HeaderItem {
	@NotNull String getVariableName();

	@NotNull HeaderValue getValue();

	default boolean equalsAssignment(@NotNull HeaderAssignment o) {
		return o == this || getVariableName().equals(o.getVariableName()) && getValue().equals(o.getValue());
	}

	@Override
	@NotNull
	default String getAsString() {
		return String.format("%s = %s\n", getVariableName(), getValue().getContent());
	}
}
