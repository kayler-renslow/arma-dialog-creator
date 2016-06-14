package com.kaylerrenslow.armaDialogCreator.gui.fx.popup;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 @author Kayler
 Basis for a popup window that uses a JavaFX Stage as its host thingy majig
 Created on 05/20/2016. */
public class StagePopup<E extends Parent> {

	protected final Scene myScene;
	protected final Stage myStage;
	protected final E myRootElement;

	/**
	 Creates a new JavaFX Stage based popup window. This popup window will inherit the first icon from the primary stage as well as all the stylesheets.<br>
	 The stylesheets will also update whenever the primary stage's stylesheets get updated.

	 @param primaryStage the primary stage of the JavaFX application (should be the one from the class that extends Application). Can also be null (won't inherit icons or stylesheets).
	 @param rootElement the root element of the scene
	 @param title title of the popup window
	 */
	public StagePopup(@Nullable Stage primaryStage, E rootElement, String title) {
		myRootElement = rootElement;
		myStage = new Stage();
		myScene = new Scene(rootElement);
		myStage.setScene(myScene);
		myStage.setTitle(title);
		if (primaryStage != null) {
			myStage.initOwner(primaryStage);
			myStage.getIcons().add(primaryStage.getIcons().get(0));
			myStage.getScene().getStylesheets().addAll(primaryStage.getScene().getStylesheets());
			primaryStage.getScene().getStylesheets().addListener(new ListChangeListener<String>() {
				@Override
				public void onChanged(Change<? extends String> c) {
					myStage.getScene().getStylesheets().clear();
					myStage.getScene().getStylesheets().addAll(primaryStage.getScene().getStylesheets());
				}
			});
		}

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

	public boolean isShowing() {
		return myStage.isShowing();
	}

	/** Force close the popup. This will also call the method closing() */
	public void close() {
		closing();
		myStage.close();
	}

	/** Hides the popup */
	public void hide() {
		myStage.hide();
	}

	/** The window's X was clicked */
	protected void onCloseRequest(WindowEvent event) {
		closing();
	}

	/** Window is definitely closing now. Default implementation is empty. */
	protected void closing() {

	}

	/** Make the popup request focus */
	public void requestFocus() {
		myStage.requestFocus();
	}

	/** Request focus and make a beep */
	public void beepFocus() {
		requestFocus();
		Toolkit.getDefaultToolkit().beep();
	}
}
