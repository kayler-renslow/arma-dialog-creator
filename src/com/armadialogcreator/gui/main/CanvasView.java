package com.armadialogcreator.gui.main;

import com.armadialogcreator.canvas.UICanvasConfiguration;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.main.treeview.UINodeTreeItemData;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/20/2016.
 */
public interface CanvasView {

	@NotNull UICanvasConfiguration getConfiguration();

	/**
	 Set the background image of the canvas

	 @param imgPath the path to the image, or null to remove the image
	 */
	void setCanvasBackgroundToImage(@Nullable String imgPath);

	/** Fetches the new ui colors and repaints the canvas */
	void updateCanvas();

	/** @return the background image of the canvas, or null if not set */
	@Nullable ImagePattern getCanvasBackgroundImage();

	/** @return the background color of the canvas */
	@NotNull Color getCanvasBackgroundColor();

	/**
	 Update the Absolute region box. For each parameter: -1 to leave unchanged, 0 for false, 1 for true

	 @param alwaysFront true if the region should always be rendered last, false if it should be rendered first
	 @param showing true the region is showing, false if not
	 */
	void updateAbsRegion(int alwaysFront, int showing);

	@NotNull EditableTreeView<ArmaControl, ? extends UINodeTreeItemData> getMainControlTreeView();

	@NotNull EditableTreeView<ArmaControl, ? extends UINodeTreeItemData> getBackgroundControlTreeView();

	/** @return true if treeView == {@link #getBackgroundControlTreeView()}, false otherwise */
	default boolean isBackgroundTreeView(@NotNull EditableTreeView<ArmaControl, ? extends UINodeTreeItemData> treeView) {
		return treeView == getBackgroundControlTreeView();
	}
}
