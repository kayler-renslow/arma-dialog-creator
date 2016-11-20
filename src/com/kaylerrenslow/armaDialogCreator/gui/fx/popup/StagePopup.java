/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.popup;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 Basis for a popup window that uses a JavaFX Stage as its host thingy majig

 @author Kayler
 @since 05/20/2016. */
public class StagePopup<E extends Parent> {

	protected final Scene myScene;
	protected final Stage myStage;
	protected final E myRootElement;
	private FXMLLoader myLoader;
	/** The buttons used when invoking {@link #getBoundResponseFooter(boolean, boolean, boolean)}. These are no longer null after invoking the method. */
	protected Button btnOk, btnCancel, btnHelp;
	protected GenericResponseFooter footer;
	private boolean hasBeenShown = false;
	private final ListChangeListener<? super String> stageStylesheetListener = new ListChangeListener<String>() {
		@Override
		public void onChanged(Change<? extends String> c) {
			myStage.getScene().getStylesheets().clear();
			myStage.getScene().getStylesheets().addAll(myStage.getOwner().getScene().getStylesheets());
		}
	};

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
		setPrimaryStage(primaryStage);

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
	 Set's the popup's primary stage.

	 @throws IllegalStateException if invoked when popup has already been made visible at some point
	 */
	protected void setPrimaryStage(@Nullable final Stage primaryStage) {
		if (primaryStage != null) {
			if (myStage.getOwner() != null) {
				myStage.getIcons().remove(primaryStage.getIcons().get(0));
				myStage.getScene().getStylesheets().removeAll(primaryStage.getScene().getStylesheets());
				primaryStage.getScene().getStylesheets().removeListener(stageStylesheetListener);
			}

			myStage.initOwner(primaryStage);
			if (primaryStage.getIcons().size() > 0) {
				myStage.getIcons().add(primaryStage.getIcons().get(0));
			}
			if (primaryStage.getScene() != null) {
				myStage.getScene().getStylesheets().addAll(primaryStage.getScene().getStylesheets());
				primaryStage.getScene().getStylesheets().addListener(stageStylesheetListener);
			}
		}
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

	/** @see Stage#sizeToScene() */
	public void sizeToScene() {
		myStage.sizeToScene();
	}

	/** Set the title of the stage */
	public void setTitle(String title) {
		myStage.setTitle(title);
	}

	/**
	 Make the popup appear

	 @see Stage#show()
	 */
	public void show() {
		hasBeenShown = true;
		myStage.show();
	}

	public boolean isShowing() {
		return myStage.isShowing();
	}

	/** Return true if the popup has ben shown via {@link #show()} at least once, false otherwise */
	public final boolean hasBeenShown() {
		return hasBeenShown;
	}

	/**
	 Force close the popup. This will also call the method closing()

	 @see Stage#close()
	 */
	public void close() {
		closing();
		myStage.close();
	}

	/**
	 Hides the popup

	 @see Stage#hide()
	 */
	public void hide() {
		hiding();
		myStage.hide();
	}

	/** Called when the cancel button is pressed (Invoked from the cancel button action event created in {@link #getBoundResponseFooter(boolean, boolean, boolean)}). Default implementation invokes {@link #close()} */
	protected void cancel() {
		close();
	}

	/** Called when the ok button is pressed (Invoked from the ok button action event created in {@link #getBoundResponseFooter(boolean, boolean, boolean)}). Default implementation invokes {@link #close()} */
	protected void ok() {
		close();
	}

	/** Called when the help button is pressed (Invoked from the help button action event created in {@link #getBoundResponseFooter(boolean, boolean, boolean)}). Default implementation does nothing */
	protected void help() {
	}

	/**
	 Get an {@link GenericResponseFooter} instance that provides default functionality for a cancel button and ok button. Will set the {@link #btnOk},{@link #btnCancel},{@link #btnHelp} instances to
	 {@link GenericResponseFooter#getBtnOk()},{@link GenericResponseFooter#getBtnCancel()},{@link GenericResponseFooter#getBtnHelp()} instances respectively. Will only create one instance per {@link StagePopup}
	 instance.

	 @param addCancel true to add cancel button
	 @param addOk true to add ok button
	 @param addHelpButton true to add help button
	 */
	protected GenericResponseFooter getBoundResponseFooter(boolean addCancel, boolean addOk, boolean addHelpButton) {
		if (footer != null) {
			return footer;
		}
		footer = new GenericResponseFooter(addCancel, addOk, addHelpButton,
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						help();
					}
				},
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						cancel();
					}
				},
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						ok();
					}
				}
		);
		btnOk = footer.getBtnOk();
		btnCancel = footer.getBtnCancel();
		btnHelp = footer.getBtnHelp();
		return footer;
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
		beep();
	}

	/** Emit a beeping noise */
	protected void beep() {
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

	public double getStageWidth() {
		return myStage.getWidth();
	}

	public double getStageHeight() {
		return myStage.getHeight();
	}

	/** Performs the operation description at {@link Stage#showAndWait()} */
	public void showAndWait() {
		myStage.showAndWait();
	}
}
