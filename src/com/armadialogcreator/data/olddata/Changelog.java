package com.armadialogcreator.data.olddata;

import com.armadialogcreator.util.ReadOnlyList;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 Used for storing changes that happened inside the application

 @author Kayler
 @since 08/02/2016. */
public class Changelog {


	@NotNull
	public static Changelog getInstance() {
		return null;
	}

	private final LinkedList<ChangeDescriptor> undo = new LinkedList<>();
	private final LinkedList<ChangeDescriptor> redo = new LinkedList<>();
	private final ReadOnlyList<ChangeDescriptor> undoReadOnly = new ReadOnlyList<>(undo);
	private final ReadOnlyList<ChangeDescriptor> redoReadOnly = new ReadOnlyList<>(redo);
	private final UpdateListenerGroup<ChangelogUpdate> changeUpdateGroup = new UpdateListenerGroup<>();
	private final LinkedList<ChangeDescriptor> recentChanges = new LinkedList<>();
	private final ReadOnlyList<ChangeDescriptor> recentChangesReadOnly = new ReadOnlyList<>(recentChanges);
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
	 Add a change to the stack. {@link #getRedoList()} will be cleared.
	 A new {@link ChangeDescriptor} instance will be placed on {@link #getRecentChanges()}
	 with the given {@link Change} instance and the {@link Change.ChangeType} will be {@link Change.ChangeType#CREATED}.

	 @param change change to add
	 */
	public void addChange(@NotNull Change change) {
		ChangeDescriptor changeDescriptor = new ChangeDescriptor(change, Change.ChangeType.CREATED, System.currentTimeMillis());
		undo.push(changeDescriptor);

		if (undo.size() >= maxChanges) {
			while (undo.size() >= maxChanges) {
				undo.removeLast();
			}
		}
		updateChanges(changeDescriptor);
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

		Change undid = undo.pop().getChange();

		ChangeDescriptor changeDescriptor = new ChangeDescriptor(undid, Change.ChangeType.UNDO, System.currentTimeMillis());
		updateChanges(changeDescriptor);
		redo.push(changeDescriptor);
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
		Change c = redo.pop().getChange();

		ChangeDescriptor changeDescriptor = new ChangeDescriptor(c, Change.ChangeType.REDO, System.currentTimeMillis());
		updateChanges(changeDescriptor);
		undo.push(changeDescriptor);
		c.getRegistrar().redo(c);

		changeUpdateGroup.update(new ChangelogUpdate(ChangelogUpdate.UpdateType.REDO, c));
	}

	private void updateChanges(@NotNull ChangeDescriptor toAdd) {
		recentChanges.addFirst(toAdd);
		while (recentChanges.size() >= maxChanges) {
			recentChanges.removeLast();
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
		return undo.peek().getChange();
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
		return redo.peek().getChange();
	}

	/**
	 Get the first {@link ChangeDescriptor} instance to undo with {@link #undo()}

	 @return first {@link ChangeDescriptor}, or null if nothing to undo
	 */
	@Nullable
	public ChangeDescriptor getToUndoDescriptor() {
		if (undo.size() == 0) {
			return null;
		}
		return undo.peek();
	}

	/**
	 Get the first {@link ChangeDescriptor} instance to redo with {@link #redo()}

	 @return first {@link ChangeDescriptor}, or null if nothing to undo
	 */
	@Nullable
	public ChangeDescriptor getToRedoDescriptor() {
		if (redo.size() == 0) {
			return null;
		}
		return redo.peek();
	}


	/**
	 Get a list of changes with the most recent at the beginning of list.
	 This will include any undo and redo changes that have occurred.

	 @return most recent change, or null if there wasn't one
	 */
	@NotNull
	public ReadOnlyList<ChangeDescriptor> getRecentChanges() {
		return recentChangesReadOnly;
	}

	/**
	 Equal to doing {@link #getRecentChanges()} on index = 0

	 @return the most recent change, or null if there wasn't one
	 */
	@Nullable
	public ChangeDescriptor getMostRecentChange() {
		return recentChanges.isEmpty() ? null : recentChanges.getFirst();
	}

	/** Get the list of things that can be undone */
	@NotNull
	public ReadOnlyList<ChangeDescriptor> getUndoList() {
		return undoReadOnly;
	}

	/** Get the list of things that can be redone */
	@NotNull
	public ReadOnlyList<ChangeDescriptor> getRedoList() {
		return redoReadOnly;
	}

	/** Add a listener to this group for when a change occurs. */
	@NotNull
	public UpdateListenerGroup<ChangelogUpdate> getChangeUpdateGroup() {
		return changeUpdateGroup;
	}
}
