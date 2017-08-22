package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.util.CustomToString;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.MissingResourceException;
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

	/**
	 Get a bundle with "com.kaylerrenslow.armaDialogCreator" prepended

	 @param bundleName name of bundle (be sure to include package information if needed)
	 @return the bundle
	 @see ResourceBundle#getBundle(String, Locale)
	 */
	@NotNull
	static ResourceBundle getBundle(@NotNull String bundleName) throws MissingResourceException {
		return ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator." + bundleName, ArmaDialogCreator.getCurrentLocale());
	}

	/**
	 @return {@link #getBundle(String)} with bundleName="ApplicationBundle"
	 */
	@NotNull
	static ResourceBundle ApplicationBundle() {
		return getBundle("ApplicationBundle");
	}

	/**
	 @return {@link #getBundle(String)} with bundleName="EditChangeBundle"
	 */
	@NotNull
	static ResourceBundle EditChangeBundle() {
		return getBundle("EditChangeBundle");
	}

	/**
	 @return {@link #getBundle(String)} with bundleName="FxControlBundle"
	 */
	@NotNull
	static ResourceBundle FxControlBundle() {
		return getBundle("FxControlBundle");
	}

	/**
	 @return {@link #getBundle(String)} with bundleName="LookupBundle"
	 */
	@NotNull
	static ResourceBundle LookupBundle() {
		return getBundle("LookupBundle");
	}

	interface Application {
		String APPLICATION_NAME = "Arma Dialog Creator";
		String VERSION = "1.0.2";
		String APPLICATION_TITLE = APPLICATION_NAME + " " + VERSION;
	}

	interface Misc {
		String REPO_URL = "https://github.com/kayler-renslow/arma-dialog-creator";
	}

	static String getPopupWindowTitle(String popupName) {
		return APPLICATION_NAME + " - " + popupName;
	}

}
