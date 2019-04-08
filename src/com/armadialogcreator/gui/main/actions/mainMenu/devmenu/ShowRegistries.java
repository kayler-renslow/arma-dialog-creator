package com.armadialogcreator.gui.main.actions.mainMenu.devmenu;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.data.*;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.util.KeyValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 @author K
 @since 3/31/19 */
public class ShowRegistries implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ChooseRegistryDialog dialog = new ChooseRegistryDialog();
		dialog.show();
		Registry registry = dialog.getRegistry();
		if (dialog.wasCancelled() || registry == null) {
			return;
		}
		RegistryViewer viewer = new RegistryViewer(registry);
		viewer.show();
	}

	private static class RegistryViewer extends StageDialog<VBox> {

		public RegistryViewer(@NotNull Registry<?, ?> registry) {
			super(ArmaDialogCreator.getPrimaryStage(),
					new VBox(5), "Registry Viewer for " + registry.getClass().getName(),
					true, true, false
			);
			setStageSize(720, 720);

			TreeView<String> treeView = new TreeView<>();
			VBox.setVgrow(treeView, Priority.ALWAYS);

			myRootElement.getChildren().add(treeView);

			TreeItem<String> root = new TreeItem<>();
			Map<DataLevel, List<KeyValue<String, Configurable>>> map = registry.copyAllToConfigurableMap();
			for (DataLevel level : DataLevel.values()) {
				TreeItem<String> levelTi = new TreeItem<>(level.name());
				root.getChildren().add(levelTi);

				List<KeyValue<String, Configurable>> list = map.get(level);
				if (list == null) {
					continue;
				}
				for (KeyValue<String, Configurable> item : list) {
					TreeItem<String> tiName = new TreeItem<>(item.getKey());
					levelTi.getChildren().add(tiName);

					Configurable value = item.getValue();
					TreeItemConfigurabeHelper.appendTreeItem(tiName, value);
				}
			}

			treeView.setRoot(root);
			treeView.setShowRoot(false);
		}


	}

	private static class ChooseRegistryDialog extends StageDialog<VBox> {

		private final ChoiceBox<RegistryDescriptor> choiceBox = new ChoiceBox<>();

		public ChooseRegistryDialog() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(4), "Choose Registry", true, true, false);
			myRootElement.getChildren().add(choiceBox);

			choiceBox.getItems().add(new RegistryDescriptor(MacroRegistry.instance));
			choiceBox.getItems().add(new RegistryDescriptor(ConfigClassRegistry.instance));
			choiceBox.getItems().add(new RegistryDescriptor(FileDependencyRegistry.instance));
			choiceBox.getItems().add(new RegistryDescriptor(DefaultValueSheetRegistry.instance));
			choiceBox.getItems().add(new RegistryDescriptor(SettingsManager.instance));
		}

		@Nullable
		public Registry getRegistry() {
			RegistryDescriptor item = choiceBox.getSelectionModel().getSelectedItem();
			return item == null ? null : item.registry;
		}

		private static class RegistryDescriptor {
			private final Registry registry;

			public RegistryDescriptor(@NotNull Registry registry) {
				this.registry = registry;
			}

			@Override
			public String toString() {
				return registry.getClass().getName();
			}
		}
	}
}
