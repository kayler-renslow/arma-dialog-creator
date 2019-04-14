package com.armadialogcreator.core.stringtable;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 @author Kayler
 @since 05/23/2016. */
public interface StringTable {
	/** @return the file that locates this {@link StringTable} */
	@NotNull
	File getFile();

	void setFile(@NotNull File file);

	/** @return all keys */
	@NotNull
	ListObserver<StringTableKey> getKeys();

	/**
	 Get the project name for the string table (for Arma 3, the root xml tag is &lt;project&gt;&lt;/project name="test"&gt; and this method returns the attribute value of 'name', which in this
	 scenario is "test").
	 */
	@NotNull
	String getStringTableProjectName();

	/** set all keys */
	void setKeys(@NotNull ListObserver<StringTableKey> keys);

	/**
	 Set this StringTable equal to <code>table</code> (copies over {@link #getFile()} and invokes {@link StringTableKey#setTo(StringTableKey)} for each {@link #getKeys()}). If the given table
	 has a key that this table doesn't, this table will copy it into its keys
	 */
	default void setTo(@NotNull StringTable table) {
		setFile(table.getFile());
		for (StringTableKey key : table.getKeys()) {
			boolean found = false;
			for (StringTableKey myKey : getKeys()) {
				if (myKey.getId().equalsIgnoreCase(key.getId())) {
					myKey.setTo(key);
					found = true;
					break;
				}
			}
			if (!found) {
				getKeys().add(key);
			}
		}
	}

	/**
	 Get a key id ({@link StringTableKey#getId()}) equal to the given id

	 @param id id to match
	 @return null if no key with given id exists, or the key matched
	 */
	@Nullable
	default StringTableKey getKeyById(@NotNull String id) {
		for (StringTableKey key : getKeys()) {
			if (key.getId().equalsIgnoreCase(id)) {
				return key;
			}
		}
		return null;
	}

	/** Invokes {@link StringTableKey#setDefaultLanguage(Language)} for each key in {@link #getKeys()} */
	default void setDefaultLanguage(@Nullable Language language) {
		for (StringTableKey key : getKeys()) {
			key.setDefaultLanguage(language);
		}
	}

	/**
	 Create a new {@link StringTable} instance as a deep copy.

	 @return new instance
	 @see StringTableKey#deepCopy()
	 */
	@NotNull
	StringTable deepCopy();

}
