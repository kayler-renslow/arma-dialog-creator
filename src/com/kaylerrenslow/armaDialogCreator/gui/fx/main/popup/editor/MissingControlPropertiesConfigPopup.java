package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
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
class MissingControlPropertiesConfigPopup extends StagePopup<VBox> {
	/**
	 */
	MissingControlPropertiesConfigPopup(@Nullable Stage primaryStage, @NotNull List<ControlProperty> missing) {
		super(primaryStage, new VBox(5), Lang.Popups.MissingControlPropertiesConfig.POPUP_TITLE);
		myRootElement.setPadding(new Insets(5));
		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);

		ListView<String> listView = new ListView<>();
		for (ControlProperty controlProperty : missing) {
			listView.getItems().add(controlProperty.getName());
		}
		Button btnOk = new Button(Lang.Popups.BTN_OK);
		btnOk.setPrefWidth(100d);
		btnOk.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				close();
			}
		});
		HBox hbox = new HBox(btnOk);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		myRootElement.getChildren().addAll(new Label(Lang.Popups.MissingControlPropertiesConfig.MISSING_PROPERTIES_MESSAGE), listView, hbox);
		myRootElement.setFillWidth(true);
		setStageSize(500, 300);
	}
}
