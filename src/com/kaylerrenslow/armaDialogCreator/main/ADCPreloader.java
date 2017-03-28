

package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImagePaths;
import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImages;
import javafx.application.Preloader;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 Created by Kayler on 10/07/2016.
 */
public class ADCPreloader extends Preloader {

	private Stage preloaderStage;
	private ProgressIndicator progressIndicator = new ProgressIndicator(-1);
	private final String[] progressText = {"Bamboozling", "Loading the Dinghy", "Doodling", "Counting to Infinity", "Finished Counting to Infinity", "Boondoggling"};
	private final Label lblProgressText = new Label(progressText[0]);

	public ADCPreloader() {
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info) {
		if (info instanceof ProgressNotification) {
			handleProgressNotificationCustom((ProgressNotification) info);
		}
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification info) {
		if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
			closePreloader();
		}
	}

	private void handleProgressNotificationCustom(ProgressNotification info) {
		progressIndicator.setProgress(info.getProgress());
		int progressTextInd = (int) (info.getProgress() * this.progressText.length);
		lblProgressText.setText(progressText[progressTextInd]);
	}

	private void closePreloader() {
		preloaderStage.close();
		preloaderStage = null;
	}


	@Override
	public void start(Stage preloaderStage) throws Exception {
		this.preloaderStage = preloaderStage;
		preloaderStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});

		preloaderStage.setTitle(Lang.Application.APPLICATION_TITLE);
		preloaderStage.getIcons().add(ADCImages.ICON_ADC);
		progressIndicator.setMaxWidth(48d);
		progressIndicator.setMaxHeight(progressIndicator.getMaxWidth());

		VBox vBox = new VBox(5, progressIndicator, lblProgressText);
		vBox.setAlignment(Pos.CENTER);
		VBox.setVgrow(progressIndicator, Priority.ALWAYS);

		Label lblBuild = new Label("Build: " + ArmaDialogCreator.getManifest().getMainAttributes().getValue("Build-Number"));
		Label lblVersion = new Label("Version: " + ArmaDialogCreator.getManifest().getMainAttributes().getValue("Specification-Version"));
		BorderPane borderPane = new BorderPane(vBox, null, null, new HBox(10, lblBuild, lblVersion), null);
		borderPane.setPadding(new Insets(5));

		StackPane.setMargin(borderPane, new Insets(248, 0, 0, 0));
		StackPane stackpane = new StackPane(new ImageView(ADCImagePaths.PRELOAD_SCREEN), borderPane);
		Scene scene = new Scene(stackpane);
		preloaderStage.initStyle(StageStyle.UNDECORATED);
		preloaderStage.setScene(scene);
		preloaderStage.sizeToScene();
		preloaderStage.show();
	}

}
