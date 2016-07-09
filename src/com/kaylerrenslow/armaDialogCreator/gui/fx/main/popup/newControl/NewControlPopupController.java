package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 @author Kayler
 Controller class for NewControlPopup
 Created on 07/06/2016. */
public class NewControlPopupController {

	@FXML
	public HBox hbHeader;

	@FXML
	public TextField tfClassName;
	@FXML
	public ComboBox<ControlType> cobBaseControl;

	@FXML
	public StackPane stackPaneProperties;

	@FXML
	public TextArea taPreviewSample;

	@FXML
	public Button btnCancel;
	@FXML
	public Button btnOk;

}
