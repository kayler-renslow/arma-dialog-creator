package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 @author Kayler
 @since 12/12/2016 */
public interface StringTableValue {

	@NotNull
	StringTableKey getKey();


	@NotNull
	ObservableMap<Language, String> getLanguageTokenMap();

	default boolean equalsValue(StringTableValue other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		for (Map.Entry<Language, String> myEntry : getLanguageTokenMap().entrySet()) {
			boolean match = false;
			for (Map.Entry<Language, String> otherEntry : other.getLanguageTokenMap().entrySet()) {
				if (myEntry.getKey().equals(otherEntry.getKey())) {
					if (myEntry.getValue().equals(otherEntry.getValue())) {
						match = true;
						break;
					}
				}
			}
			if (!match) {
				return false;
			}
		}

		return true;
	}
}
