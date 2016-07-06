package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

/**
 Created by Kayler on 07/06/2016.
 */
public class NewControlPopup extends StagePopup<VBox> {
	private final NewControlAddPropertiesPopup addPropertiesPopup = new NewControlAddPropertiesPopup(this);

	public NewControlPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), FXUtil.loadFxml("/com/kaylerrenslow/armaDialogCreator/gui/fx/main/popup/newControl/newControl.fxml"), Lang.Popups.NewControl.POPUP_TITLE);
		NewControlPopupController controller = getMyLoader().getController();
		controller.mbtnAddProperty.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addPropertiesPopup.show();
			}
		});
	}
}
