package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImagePaths;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ResourceBundle;

/**
 Created by Kayler on 10/12/2016.
 */
public class AboutDialog extends StageDialog<VBox> {
	public AboutDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null, false, true, false);

		ResourceBundle bundle = Lang.ApplicationBundle();

		setTitle(bundle.getString("Popups.About.dialog_title"));

		final Link hypRepo = new Link(Lang.Misc.REPO_URL);
		final Label lblRepoUrl = new Label(bundle.getString("Popups.About.repository") + ":", hypRepo);
		myStage.setResizable(false);
		lblRepoUrl.setContentDisplay(ContentDisplay.RIGHT);

		String version = Lang.Application.VERSION;
		String build = ArmaDialogCreator.getManifest().getMainAttributes().getValue("Build-Number");
		String javafx = System.getProperty("javafx.runtime.version");
		String java = System.getProperty("java.version");

		myRootElement.getChildren().addAll(
				new ImageView(ADCImagePaths.ABOUT_HEADER),
				new Label(bundle.getString("Popups.About.author") + ": " + "Kayler \"K-Town\" Renslow"),
				new Label(bundle.getString("Popups.About.version") + ": " + version),
				new Label(bundle.getString("Popups.About.build") + ": " + build),
				new Label("Java: " + java),
				new Label("JavaFX: " + javafx),
				lblRepoUrl
		);

		HBox hboxLib = new HBox(5,
				new Label(bundle.getString("Popups.About.libraries")),
				new Link("ANTLR 4.7", "http://www.antlr.org/"),
				new Link("RichTextFX 0.7-M2", "https://github.com/TomasMikula/RichTextFX"),
				new Link("Json Simple 1.1.1", "https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1")
		);
		hboxLib.setAlignment(Pos.CENTER_LEFT);
		myRootElement.getChildren().add(hboxLib);

		Button btnCopyInfo = new Button(bundle.getString("Popups.About.copy_info"));
		btnCopyInfo.setOnAction((e) -> {
			StringSelection selection = new StringSelection(
					String.format(
							"Arma Dialog Creator Details\nVersion: %s\nBuild: %s\nJava: %s\nJavaFX: %s",
							version,
							build,
							java,
							javafx
					)
			);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		});

		footer.getRightContainer().getChildren().add(0, btnCopyInfo);

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
