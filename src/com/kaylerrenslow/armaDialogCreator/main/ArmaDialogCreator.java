package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCWindow;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.SelectSaveLocationPopup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.io.ApplicationDataManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 @author Kayler
 Created on 05/11/2016. */
public class ArmaDialogCreator extends Application {
	private static ArmaDialogCreator INSTANCE;

	public static void main(String[] args) {
		ArmaDialogCreator.launch(args);
	}

	private Stage primaryStage;
	private ADCWindow mainWindow;
	private ApplicationDataManager saveManager = new ApplicationDataManager();

	@Override
	public void start(Stage primaryStage) throws Exception {
		ArmaDialogCreator.INSTANCE = this;
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(new ArmaDialogCreatorWindowCloseEvent());
		primaryStage.getIcons().add(new Image(ImagePaths.ICON_APP));
		primaryStage.setTitle(Lang.Application.APPLICATION_TITLE);
		loadWindow();
	}

	private void loadWindow() {
		mainWindow = new ADCWindow(primaryStage);
		if (!saveManager.appSaveDataDirectorySet()) {
			showNewSaveLocationsPopup();
		}
	}

	public static void showNewSaveLocationsPopup() {
		SelectSaveLocationPopup p = new SelectSaveLocationPopup(INSTANCE.primaryStage, INSTANCE.saveManager.getAppSaveDataDirectory(), INSTANCE.saveManager.getArma3ToolsDirectory());
		p.show();
	}

	public static CanvasView getCanvasView() {
		return INSTANCE.mainWindow.getCanvasView();
	}

	public static Stage getPrimaryStage() {
		return INSTANCE.primaryStage;
	}

	public static ADCWindow getWindow() {
		return INSTANCE.mainWindow;
	}

	public static ApplicationDataManager getSaveDataManager() {
		return INSTANCE.saveManager;
	}

	public static ApplicationData getApplicationData() {
		return INSTANCE.saveManager.applicationData;
	}

	/** Make an error window popup with the stack trace printed. Only use this for when the error is recoverable. If the error is non-recoverable, use ArmaDialogCreator.fatal() */
	public static void error(Exception e) {
		new StagePopup<>(INSTANCE.primaryStage, getExceptionTextArea(e), "An internal error occurred.").show();
	}

	/** Makes an error window popup with the stack trace printed. This method should be used when a non-recoverable error occurred. After the error window is closed, the application will also close. */
	public static void fatal(Exception e) {
		if (INSTANCE.saveManager == null || !INSTANCE.primaryStage.isShowing()) { //can be null if this method is called when ApplicationDataManager had an error before constructor finished
			JOptionPane.showMessageDialog(null, getExceptionString(e), "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		StagePopup sp = new StagePopup<TextArea>(INSTANCE.primaryStage, getExceptionTextArea(e), "A FATAL error occurred.") {
			@Override
			protected void onCloseRequest(WindowEvent event) {
				boolean good = INSTANCE.saveManager.forceSave();
				new StagePopup<TextArea>(INSTANCE.primaryStage, new TextArea(good ? "Your entry was successfully saved regardless of the error." : "Your entry couldn't be saved."), "Save notification") {
					@Override
					protected void onCloseRequest(WindowEvent event) {
						INSTANCE.primaryStage.close();
						super.onCloseRequest(event);
					}

					@Override
					public void show() {
						myStage.initModality(Modality.APPLICATION_MODAL);
						super.show();
					}
				};

			}
		};
		sp.show();
	}

	@NotNull
	private static TextArea getExceptionTextArea(Exception e) {
		String err = getExceptionString(e);
		TextArea ta = new TextArea();
		ta.setText(err);
		return ta;
	}

	private static String getExceptionString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return "An error occurred. Please report this message to the developer(s).\n" + sw.toString();
	}

	private static class ArmaDialogCreatorWindowCloseEvent implements EventHandler<WindowEvent>{

		@Override
		public void handle(WindowEvent event) {
			ArmaDialogCreator.INSTANCE.saveManager.forceSave();
		}
	}
}
