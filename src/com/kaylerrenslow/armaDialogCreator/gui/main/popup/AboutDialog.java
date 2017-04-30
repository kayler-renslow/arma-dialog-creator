package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImagePaths;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 Created by Kayler on 10/12/2016.
 */
public class AboutDialog extends StageDialog<VBox> {
	public AboutDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), Lang.ApplicationBundle().getString("Popups.About.dialog_title"), false, true, false);
		final Link hypRepo = new Link(Lang.Misc.REPO_URL);
		final Label lblRepoUrl = new Label(Lang.ApplicationBundle().getString("Popups.About.repository") + ":", hypRepo);
		myStage.setResizable(false);
		lblRepoUrl.setContentDisplay(ContentDisplay.RIGHT);
		myRootElement.getChildren().addAll(
				new ImageView(ADCImagePaths.ABOUT_HEADER),
				new Label(Lang.ApplicationBundle().getString("Popups.About.author") + ": " + "Kayler \"K-Town\" Renslow"),
				new Label(Lang.ApplicationBundle().getString("Popups.About.version") + ": " + Lang.Application.VERSION),
				new Label(Lang.ApplicationBundle().getString("Popups.About.build") + ": " + ArmaDialogCreator.getManifest().getMainAttributes().getValue("Build-Number")),
				lblRepoUrl
		);

		HBox hboxLib = new HBox(5,
				new Label(Lang.ApplicationBundle().getString("Popups.About.libraries")),
				new Link("ANTLR 4.7", "http://www.antlr.org/"),
				new Link("RichTextFX 0.7-M2", "https://github.com/TomasMikula/RichTextFX"),
				new Link("Json Simple 1.1.1", "https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1")
		);
		hboxLib.setAlignment(Pos.CENTER_LEFT);
		myRootElement.getChildren().add(hboxLib);

		btnOk.requestFocus();
	}

	private static class Link extends Hyperlink {
		public Link(String text, String url) {
			super(text);
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					BrowserUtil.browse(url);
					setVisited(false);
				}
			});

		}

		public Link(String text) {
			this(text, text);
		}
	}
}
