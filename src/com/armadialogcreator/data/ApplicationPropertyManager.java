package com.armadialogcreator.data;

import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.data.xml.ApplicationPropertyXmlLoader;
import com.armadialogcreator.data.xml.XmlParseException;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.XmlWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.File;
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
			return System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support";
		} else if (OS.contains("NUX")) {
			return System.getProperty("user.home");
		}
		return System.getProperty("user.dir");
	}

	/**
	 Appdata folder
	 */
	private final File appdataFolder = new File(defaultDirectory() + File.separator + "Arma Dialog Creator");
	/**
	 DataContext holds all application properties
	 */
	private DataContext applicationProperties = new DataContext();

	private final File appPropertiesFile = new File(appdataFolder.getPath() + File.separator + "config.xml");

	/** Location of Arma 3 tools. Arma 3 tools has some executables valuable to Arma Dialog Creator, such as .paa converter */
	private File a3ToolsDir;

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

	void saveApplicationProperties() throws TransformerException {
		XmlWriter writer = new XmlWriter(appPropertiesFile, "config");

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
			Element applicationPropertyEle = writer.appendElementToRoot("application-property");
			applicationPropertyEle.setAttribute("name", p.getName());
			writer.appendTextNode(value, applicationPropertyEle);
		}

		writer.writeToFile(-1);
	}


	/**
	 Get the directory for where Arma 3 tools is saved. If the directory hasn't been set or doesn't exist or the file that is set isn't a directory, will return null.
	 */
	@Nullable
	public File getArma3ToolsDirectory() {
		return a3ToolsDir;
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
