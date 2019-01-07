package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 A FileDependencyRegistry is a location for storing file dependencies that ADC uses (image files, scripts, etc).

 @author Kayler
 @since 07/19/2016. */
public class ProjectFileDependencyRegistry extends FileDependencyRegistry {

	public ProjectFileDependencyRegistry(@NotNull Project p) {
		super(p.getFileForName(".adc_resources/project_dependencies.xml"));
	}

	@Override
	public void applicationInitialized() {

	}

	@Override
	public void applicationDataLoaded() {

	}

	@Override
	public void applicationExit() {

	}

	@Override
	public void projectInitialized(@NotNull Project project) {

	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {

	}

	@Override
	public void projectReady(@NotNull Project project) {

	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	@Override
	public void workspaceInitialized(@NotNull Workspace workspace) {

	}

	@Override
	public void workspaceReady(@NotNull Workspace workspace) {

	}

	@Override
	public void workspaceDataLoaded(@NotNull Workspace workspace) {

	}
}
