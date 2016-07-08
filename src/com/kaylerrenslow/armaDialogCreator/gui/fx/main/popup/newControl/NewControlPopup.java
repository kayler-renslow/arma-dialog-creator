package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 Created by Kayler on 07/06/2016.
 */
public class NewControlPopup extends StagePopup<VBox> {
	private final NewControlAddPropertiesPopup addPropertiesPopup = new NewControlAddPropertiesPopup(this);

	public NewControlPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), FXUtil.loadFxml("/com/kaylerrenslow/armaDialogCreator/gui/fx/main/popup/newControl/newControl.fxml"), Lang.Popups.NewControl.POPUP_TITLE);
		NewControlPopupController controller = getMyLoader().getController();
		controller.mbtnConfigureProperties.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addPropertiesPopup.show();
			}
		});
		controller.cobBaseControl.getItems().addAll(ControlType.values());
		controller.cobBaseControl.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ControlType>() {
			@Override
			public void changed(ObservableValue<? extends ControlType> observable, ControlType oldValue, ControlType newValue) {
				VBox vbProperties = controller.vbProperties;
				vbProperties.getChildren().clear();
				ArmaControlLookup lookup = ArmaControlLookup.findByControlType(newValue);
				ControlPropertyLookup[] required = lookup.specProvider.getRequiredProperties();
				for(ControlPropertyLookup req : required){
					vbProperties.getChildren().add(new Label(req.propertyName));
				}
			}
		});
	}
}
