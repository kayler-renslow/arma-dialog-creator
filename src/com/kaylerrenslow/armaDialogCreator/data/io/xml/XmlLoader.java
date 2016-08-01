package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 Created by Kayler on 07/31/2016.
 */
class XmlLoader {
	protected final Document document;
	protected final @Nullable DataContext dataContext;
	private final ArrayList<ParseError> errors = new ArrayList<>();
	protected final DocumentBuilderFactory builderFactory;
	protected final DocumentBuilder documentBuilder;
	
	protected XmlLoader(@NotNull File xmlFile, @Nullable DataContext context, Key<?>[] keys) throws XmlParseException {
		if (keys != null && context != null && !context.keysSet(keys)) {
			throw new IllegalArgumentException("DataContext must contain keys:" + Arrays.toString(keys));
		}
		try {
			builderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = builderFactory.newDocumentBuilder();
			document = documentBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(Lang.XmlParse.FAILED_TO_READ_XML);
		}
		
		this.dataContext = context;
	}
	
	public static class ParseResult {
		private final ArrayList<ParseError> errors;
		
		protected ParseResult(ArrayList<ParseError> errors) {
			this.errors = errors;
		}
		
		@NotNull
		public ArrayList<ParseError> getErrors() {
			return errors;
		}
	}
	
	public static class ParseError {
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
		
		/** Get the recover message (used for when the error was recovered. If not recoverable, this will return null). */
		@Nullable
		public String getRecoverMessage() {
			return recoverMessage;
		}
		
		public static String genericRecover(String value) {
			return String.format(Lang.XmlParse.GENERIC_RECOVER_MESSAGE_F, value);
		}
	}
	
	protected void addError(@NotNull ParseError error) {
		errors.add(error);
	}
	
	protected final ArrayList<ParseError> getErrors() {
		return errors;
	}
	
	/** Equivalent of doing {@link #document}.getDocumentElement() */
	protected Element getDocumentElement() {
		return document.getDocumentElement();
	}
}
