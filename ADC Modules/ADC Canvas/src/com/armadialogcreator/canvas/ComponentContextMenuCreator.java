package com.armadialogcreator.canvas;

import javafx.scene.control.ContextMenu;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/13/2016.
 */
public interface ComponentContextMenuCreator {
	@NotNull
	ContextMenu initialize(@NotNull RenderAnchorPoint anchorPoint);
}
