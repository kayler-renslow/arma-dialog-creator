package com.kaylerrenslow.armaDialogCreator.gui.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasControl;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/14/2016.
 */
public interface Selection {

	@NotNull ObservableList<CanvasControl> getSelected();

	/**
	 Gets the first item in the selection
	 */
	@Nullable CanvasControl getFirst();

	/**
	 Adds or removes the given Control from the selection. If the Control is selected, it will no longer be selected. If it isn't selected, it will be selected
	 */
	void toggleFromSelection(CanvasControl control);

	void addToSelection(CanvasControl control);

	boolean isSelected(@Nullable CanvasControl control);

	boolean removeFromSelection(CanvasControl control);

	void clearSelected();

	int numSelected();
}
