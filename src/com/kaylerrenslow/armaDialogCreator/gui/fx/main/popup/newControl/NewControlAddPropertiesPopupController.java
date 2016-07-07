package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 Created by Kayler on 07/06/2016.
 */
public class NewControlAddPropertiesPopupController {
	@FXML
	public TextField tfFilter;

	@FXML
	public TableView<ObservableList> tvAllProperties;
	@FXML
	public Button btnToTv;
	@FXML
	public Button btnToLv;
	@FXML
	public TableView<ObservableList> tvSelectedProperties;

	@FXML
	public Button btnCancel;
	@FXML
	public Button btnOk;
}
