package com.armadialogcreator.gui.main.actions.mainMenu.devmenu;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.gui.main.popup.SimpleInfoDialog;
import com.armadialogcreator.util.KeyValueString;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 4/8/19 */
public class TreeItemConfigurabeHelper {
	@NotNull
	public static TreeItem<String> appendTreeItem(@NotNull TreeItem<String> parent, @NotNull Configurable c) {
		TreeItem<String> child = new TreeItem<>(c.getConfigurableName());
		parent.getChildren().add(child);

		if (c.getNestedConfigurableCount() == 0 && c.getConfigurableBody().isEmpty() && c.getConfigurableAttributeCount() == 0) {
			Label lbl = new Label("(EMPTY) ");
			child.setGraphic(lbl);
			return child;
		}

		{
			TreeItem<String> viewAsXml = new TreeItem<>("As XML");
			child.getChildren().add(viewAsXml);
			Button b = new Button("View");
			b.setOnAction(event -> {
				new SimpleInfoDialog.AsTextArea(
						ArmaDialogCreator.getPrimaryStage(),
						"XML",
						Configurable.toFormattedString(c, 1)
				).show();
			});
			viewAsXml.setGraphic(b);
		}

		{
			boolean bodyEmpty = c.getConfigurableBody().isEmpty();
			TreeItem<String> body = new TreeItem<>("Body" + (bodyEmpty ? " (EMPTY)" : ""));
			if (!bodyEmpty) {
				Button b = new Button("View");
				b.setOnAction(event -> {
					new SimpleInfoDialog.AsTextArea(ArmaDialogCreator.getPrimaryStage(), "Body", c.getConfigurableBody()).show();
				});
				body.setGraphic(b);
			}

			child.getChildren().add(body);
		}

		{
			TreeItem<String> attributes = new TreeItem<>("Attributes");
			child.getChildren().add(attributes);

			for (KeyValueString kv : c.getConfigurableAttributes()) {
				TreeItem<String> item = new TreeItem<>(kv.getKey() + "=" + kv.getValue());
				attributes.getChildren().add(item);
			}
		}

		{
			TreeItem<String> nested = new TreeItem<>("Nested Configurables");
			child.getChildren().add(nested);

			for (Configurable nestedConf : c.getNestedConfigurables()) {
				appendTreeItem(nested, nestedConf);
			}
		}


		return child;
	}
}
