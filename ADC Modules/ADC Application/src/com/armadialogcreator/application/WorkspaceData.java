package com.armadialogcreator.application;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 @see DataLevel#Workspace
 @author K
 @since 01/04/2019 */
public interface WorkspaceData extends ADCData<WorkspaceData> {
	@Override
	@NotNull WorkspaceData constructNew();

	@Override
	void loadFromConfigurable(@NotNull Configurable config);

	@Override
	@NotNull Configurable exportToConfigurable();

	/** A universally unique id used for identifying which part of the config belongs to which {@link ADCData} instance */
	@Override
	@NonNls
	@NotNull String getDataID();
}
