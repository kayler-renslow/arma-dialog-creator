package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 @since 05/23/2016. */
public interface StringTable {
	/** @return the file that locates this {@link StringTable} */
	@NotNull
	File getFile();

	/** @return all keys */
	@NotNull
	List<StringTableKey> getKeys();

	/** set all keys */
	void setKeys(@NotNull List<StringTableKey> keys);

	/**
	 Get a key id ({@link StringTableKey#getId()}) equal to the given id

	 @param id id to match
	 @return null if no key with given id exists, or the key matched
	 */
	@Nullable
	default StringTableKey getKeyById(@NotNull String id) {
		for (StringTableKey key : getKeys()) {
			if (key.getId().equals(id)) {
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

	/**
	 Get a map that pairs each of the keys in <code>keyList</code>. If {@link StringTableKey#getPackageName()} == null, keys will be placed in {@link StringTableKeyMatchMap#getNullItems()}.

	 @param keyList list of keys
	 @return map
	 */
	static StringTableKeyMatchMap combineKeysByPackageName(@NotNull List<StringTableKey> keyList) {
		StringTableKeyMatchMap map = new StringTableKeyMatchMap(keyList.size());
		for (StringTableKey key : keyList) {
			if (key.getPackageName() == null) {
				if (map.hasNullItems()) {
					continue;
				}
				map.setNullItems(getKeysByPackageName(keyList, null));
			} else {
				if (map.keySet().contains(key.getPackageName())) {
					continue;
				}
				map.put(key.getPackageName(), getKeysByPackageName(keyList, key.getPackageName()));
			}
		}

		return map;
	}

	/**
	 Get a map that pairs each of the keys in <code>keyList</code>. If {@link StringTableKey#getContainerName()} == null, keys will be placed in {@link StringTableKeyMatchMap#getNullItems()}.

	 @param keyList list of keys
	 @return map
	 */
	static StringTableKeyMatchMap combineKeysByContainerName(@NotNull List<StringTableKey> keyList) {
		StringTableKeyMatchMap map = new StringTableKeyMatchMap(keyList.size());
		for (StringTableKey key : keyList) {
			if (key.getContainerName() == null) {
				if (map.hasNullItems()) {
					continue;
				}
				map.setNullItems(getKeysByContainerName(keyList, null));
			} else {
				if (map.keySet().contains(key.getContainerName())) {
					continue;
				}
				map.put(key.getContainerName(), getKeysByContainerName(keyList, key.getContainerName()));
			}
		}

		return map;
	}


	/**
	 Get all {@link StringTableKey} instances that match the given <code>packageName</code>.

	 @param keyList list of keys
	 @param packageName if null, will match all keys that have {@link StringTableKey#getPackageName()} == null. If not null, will match all keys with equal package name
	 @return iterable of all matched keys
	 */
	static LinkedList<StringTableKey> getKeysByPackageName(@NotNull List<StringTableKey> keyList, @Nullable String packageName) {
		LinkedList<StringTableKey> matchKeys = new LinkedList<>();
		for (StringTableKey key : keyList) {
			if (packageName == null && key.getPackageName() == null) {
				matchKeys.add(key);
			} else if (packageName != null && key.getPackageName() != null) {
				if (key.getPackageName().equals(packageName)) {
					matchKeys.add(key);
				}
			}
		}
		return matchKeys;
	}

	/**
	 Get all {@link StringTableKey} instances that match the given <code>containerName</code>.

	 @param keyList list of keys
	 @param containerName if null, will match all keys that have {@link StringTableKey#getContainerName()} == null. If not null, will match all keys with equal container name
	 @return iterable of all matched keys
	 */
	static LinkedList<StringTableKey> getKeysByContainerName(@NotNull List<StringTableKey> keyList, @Nullable String containerName) {
		LinkedList<StringTableKey> keys = new LinkedList<>();
		for (StringTableKey key : keyList) {
			if (containerName == null && key.getContainerName() == null) {
				keys.add(key);
			} else if (containerName != null && key.getContainerName() != null) {
				if (key.getContainerName().equals(containerName)) {
					keys.add(key);
				}
			}
		}
		return keys;
	}

	class StringTableKeyMatchMap extends HashMap<String, LinkedList<StringTableKey>> {
		private LinkedList<StringTableKey> nullItems;

		public StringTableKeyMatchMap(int size) {
			super(size);
		}


		void setNullItems(@NotNull LinkedList<StringTableKey> nullItems) {
			this.nullItems = nullItems;
		}

		/** Get items that have a null name match. Could be {@link StringTableKey#getPackageName()}==null or something else */
		@Nullable
		public LinkedList<StringTableKey> getNullItems() {
			return nullItems;
		}

		/** @return true if {@link #getNullItems()}!= null, false if is null */
		public boolean hasNullItems() {
			return getNullItems() != null;
		}

	}
}
