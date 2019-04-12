package com.armadialogcreator.gui.main.actions.mainMenu.devmenu;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.application.ADCFile;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.application.XmlConfigurableLoader;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.util.XmlParseException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author K
 @since 4/8/19 */
public class ShowXmlAsConfigurable implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
		if (file == null) {
			return;
		}
		Viewer viewer = null;
		try {
			viewer = new Viewer(XmlConfigurableLoader.load(ADCFile.toADCFile(file)));
		} catch (XmlParseException e) {
			ExceptionHandler.error(e);
			return;
		}
		viewer.show();
	}

	private static class Viewer extends StageDialog<VBox> {

		public Viewer(@NotNull Configurable c) {
			super(ArmaDialogCreator.getPrimaryStage(),
					new VBox(5), "Editing Display Viewer",
					true, true, false
			);
			setStageSize(720, 720);

			TreeView<String> treeView = new TreeView<>();
			VBox.setVgrow(treeView, Priority.ALWAYS);

			myRootElement.getChildren().add(treeView);

			TreeItem<String> root = new TreeItem<>();
			TreeItemConfigurabeHelper.appendTreeItem(root, c);
			treeView.setRoot(root);
			treeView.setShowRoot(false);
		}

	}

}
