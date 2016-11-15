/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.data.io.xml.ApplicationPropertyXmlLoader;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.XmlParseException;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.SelectSaveLocationPopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 Manages application property values from APPDATA/config.xml<br>
 The only instance is inside {@link ApplicationPropertyManager}

 @author Kayler
 @since 07/12/2016. */
class ApplicationPropertyManager {
	private static String defaultDirectory() {
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN")) {
			return System.getenv("APPDATA");
		} else if (OS.contains("MAC")) {
			return System.getProperty("user.home") + "/Library/Application Support";
		} else if (OS.contains("NUX")) {
			return System.getProperty("user.home");
		}
		return System.getProperty("user.dir");
	}

	/**
	 Appdata folder
	 */
	private final File appdataFolder = new File(defaultDirectory() + "/.Arma Dialog Creator");
	/**
	 DataContext holds all application properties
	 */
	private DataContext applicationProperties = new DataContext();

	private final File appPropertiesFile = new File(appdataFolder.getPath() + "/config.xml");

	/** Location for application save data. This is where all projects and their data are saved to. */
	private File appSaveDataDir,
	/** Location of Arma 3 tools. Arma 3 tools has some executables valuable to Arma Dialog Creator, such as .paa converter */
	a3ToolsDir;

	/** Loads the AppData properties file and stores properties in application properties. */
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
		File f = ApplicationProperty.A3_TOOLS_DIR.get(applicationProperties);
		if (f == null) {
			//todo notify user that the previously set a3 tools directory is now invalid
		} else if (f.exists() && f.isDirectory()) {
			a3ToolsDir = f;
		}

		appSaveDataDir = ApplicationProperty.APP_SAVE_DATA_DIR.get(applicationProperties);
		if (appSaveDataDir == null || !appSaveDataDir.exists()) {
			ArmaDialogCreator.showAfterMainWindowLoaded(new Runnable() {
				@Override
				public void run() {
					new SelectSaveLocationPopup(null, a3ToolsDir).show();
				}
			});
		} else if (!appSaveDataDir.isDirectory()) {
			ExceptionHandler.fatal(new IllegalStateException("appSaveDataDir exists and is not a directory"));
		}
	}

	private void loadApplicationProperties() {
		try {
			ApplicationPropertyXmlLoader.ApplicationPropertyParseResult result = ApplicationPropertyXmlLoader.parse(appPropertiesFile);
			this.applicationProperties = result.getProperties();
			//			System.out.println(Arrays.toString(result.getErrors().toArray()));
		} catch (XmlParseException e) {
			ExceptionHandler.error(e);
			loadDefaultValues();
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
		loadDefaultValues();
	}

	private void loadDefaultValues() {
		for (ApplicationProperty p : ApplicationProperty.values()) {
			applicationProperties.put(p, p.getDefaultValue());
		}
	}

	void saveApplicationProperties() throws IOException {
		final String applicationProperty_f = "<application-property name='%s'>%s</application-property>";

		FileOutputStream fos = new FileOutputStream(appPropertiesFile);

		fos.write("<?xml version='1.0' encoding='UTF-8' ?>\n<config>".getBytes());
		String value;
		for (ApplicationProperty p : ApplicationProperty.values()) {
			if (applicationProperties.getValue(p) == null) {
				value = "";
			} else {
				if (p.toStringMethod == null) {
					value = applicationProperties.getValue(p).toString();
				} else {
					value = p.toStringMethod.toString(applicationProperties.getValue(p));
				}
			}
			fos.write(String.format(applicationProperty_f, p.getName(), value).getBytes());
		}
		fos.write("</config>".getBytes());
		fos.flush();
		fos.close();
	}

	/**
	 Get where application save files should be saved to.
	 */
	@NotNull
	public File getAppSaveDataDirectory() {
		return appSaveDataDir;
	}

	/**
	 Get the directory for where Arma 3 tools is saved. If the directory hasn't been set or doesn't exist or the file that is set isn't a directory, will return null.
	 */
	@Nullable
	public File getArma3ToolsDirectory() {
		return a3ToolsDir;
	}

	/**
	 Set the application save data directory to a new one. Automatically updates application properties.
	 */
	public void setAppSaveDataLocation(@NotNull File saveLocation) {
		if (!saveLocation.exists()) {
			throw new IllegalStateException("Save location should exist");
		}
		this.appSaveDataDir = saveLocation;
		applicationProperties.put(ApplicationProperty.APP_SAVE_DATA_DIR, saveLocation);
	}

	/**
	 Set the arma 3 tools directory to a new one (can be null). Automatically updates application properties.
	 */
	public void setArma3ToolsLocation(@Nullable File file) {
		this.a3ToolsDir = file;
		applicationProperties.put(ApplicationProperty.A3_TOOLS_DIR, file);
	}

	@NotNull
	public DataContext getApplicationProperties() {
		return applicationProperties;
	}
}
