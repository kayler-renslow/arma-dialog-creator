package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ScreenDimension;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.UIScale;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static javafx.application.Application.STYLESHEET_CASPIAN;
import static javafx.application.Application.STYLESHEET_MODENA;

/**
 Created by Kayler on 05/11/2016.
 */
public class ADCWindow {
	private final Stage primaryStage;
	private final VBox rootElement = new VBox();
	private Resolution resolution = new Resolution(ScreenDimension.D1600, UIScale.SMALL);
	private final CanvasView canvasView = new CanvasView(resolution);
	private final ADCMenuBar mainMenuBar = new ADCMenuBar();

	private boolean dontAddMenuBar;

	public ADCWindow(Stage primaryStage) {
		this.primaryStage = primaryStage;
		if (resolution.getScreenWidth() == ScreenDimension.D1920.width) {
			primaryStage.setFullScreen(true);
			dontAddMenuBar = true;
		}

		Scene scene = new Scene(rootElement);
		this.primaryStage.setScene(scene);
		initialize(scene);
		show();

	}

	private void initialize(Scene scene) {
		if (!dontAddMenuBar) {
			rootElement.getChildren().add(mainMenuBar);
		}
		rootElement.getChildren().addAll(canvasView);
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

	public ICanvasView getCanvasView() {
		return canvasView;
	}

	public void setToDarkTheme(boolean set) {
		String DARK_THEME = "/com/kaylerrenslow/armaDialogCreator/gui/fx/dark.css";
		if (set) {
			CanvasViewColors.EDITOR_BG = CanvasViewColors.Default.DARK_THEME_EDITOR_BG;
			CanvasViewColors.GRID = CanvasViewColors.Default.DARK_THEME_GRID;
			primaryStage.getScene().getStylesheets().add(DARK_THEME);
		} else {
			CanvasViewColors.EDITOR_BG = CanvasViewColors.Default.EDITOR_BG;
			CanvasViewColors.GRID = CanvasViewColors.Default.GRID;
			primaryStage.getScene().getStylesheets().remove(DARK_THEME);
		}
		canvasView.updateCanvas();
	}
}
