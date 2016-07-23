package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaUIScale;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ScreenDimension;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 Created by Kayler on 05/11/2016.
 */
public class ADCWindow {
	private final Stage primaryStage;
	private final VBox rootElement = new VBox();
	private ArmaResolution resolution = new ArmaResolution(ScreenDimension.D1600, ArmaUIScale.SMALL);
	private final ADCCanvasView canvasView = new ADCCanvasView(resolution);
	private final ADCMenuBar mainMenuBar = new ADCMenuBar();

	public ADCWindow(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				setToFullScreen(newValue);
			}
		});
		Scene scene = new Scene(rootElement);
		this.primaryStage.setScene(scene);
		initialize(scene);
		show();
		
		new StagePopup<VBox>(primaryStage, new VBox(5), Lang.Popups.Beta.POPUP_TITLE){
			@Override
			public void show() {
				myStage.initModality(Modality.APPLICATION_MODAL);
				
				myRootElement.setPadding(new Insets(10));
				myStage.setResizable(false);
				final Label lblBody = new Label(Lang.Popups.Beta.BODY);
				final Hyperlink hyperlink = new Hyperlink(Lang.Popups.Beta.REPORT_TO_LINK);
				hyperlink.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						BrowserUtil.browse(Lang.Popups.Beta.REPORT_TO_LINK);
					}
				});
				myRootElement.getChildren().addAll(lblBody, hyperlink, new Separator(Orientation.HORIZONTAL), getResponseFooter(false, true, false));
				myStage.sizeToScene();
				super.show();
			}
		}.show();
	}

	private void initialize(Scene scene) {
		scene.getStylesheets().add("/com/kaylerrenslow/armaDialogCreator/gui/fx/misc.css");
		rootElement.getChildren().addAll(mainMenuBar, canvasView);
		rootElement.minWidth(resolution.getScreenWidth() + 250.0);
		rootElement.minHeight(resolution.getScreenHeight() + 50.0);
		EventHandler<KeyEvent> keyEvent = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				canvasView.keyEvent(event.getText(), event.getEventType() == KeyEvent.KEY_PRESSED, event.isShiftDown(), event.isControlDown(), event.isAltDown());
			}
		};
		scene.setOnKeyPressed(keyEvent);
		scene.setOnKeyReleased(keyEvent);
		scene.getMnemonics().clear();

	}

	private void show() {
		this.primaryStage.show();
	}

	public CanvasView getCanvasView() {
		return canvasView;
	}

	public void setToDarkTheme(boolean set) {
		final String darkTheme = "/com/kaylerrenslow/armaDialogCreator/gui/fx/dark.css";
		if (set) {
			CanvasViewColors.EDITOR_BG = CanvasViewColors.Default.DARK_THEME_EDITOR_BG;
			CanvasViewColors.GRID = CanvasViewColors.Default.DARK_THEME_GRID;
			primaryStage.getScene().getStylesheets().add(darkTheme);
		} else {
			CanvasViewColors.EDITOR_BG = CanvasViewColors.Default.EDITOR_BG;
			CanvasViewColors.GRID = CanvasViewColors.Default.GRID;
			primaryStage.getScene().getStylesheets().remove(darkTheme);
		}
		canvasView.updateCanvas();
	}

	public void setToFullScreen(boolean fullScreen) {
		primaryStage.setFullScreen(fullScreen);
		if (fullScreen) {
			rootElement.getChildren().remove(mainMenuBar);
		} else {
			rootElement.getChildren().add(0, mainMenuBar);
		}
	}
}
