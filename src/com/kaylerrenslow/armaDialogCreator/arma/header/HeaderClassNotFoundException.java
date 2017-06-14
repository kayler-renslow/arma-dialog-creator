package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 05/03/2017 */
public class HeaderClassNotFoundException extends RuntimeException {

	private final String message;
	private final String missingClassName;

	/**
	 @param message custom message (will appear in stack trace concatenated with <code>missingClassName</code>)
	 @param missingClassName class that is missing (will appear in stack trace concatenated with <code>message</code>)
	 */
	public HeaderClassNotFoundException(@NotNull String message, @NotNull String missingClassName) {
		super("Class " + missingClassName + " is missing. Message=" + message);
		this.message = message;
		this.missingClassName = missingClassName;
	}

	/** @return the message passed through the constructor. This will differ from {@link #getMessage()} */
	@NotNull
	public String getConstructorMessage() {
		return message;
	}

	/** @return the missing class's name */
	@NotNull
	public String getMissing() {
		return missingClassName;
	}
}
