package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 The context menu for when a tree item is selected in the tree view
 Created on 07/13/2016. */
public class ControlEditContextMenu extends ContextMenu {
	public ControlEditContextMenu(@NotNull ControlTreeItemEntry entryClicked) {
		CheckMenuItem checkMenuItemEnable = new CheckMenuItem("Enable");
		checkMenuItemEnable.setSelected(entryClicked.isEnabled());
		checkMenuItemEnable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				entryClicked.setEnabled(checkMenuItemEnable.isSelected());
			}
		});
		getItems().add(checkMenuItemEnable);
	}
}
