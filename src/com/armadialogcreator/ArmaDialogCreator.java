package com.armadialogcreator;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
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
					"com.armadialogcreator.ApplicationStateChangeLogger",
					"com.armadialogcreator.ConfigClassRegistry",
					"com.armadialogcreator.ControlClassRegistry",
					"com.armadialogcreator.EditorManager",
					"com.armadialogcreator.ExpressionEnvManager",
					"com.armadialogcreator.FileDependencyRegistry",
					"com.armadialogcreator.MacroRegistry",
					"com.armadialogcreator.StringTableManager",
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
		ApplicationManager.getInstance().addStateSubscriber(this);
	}

	@Override
	public void init() throws Exception {
		ApplicationManager.getInstance().initializeApplication();

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

	//	public static void loadNewProject() {
	//		loadNewProject(true);
	//	}
	//
	//	private static void loadNewProject(boolean askToSave) {
	//		if (askToSave) {
	//			if (!ApplicationDataManager.getInstance().askSaveAll()) {
	//				return;
	//			}
	//		}
	//
	//		ADCWindow adcWindow = getADCWindow();
	//		adcWindow.getStage().close();
	//
	//
	//		ApplicationDataManager.getInstance().beginInitializing();
	//		ApplicationLoader.ApplicationLoadConfig config = ApplicationLoader.getInstance().getNewLoadConfig();
	//
	//		adcWindow.preInit();
	//		adcWindow.getStage().show();
	//
	//		Task<Boolean> task = new Task<Boolean>() {
	//			@Override
	//			protected Boolean call() throws Exception {
	//				ApplicationData applicationData = getApplicationDataManager().initializeApplicationData();
	//
	//				ProjectXmlReader.ProjectParseResult result = null;
	//				boolean newProject = false;
	//				final List<ParseError> parseErrors = new ArrayList<>();
	//
	//				if (config.getLoadType() == ApplicationLoader.LoadType.LOAD) {
	//					try {
	//						result = ProjectXmlReader.parseProjectXmlFile(config.getProjectDescriptor(), applicationData);
	//						parseErrors.addAll(result.getErrors());
	//					} catch (Exception e) {
	//						newProject = true;
	//						INSTANCE.showLater.add(new Runnable() {
	//							@Override
	//							public void run() {
	//								new CouldNotLoadProjectDialog(e).show();
	//							}
	//						});
	//					}
	//				} else {
	//					newProject = true;
	//				}
	//				if (newProject) {
	//					Project project = new Project(applicationData, config.getProjectDescriptor());
	//					try {
	//						if (project.getWorkspaceCustomControlClassesFile().exists()) {
	//							WorkspaceCustomControlClassXmlReader loader = new WorkspaceCustomControlClassXmlReader(applicationData, null, project);
	//							loader.readDocument();
	//							parseErrors.addAll(loader.getErrors());
	//						}
	//					} catch (Exception e) {
	//						INSTANCE.showLater.add(() -> {
	//							new CouldNotLoadWorkspaceCustomControlClassesDialog(e);
	//						});
	//					}
	//				}
	//
	//				ApplicationDataManager.getInstance().initializeDone();
	//
	//				final ProjectXmlReader.ProjectParseResult finalResult = result;
	//				Platform.runLater(new Runnable() {
	//					@Override
	//					public void run() {
	//						adcWindow.initialize(finalResult != null ? finalResult.getTreeStructureBg() : null,
	//								finalResult != null ? finalResult.getTreeStructureMain() : null
	//						);
	//						adcWindow.show();
	//
	//						if (parseErrors.size() > 0) {
	//							new ProjectImproperResultDialog(parseErrors).showAndWait();
	//						}
	//
	//						for (Runnable run : INSTANCE.showLater) {
	//							run.run();
	//						}
	//						INSTANCE.showLater.clear();
	//					}
	//				});
	//
	//				return true;
	//			}
	//		};
	//		task.exceptionProperty().addListener((observable, oldValue, newValue) -> {
	//			ExceptionHandler.fatal(newValue);
	//		});
	//
	//		initializingThread = new Thread(task);
	//		initializingThread.setName("ADC - Project Initializing Thread");
	//		initializingThread.setDaemon(false);
	//		initializingThread.start();
	//	}

	@NotNull
	public static Stage getPrimaryStage() {
		return INSTANCE.primaryStage;
	}

	//	public static void setToDarkTheme(boolean set) {
	//		final String darkTheme = ADCStyleSheets.getStylesheet("dark.css");
	//		if (set) {
	//			CanvasViewColors.EDITOR_BG = CanvasViewColors.DARK_THEME_EDITOR_BG;
	//			CanvasViewColors.GRID = CanvasViewColors.DARK_THEME_GRID;
	//			INSTANCE.primaryStage.getScene().getStylesheets().add(darkTheme);
	//		} else {
	//			CanvasViewColors.EDITOR_BG = CanvasViewColors.DEFAULT_EDITOR_BG;
	//			CanvasViewColors.GRID = CanvasViewColors.DEFAULT_GRID;
	//			INSTANCE.primaryStage.getScene().getStylesheets().remove(darkTheme);
	//		}
	//		if (getADCWindow().isShowing()) {
	//			getCanvasView().updateCanvas();
	//		}
	//		getApplicationDataManager().getApplicationProperties().put(ApplicationProperty.DARK_THEME, set);
	//		getApplicationDataManager().saveApplicationProperties();
	//	}
	//
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
	public void applicationExit() {
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
				ApplicationManager.getInstance().saveProject();
			}
			ApplicationManager.getInstance().closeApplication();
		}
	}
}
