/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationProperty;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCProjectInitWindow;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCWindow;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
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
		if(INSTANCE != null){
			throw new IllegalStateException("Should not create a new ArmaDialogCreator instance when one already exists");
		}
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
		
		//load main window
		mainWindow = new ADCWindow(primaryStage);
		
		setToDarkTheme(ApplicationProperty.DARK_THEME.get(ArmaDialogCreator.getApplicationDataManager().getApplicationProperties()));
		
		initProject();
		
		mainWindow.show();
		
		/*don't need iterator here since Java will make the foreach loop behave like an iterator (http://stackoverflow.com/questions/85190/how-does-the-java-for-each-loop-work)*/
		for (StagePopup aShowLater : showLater) {
			aShowLater.show();
		}
		showLater = null;
	}
	
	private void initProject() {
		ADCProjectInitWindow projectInitWindow = new ADCProjectInitWindow();
		projectInitWindow.showAndWait();
		
		ADCProjectInitWindow.ProjectInit init = projectInitWindow.getProjectInit();
		Project project;
		if (init instanceof ADCProjectInitWindow.ProjectInit.NewProject) {
			ADCProjectInitWindow.ProjectInit.NewProject newProject = (ADCProjectInitWindow.ProjectInit.NewProject) init;
			String projectName = newProject.getProjectName();
			project = new Project(projectName, applicationDataManager.getAppSaveDataDirectory());
		} else if (init instanceof ADCProjectInitWindow.ProjectInit.OpenProject) {
			ADCProjectInitWindow.ProjectInit.OpenProject openProject = (ADCProjectInitWindow.ProjectInit.OpenProject) init;
			project = openProject.getProject();
		} else {
			System.err.println("WARNING: project init type wasn't matched with any known types. Just creating new project from scratch.");
			project = new Project(null, applicationDataManager.getAppSaveDataDirectory());
		}
		
		applicationDataManager.applicationData.setCurrentProject(project);
	}
	
	public static CanvasView getCanvasView() {
		return INSTANCE.mainWindow.getCanvasView();
	}
	
	public static Stage getPrimaryStage() {
		return INSTANCE.primaryStage;
	}
	
	public static ADCWindow getMainWindow() {
		return INSTANCE.mainWindow;
	}
	
	public static void setToDarkTheme(boolean set) {
		final String darkTheme = "/com/kaylerrenslow/armaDialogCreator/gui/fx/dark.css";
		if (set) {
			CanvasViewColors.EDITOR_BG = CanvasViewColors.DARK_THEME_EDITOR_BG;
			CanvasViewColors.GRID = CanvasViewColors.DARK_THEME_GRID;
			INSTANCE.primaryStage.getScene().getStylesheets().add(darkTheme);
		} else {
			CanvasViewColors.EDITOR_BG = CanvasViewColors.DEFAULT_EDITOR_BG;
			CanvasViewColors.GRID = CanvasViewColors.DEFAULT_GRID;
			INSTANCE.primaryStage.getScene().getStylesheets().remove(darkTheme);
		}
		if (getCanvasView() != null) {
			getCanvasView().updateCanvas();
		}
		getApplicationDataManager().getApplicationProperties().put(ApplicationProperty.DARK_THEME, set);
		getApplicationDataManager().saveApplicationProperties();
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
			//			ArmaDialogCreator.INSTANCE.applicationDataManager.forceSave();
		}
	}
}
