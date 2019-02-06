package com.armadialogcreator.gui.main;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.lang.Lang;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

/**
 @author K
 @since 01/07/2019 */
public class AskSaveProjectDialog extends StageDialog<VBox> {

	private boolean saveProgress = false;

	public AskSaveProjectDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, false);
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("Popups.SaveProject.popup_title"));

		myRootElement.getChildren().add(new Label(bundle.getString("Popups.SaveProject.message")));
		btnOk.setText(bundle.getString("Confirmation.yes"));
		Button btnNo = new Button(bundle.getString("Confirmation.no"));
		btnNo.setOnAction(event -> {
			close();
		});
		btnNo.setPrefWidth(btnOk.getPrefWidth());
		getFooter().getRightContainer().getChildren().add(1, btnNo);

		myStage.setResizable(false);
	}

	@Override
	protected void ok() {
		saveProgress = true;
		super.ok();
	}

	/** @return true if the user responded yes for saving, false if no progress should be saved */
	public boolean saveProgress() {
		return saveProgress;
	}
}

