package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;

/**
 All known languages in Arma 3

 @author Kayler
 @see StringTableValue
 @since 12/12/2016 */
public enum KnownLanguage implements Language {
	Original, English, Czech, French, Spanish, Italian, Polish, Portuguese, Russian, German, Korean, Japanese;

	@Override
	@NotNull
	public String getName() {
		return name();
	}
}
