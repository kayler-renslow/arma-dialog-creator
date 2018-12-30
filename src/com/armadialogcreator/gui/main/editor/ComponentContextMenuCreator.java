package com.armadialogcreator.gui.main.editor;

import com.armadialogcreator.gui.uicanvas.CanvasComponent;
import javafx.scene.control.ContextMenu;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/13/2016.
 */
public interface ComponentContextMenuCreator {
	@NotNull
	ContextMenu initialize(CanvasComponent component);
}
