package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 08/03/2016.
 */
public class ParseError {
	private final String message;
	private final String recoverMessage;
	
	/**
	 @param message message to user
	 @param recoverMessage recover message, or null if wasn't recovered
	 */
	public ParseError(String message, @Nullable String recoverMessage) {
		this.message = message;
		this.recoverMessage = recoverMessage;
	}
	
	public ParseError(String message) {
		this(message, null);
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
	
	public static String genericRecover(String value) {
		return String.format(Lang.XmlParse.GENERIC_RECOVER_MESSAGE_F, value);
	}
}
