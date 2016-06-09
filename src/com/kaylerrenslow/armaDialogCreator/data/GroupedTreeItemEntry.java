package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.data.exception.ItemDoesNotExistException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 @author Kayler
 Base class for a TreeItemEntry instance that can have children
 Created on 06/08/2016. */
public abstract class GroupedTreeItemEntry implements TreeItemEntry {

	private final LinkedList<TreeItemEntry> treeItems;

	public GroupedTreeItemEntry(@Nullable TreeItemEntry... items) {
		this.treeItems = new LinkedList<>();
		if (items == null) {
			return;
		}
		for (TreeItemEntry item : items) {
			treeItems.add(item);
		}
	}

	/** Returns a read only list. The internal tree items' list is passed by reference so that when an item to moved in the folder, it will be moved in the read only list. */
	public ReadOnlyDataList<TreeItemEntry> getTreeItems() {
		return new ReadOnlyDataList<>(treeItems);
	}

	/**
	 Moves the given item to the new index

	 @param entryToMove entry to move
	 @param newIndex 0 based index to move to
	 @throws IndexOutOfBoundsException when newIndex <0 or >= list size
	 @throws ItemDoesNotExistException when the given entry isn't in the folder
	 */
	public void reorder(@NotNull TreeItemEntry entryToMove, int newIndex) {
		int curIndex = treeItems.indexOf(entryToMove);
		if (newIndex == curIndex) {
			return;
		}
		if (curIndex < 0) {
			throw new ItemDoesNotExistException("Entry '" + entryToMove + "' doesn't exist inside this tree item.");
		}
		boundCheck(newIndex);
		treeItems.set(curIndex, null);
		treeItems.add(newIndex, entryToMove);
		Iterator<TreeItemEntry> iter = treeItems.iterator();
		while (iter.hasNext()) {
			if (iter.next() == null) {
				iter.remove();
			}
		}
	}

	/**
	 Returns true if the folder's list of items contained the specified element
	 */
	public boolean remove(@NotNull TreeItemEntry toRemove) {
		return treeItems.remove(toRemove);
	}

	/**
	 Inserts the specified item into this item. If the item is already inside, this operation has no effect

	 @param toInsert item to insert
	 @param index index of where to index
	 @throws IndexOutOfBoundsException when index <0 or >= list size
	 */
	public void insert(@NotNull TreeItemEntry toInsert, int index) {
		boundCheck(index);
		if (!treeItems.contains(toInsert)) {
			treeItems.add(index, toInsert);
		}
	}

	private void boundCheck(int index) {
		if (index < 0 || index >= treeItems.size()) {
			throw new IndexOutOfBoundsException("index is out of bounds. Value=" + index);
		}
	}

}
