package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 @author Kayler
 @since 12/12/2016 */
public interface StringTableValue {

	@NotNull
	StringTableKey getKey();

	@NotNull
	Map<Language, String> getValues();

}
