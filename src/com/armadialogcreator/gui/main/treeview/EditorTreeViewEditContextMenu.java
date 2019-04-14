package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.gui.DefaultComponentContextMenu;
import com.armadialogcreator.gui.SimpleResponseDialog;
import com.armadialogcreator.gui.fxcontrol.inputfield.IdentifierChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.StringChecker;
import com.armadialogcreator.gui.main.popup.NameInputFieldDialog;
import com.armadialogcreator.lang.Lang;
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
public class EditorTreeViewEditContextMenu extends ContextMenu {
	public EditorTreeViewEditContextMenu(@NotNull EditorComponentTreeView treeView,
										 @NotNull ControlTreeItemEntry entryClicked) {
		ResourceBundle bundle = Lang.ApplicationBundle();
		CheckMenuItem checkMenuItemEnable = new CheckMenuItem(bundle.getString("ContextMenu.ControlEdit.enable"));
		checkMenuItemEnable.setSelected(entryClicked.isEnabled());
		checkMenuItemEnable.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				entryClicked.setEnabled(checkMenuItemEnable.isSelected());
			}
		});
		getItems().add(checkMenuItemEnable);


		MenuItem miConfigProperties = new MenuItem(bundle.getString("ContextMenu.DefaultComponent.configure"));
		miConfigProperties.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				DefaultComponentContextMenu.showControlPropertiesPopup(entryClicked.getMyArmaControl());
			}
		});
		getItems().add(miConfigProperties);

		MenuItem miRename = new MenuItem(bundle.getString("ContextMenu.DefaultComponent.rename"));
		miRename.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				NameInputFieldDialog<IdentifierChecker, String> dialog = new NameInputFieldDialog<>(
						miRename.getText(), miRename.getText(),
						new IdentifierChecker()
				);
				dialog.getInputField().getValueObserver().updateValue(entryClicked.getText());
				dialog.getInputField().selectAll();
				dialog.show();
				String value = dialog.getInputField().getValue();
				if (dialog.wasCancelled() || value == null) {
					return;
				}
				entryClicked.getMyArmaControl().setClassName(value);
			}
		});
		getItems().add(miRename);

		addCommon(treeView, entryClicked, bundle);
	}

	public EditorTreeViewEditContextMenu(@NotNull EditorComponentTreeView treeView,
										 @NotNull FolderTreeItemEntry entryClicked) {
		ResourceBundle bundle = Lang.ApplicationBundle();

		MenuItem miRename = new MenuItem(bundle.getString("ContextMenu.DefaultComponent.rename"));
		miRename.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				NameInputFieldDialog<StringChecker, String> dialog = new NameInputFieldDialog<>(
						miRename.getText(), miRename.getText(),
						new StringChecker()
				);
				dialog.getInputField().getValueObserver().updateValue(entryClicked.getText());
				dialog.getInputField().selectAll();
				dialog.show();
				String value = dialog.getInputField().getValue();
				if (dialog.wasCancelled() || value == null) {
					return;
				}
				entryClicked.setText(value);
			}
		});
		getItems().add(miRename);

		addCommon(treeView, entryClicked, bundle);
	}

	private void addCommon(@NotNull EditorComponentTreeView treeView, @NotNull UINodeTreeItemData entryClicked, ResourceBundle bundle) {
		MenuItem miClearSelection = new MenuItem(bundle.getString("ContextMenu.ControlEdit.clear_selection"));
		miClearSelection.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				treeView.getSelectionModel().clearSelection();
			}
		});
		getItems().add(miClearSelection);

		MenuItem menuItemRemove = new MenuItem(bundle.getString("ContextMenu.ControlEdit.remove"));
		menuItemRemove.setOnAction(new EventHandler<>() {
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
		getItems().add(menuItemRemove);


		MenuItem miDuplicate = new MenuItem(bundle.getString("ContextMenu.ControlEdit.duplicate"));
		getItems().add(miDuplicate);
		miDuplicate.setOnAction(e -> {
			TreeItem<? extends UINodeTreeItemData> selected = treeView.getSelectionModel().getSelectedItem();
			if (selected == null) {
				return;
			}
			selected.getValue().duplicate(treeView);
		});
	}
}
