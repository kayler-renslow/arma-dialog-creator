package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 @author Kayler
 @since 12/12/2016 */
public interface StringTableValue {

	/**
	 Get a deep copied instance of this value. Everything is deep copied, except the {@link Language} instances in the {@link #getLanguageTokenMap()}

	 @return new instance
	 @see StringTable#deepCopy()
	 */
	@NotNull
	StringTableValue deepCopy();

	@NotNull
	ObservableMap<Language, String> getLanguageTokenMap();

	@Nullable
	default Map.Entry<Language, String> getFirstLanguageTokenEntry() {
		if (getLanguageTokenMap().entrySet().isEmpty()) {
			return null;
		}
		return getLanguageTokenMap().entrySet().iterator().next();

	}

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
