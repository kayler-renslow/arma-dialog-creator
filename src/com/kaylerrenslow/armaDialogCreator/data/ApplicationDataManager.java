package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 @author Kayler
 Manages save data
 Created on 05/26/2016. */
public class ApplicationDataManager {
	private final ApplicationPropertyManager propertyManager = new ApplicationPropertyManager();
	public ApplicationData applicationData = new ApplicationData();
	
	
	/** Set the application save data directory to a new one. Automatically updates application properties. */
	public void setAppSaveDataLocation(@NotNull File file) {
		propertyManager.setAppSaveDataLocation(file);
	}

	/** Set the arma 3 tools directory to a new one (can be null). Automatically updates application properties. */
	public void setArma3ToolsLocation(@NotNull File file) {
		propertyManager.setArma3ToolsLocation(file);
	}

	/** Get where application save files should be saved to. */
	@NotNull
	public File getAppSaveDataDirectory() {
		return propertyManager.getAppSaveDataDirectory();
	}

	/** Get the directory for where Arma 3 tools is saved. If the directory hasn't been set or doesn't exist or the file that is set isn't a directory, will return null. */
	@Nullable
	public File getArma3ToolsDirectory() {
		return propertyManager.getArma3ToolsDirectory();
	}

	/** Gets the application property, or null if doesn't exist */
	@NotNull
	public String getApplicationProperty(@NotNull ApplicationProperty p) {
		return propertyManager.getApplicationProperty(p);
	}

	/**Sets the application property to a new value*/
	public void setApplicationProperty(@NotNull ApplicationProperty p, @NotNull String value) {
		propertyManager.setApplicationProperty(p, value);
	}


	/**
	 This should be called when the application is exiting unexpectedly and the data should be saved.

	 @return true if the data could be successfully saved, false if it couldn't
	 */
	public boolean forceSave() {
		try {
			propertyManager.saveApplicationProperties();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
		return true;
	}

}
