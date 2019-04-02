package com.armadialogcreator.lang;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static com.armadialogcreator.lang.Lang.Application.APPLICATION_NAME;

public interface Lang {

	Locale[] SUPPORTED_LOCALES = {Locale.US};

	/**
	 Get a bundle with "com.armadialogcreator.lang." prepended

	 @param bundleName name of bundle (be sure to include package information if needed)
	 @return the bundle
	 @see ResourceBundle#getBundle(String, Locale)
	 */
	@NotNull
	static ResourceBundle getBundle(@NotNull String bundleName) throws MissingResourceException {
		return ResourceBundle.getBundle("com.armadialogcreator.lang." + bundleName, Locale.US);
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

	/** @return {@link #getBundle(String)} with bundleName="configPropertyLookupBundles." +name */
	@NotNull
	static ResourceBundle ConfigPropertyLookupBundle(@NotNull String name) {
		return getBundle("configPropertyLookupBundles." + name);
	}

	interface Application {
		String APPLICATION_NAME = "Arma Dialog Creator";
		String VERSION = "2019.1.0";
		String APPLICATION_TITLE = APPLICATION_NAME + " " + VERSION;
	}

	interface Misc {
		String REPO_URL = "https://github.com/kayler-renslow/arma-dialog-creator";
	}

	static String getPopupWindowTitle(String popupName) {
		return APPLICATION_NAME + " - " + popupName;
	}

}
