package com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 12/12/2016 */
public class StringTableValueImpl implements StringTableValue {

	private final ObservableMap<Language, String> values;

	public StringTableValueImpl(@NotNull ObservableMap<Language, String> values) {
		this.values = values;
	}


	@Override
	@NotNull
	public StringTableValue deepCopy() {
		StringTableValue copy = new StringTableValueImpl(FXCollections.emptyObservableMap());
		copy.getLanguageTokenMap().putAll(this.getLanguageTokenMap());
		return copy;
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
