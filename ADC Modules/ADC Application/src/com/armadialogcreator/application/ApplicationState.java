package com.armadialogcreator.application;

/**
 @author K
 @since 01/06/2019 */
public enum ApplicationState {
	/**
	 Called one time when ADC is launched. This method should be used to initialize any {@link ApplicationData} instances.
	 */
	ApplicationInitializing,
	/**
	 Called one time when {@link ApplicationDataManager#getApplicationDataList()} is fully loaded and ready.
	 */
	ApplicationDataLoaded,
	/** Called one time when ADC is about to close */
	ApplicationExit,
	/**
	 Called when the what project XML file should be loaded and will load shortly.
	 */
	ProjectInitializing,
	/**
	 Called whenever a project's {@link Project#getProjectDataList()} is fully loaded and ready.
	 */
	ProjectDataLoaded,
	/**
	 Called whenever a project is loaded into memory and ready to be accessed/modified by the main part of the program.
	 */
	ProjectReady,
	/**
	 Called whenever a project is closed.
	 This method can be called when the application is exiting or the user is just loading a new project.
	 */
	ProjectClosed,
	/**
	 Called when the what workspace should be loaded and will load shortly.
	 */
	WorkspaceInitializing,
	/**
	 Called whenever a workspace is loaded into memory and ready to be accessed/modified by the main part of the program.
	 */
	WorkspaceDataLoaded,
	/**
	 Called whenever a workspace's {@link Workspace#getWorkspaceDataList()} is fully loaded and ready.
	 */
	WorkspaceReady,
	/**
	 Called whenever a workspace is closed.
	 This method can be called when the application is exiting or the user is just loading a new workspace.
	 */
	WorkspaceClosed
}
