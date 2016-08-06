package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ScreenDimension;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
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
	private ADCCanvasView canvasView;
	private ADCMenuBar mainMenuBar;
	private boolean fullscreen = false;
	
	public ADCWindow(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				setToFullScreen(newValue);
			}
		});
		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				autoResizeCanvasView();
			}
		});
		Scene scene = new Scene(rootElement);
		scene.getStylesheets().add("/com/kaylerrenslow/armaDialogCreator/gui/fx/misc.css");
		this.primaryStage.setScene(scene);
	}
	
	private void initialize(Scene scene) {
		canvasView = new ADCCanvasView();
		mainMenuBar = new ADCMenuBar();
		rootElement.getChildren().addAll(mainMenuBar, canvasView);
		rootElement.minWidth(ScreenDimension.SMALLEST.width + CanvasControls.PREFERRED_WIDTH);
		rootElement.minHeight(ScreenDimension.SMALLEST.height + 50.0);
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
	
	public void show() {
		initialize(primaryStage.getScene());
		this.primaryStage.show();
		
		new StagePopup<VBox>(primaryStage, new VBox(5), Lang.Popups.Beta.POPUP_TITLE) {
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
	
	public CanvasView getCanvasView() {
		return canvasView;
	}
	
	private void autoResizeCanvasView() {
		ScreenDimension closest = ScreenDimension.SMALLEST;
		for (ScreenDimension dimension : ScreenDimension.values()) {
			if (primaryStage.getWidth() - (fullscreen ? 0 : CanvasControls.PREFERRED_WIDTH) >= dimension.width) {
				closest = dimension;
			}
		}
		DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData()).setScreenDimension(closest);
	}
	
	public void setToFullScreen(boolean fullScreen) {
		primaryStage.setFullScreen(fullScreen);
		this.fullscreen = fullScreen;
		if (fullScreen) {
			rootElement.getChildren().remove(mainMenuBar);
		} else {
			rootElement.getChildren().add(0, mainMenuBar);
		}
		canvasView.hideCanvasControls(fullScreen);
		autoResizeCanvasView();
	}
}
