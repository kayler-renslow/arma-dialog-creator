package com.armadialogcreator.application;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 @author K
 @see ApplicationData
 @see ProjectData
 @see WorkspaceData
 @see SystemData
 @since 01/06/2019 */
public interface ADCData<T> {
	@NotNull T constructNew();

	void loadFromConfigurable(@NotNull Configurable config);

	@NotNull Configurable exportToConfigurable();

	/** A universally unique id used for identifying which part of the config belongs to which {@link ADCData} instance */
	@NonNls
	@NotNull String getDataID();

	List<Class<? extends SystemData>> SYSTEM_DATA_IMPLS = new ArrayList<>();
	List<Class<? extends ApplicationData>> APPLICATION_DATA_IMPLS = new ArrayList<>();
	List<Class<? extends WorkspaceData>> WORKSPACE_DATA_IMPLS = new ArrayList<>();
	List<Class<? extends ProjectData>> PROJECT_DATA_IMPLS = new ArrayList<>();
}
