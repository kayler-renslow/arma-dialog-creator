/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.data.io.xml.ParseError;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.ProjectXmlLoader;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/23/2016 */
public class ProjectImproperResultDialog extends StageDialog<ScrollPane> {

	public ProjectImproperResultDialog(@NotNull ProjectXmlLoader.ProjectParseResult result) {
		super(ArmaDialogCreator.getPrimaryStage(), new ScrollPane(new VBox(15)), Lang.ApplicationBundle().getString("ProjectInitWindow.ProjectResultErrorPopup.popup_title"), false, true, false);
		myRootElement.setFitToWidth(true);
		myRootElement.setFitToHeight(true);
		VBox root = (VBox) myRootElement.getContent();

		//swap out the dialog's root element padding for the vbox that is the root element of scrollpane
		root.setPadding(super.myRootElement.getPadding());
		super.myRootElement.setPadding(new Insets(0));

		root.getChildren().add(new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.ProjectResultErrorPopup.errors_title")));
		root.getChildren().add(new Separator(Orientation.HORIZONTAL));
		for (ParseError error : result.getErrors()) {
			VBox vbErrorMsg = new VBox(5);
			vbErrorMsg.getChildren().addAll(
					getLabel(Lang.ApplicationBundle().getString("ProjectInitWindow.ProjectResultErrorPopup.error_message") + " " + error.getMessage(), null),
					getLabel(Lang.ApplicationBundle().getString("ProjectInitWindow.ProjectResultErrorPopup.recovered"), getLabel(error.recovered() ? Lang.ApplicationBundle().getString("ProjectInitWindow.ProjectResultErrorPopup.yes") : Lang.ApplicationBundle().getString("ProjectInitWindow.ProjectResultErrorPopup.no"), null))
			);
			if (error.recovered()) {
				vbErrorMsg.getChildren().add(getLabel(Lang.ApplicationBundle().getString("ProjectInitWindow.ProjectResultErrorPopup.recover_message") + " " + error.getRecoverMessage(), null));
			}

			root.getChildren().add(vbErrorMsg);
		}
		myStage.setWidth(340d);
	}

	private Label getLabel(String text, Node graphic) {
		Label label = new Label(text, graphic);
		label.setContentDisplay(ContentDisplay.RIGHT);
		return label;
	}

}
