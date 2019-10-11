package com.armadialogcreator.canvas;

import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/14/2016.
 */
public interface Selection<Item> {

	@NotNull ObservableList<Item> getSelected();

	/**
	 Gets the first item in the selection
	 */
	@Nullable Item getFirst();

	/**
	 Adds or removes the given item from the selection.
	 If the item is selected, it will no longer be selected. If it isn't selected, it will be selected
	 */
	void toggleFromSelection(@NotNull Item control);

	void addToSelection(@NotNull Item control);

	boolean isSelected(@Nullable Item control);

	boolean removeFromSelection(@NotNull Item control);

	void clearSelected();

	int numSelected();
}
