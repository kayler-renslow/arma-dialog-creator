package com.armadialogcreator.gui.main.actions.mainMenu.create;

import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.main.CanvasView;
import com.armadialogcreator.gui.main.popup.NameTextFieldDialog;
import com.armadialogcreator.gui.main.treeview.FolderTreeItemEntry;
import com.armadialogcreator.main.ArmaDialogCreator;
import com.armadialogcreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 @author Kayler
 @since 6/27/2017 */
public class CreateNewFolderAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ResourceBundle bundle = Lang.ApplicationBundle();
		NewFolderDialog dialog = new NewFolderDialog(
				bundle.getString("Popups.NewFolder.popup_title"),
				bundle.getString("Popups.NewFolder.message"),
				bundle.getString("Popups.NewFolder.is_background")
		);

		dialog.getTextField().textProperty().addListener((observable, oldValue, newValue) -> {
			dialog.getCanOkProperty().set(newValue != null && newValue.trim().length() > 0);
		});
		dialog.getTextField().setText("folder");
		dialog.getTextField().requestFocus();
		dialog.show();
		if (dialog.wasCancelled()) {
			return;
		}

		String folderName = dialog.getInputText();
		boolean background = dialog.isBackground();

		CanvasView canvasView = ArmaDialogCreator.getCanvasView();

		EditableTreeView treeView = background ? canvasView.getBackgroundControlTreeView() :
				canvasView.getMainControlTreeView();
		treeView.addChildDataToRoot(new FolderTreeItemEntry(folderName));
	}

	private static class NewFolderDialog extends NameTextFieldDialog {
		private final CheckBox checkBoxBackground = new CheckBox();

		public NewFolderDialog(@NotNull String title, @NotNull String message, @NotNull String backgroundLbl) {
			super(title, message);
			myRootElement.getChildren().add(checkBoxBackground);
			checkBoxBackground.setText(backgroundLbl);
		}

		public boolean isBackground() {
			return checkBoxBackground.isSelected();
		}
	}
}
