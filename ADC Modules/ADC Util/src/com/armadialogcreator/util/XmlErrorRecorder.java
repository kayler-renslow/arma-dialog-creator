package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 Created by Kayler on 11/12/2016.
 */
public interface XmlErrorRecorder {
	default void addError(@NotNull ParseError error) {
		getErrors().add(error);
	}

	@NotNull
	List<ParseError> getErrors();
}
