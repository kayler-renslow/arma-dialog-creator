package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCProjectInitWindow;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCWindow;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.LinkedList;

/**
 @author Kayler
 Contains main method for running Arma Dialog Creator
 Created on 05/11/2016. */
public final class ArmaDialogCreator extends Application {
	
	private static ArmaDialogCreator INSTANCE;
	
	public ArmaDialogCreator() {
		INSTANCE = this;
	}
	
	/**
	 Launches the Arma Dialog Creator. Only one instance is allowed to be opened at a time per Java process.
	 */
	public static void main(String[] args) {
		if (INSTANCE != null) {
			getPrimaryStage().requestFocus();
			return;
		}
		ArmaDialogCreator.launch(args);
	}
	
	private Stage primaryStage;
	private ADCWindow mainWindow;
	private ApplicationDataManager applicationDataManager;
	
	private LinkedList<StagePopup> showLater = new LinkedList<>();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//load this stuff first
		ExceptionHandler.init();
		this.primaryStage = primaryStage;
		Thread.currentThread().setName("Arma Dialog Creator Main Thread");
		primaryStage.setOnCloseRequest(new ArmaDialogCreatorWindowCloseEvent());
		primaryStage.getIcons().add(new Image(ImagePaths.ICON_APP));
		primaryStage.setTitle(Lang.Application.APPLICATION_TITLE);
		
		//now can load save manager
		applicationDataManager = new ApplicationDataManager();
		
		ADCProjectInitWindow projectInitWindow = new ADCProjectInitWindow();
		projectInitWindow.showAndWait();
		
		ADCProjectInitWindow.ProjectInit init = projectInitWindow.getProjectInit();
		Project project = null;
		if (init instanceof ADCProjectInitWindow.ProjectInit.NewProject) {
			ADCProjectInitWindow.ProjectInit.NewProject newProject = (ADCProjectInitWindow.ProjectInit.NewProject) init;
			String projectName = newProject.getProjectName();
			project = new Project(projectName, applicationDataManager.getAppSaveDataDirectory());
		} else if (init instanceof ADCProjectInitWindow.ProjectInit.OpenProject) {
			ADCProjectInitWindow.ProjectInit.OpenProject openProject = (ADCProjectInitWindow.ProjectInit.OpenProject) init;
			
		}
		
		applicationDataManager.applicationData.initApplicationData(project);
		
		//		//load main window
		mainWindow = new ADCWindow(primaryStage);
		/*don't need iterator here since Java will make the foreach loop behave like an iterator (http://stackoverflow.com/questions/85190/how-does-the-java-for-each-loop-work)*/
		for (StagePopup aShowLater : showLater) {
			aShowLater.show();
		}
		showLater = null;
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
	
	public static ApplicationDataManager getApplicationDataManager() {
		return INSTANCE.applicationDataManager;
	}
	
	public static ApplicationData getApplicationData() {
		return INSTANCE.applicationDataManager.applicationData;
	}
	
	/** Show the given popup after the application's main window has been initialized */
	public static void showAfterMainWindowLoaded(StagePopup<?> selectSaveLocationPopup) {
		INSTANCE.showLater.add(selectSaveLocationPopup);
	}
	
	private static class ArmaDialogCreatorWindowCloseEvent implements EventHandler<WindowEvent> {
		
		@Override
		public void handle(WindowEvent event) {
			ArmaDialogCreator.INSTANCE.applicationDataManager.forceSave();
		}
	}
}
