package com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableValue;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 12/12/2016 */
public class StringTableValueImpl implements StringTableValue {
	private final StringTableKey key;
	private final ObservableMap<Language, String> values;

	public StringTableValueImpl(@NotNull StringTableKey key, @NotNull ObservableMap<Language, String> values) {
		this.key = key;
		this.values = values;
	}

	@Override
	@NotNull
	public StringTableKey getKey() {
		return key;
	}

	@NotNull
	@Override
	public ObservableMap<Language, String> getLanguageTokenMap() {
		return values;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof StringTableValue) {
			StringTableValue other = (StringTableValue) o;
			return equalsValue(other);
		}
		return false;
	}
}
