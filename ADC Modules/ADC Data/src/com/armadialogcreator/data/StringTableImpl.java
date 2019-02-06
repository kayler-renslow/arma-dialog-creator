package com.armadialogcreator.data;

import com.armadialogcreator.core.stringtable.StringTable;
import com.armadialogcreator.core.stringtable.StringTableKey;
import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

/**
 @author Kayler
 @since 12/12/2016 */
class StringTableImpl implements StringTable {
	private File file;
	private ListObserver<StringTableKey> keys;
	private String projectName;

	public StringTableImpl(@NotNull File file, @NotNull ListObserver<StringTableKey> keys, @NotNull String projectName) {
		this.file = file;
		this.keys = keys;
		this.projectName = projectName;
	}

	@Override
	@NotNull
	public File getFile() {
		return file;
	}

	public void setFile(@NotNull File file) {
		this.file = file;
	}

	@NotNull
	@Override
	public ListObserver<StringTableKey> getKeys() {
		return keys;
	}

	@Override
	public void setKeys(@NotNull ListObserver<StringTableKey> keys) {
		this.keys = keys;
	}

	@NotNull
	@Override
	public String getStringTableProjectName() {
		return projectName;
	}

	@Override
	@NotNull
	public StringTable deepCopy() {
		ListObserver<StringTableKey> keys = new ListObserver<>(new ArrayList<>(this.keys.size()));
		for (StringTableKey key : this.keys) {
			keys.add(key.deepCopy());
		}
		return new StringTableImpl(file, keys, projectName);
	}
}
