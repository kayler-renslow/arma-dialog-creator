package com.kaylerrenslow.armaDialogCreator.gui.fx.popup;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 @author Kayler
 Basis for a popup window that uses a JavaFX Stage as its host thingy majig
 Created on 05/20/2016. */
public class StagePopup<E extends Parent> {

	protected final Scene myScene;
	protected final Stage myStage;
	protected final E myRootElement;
	private FXMLLoader myLoader;

	/**
	 Creates a new JavaFX Stage based popup window. This popup window will inherit the first icon from the primary stage as well as all the stylesheets.<br>
	 The stylesheets will also update whenever the primary stage's stylesheets get updated.

	 @param primaryStage the primary stage of the JavaFX application (should be the one from the class that extends Application). Can also be null (won't inherit icons or stylesheets).
	 @param popupStage the already created stage that will house the popup
	 @param rootElement the root element of the scene
	 @param title title of the popup window
	 */
	public StagePopup(@Nullable Stage primaryStage, @NotNull Stage popupStage, @NotNull E rootElement, @Nullable String title) {
		myRootElement = rootElement;
		myStage = popupStage;
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
		myStage.setOnHiding(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				hiding();
			}
		});
	}

	/**
	 Creates a new JavaFX Stage based popup window. This popup window will inherit the first icon from the primary stage as well as all the stylesheets.<br>
	 The stylesheets will also update whenever the primary stage's stylesheets get updated.

	 @param primaryStage the primary stage of the JavaFX application (should be the one from the class that extends Application). Can also be null (won't inherit icons or stylesheets).
	 @param rootElement the root element of the scene
	 @param title title of the popup window
	 */
	public StagePopup(@Nullable Stage primaryStage, @NotNull E rootElement, @Nullable String title) {
		this(primaryStage, new Stage(), rootElement, title);
	}

	/**
	 Creates a new JavaFX Stage based popup window from a .fxml file. This popup window will inherit the first icon from the primary stage as well as all the stylesheets.<br>
	 The stylesheets will also update whenever the primary stage's stylesheets get updated.

	 @param primaryStage the primary stage of the JavaFX application (should be the one from the class that extends Application). Can also be null (won't inherit icons or stylesheets).
	 @param loader the loader that is <b>already loaded</b> and contains the root element and the controller class
	 @param title title of the popup window
	 */
	public StagePopup(@Nullable Stage primaryStage, @NotNull FXMLLoader loader, @Nullable String title) {
		this(primaryStage, new Stage(), loader.getRoot(), title);
		this.myLoader = loader;
	}

	/**
	 Creates a new JavaFX Stage based popup window from an .fxml file and returns it. This popup window will inherit the first icon from the primary stage as well as all the stylesheets.<br>
	 The stylesheets will also update whenever the primary stage's stylesheets get updated.

	 @param primaryStage the primary stage of the JavaFX application (should be the one from the class that extends Application). Can also be null (won't inherit icons or stylesheets).
	 @param fxmlLocation String location inside the buildpath that points to the .fxml file
	 @param title title of the popup window
	 */
	public static <T extends Parent> StagePopup newFxmlInstance(@Nullable Stage primaryStage, @NotNull URL fxmlLocation, @Nullable String title) throws IOException {
		T root = FXMLLoader.load(fxmlLocation);
		return new StagePopup<>(primaryStage, new Stage(), root, title);
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
		hiding();
		myStage.hide();
	}

	/** Called when the popup is about to hide. Defualt implementation is nothing. */
	protected void hiding() {
	}

	/** The window's X was clicked */
	protected void onCloseRequest(WindowEvent event) {
		closing();
	}

	/** Window is definitely closing now. Default implementation is nothing. */
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

	/** Gets the loader. This will be null if constructor {@link StagePopup#StagePopup(Stage, FXMLLoader, String)} isn't used */
	@Nullable
	protected final FXMLLoader getMyLoader() {
		return myLoader;
	}

	public void setStageSize(double w, double h) {
		myStage.setWidth(w);
		myStage.setHeight(h);
	}

	public double getStageWidth(){
		return myStage.getWidth();
	}

	public double getStageHeight(){
		return myStage.getHeight();
	}
}
