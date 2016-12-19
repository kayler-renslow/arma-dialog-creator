package com.kaylerrenslow.armaDialogCreator.gui.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.main.editor.DefaultComponentContextMenu;
import com.kaylerrenslow.armaDialogCreator.gui.popup.SimpleResponseDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 The context menu for when a tree item is selected in the tree view

 @author Kayler
 @since 07/13/2016. */
public class ControlEditContextMenu extends ContextMenu {
	public ControlEditContextMenu(EditorComponentTreeView<? extends TreeItemEntry> treeView, @NotNull ControlTreeItemEntry entryClicked) {
		ResourceBundle bundle = Lang.ApplicationBundle();
		CheckMenuItem checkMenuItemEnable = new CheckMenuItem(bundle.getString("ContextMenu.ControlEdit.enable"));
		checkMenuItemEnable.setSelected(entryClicked.isEnabled());
		checkMenuItemEnable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				entryClicked.setEnabled(checkMenuItemEnable.isSelected());
			}
		});
		getItems().add(checkMenuItemEnable);


		MenuItem miConfigProperties = new MenuItem(bundle.getString("ContextMenu.DefaultComponent.configure"));
		miConfigProperties.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DefaultComponentContextMenu.showControlPropertiesPopup(entryClicked.getMyArmaControl());
			}
		});
		getItems().add(miConfigProperties);

		MenuItem miClearSelection = new MenuItem(bundle.getString("ContextMenu.ControlEdit.clear_selection"));
		miClearSelection.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				treeView.getSelectionModel().clearSelection();
			}
		});
		getItems().add(miClearSelection);

		MenuItem menuItemRemoveControl = new MenuItem(bundle.getString("ContextMenu.ControlEdit.remove"));
		menuItemRemoveControl.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SimpleResponseDialog dialog = new SimpleResponseDialog(
						ArmaDialogCreator.getPrimaryStage(), bundle.getString("ContextMenu.ControlEdit.RemoveDialog.title"),
						String.format(bundle.getString("ContextMenu.ControlEdit.RemoveDialog.body_f"), entryClicked.getText()),
						true, true, false
				);
				dialog.sizeToScene();
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				TreeItem selected = treeView.getSelectionModel().getSelectedItem();
				if (selected == null) {
					return;
				}

				treeView.removeChild(selected.getParent(), selected);
			}
		});
		getItems().add(menuItemRemoveControl);
	}
}
