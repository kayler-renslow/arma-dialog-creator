package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/03/2019 */
public interface ApplicationStateSubscriber {
	/**
	 Called one time when ADC is launched. This method should be used to initialize any {@link ApplicationData} instances.
	 */
	void applicationInitializing();

	/**
	 Called one time when {@link ApplicationDataManager#getApplicationDataList()} is fully loaded and ready.
	 */
	void applicationDataLoaded();

	/** Called one time when ADC is about to close */
	void applicationExit();

	/**
	 Called when the what project XML file should be loaded and will load shortly.

	 @param project new project that is initialized
	 */
	void projectInitializing(@NotNull Project project);

	/**
	 Called whenever a project's {@link Project#getProjectDataList()} is fully loaded and ready.

	 @param project project that has it's data loaded
	 */
	void projectDataLoaded(@NotNull Project project);

	/**
	 Called whenever a project is loaded into memory and ready to be accessed/modified by the main part of the program.

	 @param project new project that is loaded
	 */
	void projectReady(@NotNull Project project);

	/**
	 Called whenever a project is closed.
	 This method can be called when the application is exiting or the user is just loading a new project.

	 @param project project that was closed
	 */
	void projectClosed(@NotNull Project project);

	/**
	 Called when the what workspace should be loaded and will load shortly.

	 @param workspace new project that is initialized
	 */
	void workspaceInitializing(@NotNull Workspace workspace);

	/**
	 Called whenever a workspace is loaded into memory and ready to be accessed/modified by the main part of the program.

	 @param workspace new workspace that is loaded
	 */
	void workspaceReady(@NotNull Workspace workspace);

	/**
	 Called whenever a workspace's {@link Workspace#getWorkspaceDataList()} is fully loaded and ready.

	 @param workspace workspace that has it's data loaded
	 */
	void workspaceDataLoaded(@NotNull Workspace workspace);
}
