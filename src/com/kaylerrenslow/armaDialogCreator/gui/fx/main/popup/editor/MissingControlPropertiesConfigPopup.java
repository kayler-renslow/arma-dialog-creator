package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.Label;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 Created by Kayler on 06/02/2016.
 */
class MissingControlPropertiesConfigPopup extends StagePopup<VBox>{
	/**
	 */
	MissingControlPropertiesConfigPopup(@Nullable Stage primaryStage, @NotNull List<ControlProperty> missing) {
		super(primaryStage, new VBox(5), Lang.Popups.MissingControlPropertiesConfig.POPUP_TITLE);
		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);
		Label lbl;
		for(ControlProperty controlProperty : missing){
			lbl = new Label(controlProperty.getName());
			myRootElement.getChildren().add(lbl);
		}
		myRootElement.setFillWidth(true);
	}
}
