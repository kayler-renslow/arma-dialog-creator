package com.armadialogcreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 12/12/2016 */
public interface Language {
	/** Get name of language */
	@NotNull
	String getName();

	default boolean equalsLanguage(Language l) {
		return l != null && getName().equals(l.getName());
	}

}
