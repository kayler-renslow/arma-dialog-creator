package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 05/03/2017 */
public class HeaderClassNotFoundException extends RuntimeException {
	private final HeaderClass missing;

	public HeaderClassNotFoundException(@NotNull HeaderClass missing) {
		this.missing = missing;
	}

	@NotNull
	public HeaderClass getMissing() {
		return missing;
	}
}
