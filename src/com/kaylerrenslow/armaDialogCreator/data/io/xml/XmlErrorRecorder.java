package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 Created by Kayler on 11/12/2016.
 */
public interface XmlErrorRecorder {
	default void addError(@NotNull ParseError error) {
		getErrors().add(error);
	}

	ArrayList<ParseError> getErrors();
}
