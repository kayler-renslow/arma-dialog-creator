package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionInterpreter;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCWindow;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.io.ApplicationDataManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 @author Kayler
 Created on 05/11/2016. */
public final class ArmaDialogCreator extends Application {
	private ArmaDialogCreator() {
	}

	private static ArmaDialogCreator INSTANCE;

	/**
	 Launches the Arma Dialog Creator. Only one instance is allowed to be opened at a time.
	 */
	public static void main(String[] args) {
		System.out.println(ExpressionInterpreter.getInstance().evaluate("3 * (3 * 1 + 1*3)", new Env() {
			@Override
			public @Nullable Value getValue(String identifier) {
				return new Value.NumVal(54);
			}
		}));
		if (true) {
			return;
		}
		if (INSTANCE != null) {
			getPrimaryStage().requestFocus();
			return;
		}
		ArmaDialogCreator.launch(args);
	}

	private Stage primaryStage;
	private ADCWindow mainWindow;
	private ApplicationDataManager saveManager;

	private LinkedList<StagePopup> showLater = new LinkedList<>();

	@Override
	public void start(Stage primaryStage) throws Exception {
		//load this stuff first
		ArmaDialogCreator.INSTANCE = this;
		ExceptionHandler.init();
		this.primaryStage = primaryStage;
		Thread.currentThread().setName("Arma_Dialog_Creator.MainThread");
		primaryStage.setOnCloseRequest(new ArmaDialogCreatorWindowCloseEvent());
		primaryStage.getIcons().add(new Image(ImagePaths.ICON_APP));
		primaryStage.setTitle(Lang.Application.APPLICATION_TITLE);

		//now can load save manager
		saveManager = new ApplicationDataManager();

		//load main window
		mainWindow = new ADCWindow(primaryStage);
		/*don't need iterator here since Java will make the foreach loop behave like an iterator (http://stackoverflow.com/questions/85190/how-does-the-java-for-each-loop-work)*/
		for (StagePopup aShowLater : showLater) {
			aShowLater.show();
		}
		showLater = null;
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

	/** Show the given popup after the application's main window has been initialized */
	public static void showAfterMainWindowLoaded(StagePopup<?> selectSaveLocationPopup) {
		INSTANCE.showLater.add(selectSaveLocationPopup);
	}

	private static class ArmaDialogCreatorWindowCloseEvent implements EventHandler<WindowEvent> {

		@Override
		public void handle(WindowEvent event) {
			ArmaDialogCreator.INSTANCE.saveManager.forceSave();
		}
	}
}
