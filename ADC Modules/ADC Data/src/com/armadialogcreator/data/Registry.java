package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.DataLevel;
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

	/** @return the number of items across all {@link DataLevel}s */
	int getEntryCount();
}
