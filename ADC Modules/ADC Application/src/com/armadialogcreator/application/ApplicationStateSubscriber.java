package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/03/2019 */
public interface ApplicationStateSubscriber {

	/**
	 Default implementation does nothing.
	 @see ApplicationState#ApplicationInitializing
	 */
	default void applicationInitializing() {

	}

	/**
	 Default implementation does nothing.

	 @see ApplicationState#SystemDataInitializing
	 */
	default void systemDataInitializing() {

	}

	/**
	 Default implementation does nothing.

	 @see ApplicationState#SystemDataLoaded
	 */
	default void systemDataLoaded() {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#ApplicationDataLoaded
	 */
	default void applicationDataLoaded() {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#ApplicationExit
	 */
	default void applicationExit() {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#ProjectInitializing
	 */
	default void projectInitializing(@NotNull Project project) {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#ProjectDataLoaded
	 */
	default void projectDataLoaded(@NotNull Project project) {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#ProjectReady
	 */
	default void projectReady(@NotNull Project project) {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#ProjectClosed
	 */
	default void projectClosed(@NotNull Project project) {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#WorkspaceInitializing
	 */
	default void workspaceInitializing(@NotNull Workspace workspace) {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#WorkspaceReady
	 */
	default void workspaceReady(@NotNull Workspace workspace) {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#WorkspaceDataLoaded
	 */
	default void workspaceDataLoaded(@NotNull Workspace workspace) {

	}

	/**
	 Default implementation does nothing.
	 @see ApplicationState#WorkspaceClosed
	 */
	default void workspaceClosed(@NotNull Workspace workspace) {

	}
}
