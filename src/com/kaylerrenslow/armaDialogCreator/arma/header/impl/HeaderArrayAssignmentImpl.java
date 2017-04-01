package com.kaylerrenslow.armaDialogCreator.arma.header.impl;

import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderArray;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderArrayAssignment;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderValue;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderArrayAssignmentImpl implements HeaderArrayAssignment {

	private String variableName;
	private HeaderArray headerArray;

	public HeaderArrayAssignmentImpl(@NotNull String variableName) {
		this.variableName = variableName;
	}

	@NotNull
	@Override
	public String getVariableName() {
		return variableName;
	}

	@NotNull
	@Override
	public HeaderValue getValue() {
		return getArray();
	}

	@NotNull
	@Override
	public HeaderArray getArray() {
		return headerArray;
	}

	public void setHeaderArray(@NotNull HeaderArray headerArray) {
		this.headerArray = headerArray;
	}
}
