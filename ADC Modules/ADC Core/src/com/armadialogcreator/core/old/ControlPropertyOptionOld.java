package com.armadialogcreator.core.old;

/**
 Created by Kayler on 05/23/2016.
 */
public class ControlPropertyOptionOld {
	public final String value, description, displayName;

	public ControlPropertyOptionOld(String displayName, String value, String description) {
		this.value = value;
		this.description = description;
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
