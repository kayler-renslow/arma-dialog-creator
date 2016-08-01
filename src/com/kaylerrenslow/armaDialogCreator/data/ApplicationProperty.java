package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.data.io.BooleanConverter;
import com.kaylerrenslow.armaDialogCreator.data.io.FileConverter;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 @author Kayler
 Keys for retrieving application properties from APPDATA/config.xml
 Created on 07/12/2016. */
public class ApplicationProperty<T> extends Key<T> {
	/** Location path to folder where application save data should be stored. */
	public static final ApplicationProperty<File> APP_SAVE_DATA_DIR = new ApplicationProperty<>("app_save_data_dir", new File(FileSystemView.getFileSystemView().getDefaultDirectory() + "/" + ApplicationPropertyManager.SAVE_LOCATION_FILE_NAME), FileConverter.INSTANCE);
	/** Directory for where Arma 3 tools is. Can be empty (not set) */
	public static final ApplicationProperty<File> A3_TOOLS_DIR = new ApplicationProperty<>("a3_tools_dir", (File) null, FileConverter.INSTANCE);
	public static final ApplicationProperty<Boolean> DARK_THEME = new ApplicationProperty<>("dark_theme", false, BooleanConverter.INSTANCE);
	
	private static final ApplicationProperty[] values = {APP_SAVE_DATA_DIR, A3_TOOLS_DIR, DARK_THEME};
	
	public final ValueConverter converter;
	
	private ApplicationProperty(String propertyKey, T defaultValue, ValueConverter converter) {
		super(propertyKey, defaultValue);
		this.converter = converter;
	}
	
	public static ApplicationProperty[] values() {
		return values;
	}
}
