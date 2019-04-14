package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 @author K
 @since 01/04/2019 */
public interface Registry<K, V> extends ApplicationStateSubscriber {
	/** @return the first value matched at any {@link DataLevel} */
	@Nullable
	V get(@NotNull K key);

	/** @return the value matched at the specified {@link DataLevel} */
	@Nullable
	V get(@NotNull K key, @NotNull DataLevel dataLevel);

	/** @return a map that maps a list of values paired with their respective {@link DataLevel} */
	@NotNull Map<DataLevel, List<V>> copyAllToMap();

	/**
	 Get a map that maps a list of values paired with their respective {@link DataLevel}.
	 Each value is associated with a key-value pair where the key is a name/id and the value is a configurable showing
	 the details of the value

	 @return a map
	 */
	@NotNull Map<DataLevel, List<KeyValue<String, Configurable>>> copyAllToConfigurableMap();

	/** @return the number of items across all {@link DataLevel}s */
	int getEntryCount();
}
