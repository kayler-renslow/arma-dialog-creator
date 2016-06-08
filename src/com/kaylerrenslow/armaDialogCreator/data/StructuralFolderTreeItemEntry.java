package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.data.exception.ItemDoesNotExistException;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;

/**
 Created by Kayler on 06/07/2016.
 */
public class StructuralFolderTreeItemEntry extends TreeItemEntry {

	private final LinkedList<TreeItemEntry> treeItems;

	public StructuralFolderTreeItemEntry(@NotNull TreeItemEntry... items) {
		this.treeItems = new LinkedList<>();
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
			throw new ItemDoesNotExistException("Entry '" + entryToMove + "' doesn't exist in the folder");
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

	/** Inserts the specified item into the folder. If the item is already in the folder, this operation has no effect
	 @param toInsert item to insert
	 @param index index of where to index
	 @throws IndexOutOfBoundsException when index <0 or >= list size
	 */
	public void insert(@NotNull TreeItemEntry toInsert, int index){
		boundCheck(index);
		if(!treeItems.contains(toInsert)){
			treeItems.add(index, toInsert);
		}
	}

	@Override
	public boolean isStructural() {
		return true;
	}

	private void boundCheck(int index){
		if (index < 0 || index >= treeItems.size()) {
			throw new IndexOutOfBoundsException("index is out of bounds. Value=" + index);
		}
	}
}
