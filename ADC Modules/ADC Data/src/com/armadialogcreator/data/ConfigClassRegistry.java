package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 @author K
 @since 01/04/2019 */
public class ConfigClassRegistry implements Registry, ApplicationData {

	private ProjectConfigClassContainer projectContainer;
	private WorkspaceConfigClassContainer workspaceContainer;

	/** @return a {@link ConfigClass} instance from the given name. Will return null if className couldn't be matched */
	@Nullable
	public ConfigClass findConfigClassByName(@NotNull String className) {
		ConfigClass c = projectContainer.findConfigClassByName(className);
		if (c != null) {
			return c;
		}
		return workspaceContainer.findConfigClassByName(className);
	}

	@Override
	public void applicationInitialized() {

	}

	@Override
	public void applicationExit() {

	}

	@Override
	public void projectLoaded(@NotNull Project project) {
		projectContainer = new ProjectConfigClassContainer();
		project.getProjectDataList().add(projectContainer);
	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	@Override
	public void workspaceLoaded(@NotNull Workspace workspace) {
		workspaceContainer = new WorkspaceConfigClassContainer();
		workspace.getWorkspaceDataList().add(workspaceContainer);
	}

	@Override
	@NotNull
	public ApplicationData constructNew() {
		return new ConfigClassRegistry();
	}

	private static abstract class Base {
		private final ListObserver<ConfigClass> classes = new ListObserver<>(new LinkedList<>());

		@Nullable
		public ConfigClass findConfigClassByName(@NotNull String className) {
			for (ConfigClass c : classes) {
				if (c.getClassName().equals(className)) {
					return c;
				}
			}
			return null;
		}

		@NotNull
		public ListObserver<ConfigClass> getClasses() {
			return classes;
		}
	}

	public static class ProjectConfigClassContainer extends Base implements ProjectData {

	}

	public static class WorkspaceConfigClassContainer extends Base implements WorkspaceData {

	}

}
