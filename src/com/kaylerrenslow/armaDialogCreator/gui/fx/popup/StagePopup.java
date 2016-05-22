package com.kaylerrenslow.armaDialogCreator.gui.fx.popup;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 @author Kayler
 Basis for a popup window that uses a JavaFX Stage as its host thingy majig
 Created on 05/20/2016. */
public abstract class StagePopup {

	protected final Scene myScene;
	protected final Stage myStage;
	protected final Parent myRootElement;

	/**
	 Creates a new JavaFX Stage based popup window.

	 @param primaryStage the stage of the JavaFX application
	 @param rootElement the root element of the scene
	 @param title title of the popup window
	 */
	public StagePopup(Stage primaryStage, Parent rootElement, String title) {
		myRootElement = rootElement;
		myStage = new Stage();
		myScene = new Scene(rootElement);
		myStage.setScene(myScene);
		myStage.setTitle(title);
		myStage.initOwner(primaryStage);
		myStage.getIcons().add(primaryStage.getIcons().get(0));

		myStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				onCloseRequest(event);
			}
		});
	}

	/** Make the popup magically appear (not really magically) */
	public void show() {
		myStage.show();
	}

	/** Force close the popup */
	public void close() {
		closing();
		myStage.close();
	}

	/** The window's X was clicked */
	protected void onCloseRequest(WindowEvent event) {
		closing();
	}

	/** Window is definitely closing now */
	protected void closing() {

	}
}
