/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.control.ControlType;
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
