/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
		return String.format(Lang.ApplicationBundle.getString("XmlParse.generic_recover_message_f"), value);
	}
}
