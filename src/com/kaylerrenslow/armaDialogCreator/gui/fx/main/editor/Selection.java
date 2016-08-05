package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Control;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/14/2016.
 */
public interface Selection {

	@NotNull ObservableList<Control> getSelected();

	/**
	 Gets the first item in the selection
	 */
	@Nullable Control getFirst();

	/**
	 Adds or removes the given Control from the selection. If the Control is selected, it will no longer be selected. If it isn't selected, it will be selected
	 */
	void toggleFromSelection(Control control);

	void addToSelection(Control control);

	boolean isSelected(@Nullable Control control);

	boolean removeFromSelection(Control control);

	void clearSelected();

	int numSelected();
}
