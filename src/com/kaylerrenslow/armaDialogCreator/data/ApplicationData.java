package com.kaylerrenslow.armaDialogCreator.data;

import java.util.ArrayList;
import java.util.List;

/**
 Created by Kayler on 06/07/2016.
 */
public final class ApplicationData {

	public final ComponentTreeViewData treeViewData = new ComponentTreeViewData();


	public static final class ComponentTreeViewData {
		private List<TreeItemEntry> treeItemList = new ArrayList<>();

		List<TreeItemEntry> getTreeItemList() {
			return treeItemList;
		}

		/** Adds the entry into the list for storage*/
		public void addTreeItemEntry(TreeItemEntry entry) {
			treeItemList.add(entry);
		}

		/** Remove the entry. Returns true if the entry was in the list. */
		public boolean removeTreeItemEntry(TreeItemEntry entry) {
			return treeItemList.remove(entry);
		}
	}
}
