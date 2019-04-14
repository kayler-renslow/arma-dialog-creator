package com.armadialogcreator.application;

/**
 @author K
 @since 01/06/2019 */
public enum ApplicationState {
	/**
	 Called one time when ADC is launched. This is the first state entered.
	 */
	ADCInitializing,
	/**
	 Called one time after {@link #ADCInitializing}.
	 This state should be used to initialize any {@link SystemData} instances
	 */
	SystemDataInitializing,
	/**
	 Called one time after {@link #SystemDataInitializing}.
	 All {@link SystemData} instances should be fully loaded and ready after this state.
	 */
	SystemDataLoaded,

	/**
	 Called after {@link #SystemDataLoaded}. This state should be used to initialize any {@link ApplicationData} instances.
	 */
	ApplicationDataInitializing,
	/**
	 Called one time after {@link #ADCInitializing} {@link ApplicationDataManager#getDataList()} is fully loaded and ready.
	 */
	ApplicationDataLoaded,
	/** Called one time when ADC is about to close */
	ApplicationExit,
	/**
	 Called when the what project XML file should be loaded and will load shortly.
	 */
	ProjectInitializing,
	/**
	 Called after {@link #ProjectInitializing}. In this state, a project's {@link Project#getDataList()} is fully loaded and ready.
	 */
	ProjectDataLoaded,
	/**
	 Called after {@link #ProjectDataLoaded}.
	 In this state, a project is loaded into memory and ready to be accessed/modified by the main part of the program.
	 */
	ProjectReady,
	/**
	 Called whenever a project is closed.
	 This state can be called when the application is exiting or the user is just loading a new project.
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
	 Called whenever a workspace's {@link Workspace#getDataList()} is fully loaded and ready.
	 */
	WorkspaceReady,
	/**
	 Called whenever a workspace is closed.
	 This state can be called when the application is exiting or the user is just loading a new workspace.
	 */
	WorkspaceClosed
}
