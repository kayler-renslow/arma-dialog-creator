package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 Created by Kayler on 06/07/2016.
 */
public final class ApplicationData {

	public final ComponentTreeViewData treeViewData = new ComponentTreeViewData();

	private ArmaDisplay editingDisplay = new ArmaDisplay(0);

	public void init() {
		editingDisplay.getControls().add(new StaticControl("Sample", 0, ControlStyle.SINGLE, 0, 0, 1, 1, ArmaDialogCreator.getCanvasView().getCurrentResolution()));
	}

	/** Get the display that the dialog creator is editing right now. */
	@NotNull
	public ArmaDisplay getEditingDisplay() {
		return editingDisplay;
	}

	/** Set the display that the dialog creator is to edit. */
	public void setEditingDisplay(@NotNull ArmaDisplay display) {
		this.editingDisplay = display;
	}


	public static final class ComponentTreeViewData {
		private List<TreeItemEntry> treeItemList = new ArrayList<>();

		List<TreeItemEntry> getTreeItemList() {
			return treeItemList;
		}

		/** Adds the entry into the list for storage */
		public void addTreeItemEntry(TreeItemEntry entry) {
			treeItemList.add(entry);
		}

		/** Remove the entry. Returns true if the entry was in the list. */
		public boolean removeTreeItemEntry(TreeItemEntry entry) {
			return treeItemList.remove(entry);
		}
	}
}
