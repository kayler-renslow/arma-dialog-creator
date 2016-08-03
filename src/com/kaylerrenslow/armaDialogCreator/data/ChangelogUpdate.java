package com.kaylerrenslow.armaDialogCreator.data;

/**
 Created by Kayler on 08/02/2016.
 */
public class ChangelogUpdate {
	private final UpdateType type;
	private final Change change;
	
	public enum UpdateType {
		CHANGE_ADDED, UNDO, REDO
	}
	
	public ChangelogUpdate(UpdateType type, Change change) {
		this.type = type;
		this.change = change;
	}
	
	public UpdateType getType() {
		return type;
	}
	
	public Change getChange() {
		return change;
	}
}
