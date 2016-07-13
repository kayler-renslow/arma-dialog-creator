package com.kaylerrenslow.armaDialogCreator.io;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.SelectSaveLocationPopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 @author Kayler
 Manages application property values from APPDATA/config.properties<br>
 The only instance is inside {@link ApplicationPropertyManager}
 Created on 07/12/2016. */
class ApplicationPropertyManager {
	static final String SAVE_LOCATION_FILE_NAME = "Arma Dialog Creator";
	/** Appdata folder */
	private final File appdataFolder = new File(System.getenv("APPDATA") + "/" + SAVE_LOCATION_FILE_NAME);
	/** Properties instance that holds all application properties */
	private final Properties applicationProperties = new Properties();

	private final File appPropertiesFile = new File(appdataFolder.getPath() + "/" + "config.properties");

	/** Location for application save data. This is where all projects and their data are saved to. */
	private File appSaveDataDir,
	/** Location of Arma 3 tools. Arma 3 tools has some executables valuable to Arma Dialog Creator, such as .paa converter */
	a3ToolsDir;

	/**
	 Loads the AppData properties file and stores properties in application properties. Each application property is retrievable via {@link #getApplicationProperty(ApplicationProperty)}
	 */
	public ApplicationPropertyManager() {
		if (!appdataFolder.exists()) {
			setupAppdataFolder();
		} else {
			if (appPropertiesFile.exists()) {
				loadApplicationProperties();
			} else {
				createApplicationPropertiesFile();
			}
		}


		//now verify that the loaded a3Tools directory and appdata save directory are actual files that exist and are directories
		File f = new File(getApplicationProperty(ApplicationProperty.A3_TOOLS_DIR));
		if (f.exists() && f.isDirectory()) {
			a3ToolsDir = f;
		} else if (!getApplicationProperty(ApplicationProperty.A3_TOOLS_DIR).equals("null")) {
			//todo notify user that the previously set a3 tools directory is now invalid
		}

		appSaveDataDir = new File(getApplicationProperty(ApplicationProperty.APP_SAVE_DATA_DIR));
		if (!appSaveDataDir.exists()) {
			ArmaDialogCreator.showAfterMainWindowLoaded(new SelectSaveLocationPopup(null, a3ToolsDir));
		} else if (!appSaveDataDir.isDirectory()) {
			ExceptionHandler.fatal(new IllegalStateException("appSaveDataDir exists and is not a directory"));
		}
	}

	private void loadApplicationProperties() {
		try {
			applicationProperties.load(new FileInputStream(appPropertiesFile));
		} catch (IOException e) {
			ExceptionHandler.fatal(e);
		}
		for (ApplicationProperty p : ApplicationProperty.values()) {
			if (applicationProperties.containsKey(p.propertyKey)) {
				continue;
			}
			applicationProperties.put(p.propertyKey, p.defaultValue);
		}
	}

	/** Creates the appdata folder and creates the properties file and with all properties set to their default values */
	private void setupAppdataFolder() {
		try {
			appdataFolder.mkdir();
		} catch (SecurityException e) {
			ExceptionHandler.fatal(e);
			return;
		}
		createApplicationPropertiesFile();
	}

	private void createApplicationPropertiesFile() {
		try {
			appPropertiesFile.createNewFile();
		} catch (IOException e) {
			ExceptionHandler.fatal(e);
			return;
		}
		for (ApplicationProperty p : ApplicationProperty.values()) {
			applicationProperties.put(p.propertyKey, p.defaultValue);
		}
	}

	void saveApplicationProperties() throws IOException {
		PrintWriter pw = new PrintWriter(appPropertiesFile);
		for (ApplicationProperty p : ApplicationProperty.values()) {
			pw.println(p.propertyKey + "=" + applicationProperties.get(p.propertyKey));
		}
		pw.flush();
		pw.close();
	}

	/** Get where application save files should be saved to. */
	@NotNull
	public File getAppSaveDataDirectory() {
		return appSaveDataDir;
	}

	/** Get the directory for where Arma 3 tools is saved. If the directory hasn't been set or doesn't exist or the file that is set isn't a directory, will return null. */
	@Nullable
	public File getArma3ToolsDirectory() {
		return a3ToolsDir;
	}

	/** Set the application save data directory to a new one. Automatically updates application properties. */
	public void setAppSaveDataLocation(@NotNull File saveLocation) {
		if (!saveLocation.exists()) {
			throw new IllegalStateException("Save location should exist");
		}
		this.appSaveDataDir = saveLocation;
		setApplicationProperty(ApplicationProperty.APP_SAVE_DATA_DIR, getPathSafe(saveLocation));
	}

	/** Set the arma 3 tools directory to a new one (can be null). Automatically updates application properties. */
	public void setArma3ToolsLocation(@Nullable File file) {
		this.a3ToolsDir = file;
		if (file == null) {
			setApplicationProperty(ApplicationProperty.A3_TOOLS_DIR, "null");
		} else {
			setApplicationProperty(ApplicationProperty.A3_TOOLS_DIR, getPathSafe(file));
		}
	}

	/** Gets the application property, or null if doesn't exist */
	@NotNull
	public String getApplicationProperty(@NotNull ApplicationProperty p) {
		return (String) applicationProperties.get(p.propertyKey);
	}

	public void setApplicationProperty(@NotNull ApplicationProperty p, @NotNull String value) {
		applicationProperties.put(p.propertyKey, value);
	}


	/**
	 Return path as a String that is safe to save in .properties file
	 */
	@NotNull
	static String getPathSafe(@NotNull File saveLocation) {
		return saveLocation.getPath().replaceAll("\\\\", "\\\\\\\\"); //convert single backslash into double backslash
	}
}
