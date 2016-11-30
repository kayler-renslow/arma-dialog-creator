package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/19/2016 */
public class ChangeDescriptor {

	private final Change change;
	private final Change.ChangeType changeType;
	private final long timePerformed;

	public ChangeDescriptor(@NotNull Change change, @NotNull Change.ChangeType changeType, long timePerformed) {
		this.change = change;
		this.changeType = changeType;
		this.timePerformed = timePerformed;
	}

	/** Get the Epoch timestamp of when the change was performed. */
	public long getTimePerformed() {
		return timePerformed;
	}

	/** Return the {@link Change} */
	@NotNull
	public Change getChange() {
		return change;
	}

	/** Return the {@link com.kaylerrenslow.armaDialogCreator.data.Change.ChangeType} of {@link #getChange()}. */
	@NotNull
	public Change.ChangeType getChangeType() {
		return changeType;
	}
}
