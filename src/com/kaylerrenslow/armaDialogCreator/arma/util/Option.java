package com.kaylerrenslow.armaDialogCreator.arma.util;

/**
 Created by Kayler on 05/23/2016.
 */
public class Option {
	public final String value, description, displayName;

	public Option(String displayName, String value, String description) {
		this.value = value;
		this.description = description;
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}