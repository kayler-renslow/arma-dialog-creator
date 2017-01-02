package com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

/**
 @author Kayler
 @since 12/12/2016 */
public class StringTableImpl implements StringTable {
	private File file;
	private ObservableList<StringTableKey> keys;
	private String projectName;

	public StringTableImpl(@NotNull File file, @NotNull ObservableList<StringTableKey> keys, @NotNull String projectName) {
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
	public ObservableList<StringTableKey> getKeys() {
		return keys;
	}

	@Override
	public void setKeys(@NotNull ObservableList<StringTableKey> keys) {
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
		ObservableList<StringTableKey> keys = FXCollections.observableList(new ArrayList<>(this.keys.size()));
		for (StringTableKey key : this.keys) {
			keys.add(key.deepCopy());
		}
		return new StringTableImpl(file, keys, projectName);
	}
}
