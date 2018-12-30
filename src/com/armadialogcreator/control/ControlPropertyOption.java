package com.armadialogcreator.control;

/**
 Created by Kayler on 05/23/2016.
 */
public class ControlPropertyOption {
	public final String value, description, displayName;

	public ControlPropertyOption(String displayName, String value, String description) {
		this.value = value;
		this.description = description;
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
