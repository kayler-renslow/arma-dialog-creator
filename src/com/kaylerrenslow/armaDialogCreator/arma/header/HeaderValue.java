package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderValue {
	/** @return the content as a String */
	@NotNull String getContent();

	default boolean equalsValue(@NotNull HeaderValue o) {
		return this.getContent().equals(o.getContent());
	}
}
