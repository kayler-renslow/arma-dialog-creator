package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 @author Kayler
 @since 11/06/2017 */
public class ParseResult {
	private final List<ParseError> errors;

	protected ParseResult(@NotNull List<ParseError> errors) {
		this.errors = errors;
	}

	@NotNull
	public List<ParseError> getErrors() {
		return errors;
	}
}
