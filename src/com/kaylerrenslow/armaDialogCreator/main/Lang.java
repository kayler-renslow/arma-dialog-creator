package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.util.CustomToString;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.kaylerrenslow.armaDialogCreator.main.Lang.Application.APPLICATION_NAME;

public interface Lang {

	Locale[] SUPPORTED_LOCALES = {Locale.US};

	LocaleConverter LOCALE_CONVERTER = new LocaleConverter();

	class LocaleConverter implements ValueConverter<Locale>, CustomToString<Locale> {

		@Override
		public String toString(Locale value) {
			return value.toLanguageTag();
		}

		@Override
		public Locale convert(DataContext context, @NotNull String... values) throws Exception {
			return Locale.forLanguageTag(values[0]);
		}

	}

	static ResourceBundle ApplicationBundle() {
		return ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.ApplicationBundle", ArmaDialogCreator.getCurrentLocale());
	}

	static ResourceBundle EditChangeBundle() {
		return ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.EditChangeBundle", ArmaDialogCreator.getCurrentLocale());
	}

	static ResourceBundle FxControlBundle() {
		return ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.FxControlBundle", ArmaDialogCreator.getCurrentLocale());
	}

	static ResourceBundle LookupBundle() {
		return ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.LookupBundle", ArmaDialogCreator.getCurrentLocale());
	}

	interface Application {
		String APPLICATION_NAME = "Arma Dialog Creator";
		String VERSION = "1.0.0";
		String APPLICATION_TITLE = APPLICATION_NAME + " " + VERSION;
	}

	interface Misc {
		String REPO_URL = "https://github.com/kayler-renslow/arma-dialog-creator";
	}

	static String getPopupWindowTitle(String popupName) {
		return APPLICATION_NAME + " - " + popupName;
	}

}
