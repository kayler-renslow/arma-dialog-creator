package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderAssignment {
	@NotNull String getVariableName();

	@NotNull HeaderValue getValue();

	default boolean equalsAssignment(@NotNull HeaderAssignment o) {
		return o == this || getVariableName().equals(o.getVariableName()) && getValue().equals(o.getValue());
	}

}
