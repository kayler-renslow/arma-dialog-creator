package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @see InputField
 Created on 05/31/2016. */
public interface InputFieldDataChecker<E> {
	/** Return null if the text inputted can correctly be parsed into the generic type E. Return a String containing the message as to why the data inputted is incorrect */
	String validData(@NotNull String data);

	/** Parses the given data and returns an instance of type E */
	@Nullable E parse(@NotNull String data);

	/** Returns a display name that describes the type of data that is allowed (e.g. integer or double) */
	String getTypeName();

	/**Return true if the input field can remain empty and be valid, false if it requires at least 1 character to be entered*/
	boolean allowEmptyData();


}
