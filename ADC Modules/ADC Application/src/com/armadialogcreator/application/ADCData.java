package com.armadialogcreator.application;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @see ApplicationData
 @see ProjectData
 @see WorkspaceData
 @see SystemData
 @since 01/06/2019 */
public interface ADCData extends ConfigurableManager {

	/** A universally unique id used for identifying which part of the config belongs to which {@link ADCData} instance */
	@NonNls
	@NotNull String getDataID();
}
