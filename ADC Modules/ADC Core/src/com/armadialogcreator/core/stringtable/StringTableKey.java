package com.armadialogcreator.core.stringtable;

import com.armadialogcreator.core.Macro;
import com.armadialogcreator.util.MapObserver;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 @author Kayler
 @see StringTable
 @since 05/23/2016. */
public interface StringTableKey extends Macro {

	String ID_REGEX = "[sS][tT][rR]_([a-zA-Z_0-9.]+)_([a-zA-Z_0-9.]+)";

	/**
	 Make this StringTableKey equal to <code>key</code>

	 @see StringTable#setTo(StringTable)
	 */
	default void setTo(@NotNull StringTableKey key) {
		setId(key.getId());
		getPath().setPackageName(key.getPath().getPackageName());
		getPath().getContainers().setAll(key.getPath().getContainers());
		setDefaultLanguage(key.getDefaultLanguage());
		getLanguageTokenMap().putAll(key.getLanguageTokenMap());
	}

	/** Get key id (e.g. str_tag_key) */
	@NotNull
	default String getId() {
		return getIdObserver().getValue();
	}

	/** Get the key id without str_ (e.g. if key= str_myTag_name, would return myTag_name) */
	@NotNull
	default String getIdWithoutStr_() {
		if (!getId().toLowerCase().startsWith("str_")) {
			return getId();
		}
		return getId().substring(getId().indexOf('_') + 1);
	}

	default void setId(@NotNull String id) {
		getIdObserver().updateValue(id);
	}

	/**
	 Will set the key via {@link Macro#getKeyObserver()} and the id in {@link #getIdObserver()}

	 @throws IllegalArgumentException when key isn't formatted properly ({@link StringTableKey#idIsProper(String)})
	 */
	@Override
	default void setKey(@NotNull String key) {
		if (key.charAt(0) == '$') {
			if (key.length() > 1) {
				key = key.substring(1);
			}
		}
		if (StringTableKey.idIsProper(key)) {
			getKeyObserver().updateValue(key);
			setId(key);
		} else {
			throw new IllegalArgumentException("id '" + key + "' isn't proper");
		}
	}

	@NotNull
	NotNullValueObserver<String> getIdObserver();

	@NotNull
	default String getTag() {
		return getId().substring(getId().indexOf('_'), getId().lastIndexOf('_'));
	}

	@NotNull
	StringTableKeyPath getPath();

	/**
	 Get a new deep copied instance of this key. Everything is deep copied, except the {@link Language} instances in the {@link #getLanguageTokenMap()}

	 @return new instance
	 @see StringTable#deepCopy()
	 */
	@NotNull
	StringTableKey deepCopy();

	@NotNull
	MapObserver<Language, String> getLanguageTokenMap();

	/** Get the first {@link Language} available in {@link #getLanguageTokenMap()}. If the map is empty, will return null. */
	@Nullable
	default Map.Entry<Language, String> getFirstLanguageTokenEntry() {
		if (getLanguageTokenMap().isEmpty()) {
			return null;
		}
		return getLanguageTokenMap().entrySet().iterator().next();
	}

	/**
	 Get the first token in {@link #getLanguageTokenMap()} with key={@link #getDefaultLanguage()}. If the map is empty, {@link #getDefaultLanguage()}==null, or the map doesn't have a value set
	 with {@link #getDefaultLanguage()}, will return null.

	 @return String value associated with {@link #getDefaultLanguage()}, or null if nothing matched or map is empty
	 */
	@Nullable
	default String getDefaultLanguageToken() {
		if (getLanguageTokenMap().isEmpty()) {
			return null;
		}
		return getLanguageTokenMap().get(getDefaultLanguage());
	}

	default void setDefaultLanguage(@NotNull Language language) {
		getDefaultLanguageObserver().updateValue(language);
	}

	@NotNull
	default Language getDefaultLanguage() {
		return getDefaultLanguageObserver().getValue();
	}

	@NotNull
	NotNullValueObserver<Language> getDefaultLanguageObserver();

	default boolean equalsKey(StringTableKey key) {
		if (key == null) {
			return false;
		}
		if (key == this) {
			return true;
		}
		if (!getId().equals(key.getId())) {
			return false;
		}
		for (Map.Entry<Language, String> myEntry : getLanguageTokenMap().entrySet()) {
			boolean match = false;
			for (Map.Entry<Language, String> otherEntry : key.getLanguageTokenMap().entrySet()) {
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
		return getPath().equals(key.getPath());

	}

	/** @return {@link #idIsProper(String)} with {@link #getId()} as the parameter */
	default boolean idIsProper() {
		return idIsProper(getId());
	}

	@NotNull
	default String getHeaderMacroId() {
		return getHeaderMacroId(this);
	}

	/**
	 Check if the given id is properly formatted (str_tag_name), false if it isn't. If <code>id==null</code>, will return false.

	 @param id id to check
	 @return true if formatted properly, false otherwise
	 */
	static boolean idIsProper(@Nullable String id) {
		return id != null && id.matches(ID_REGEX);
	}

	/** Returns "$STR_{@link StringTableKey#getIdWithoutStr_()}" (e.g. $STR_tag_name) */
	@NotNull
	static String getHeaderMacroId(@NotNull StringTableKey key) {
		return "$STR_" + key.getIdWithoutStr_();
	}
}
