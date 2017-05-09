package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 05/03/2017 */
public class HeaderClassNotFoundException extends RuntimeException {

	private final String message;
	private final String missingClassName;

	public HeaderClassNotFoundException(@NotNull String message, @NotNull String missingClassName) {
		this.message = message;
		this.missingClassName = missingClassName;
	}

	/** @return the missing class's name */
	@NotNull
	public String getMissing() {
		return missingClassName;
	}
}
