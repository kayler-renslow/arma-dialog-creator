package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 @author Kayler
 @since 07/30/2017 */
public class EditProjectSettingsDialog extends StageDialog<VBox> {
	private final TextField tfProjectName = new TextField();
	private final TextArea taProjectDesc = new TextArea();
	private final CheckBox checkBoxMoveProjectDir;

	public EditProjectSettingsDialog(@NotNull Project project) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null, true, true, true);
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("Popups.EditProjectSettings.popup_title"));

		checkBoxMoveProjectDir = new CheckBox(bundle.getString("Popups.EditProjectSettings.move_project_directory"));
		checkBoxMoveProjectDir.setTooltip(new Tooltip(bundle.getString("Popups.EditProjectSettings.move_project_directory_tooltip")));


		myRootElement.getChildren().add(getHbox(bundle.getString("Popups.EditProjectSettings.project_name"), tfProjectName));
		myRootElement.getChildren().add(checkBoxMoveProjectDir);
		myRootElement.getChildren().add(getHbox(bundle.getString("Popups.EditProjectSettings.project_desc"), taProjectDesc));

		HBox.setHgrow(tfProjectName, Priority.ALWAYS);
		HBox.setHgrow(taProjectDesc, Priority.ALWAYS);

		tfProjectName.setText(project.getProjectName());
		taProjectDesc.setText(project.getProjectDescription());

	}

	@NotNull
	private HBox getHbox(String label, Node n) {
		return new HBox(5, new Label(label), n);
	}

	public boolean moveProjectDirectory() {
		return checkBoxMoveProjectDir.isSelected();
	}

	@NotNull
	public String getProjectName() {
		return tfProjectName.getText();
	}

	@NotNull
	public String getProjectDescription() {
		return taProjectDesc.getText();
	}
}
