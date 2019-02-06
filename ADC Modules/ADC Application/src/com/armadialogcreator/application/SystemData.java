package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 A unique type of {@link ADCData} that will not be stored in any file when the application saves the project/workspace/application data

 @author K
 @see DataLevel#System
 @since 01/06/2019 */
public interface SystemData extends ADCData {

	/** Default implementation does nothing */
	@Override
	default void loadFromConfigurable(@NotNull Configurable config) {

	}

	/**
	 Default implementation does nothing
	 */
	@Override
	default void exportToConfigurable(@NotNull Configurable configurable) {

	}

	/** @return empty string */
	@Override
	@NotNull
	default String getDataID() {
		return "";
	}
}
