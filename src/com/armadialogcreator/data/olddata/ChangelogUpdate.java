package com.armadialogcreator.data.olddata;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 08/02/2016.
 */
public class ChangelogUpdate {
	private final UpdateType type;
	private final Change change;
	
	public enum UpdateType {
		CHANGE_ADDED, UNDO, REDO
	}

	public ChangelogUpdate(@NotNull UpdateType type, @NotNull Change change) {
		this.type = type;
		this.change = change;
	}

	@NotNull
	public UpdateType getType() {
		return type;
	}

	@NotNull
	public Change getChange() {
		return change;
	}
}
