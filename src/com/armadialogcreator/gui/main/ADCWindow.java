package com.armadialogcreator.gui.main;

import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.data.ImagesTool;
import com.armadialogcreator.gui.FXUtil;
import com.armadialogcreator.gui.main.popup.ConvertingImageSubscriberDialog;
import com.armadialogcreator.gui.main.popup.ConvertingImageSubscriberNotificationCreator;
import com.armadialogcreator.gui.styles.ADCStyleSheets;
import com.armadialogcreator.util.ScreenDimension;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 The main window of Arma Dialog Creator

 @author Kayler
 @since 05/11/2016 */
public class ADCWindow implements ADCMainWindow {
	private final Stage stage;
	private VBox rootElement = new VBox();
	private ADCCanvasView canvasView;
	private ADCMenuBar mainMenuBar;
	private boolean fullscreen = false;
	private boolean preInit = false;
	private final List<Runnable> runWhenShowing = new ArrayList<>();
	private volatile boolean initialized = false;

	public ADCWindow(Stage stage) {
		this.stage = stage;
		stage.fullScreenProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				setToFullScreen(newValue);
			}
		});
		stage.widthProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (preInit) {
					return;
				}
				autoResizeCanvasView();
			}
		});
		Scene scene = new Scene(rootElement);
		scene.getStylesheets().add(ADCStyleSheets.getStylesheet("misc.css"));
		this.stage.setScene(scene);

		ImagesTool.subscribeToConversion(new ConvertingImageSubscriberDialog());
		ImagesTool.subscribeToConversion(new ConvertingImageSubscriberNotificationCreator());
	}

	@Override
	public void initialize() {
		preInit = true;
		rootElement = new VBox();
		rootElement.setPadding(new Insets(10));
		stage.getScene().setRoot(rootElement);
		StackPane stackPane = new StackPane();
		rootElement.getChildren().add(stackPane);

		ProgressBar bar = new ProgressBar(-1);
		stackPane.getChildren().add(bar);

		stage.setWidth(ScreenDimension.D1024.width + CanvasControls.PREFERRED_WIDTH);
		stage.setHeight(ScreenDimension.D1024.height + 100);

		preInit = false;
		rootElement = new VBox();
		Scene scene = stage.getScene();
		scene.setRoot(rootElement);

		FXUtil.runWhenVisible(rootElement, new Runnable() {
			@Override
			public void run() {
				canvasView = new ADCCanvasView();
				mainMenuBar = new ADCMenuBar();

				rootElement.getChildren().addAll(mainMenuBar, canvasView);

				//force canvas to render at proper size
				autoResizeCanvasView();
				canvasView.updateCanvas();

				initialized = true;

				for (Runnable r : runWhenShowing) {
					r.run();
				}
				runWhenShowing.clear();

			}
		});


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

	@Override
	public void show() {
		this.stage.show();
		autoResizeCanvasView();
		if (initialized) {
			for (Runnable r : runWhenShowing) {
				r.run();
			}
			runWhenShowing.clear();
		}
	}

	@Override
	public void runWhenReady(@NotNull Runnable r) {
		if (initialized) {
			r.run();
		} else {
			runWhenShowing.add(r);
		}
	}

	private void autoResizeCanvasView() {
		ScreenDimension closest = ScreenDimension.SMALLEST;
		for (ScreenDimension dimension : ScreenDimension.values()) {
			if (stage.getWidth() - (fullscreen ? 0 : CanvasControls.PREFERRED_WIDTH) >= dimension.width) {
				closest = dimension;
			}
		}
		ArmaResolution resolution = EditorManager.instance.getResolution();
		resolution.setScreenDimension(closest);
	}

	@Override
	@NotNull
	public CanvasView getCanvasView() {
		if (canvasView == null) {
			//attempting to access canvasView before initialized
			throw new IllegalStateException();
		}
		return canvasView;
	}

	@Override
	public void setToFullScreen(boolean fullScreen) {
		stage.setFullScreen(fullScreen);
		this.fullscreen = fullScreen;
		if (fullScreen) {
			rootElement.getChildren().remove(mainMenuBar);
		} else {
			rootElement.getChildren().add(0, mainMenuBar);
		}
		canvasView.hideCanvasControls(fullScreen);
		autoResizeCanvasView();
	}

	@Override
	public boolean isShowing() {
		return stage.isShowing();
	}

	@Override
	public void hide() {
		stage.hide();
	}

	@NotNull
	public Stage getStage() {
		return stage;
	}
}
