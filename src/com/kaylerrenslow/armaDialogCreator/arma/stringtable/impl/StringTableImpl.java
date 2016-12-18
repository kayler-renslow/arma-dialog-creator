package com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 @since 12/12/2016 */
public class StringTableImpl implements StringTable {
	private File file;
	private List<StringTableKey> keys;

	public StringTableImpl(@NotNull File file, @NotNull List<StringTableKey> keys) {
		this.file = file;
		this.keys = keys;
	}

	@Override
	@NotNull
	public File getFile() {
		return file;
	}

	@NotNull
	@Override
	public List<StringTableKey> getKeys() {
		return keys;
	}

	@Override
	@NotNull
	public StringTable deepCopy() {
		List<StringTableKey> keys = new ArrayList<>(this.keys.size());
		for (StringTableKey key : this.keys) {
			keys.add(key.deepCopy());
		}
		return new StringTableImpl(file, keys);
	}
}
