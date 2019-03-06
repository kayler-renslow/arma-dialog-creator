package com.armadialogcreator;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.gui.main.ADCMainWindow;
import com.armadialogcreator.gui.main.ADCWindow;
import com.armadialogcreator.gui.main.AskSaveProjectDialog;
import com.armadialogcreator.img.icons.ADCIcons;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.ADCExecutors;
import com.armadialogcreator.util.ApplicationSingleton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.jar.Manifest;

/**
 Contains main method for running Arma Dialog Creator

 @author Kayler
 @since 05/11/2016. */
public final class ArmaDialogCreator extends Application implements ApplicationStateSubscriber {

	private static ArmaDialogCreator INSTANCE;
	private static Thread JavaFXThread;

	/**
	 Launches the Arma Dialog Creator. Only one instance is allowed to be opened at a time per Java process.
	 */
	public static void main(String[] args) {
		if (INSTANCE != null) {
			getPrimaryStage().requestFocus();
			return;
		}
		try {
			Class c = Class.forName("javafx.application.Application");
		} catch (ClassNotFoundException e) {
			SwingUtilities.invokeLater(() -> {
				ResourceBundle b = Lang.ApplicationBundle();
				JOptionPane.showMessageDialog(null, b.getString("no_javafx"), b.getString("no_javafx_short"), JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			});
			return;
		}

		//This is to ensure that numbers use periods instead of commas for decimals and use commas for thousands place.
		//Do not move out of main method to ensure that all static objects that may use the Locale will initialize with this Locale
		Locale.setDefault(Locale.Category.FORMAT, Locale.US);

		ExceptionHandler.init();

		//load application singleton classes
		try {
			String[] classes = {
					"com.armadialogcreator.data.ApplicationStateChangeLogger",
					"com.armadialogcreator.data.ConfigClassRegistry",
					"com.armadialogcreator.data.EditorManager",
					"com.armadialogcreator.data.ExpressionEnvManager",
					"com.armadialogcreator.data.FileDependencyRegistry",
					"com.armadialogcreator.data.MacroRegistry",
					"com.armadialogcreator.data.StringTableManager",
					"com.armadialogcreator.data.SettingsManager",
					"com.armadialogcreator.data.ApplicationProperties",
			};

			ClassLoader loader = ArmaDialogCreator.class.getClassLoader();
			Class<ApplicationSingleton> applicationSingletonAnnotation = (Class<ApplicationSingleton>) loader.loadClass("com.armadialogcreator.util.ApplicationSingleton");

			for (String clazz : classes) {
				try {
					Class<?> aClass = loader.loadClass(clazz);
					ApplicationSingleton annotation = aClass.getAnnotation(applicationSingletonAnnotation);
					aClass.getField(annotation.field()).get(aClass); //this is how we invoke static block / initialize the class
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		launch(args);
	}

	private Stage primaryStage;
	private ADCWindow mainWindow;

	public ArmaDialogCreator() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Should not create a new ArmaDialogCreator instance when one already exists");
		}
		INSTANCE = this;
		ApplicationManager.instance.addStateSubscriber(this);
	}

	@NotNull
	public static ArmaDialogCreator getInstance() {
		return INSTANCE;
	}

	@Override
	public void init() throws Exception {
		ApplicationManager.instance.initializeADC();

		Thread t = new Thread(() -> {
			int progress = 0;
			if (!containsUnnamedLaunchParameter(ProgramArgument.NoSplash)) {
				for (; progress < 100; progress++) {
					try {
						Thread.sleep(40);
					} catch (InterruptedException ignore) {

					}
					notifyPreloaderLog(new Preloader.ProgressNotification(progress / 100.0));
				}
			}
		});

		t.start();
		t.join();
	}

	private void notifyPreloaderLog(Preloader.PreloaderNotification notification) {
		if (containsUnnamedLaunchParameter(ProgramArgument.LogInitProgress)) {
			if (notification instanceof Preloader.ProgressNotification) {
				System.out.println("Preloader Log Progress: " + ((Preloader.ProgressNotification) notification).getProgress());
			} else if (notification instanceof Preloader.StateChangeNotification) {
				System.out.println("Preloader Stage Change: " + ((Preloader.StateChangeNotification) notification).getType());
			} else if (notification instanceof Preloader.ErrorNotification) {
				System.out.println("Preloader Error: " + notification);
			}
		}
		notifyPreloader(notification);
	}

	@Override
	public void stop() throws Exception {
		ADCExecutors.terminateAll();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		JavaFXThread = Thread.currentThread();

		//load this stuff first
		this.primaryStage = primaryStage;
		Thread.currentThread().setName("Arma Dialog Creator JavaFX Thread");
		primaryStage.setOnCloseRequest(new ArmaDialogCreatorWindowCloseEvent());
		primaryStage.getIcons().add(ADCIcons.ICON_ADC);
		primaryStage.setTitle(Lang.Application.APPLICATION_TITLE);

		//load main window
		mainWindow = new ADCWindow(primaryStage);
		ADCGuiManager.instance.guiReady();
		ApplicationProjectSwitcher.instance.firstProjectSelection();
	}

	@NotNull
	public static ADCMainWindow getMainWindow() {
		return INSTANCE.mainWindow;
	}

	@NotNull
	public static Manifest getManifest() {
		Exception e = null;
		try {
			Enumeration<URL> resources = ArmaDialogCreator.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
			while (resources.hasMoreElements()) {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				String specTitle = manifest.getMainAttributes().getValue("Specification-Title");
				if (specTitle != null && specTitle.equals("Arma Dialog Creator")) {
					return manifest;
				}
			}
		} catch (IOException ioe) {
			e = ioe;
		}
		throw new RuntimeException(e);
	}

	@NotNull
	public static Stage getPrimaryStage() {
		return INSTANCE.primaryStage;
	}

	@NotNull
	public static Parameters getLaunchParameters() {
		return INSTANCE.getParameters();
	}

	public static boolean containsUnnamedLaunchParameter(@NotNull ProgramArgument argument) {
		return getLaunchParameters().getUnnamed().contains(argument.getArgKey());
	}

	/** @return the JavaFX thread */
	@NotNull
	public static Thread getJavaFXThread() {
		return JavaFXThread;
	}

	/**
	 Do not call this method directly.

	 @see ApplicationManager#closeApplication()
	 */
	@Override
	public void ADCExit() {
		Platform.exit();
	}

	private static class ArmaDialogCreatorWindowCloseEvent implements EventHandler<WindowEvent> {

		@Override
		public void handle(WindowEvent event) {
			/*we want to keep the Arma Dialog Creator window still open when asking to save progress before exiting.
			Consuming the event will keep window open and then we call closeApplication to execute the closing procedure and in turn, close the window*/
			event.consume();
			AskSaveProjectDialog dialog = new AskSaveProjectDialog();
			dialog.show();
			if (dialog.saveProgress()) {
				ApplicationManager.instance.saveProject();
			}
			ApplicationManager.instance.closeApplication();
		}
	}
}
