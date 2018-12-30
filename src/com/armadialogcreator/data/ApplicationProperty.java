package com.armadialogcreator.data;

import com.armadialogcreator.main.Lang;
import com.armadialogcreator.util.CustomToString;
import com.armadialogcreator.util.Key;
import com.armadialogcreator.util.ValueConverter;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Locale;

/**
 Keys for retrieving application properties from APPDATA/config.xml

 @author Kayler
 @since 07/12/2016. */
public class ApplicationProperty<T> extends Key<T> {
	/** File of last used workspace. */
	public static final ApplicationProperty<File> LAST_WORKSPACE = new ApplicationProperty<>(
			"last_workspace_folder", Workspace.DEFAULT_WORKSPACE_DIRECTORY, FileConverter.INSTANCE
	);
	/** Directory for where Arma 3 tools is. Can be empty (not set) */
	public static final ApplicationProperty<File> A3_TOOLS_DIR = new ApplicationProperty<>("a3_tools_dir", (File) null, FileConverter.INSTANCE);
	public static final ApplicationProperty<Boolean> DARK_THEME = new ApplicationProperty<>("dark_theme", false, BooleanConverter.INSTANCE);

	public static final ApplicationProperty<Locale> LOCALE = new ApplicationProperty<>("locale", Locale.US, Lang.LOCALE_CONVERTER, Lang.LOCALE_CONVERTER);

	private static final ApplicationProperty[] values = {LAST_WORKSPACE, A3_TOOLS_DIR, DARK_THEME, LOCALE};


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

	public T getValue() {
		return this.get(ApplicationDataManager.getApplicationProperties());
	}
}
