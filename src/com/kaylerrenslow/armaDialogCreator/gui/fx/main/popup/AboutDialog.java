/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.BuildProperty;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 Created by Kayler on 10/12/2016.
 */
public class AboutDialog extends StageDialog<VBox> {
	public AboutDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), Lang.ApplicationBundle.getString("Popups.About.dialog_title"), false, true, false);
		final Hyperlink hypRepo = new Hyperlink(Lang.Misc.REPO_URL);
		hypRepo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				BrowserUtil.browse(hypRepo.getText());
			}
		});
		final Label lblRepoUrl = new Label(Lang.ApplicationBundle.getString("Popups.About.repository") + ":", hypRepo);
		myStage.setResizable(false);
		lblRepoUrl.setContentDisplay(ContentDisplay.RIGHT);
		myRootElement.getChildren().addAll(
				new ImageView(ImagePaths.ABOUT_HEADER),
				new Label(Lang.ApplicationBundle.getString("Popups.About.author") + ": " + "Kayler \"K-Town\" Renslow"),
				new Label(Lang.ApplicationBundle.getString("Popups.About.version") + ": " + Lang.Application.VERSION),
				new Label(Lang.ApplicationBundle.getString("Popups.About.build") + ": " + ArmaDialogCreator.getBuildProperty(BuildProperty.BUILD_NUMBER, "unknown")),
				lblRepoUrl
		);
		btnOk.requestFocus();
	}
}
