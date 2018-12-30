package com.armadialogcreator.data.xml;

import com.armadialogcreator.main.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 08/03/2016.
 */
public class ParseError {
	private final String message;
	private final String recoverMessage;
	private final Exception exception;

	/**
	 @param message message to user
	 @param recoverMessage recover message, or null if wasn't recovered
	 @param e Exception that may provide insight into the parse error
	 */
	public ParseError(String message, @Nullable String recoverMessage, @Nullable Exception e) {
		this.message = message;
		this.recoverMessage = recoverMessage;
		this.exception = e;
	}

	/**
	 @param message message to user
	 @param recoverMessage recover message, or null if wasn't recovered
	 */
	public ParseError(String message, @Nullable String recoverMessage) {
		this(message, recoverMessage, null);
	}

	public ParseError(String message) {
		this(message, null, null);
	}

	public ParseError(String message, @Nullable Exception exception) {
		this(message, null, exception);
	}

	@Nullable
	public Exception getException() {
		return exception;
	}

	@NotNull
	public String getMessage() {
		return message;
	}

	public boolean recovered() {
		return recoverMessage != null;
	}

	/** Get the recover message (used for when the error was recovered. If not recoverable, this will return null). */
	@Nullable
	public String getRecoverMessage() {
		return recoverMessage;
	}

	@NotNull
	public static String genericRecover(String value) {
		return String.format(Lang.ApplicationBundle().getString("XmlParse.generic_recover_message_f"), value);
	}
}
