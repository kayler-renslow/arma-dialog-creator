package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderArrayItem extends HeaderItem {
	@NotNull HeaderValue getValue();

	default boolean arrayItemEquals(@NotNull HeaderArrayItem o) {
		return o == this || this.getValue().equals(o.getValue());
	}

	@Override
	@NotNull
	default String getAsString() {
		return getValue().getContent();
	}
}
