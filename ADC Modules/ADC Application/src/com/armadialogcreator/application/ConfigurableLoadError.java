package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 01/06/2019 */
public class ConfigurableLoadError {
	private final String message;
	private final String recoverMessage;
	private final Exception exception;

	/**
	 @param message message to user
	 @param recoverMessage recover message, or null if wasn't recovered
	 @param e Exception that may provide insight into the parse error
	 */
	public ConfigurableLoadError(String message, @Nullable String recoverMessage, @Nullable Exception e) {
		this.message = message;
		this.recoverMessage = recoverMessage;
		this.exception = e;
	}

	/**
	 @param message message to user
	 @param recoverMessage recover message, or null if wasn't recovered
	 */
	public ConfigurableLoadError(String message, @Nullable String recoverMessage) {
		this(message, recoverMessage, null);
	}

	public ConfigurableLoadError(String message) {
		this(message, null, null);
	}

	public ConfigurableLoadError(String message, @Nullable Exception exception) {
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
}
