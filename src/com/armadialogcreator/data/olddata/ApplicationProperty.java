package com.armadialogcreator.data.olddata;

import com.armadialogcreator.util.Key;

import java.io.File;

/**
 Keys for retrieving application properties from APPDATA/config.xml

 @author Kayler
 @since 07/12/2016. */
public class ApplicationProperty<T> extends Key<T> {
	/** Directory for where Arma 3 tools is. Can be empty (not set) */
	public static final ApplicationProperty<File> A3_TOOLS_DIR = new ApplicationProperty<>("a3_tools_dir", null);
	public static final ApplicationProperty<Boolean> DARK_THEME = new ApplicationProperty<>("dark_theme", false);

	private ApplicationProperty(String propertyKey, T defaultValue) {
		super(propertyKey, defaultValue);
	}

	public T getValue() {
		return null;
		//		return this.get(ApplicationDataManager.getApplicationProperties());
	}
}
