package com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableValue;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 12/12/2016 */
public class StringTableKeyImpl implements StringTableKey {
	private final String key;
	private final StringTableValue value;
	private final ValueObserver<String> packageNameObserver = new ValueObserver<>(null);
	private final ValueObserver<String> containerNameObserver = new ValueObserver<>(null);

	public StringTableKeyImpl(@NotNull String key, @NotNull ObservableMap<Language, String> values) {
		this(key, null, null, values);
	}

	public StringTableKeyImpl(@NotNull String key, @Nullable String packageName, @Nullable String containerName, @NotNull ObservableMap<Language, String> values) {
		this.key = key;
		packageNameObserver().updateValue(packageName != null && packageName.trim().length() == 0 ? null : packageName);
		containerNameObserver().updateValue(containerName != null && containerName.trim().length() == 0 ? null : containerName);
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

	@NotNull
	@Override
	public ValueObserver<String> packageNameObserver() {
		return packageNameObserver;
	}

	@NotNull
	@Override
	public ValueObserver<String> containerNameObserver() {
		return containerNameObserver;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof StringTableKey) {
			StringTableKey other = (StringTableKey) o;
			return equalsKey(other);
		}
		return false;
	}

	@Override
	public String toString() {
		return getId();
	}
}
