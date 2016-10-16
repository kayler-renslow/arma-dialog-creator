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

import com.kaylerrenslow.armaDialogCreator.data.io.BooleanConverter;
import com.kaylerrenslow.armaDialogCreator.data.io.FileConverter;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.CustomToString;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.Nullable;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Locale;

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

	public static final ApplicationProperty<Locale> LOCALE = new ApplicationProperty<>("locale", Locale.US, Lang.LOCALE_CONVERTER, Lang.LOCALE_CONVERTER);

	private static final ApplicationProperty[] values = {APP_SAVE_DATA_DIR, A3_TOOLS_DIR, DARK_THEME, LOCALE};


	public final ValueConverter converter;

	@Nullable
	public final CustomToString<T> toStringMethod;

	private ApplicationProperty(String propertyKey, T defaultValue, ValueConverter converter) {
		this(propertyKey, defaultValue, converter, null);
	}

	private ApplicationProperty(String propertyKey, T defaultValue, ValueConverter converter, @Nullable CustomToString<T> toStringMethod) {
		super(propertyKey, defaultValue);
		this.converter = converter;
		this.toStringMethod = toStringMethod;
	}

	public static ApplicationProperty[] values() {
		return values;
	}
}
