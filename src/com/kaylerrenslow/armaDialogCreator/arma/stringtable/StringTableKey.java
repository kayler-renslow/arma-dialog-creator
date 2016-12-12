package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/23/2016.
 */
public interface StringTableKey {
	/** Get key id (e.g. str_tag_key) */
	@NotNull
	String getId();

	@NotNull
	StringTableValue getValue();

	@Nullable
	String getPackageName();

	@Nullable
	String getContainerName();
}
