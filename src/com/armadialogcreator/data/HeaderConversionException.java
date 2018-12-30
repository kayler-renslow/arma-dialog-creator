package com.armadialogcreator.data;

/**
 @author Kayler
 @see HeaderToProject
 @since 04/30/2017 */
public class HeaderConversionException extends Exception {
	public HeaderConversionException(String message) {
		super(message);
	}

	public HeaderConversionException(String message, Throwable e) {
		super(message, e);
	}
}
