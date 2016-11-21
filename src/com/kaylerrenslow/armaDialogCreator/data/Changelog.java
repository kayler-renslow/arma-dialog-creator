/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 Used for storing changes that happened inside the application

 @author Kayler
 @since 08/02/2016. */
public class Changelog {


	public static Changelog getInstance() {
		return ArmaDialogCreator.getApplicationData().getChangelog();
	}

	private final LinkedList<Change> undo = new LinkedList<>();
	private final LinkedList<Change> redo = new LinkedList<>();
	private final ReadOnlyList<Change> undoReadOnly = new ReadOnlyList<>(undo);
	private final ReadOnlyList<Change> redoReadOnly = new ReadOnlyList<>(redo);
	private final UpdateListenerGroup<ChangelogUpdate> changeUpdateGroup = new UpdateListenerGroup<>();
	private final LinkedList<ChangeDescriptor> pastChanges = new LinkedList<>();
	private final ReadOnlyList<ChangeDescriptor> pastChangesReadOnly = new ReadOnlyList<>(pastChanges);
	private int maxChanges;

	/**
	 Constructs a {@link Changelog} that stores <code>maxChanges</code> number of changes.

	 @param maxChanges how many changes to store before some are removed after {@link #addChange(Change)}
	 */
	public Changelog(int maxChanges) {
		setMaxChanges(maxChanges);
	}

	/**
	 Get how many changes to store before some are removed after {@link #addChange(Change)}

	 @return max number of changes
	 */
	public int getMaxChanges() {
		return maxChanges;
	}

	/**
	 Set maximum number of stored changes

	 @param maxChanges maximum number of stored changes
	 */
	public void setMaxChanges(int maxChanges) {
		if (maxChanges <= 0) {
			throw new IllegalArgumentException("maxChanges must be > 0");
		}
		this.maxChanges = maxChanges;
	}

	/**
	 Add a change to the stack. {@link #getRedoList()} will be cleared

	 @param change change to add
	 */
	public void addChange(Change change) {
		undo.push(change);
		if (undo.size() >= maxChanges) {
			while (undo.size() >= maxChanges) {
				undo.removeLast();
			}
		}
		updateChanges(change, Change.ChangeType.CREATED);
		redo.clear();
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.CHANGE_ADDED, change));
	}

	/**
	 Undo {@link #getToUndo()}

	 @throws ChangeUpdateFailedException when undo couldn't be performed
	 */
	public void undo() throws ChangeUpdateFailedException {
		if (undo.size() == 0) {
			return;
		}
		Change undid = undo.pop();
		updateChanges(undid, Change.ChangeType.UNDO);
		redo.push(undid);
		undid.getRegistrar().undo(undid);
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.UNDO, undid));
	}

	/**
	 Redo {@link #getToRedo()}

	 @throws ChangeUpdateFailedException when redo couldn't be performed
	 */
	public void redo() throws ChangeUpdateFailedException {
		if (redo.size() == 0) {
			return;
		}
		Change c = redo.pop();
		updateChanges(c, Change.ChangeType.REDO);
		undo.push(c);
		c.getRegistrar().redo(c);
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.REDO, c));
	}

	private void updateChanges(@NotNull Change toAdd, Change.ChangeType changeType) {
		pastChanges.add(0, new ChangeDescriptor(toAdd, changeType, System.currentTimeMillis()));
		while (pastChanges.size() >= maxChanges) {
			pastChanges.removeLast();
		}
	}

	/**
	 Get the first {@link Change} instance to undo with {@link #undo()}

	 @return first {@link Change}, or null if nothing to undo
	 */
	@Nullable
	public Change getToUndo() {
		if (undo.size() == 0) {
			return null;
		}
		return undo.peek();
	}

	/**
	 Get the first {@link Change} instance to redo with {@link #redo()}

	 @return first {@link Change}, or null if nothing to undo
	 */
	@Nullable
	public Change getToRedo() {
		if (redo.size() == 0) {
			return null;
		}
		return redo.peek();
	}

	/** Get a list of changes with the most recent at the beginning of list */
	@NotNull
	public ReadOnlyList<ChangeDescriptor> getChanges() {
		return pastChangesReadOnly;
	}

	/** Get the list of things that can be undone */
	public ReadOnlyList<Change> getUndoList() {
		return undoReadOnly;
	}

	/** Get the list of things that can be redone */
	public ReadOnlyList<Change> getRedoList() {
		return redoReadOnly;
	}

	/** Add a listener to this group for when a change occurs. */
	public UpdateListenerGroup<ChangelogUpdate> getChangeUpdateGroup() {
		return changeUpdateGroup;
	}
}
