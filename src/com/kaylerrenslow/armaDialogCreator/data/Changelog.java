package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 @author Kayler
 Used for storing changes that happened inside the application
 Created on 08/02/2016. */
public class Changelog {
	private final LinkedList<Change> undo = new LinkedList<>();
	private final LinkedList<Change> redo = new LinkedList<>();
	private final ReadOnlyList<Change> undoReadOnly = new ReadOnlyList<>(undo);
	private final ReadOnlyList<Change> redoReadOnly = new ReadOnlyList<>(redo);
	private final UpdateListenerGroup<ChangelogUpdate> changeUpdateGroup = new UpdateListenerGroup<>();
	private boolean hadChanges = false;
	private int maxChanges;
	
	public Changelog(int maxChanges) {
		setMaxChanges(maxChanges);
	}
	
	public int getMaxChanges() {
		return maxChanges;
	}
	
	public void setMaxChanges(int maxChanges) {
		if (maxChanges <= 0) {
			throw new IllegalArgumentException("maxChanges must be >= 0");
		}
		this.maxChanges = maxChanges;
	}
	
	public boolean hadChanges() {
		return hadChanges;
	}
	
	public void addChange(Change change) {
		hadChanges = true;
		undo.addFirst(change);
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.CHANGE_ADDED, change));
		if (undo.size() >= maxChanges) {
			while (undo.size() >= maxChanges) {
				undo.removeLast();
			}
		}
	}
	
	public void undo() {
		if (undo.size() == 0) {
			return;
		}
		Change undid = undo.removeFirst();
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.UNDO, undid));
		redo.add(undid);
	}
	
	public void redo() {
		if (redo.size() == 0) {
			return;
		}
		Change c = redo.removeFirst();
		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.REDO, c));
		undo.add(c);
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
	
	public ReadOnlyList<Change> getUndoList() {
		return undoReadOnly;
	}
	
	public ReadOnlyList<Change> getRedoList() {
		return redoReadOnly;
	}
	
	public UpdateListenerGroup<ChangelogUpdate> getChangeUpdateGroup() {
		return changeUpdateGroup;
	}
}
