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
	protected StringTableValue value;
	protected final ValueObserver<String> idObserver = new ValueObserver<>("str_no_id");
	protected final ValueObserver<String> packageNameObserver = new ValueObserver<>(null);
	protected final ValueObserver<String> containerNameObserver = new ValueObserver<>(null);

	public StringTableKeyImpl(@NotNull String id, @NotNull ObservableMap<Language, String> values) {
		this(id, null, null, values);
	}

	public StringTableKeyImpl(@NotNull String id, @Nullable String packageName, @Nullable String containerName, @NotNull ObservableMap<Language, String> values) {
		setId(id);
		initNames(packageName, containerName);
		this.value = new StringTableValueImpl(values);
	}

	protected StringTableKeyImpl(@NotNull String id, @Nullable String packageName, @Nullable String containerName, @NotNull StringTableValue value) {
		setId(id);
		this.value = value;
		initNames(packageName, containerName);
	}

	private void initNames(@Nullable String packageName, @Nullable String containerName) {
		packageNameObserver().updateValue(packageName != null && packageName.trim().length() == 0 ? null : packageName);
		containerNameObserver().updateValue(containerName != null && containerName.trim().length() == 0 ? null : containerName);
	}


	@Override
	public ValueObserver<String> idObserver() {
		return idObserver;
	}

	@NotNull
	@Override
	public StringTableValue getValue() {
		return value;
	}

	@Override
	@NotNull
	public StringTableKey deepCopy() {
		return new StringTableKeyImpl(getId(), getPackageName(), getContainerName(), this.value.deepCopy());
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
