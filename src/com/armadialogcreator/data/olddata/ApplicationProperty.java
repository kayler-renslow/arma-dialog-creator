package com.armadialogcreator.data.olddata;

import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.util.Key;

import java.io.File;
import java.util.Locale;

/**
 Keys for retrieving application properties from APPDATA/config.xml

 @author Kayler
 @since 07/12/2016. */
public class ApplicationProperty<T> extends Key<T> {
	/** File of last used workspace. */
	public static final ApplicationProperty<File> LAST_WORKSPACE = new ApplicationProperty<>(
			"last_workspace_folder", Workspace.DEFAULT_WORKSPACE_DIRECTORY
	);
	/** Directory for where Arma 3 tools is. Can be empty (not set) */
	public static final ApplicationProperty<File> A3_TOOLS_DIR = new ApplicationProperty<>("a3_tools_dir", null);
	public static final ApplicationProperty<Boolean> DARK_THEME = new ApplicationProperty<>("dark_theme", false);

	public static final ApplicationProperty<Locale> LOCALE = new ApplicationProperty<>("locale", Locale.US);

	private static final ApplicationProperty[] values = {LAST_WORKSPACE, A3_TOOLS_DIR, DARK_THEME, LOCALE};


	private ApplicationProperty(String propertyKey, T defaultValue) {
		super(propertyKey, defaultValue);
	}

	public static ApplicationProperty[] values() {
		return values;
	}

	public T getValue() {
		return null;
		//		return this.get(ApplicationDataManager.getApplicationProperties());
	}
}
