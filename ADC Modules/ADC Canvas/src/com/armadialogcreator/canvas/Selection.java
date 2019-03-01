package com.armadialogcreator.canvas;

import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/14/2016.
 */
public interface Selection {

	@NotNull ObservableList<UINode> getSelected();

	/**
	 Gets the first item in the selection
	 */
	@Nullable UINode getFirst();

	/**
	 Adds or removes the given Control from the selection. If the Control is selected, it will no longer be selected. If it isn't selected, it will be selected
	 */
	void toggleFromSelection(@NotNull UINode control);

	void addToSelection(@NotNull UINode control);

	boolean isSelected(@Nullable UINode control);

	boolean removeFromSelection(@NotNull UINode control);

	void clearSelected();

	int numSelected();
}
