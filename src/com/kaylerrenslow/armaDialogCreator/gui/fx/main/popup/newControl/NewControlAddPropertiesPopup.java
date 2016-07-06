package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 @author Kayler
 The popup that allows the user to pick and choose which properties go into the new control
 Created on 07/06/2016. */
public class NewControlAddPropertiesPopup extends StagePopup<VBox> {
	private final NewControlPopup newControlPopup;

	public NewControlAddPropertiesPopup(NewControlPopup newControlPopup) {
		super(ArmaDialogCreator.getPrimaryStage(), FXUtil.loadFxml("/com/kaylerrenslow/armaDialogCreator/gui/fx/main/popup/newControl/newControlAddProperties.fxml"), Lang.Popups.NewControl.AddPropertiesPopup.POPUP_TITLE);
		this.newControlPopup = newControlPopup;
		myStage.initModality(Modality.WINDOW_MODAL);
		myStage.initStyle(StageStyle.UTILITY);
	}


}
