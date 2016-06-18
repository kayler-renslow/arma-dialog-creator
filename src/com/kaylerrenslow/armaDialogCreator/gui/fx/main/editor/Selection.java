package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 Created by Kayler on 05/14/2016.
 */
public interface Selection {

	@NotNull
	ArrayList<CanvasComponent> getSelected();

	/**
	 Gets the first item in the selection
	 */
	@Nullable
	CanvasComponent getFirst();

	/**
	 Adds or removes the given component from the selection. If the component is selected, it will no longer be selected. If it isn't selected, it will be selected
	 */
	void toggleFromSelection(CanvasComponent component);

	void addToSelection(CanvasComponent component);

	boolean isSelected(@Nullable CanvasComponent component);

	boolean removeFromSelection(CanvasComponent component);

	void clearSelected();

	int numSelected();
}
