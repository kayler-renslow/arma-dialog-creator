package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCWindow;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.SelectSaveLocationPopup;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.io.ApplicationDataManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 @author Kayler
 Created on 05/11/2016. */
public class ArmaDialogCreator extends Application {
	private static ArmaDialogCreator INSTANCE;

	public static void main(String[] args) {
		ArmaDialogCreator.launch(args);
	}

	private Stage primaryStage;
	private ADCWindow mainWindow;
	private ApplicationDataManager saveManager = new ApplicationDataManager();

	@Override
	public void start(Stage primaryStage) throws Exception {
		ArmaDialogCreator.INSTANCE = this;
		ExceptionHandler.init();
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(new ArmaDialogCreatorWindowCloseEvent());
		primaryStage.getIcons().add(new Image(ImagePaths.ICON_APP));
		primaryStage.setTitle(Lang.Application.APPLICATION_TITLE);
		Thread.currentThread().setName("Arma_Dialog_Creator.MainThread");
		loadWindow();
	}

	private void loadWindow() {
		mainWindow = new ADCWindow(primaryStage);
		if (!saveManager.appSaveDataDirectorySet()) {
			new SelectSaveLocationPopup(INSTANCE.saveManager.getAppSaveDataDirectory(), INSTANCE.saveManager.getArma3ToolsDirectory()).show();
		}
	}

	public static CanvasView getCanvasView() {
		return INSTANCE.mainWindow.getCanvasView();
	}

	public static Stage getPrimaryStage() {
		return INSTANCE.primaryStage;
	}

	public static ADCWindow getWindow() {
		return INSTANCE.mainWindow;
	}

	public static ApplicationDataManager getSaveDataManager() {
		return INSTANCE.saveManager;
	}

	public static ApplicationData getApplicationData() {
		return INSTANCE.saveManager.applicationData;
	}


	private static class ArmaDialogCreatorWindowCloseEvent implements EventHandler<WindowEvent> {

		@Override
		public void handle(WindowEvent event) {
			ArmaDialogCreator.INSTANCE.saveManager.forceSave();
		}
	}
}
