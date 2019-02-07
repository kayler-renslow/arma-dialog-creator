package com.armadialogcreator.core;

import com.armadialogcreator.core.old.ControlPropertyOptionOld;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 01/03/2019 */
public interface ConfigPropertyKey {
	/** @return all values that the property can be, or null if not using options. */
	@Nullable
	ReadOnlyList<ControlPropertyOptionOld> getOptions();

	/** @return the name associated with the property. Is not guaranteed to be unique. */
	@NotNull
	String getPropertyName();

	/**
	 @return the {@link PropertyType} associated with the property. This is the <b>initial</b> {@link PropertyType}.
	 */
	@NotNull
	PropertyType getInitialPropertyType();

	/**
	 A globally unique id for this {@link ConfigPropertyKey} to guarantee a match by despite order change or property name
	 change, or some other change. This id should only be generated once.

	 @return a unique id
	 */
	int getPropertyId();

	@NotNull
	default String debugToString() {
		return getPropertyName() + "[id=" + getPropertyId() + "]";
	}

	default boolean keyEquals(@NotNull ConfigPropertyKey key) {
		return this == key || getPropertyName().equals(key.getPropertyName());
	}
}
