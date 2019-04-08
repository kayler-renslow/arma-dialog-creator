package com.armadialogcreator.gui.main.actions.mainMenu.devmenu;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.data.UINodeConfigurable;
import com.armadialogcreator.gui.StageDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 4/8/19 */
public class ShowDisplay implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		DisplayViewer viewer = new DisplayViewer();
		viewer.show();
	}

	private static class DisplayViewer extends StageDialog<VBox> {

		public DisplayViewer() {
			super(ArmaDialogCreator.getPrimaryStage(),
					new VBox(5), "Editing Display Viewer",
					true, true, false
			);
			setStageSize(720, 720);

			TreeView<String> treeView = new TreeView<>();
			VBox.setVgrow(treeView, Priority.ALWAYS);

			myRootElement.getChildren().add(treeView);

			TreeItem<String> root = new TreeItem<>();
			ArmaDisplay display = EditorManager.instance.getEditingDisplay();
			TreeItem<String> bgControls = new TreeItem<>("Background Controls");
			TreeItem<String> controls = new TreeItem<>("Controls");

			root.getChildren().add(bgControls);
			root.getChildren().add(controls);

			for (UINode node : display.getControlNodes().iterateChildNodes()) {
				appendTreeItem(controls, node);
			}
			for (UINode node : display.getBackgroundControlNodes().iterateChildNodes()) {
				appendTreeItem(bgControls, node);
			}

			treeView.setRoot(root);
			treeView.setShowRoot(false);
		}

		private void appendTreeItem(@NotNull TreeItem<String> parent, @NotNull UINode node) {
			TreeItem<String> nodeItem = getTreeItem(parent, node);
			for (UINode child : node.iterateChildNodes()) {
				appendTreeItem(nodeItem, child);
			}
		}

		private TreeItem<String> getTreeItem(@NotNull TreeItem<String> parent, @NotNull UINode node) {
			UINodeConfigurable configurable = new UINodeConfigurable(node, false);
			return TreeItemConfigurabeHelper.appendTreeItem(parent, configurable);
		}
	}

}
