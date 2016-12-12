package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

/**
 @author Kayler
 @since 05/23/2016. */
public interface StringTable {
	/** @return the file that locates this {@link StringTable} */
	@NotNull
	File getFile();

	/** @return all keys */
	@NotNull
	List<StringTableKey> getKeys();

	@Nullable
	default StringTableKey getKeyById(@NotNull String id) {
		for (StringTableKey key : getKeys()) {
			if (key.getId().equals(id)) {
				return key;
			}
		}
		return null;
	}
}
