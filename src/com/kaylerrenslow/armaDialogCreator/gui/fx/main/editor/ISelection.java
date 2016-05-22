package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 Created by Kayler on 05/14/2016.
 */
public interface ISelection {

	@NotNull
	ArrayList<Component> getSelected();

	/**
	 Gets the first item in the selection
	 */
	@Nullable
	Component getFirst();

	/**
	 Adds or removes the given component from the selection. If the component is selected, it will no longer be selected. If it isn't selected, it will be selected
	 */
	void toggleFromSelection(Component component);

	void addToSelection(Component component);

	boolean isSelected(@Nullable Component component);

	boolean removeFromSelection(Component component);

	void clearSelected();

	int numSelected();
}
