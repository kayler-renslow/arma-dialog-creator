package com.kaylerrenslow.armaDialogCreator.data.xml;

/**
 Created by Kayler on 07/29/2016.
 */
public class XmlParseException extends Exception {
	
	public XmlParseException(String message) {
		super(message);
	}

	public XmlParseException(String message, Exception e) {
		super(message, e);
	}
}
