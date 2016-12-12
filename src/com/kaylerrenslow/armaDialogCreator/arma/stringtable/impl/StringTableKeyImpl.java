package com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 @author Kayler
 @since 12/12/2016 */
public class StringTableKeyImpl implements StringTableKey {
	private final String key;
	private String packageName;
	private String containerName;
	private final StringTableValue value;

	public StringTableKeyImpl(@NotNull String key, @NotNull Map<Language, String> values) {
		this(key, null, null, values);
	}

	public StringTableKeyImpl(@NotNull String key, @Nullable String packageName, @Nullable String containerName, @NotNull Map<Language, String> values) {
		this.key = key;
		this.packageName = packageName != null && packageName.trim().length() == 0 ? null : packageName;
		this.containerName = containerName != null && containerName.trim().length() == 0 ? null : containerName;
		this.value = new StringTableValueImpl(this, values);
	}

	@NotNull
	@Override
	public String getId() {
		return key;
	}

	@NotNull
	@Override
	public StringTableValue getValue() {
		return value;
	}

	@Override
	@Nullable
	public String getPackageName() {
		return packageName;
	}

	@Override
	@Nullable
	public String getContainerName() {
		return containerName;
	}
}
