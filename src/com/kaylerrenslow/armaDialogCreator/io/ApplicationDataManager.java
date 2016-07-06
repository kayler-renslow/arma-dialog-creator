package com.kaylerrenslow.armaDialogCreator.io;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 @author Kayler
 Manages save data
 Created on 05/26/2016. */
public class ApplicationDataManager {
	private File appSaveDataDir, a3ToolsDir;
	private static final String SAVE_LOCATION_FILE_NAME = "Arma Dialog Creator";
	private File appdataFolder = new File(System.getenv("APPDATA") + "/" + SAVE_LOCATION_FILE_NAME);

	private Properties applicationProperties = new Properties();
	private File appPropertiesFile = new File(makePathSafe(appdataFolder) + "/" + "config.properties");

	public final ApplicationData applicationData = new ApplicationData();

	public enum ApplicationProperty {
		/** Location path to folder where application save data should be stored. */
		APP_SAVE_DATA_DIR("app_save_data_dir", makePathSafe(FileSystemView.getFileSystemView().getDefaultDirectory()) + "/" + SAVE_LOCATION_FILE_NAME),
		/** Directory for where Arma 3 tools is. Can be empty (not set) */
		A3_TOOLS_DIR("a3_tools_dir", "null");

		final String propertyKey;
		final String defaultValue;
		ApplicationProperty(String propertyKey, String defaultValue) {
			this.propertyKey = propertyKey;
			this.defaultValue = defaultValue;
		}
	}

	public ApplicationDataManager() {
		if (!appdataFolder.exists()) {
			setupAppdataFolder();
		} else {
			loadApplicationProperties();
		}
		File f = new File(getApplicationProperty(ApplicationProperty.A3_TOOLS_DIR));
		if (f.exists() && f.isDirectory()) {
			a3ToolsDir = f;
		}

		appSaveDataDir = new File(applicationProperties.getProperty(ApplicationProperty.APP_SAVE_DATA_DIR.propertyKey));
		if (!appSaveDataDir.exists() || !appSaveDataDir.isDirectory()) {
			try {
				appSaveDataDir.mkdir();
			} catch (SecurityException e) {
				e.printStackTrace(System.out);
			}
		}
	}

	private void loadApplicationProperties() {
		if (!appPropertiesFile.exists()) {
			createApplicationPropertiesFile();
			return;
		}
		try {
			applicationProperties.load(new FileInputStream(appPropertiesFile));
		} catch (IOException e) {
			ExceptionHandler.fatal(e);
		}
		for (ApplicationProperty p : ApplicationProperty.values()) {
			if (applicationProperties.contains(p.propertyKey)) {
				continue;
			}
			applicationProperties.put(p.propertyKey, p.defaultValue);
		}
	}

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

	private void saveApplicationProperties() throws IOException {
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

	/** Return true if the application save data directory has been set, false if it should be set. */
	public boolean appSaveDataDirectorySet() {
		return appSaveDataDir.exists();
	}

	@Nullable
	/**Get the directory for where Arma 3 tools is saved. If the directory hasn't been set or doesn't exist or the file that is set isn't a directory, will return null.*/
	public File getArma3ToolsDirectory() {
		return a3ToolsDir;
	}

	/** Set the application save data directory to a new one. Automatically updates application properties. */
	public void setAppSaveDataLocation(@NotNull File saveLocation) {
		if (!saveLocation.exists()) {
			throw new IllegalStateException("Save location should exist");
		}
		this.appSaveDataDir = saveLocation;
		setApplicationProperty(ApplicationProperty.APP_SAVE_DATA_DIR, makePathSafe(saveLocation));
	}

	/** Set the arma 3 tools directory to a new one (can be null). Automatically updates application properties. */
	public void setArma3ToolsLocation(@Nullable File file) {
		this.a3ToolsDir = file;
		if (file == null) {
			setApplicationProperty(ApplicationProperty.A3_TOOLS_DIR, "null");
		} else {
			setApplicationProperty(ApplicationProperty.A3_TOOLS_DIR, makePathSafe(file));
		}
	}

	/** Gets the application property, or null if doesn't exist */
	public String getApplicationProperty(@NotNull ApplicationProperty p) {
		return (String) applicationProperties.get(p.propertyKey);
	}

	public void setApplicationProperty(@NotNull ApplicationProperty p, String value) {
		applicationProperties.put(p.propertyKey, value);
	}


	/**
	 This should be called when the application is exiting unexpectedly and the data should be saved.

	 @return true if the data could be successfully saved, false if it couldn't
	 */
	public boolean forceSave() {
		try {
			saveApplicationProperties();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return true;
	}


	@NotNull
	/**Return path as a String with all backslashes converted to forward slashes*/
	private static String makePathSafe(@NotNull File saveLocation) {
		return saveLocation.getPath().replaceAll("\\\\", "/");
	}
}
