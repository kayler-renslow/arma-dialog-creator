package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/03/2019 */
public interface ApplicationStateSubscriber {
	/** Called one time when ADC is launched and loaded. */
	void applicationInitialized();

	/** Called one time when ADC is about to close */
	void applicationExit();

	/**
	 Called whenever a project is loaded into memory and ready to be accessed/modified by the main part of the program.

	 @param project new project that is loaded
	 */
	void projectLoaded(@NotNull Project project);

	/**
	 Called whenever a project is closed.
	 This method can be called when the application is exiting or the user is just loading a new project.

	 @param project project that was closed
	 */
	void projectClosed(@NotNull Project project);

	/**
	 Called whenever a workspace is loaded into memory and ready to be accessed/modified by the main part of the program.

	 @param workspace new workspace that is loaded
	 */
	void workspaceLoaded(@NotNull Workspace workspace);
}
