package com.armadialogcreator.data;

import org.jetbrains.annotations.NotNull;

/**
 A {@link ChangeRegistrar} registers changes to {@link Changelog} and handles how to undo/redo those changes via {@link #undo(Change)} and {@link #redo(Change)} respectively

 @author Kayler
 @since 08/02/2016 */
public interface ChangeRegistrar {
	/**
	 Undoes the given change.

	 @param c change to undo
	 @throws ChangeUpdateFailedException when the undo operation failed
	 */
	void undo(@NotNull Change c) throws ChangeUpdateFailedException;

	/**
	 Redoes the given change

	 @param c change to redo
	 @throws ChangeUpdateFailedException when the redo operation failed
	 */
	void redo(@NotNull Change c) throws ChangeUpdateFailedException;
}
