/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor;

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
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
		super(primaryStage, new VBox(5), Lang.ApplicationBundle().getString("Popups.MissingControlPropertiesConfig.popup_title"));
		myRootElement.setPadding(new Insets(5));
		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);

		ListView<String> listView = new ListView<>();
		for (ControlProperty controlProperty : missing) {
			listView.getItems().add(controlProperty.getName());
		}
		Button btnOk = new Button(Lang.ApplicationBundle().getString("Popups.btn_ok"));
		btnOk.setPrefWidth(100d);
		btnOk.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				close();
			}
		});
		HBox hbox = new HBox(btnOk);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		myRootElement.getChildren().addAll(new Label(Lang.ApplicationBundle().getString("Popups.MissingControlPropertiesConfig.missing_properties_message")), listView, hbox);
		myRootElement.setFillWidth(true);
		setStageSize(500, 300);
	}
}
