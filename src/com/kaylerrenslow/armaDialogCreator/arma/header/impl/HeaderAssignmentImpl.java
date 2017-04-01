package com.kaylerrenslow.armaDialogCreator.arma.header.impl;

import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderAssignment;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderValue;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderAssignmentImpl implements HeaderAssignment {

	private String variableName;
	private HeaderValue headerValue;

	public HeaderAssignmentImpl(@NotNull String variableName) {
		this.variableName = variableName;
	}

	@Override
	@NotNull
	public String getVariableName() {
		return variableName;
	}

	@Override
	@NotNull
	public HeaderValue getValue() {
		return headerValue;
	}

	public void setHeaderValue(@NotNull HeaderValue headerValue) {
		this.headerValue = headerValue;
	}
}
