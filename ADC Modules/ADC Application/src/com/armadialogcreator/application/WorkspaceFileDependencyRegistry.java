package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 A {@link WorkspaceFileDependencyRegistry} is a workspace level {@link FileDependencyRegistry}.
 This {@link FileDependencyRegistry} has the option to share resources between projects.

 @author Kayler
 @since 11/23/2016 */
public class WorkspaceFileDependencyRegistry extends FileDependencyRegistry {

	public WorkspaceFileDependencyRegistry(@NotNull Workspace workspace) {
		super(workspace.getFileInAdcDirectory("resources" + File.separator + "workspace_dependencies.xml"));
	}

	/**
	 Get the {@link WorkspaceFileDependencyRegistry} for the instance returned by {@link Workspace#getWorkspace()}

	 @return registry
	 */
	@NotNull
	public static WorkspaceFileDependencyRegistry getInstance() {
		return Workspace.getWorkspace().getWorkspaceFileDependencyRegistry();
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
