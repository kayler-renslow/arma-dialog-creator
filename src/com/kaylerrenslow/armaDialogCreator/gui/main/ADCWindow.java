package com.kaylerrenslow.armaDialogCreator.gui.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.notification.NotificationPane;
import com.kaylerrenslow.armaDialogCreator.gui.notification.Notifications;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ScreenDimension;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 The main window of Arma Dialog Creator
 @author Kayler
 @since 05/11/2016
 */
public class ADCWindow {
	private final Stage primaryStage;
	private VBox rootElement = new VBox();
	private ADCCanvasView canvasView;
	private NotificationPane notificationPane;
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
		scene.getStylesheets().add("/com/kaylerrenslow/armaDialogCreator/gui/misc.css");
		this.primaryStage.setScene(scene);
	}

	public void initialize(@Nullable TreeStructure<ArmaControl> backgroundTreeStructure, @Nullable TreeStructure<ArmaControl> mainTreeStructure) {
		rootElement = new VBox();
		primaryStage.getScene().setRoot(rootElement);
		Scene scene = primaryStage.getScene();

		FXUtil.runWhenVisible(rootElement, new Runnable() {
			@Override
			public void run() {
				initNotificationPane();
				canvasView = new ADCCanvasView(notificationPane);
				mainMenuBar = new ADCMenuBar();

				rootElement.getChildren().addAll(mainMenuBar, canvasView);

				canvasView.setTreeStructure(true, backgroundTreeStructure);
				canvasView.setTreeStructure(false, mainTreeStructure);
			}
		});


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

	private void initNotificationPane() {
		final VBox vboxNotifications = new VBox(10);
		vboxNotifications.setAlignment(Pos.BOTTOM_RIGHT);
		vboxNotifications.setPadding(new Insets(5));
		notificationPane = new NotificationPane(vboxNotifications);
		Notifications.setNotificationPane(notificationPane);
	}

	@NotNull
	public NotificationPane getNotificationPane() {
		return notificationPane;
	}

	public void show() {
		this.primaryStage.show();
		autoResizeCanvasView();
	}

	@NotNull
	public CanvasView getCanvasView() {
		if (!isShowing()) {
			throw new IllegalStateException("can't access canvas view when main window is not showing");
		}
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

	public boolean isShowing() {
		return primaryStage.isShowing();
	}
}
