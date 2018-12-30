package com.armadialogcreator.arma.stringtable;

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

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof CustomLanguage) {
			CustomLanguage other = (CustomLanguage) o;
			return equalsLanguage(other);
		}
		return false;
	}

	@Override
	public String toString() {
		return name;
	}
}
