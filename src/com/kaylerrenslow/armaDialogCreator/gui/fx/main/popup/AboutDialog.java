package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
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
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), Lang.ApplicationBundle().getString("Popups.About.dialog_title"), false, true, false);
		final Hyperlink hypRepo = new Hyperlink(Lang.Misc.REPO_URL);
		hypRepo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				BrowserUtil.browse(hypRepo.getText());
			}
		});
		final Label lblRepoUrl = new Label(Lang.ApplicationBundle().getString("Popups.About.repository") + ":", hypRepo);
		myStage.setResizable(false);
		lblRepoUrl.setContentDisplay(ContentDisplay.RIGHT);
		myRootElement.getChildren().addAll(
				new ImageView(ImagePaths.ABOUT_HEADER),
				new Label(Lang.ApplicationBundle().getString("Popups.About.author") + ": " + "Kayler \"K-Town\" Renslow"),
				new Label(Lang.ApplicationBundle().getString("Popups.About.version") + ": " + Lang.Application.VERSION),
				new Label(Lang.ApplicationBundle().getString("Popups.About.build") + ": " + ArmaDialogCreator.getManifest().getMainAttributes().getValue("Build-Number")),
				lblRepoUrl
		);
		btnOk.requestFocus();
	}
}
