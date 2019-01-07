package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 A unique type of {@link ADCData} that will not need to be stored in any file

 @author K
 @see DataLevel#System
 @since 01/06/2019 */
public interface SystemData extends ADCData<SystemData> {
	/**
	 Default implementation returns "this" because this type of {@link ADCData} isn't stored in a list
	 somewhere like {@link ApplicationDataManager#getApplicationDataList()} because it doesn't need to be saved to file.

	 @return this
	 */
	@NotNull
	@Override
	default SystemData constructNew() {
		return this; //no need to construct new because it's not stored in a file or in ApplicationDataManager
	}

	/** Default implementation does nothing */
	@Override
	default void loadFromConfigurable(@NotNull Configurable config) {

	}

	/** @return {@link Configurable#EMPTY} */
	@Override
	default @NotNull Configurable exportToConfigurable() {
		return Configurable.EMPTY;
	}

	/** @return empty string */
	@Override
	@NotNull
	default String getDataID() {
		return "";
	}
}
