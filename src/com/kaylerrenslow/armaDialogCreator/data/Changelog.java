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

	public Changelog(int maxChanges) {
		setMaxChanges(maxChanges);
	}

	public int getMaxChanges() {
		return maxChanges;
	}

	public void setMaxChanges(int maxChanges) {
		if (maxChanges <= 0) {
			throw new IllegalArgumentException("maxChanges must be > 0");
		}
		this.maxChanges = maxChanges;
	}

	public void addChange(Change change) {
		undo.addFirst(change);
		if (undo.size() >= maxChanges) {
			while (undo.size() >= maxChanges) {
				undo.removeLast();
			}
		}
		updateChanges(change, Change.ChangeType.CREATED);
		redo.clear();
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.CHANGE_ADDED, change));
	}

	public void undo() throws ChangeUpdateFailedException {
		if (undo.size() == 0) {
			return;
		}
		Change undid = undo.removeFirst();
		updateChanges(undid, Change.ChangeType.UNDO);
		redo.add(undid);
		undid.getRegistrar().undo(undid);
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.UNDO, undid));
	}

	public void redo() throws ChangeUpdateFailedException {
		if (redo.size() == 0) {
			return;
		}
		Change c = redo.removeFirst();
		updateChanges(c, Change.ChangeType.REDO);
		undo.add(c);
		c.getRegistrar().redo(c);
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.REDO, c));
	}

	private void updateChanges(@NotNull Change toAdd, Change.ChangeType changeType) {
		pastChanges.add(0, new ChangeDescriptor(toAdd, changeType, System.currentTimeMillis()));
		while (pastChanges.size() >= maxChanges) {
			pastChanges.removeLast();
		}
	}

	@Nullable
	public Change getToUndo() {
		if (undo.size() == 0) {
			return null;
		}
		return undo.getFirst();
	}

	@Nullable
	public Change getToRedo() {
		if (redo.size() == 0) {
			return null;
		}
		return redo.getFirst();
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
