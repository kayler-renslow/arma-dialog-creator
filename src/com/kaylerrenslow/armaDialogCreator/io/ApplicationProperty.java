package com.kaylerrenslow.armaDialogCreator.io;

import javax.swing.filechooser.FileSystemView;

/**
 @author Kayler
 Keys for retrieving application properties from APPDATA/config.properties
 Created on 07/12/2016.
 */
public enum ApplicationProperty {
	/** Location path to folder where application save data should be stored. */
	APP_SAVE_DATA_DIR("app_save_data_dir", ApplicationPropertyManager.getPathSafe(FileSystemView.getFileSystemView().getDefaultDirectory()) + "/" + ApplicationPropertyManager.SAVE_LOCATION_FILE_NAME),
	/** Directory for where Arma 3 tools is. Can be empty (not set) */
	A3_TOOLS_DIR("a3_tools_dir", "null");

	/** Property key name */
	final String propertyKey;

	/** Default value */
	final String defaultValue;

	ApplicationProperty(String propertyKey, String defaultValue) {
		this.propertyKey = propertyKey;
		this.defaultValue = defaultValue;
	}
}
