package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;

/**
 All known languages in Arma 3

 @author Kayler
 @since 12/12/2016 */
public class KnownLanguage implements Language {
	public static final KnownLanguage Original = new KnownLanguage("Original");
	public static final KnownLanguage English = new KnownLanguage("English");
	public static final KnownLanguage Czech = new KnownLanguage("Czech");
	public static final KnownLanguage French = new KnownLanguage("French");
	public static final KnownLanguage Spanish = new KnownLanguage("Spanish");
	public static final KnownLanguage Italian = new KnownLanguage("Italian");
	public static final KnownLanguage Polish = new KnownLanguage("Polish");
	public static final KnownLanguage Portuguese = new KnownLanguage("Portuguese");
	public static final KnownLanguage Russian = new KnownLanguage("Russian");
	public static final KnownLanguage German = new KnownLanguage("German");
	public static final KnownLanguage Korean = new KnownLanguage("Korean");
	public static final KnownLanguage Japanese = new KnownLanguage("Japanese");

	private static final KnownLanguage[] values = {Original, English, Czech, French, Spanish, Italian, Polish, Portuguese, Russian, German, Korean, Japanese};

	private String name;

	private KnownLanguage(String name) {
		this.name = name;
	}

	@Override
	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public static KnownLanguage[] values() {
		return values;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof Language) {
			Language other = (Language) o;
			return equalsLanguage(other);
		}
		return false;
	}

	@NotNull
	public static Language valueOf(@NotNull String langName) {
		for (KnownLanguage language : values) {
			if (language.getName().equals(langName)) {
				return language;
			}
		}
		throw new IllegalArgumentException("no name matched for " + langName);
	}

	@Override
	public String toString() {
		return name;
	}
}
