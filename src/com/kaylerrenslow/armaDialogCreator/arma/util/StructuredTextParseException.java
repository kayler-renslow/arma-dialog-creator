package com.kaylerrenslow.armaDialogCreator.arma.util;

/**
 @author Kayler
 @see StructuredTextParser
 @since 07/03/2017 */
public class StructuredTextParseException extends Exception {
	public StructuredTextParseException() {
	}

	public StructuredTextParseException(Throwable cause) {
		super(cause);
	}

	public StructuredTextParseException(String message) {
		super(message);
	}

	public StructuredTextParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
