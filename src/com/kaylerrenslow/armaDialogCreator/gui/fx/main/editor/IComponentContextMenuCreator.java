package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Component;
import javafx.scene.control.ContextMenu;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/13/2016.
 */
public interface IComponentContextMenuCreator {
	@NotNull
	ContextMenu initialize(Component component);
}
