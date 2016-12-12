package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 12/12/2016 */
public class CustomLanguage implements Language {
	private String name;

	public CustomLanguage(@NotNull String name) {
		this.name = name;
	}

	@NotNull
	public String getName() {
		return name;
	}
}
