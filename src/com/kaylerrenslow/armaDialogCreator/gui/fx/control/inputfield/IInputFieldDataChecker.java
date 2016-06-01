package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

/**
 @author Kayler
 @see InputField
 Created on 05/31/2016. */
public interface IInputFieldDataChecker<E> {
	/** Return true if the text inputted can correctly be parsed into the generic type E, false if the data inputted is incorrect */
	boolean validData(String data);

	/** Parses the given data and returns an instance of type E */
	E parse(String data);

	/** Returns a display name that describes the type of data that is allowed (e.g. integer or double) */
	String getTypeName();
}
