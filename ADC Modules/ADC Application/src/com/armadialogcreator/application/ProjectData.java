package com.armadialogcreator.application;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @see DataLevel#Project
 @since 01/04/2019 */
public interface ProjectData extends ADCData<ProjectData> {
	@Override
	@NotNull ProjectData constructNew();

	@Override
	void loadFromConfigurable(@NotNull Configurable config);

	@Override
	@NotNull Configurable exportToConfigurable();

	/** A universally unique id used for identifying which part of the config belongs to which {@link ADCData} instance */
	@Override
	@NonNls
	@NotNull String getDataID();
}
