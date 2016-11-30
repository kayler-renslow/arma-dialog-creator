package com.kaylerrenslow.armaDialogCreator.main;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 Created by Kayler on 10/16/2016.
 */
public class LocaleDescriptor {
	private Locale locale;

	public LocaleDescriptor(Locale locale) {
		this.locale = locale;
	}

	@NotNull
	public Locale getLocale() {
		return locale;
	}

	@Override
	public String toString() {
		String displayCountry = locale.getDisplayCountry(locale);
		return locale.getDisplayLanguage(locale) + (displayCountry.length() == 0 ? "" : " (" + displayCountry + ")");
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof LocaleDescriptor) {
			LocaleDescriptor that = (LocaleDescriptor) o;
			return this.locale.equals(that.locale);
		}
		return false;
	}
}
