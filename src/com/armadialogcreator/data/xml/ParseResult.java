package com.armadialogcreator.data.xml;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 @author Kayler
 @since 11/06/2017 */
public class ParseResult {
	private final ArrayList<ParseError> errors;

	protected ParseResult(@NotNull ArrayList<ParseError> errors) {
		this.errors = errors;
	}

	@NotNull
	public ArrayList<ParseError> getErrors() {
		return errors;
	}
}
