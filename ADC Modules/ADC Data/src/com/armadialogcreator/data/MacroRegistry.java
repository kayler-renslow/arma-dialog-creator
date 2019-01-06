package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 @author K
 @since 01/04/2019 */
public class MacroRegistry implements Registry, ApplicationData {
	@NotNull
	private ProjectMacros projectMacros = new ProjectMacros();
	@NotNull
	private WorkspaceMacros workspaceMacros = new WorkspaceMacros();

	/** @return a {@link Macro} instance from the given name. Will return null if key couldn't be matched */
	@Nullable
	public Macro findMacroByKey(@NotNull String macroKey) {
		Macro c = projectMacros.findMacroByKey(macroKey);
		if (c != null) {
			return c;
		}
		return workspaceMacros.findMacroByKey(macroKey);
	}

	@Override
	public void applicationInitialized() {

	}

	@Override
	public void applicationExit() {

	}

	@Override
	public void projectLoaded(@NotNull Project project) {
		projectMacros = new ProjectMacros();
		project.getProjectDataList().add(projectMacros);
	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	@Override
	public void workspaceLoaded(@NotNull Workspace workspace) {
		workspaceMacros = new WorkspaceMacros();
		workspace.getWorkspaceDataList().add(workspaceMacros);
	}

	@NotNull
	public ProjectMacros getProjectMacros() {
		return projectMacros;
	}

	@NotNull
	public WorkspaceMacros getWorkspaceMacros() {
		return workspaceMacros;
	}

	/** @return this */
	@Override
	@NotNull
	public MacroRegistry constructNew() {
		return this; //never reinstantiate
	}

	private static abstract class Base {
		private final ListObserver<Macro> macros = new ListObserver<>(new LinkedList<>());

		@Nullable
		public Macro findMacroByKey(@NotNull String macroKey) {
			for (Macro m : macros) {
				if (m.getKey().equals(macroKey)) {
					return m;
				}
			}
			return null;
		}

		@NotNull
		public ListObserver<Macro> getMacros() {
			return macros;
		}

	}

	public static class ProjectMacros extends Base implements ProjectData {
		/** @return this */
		@Override
		@NotNull
		public ProjectMacros constructNew() {
			return this;
		}
	}

	public static class WorkspaceMacros extends Base implements WorkspaceData {
		/** @return this */
		@Override
		@NotNull
		public WorkspaceMacros constructNew() {
			return this;
		}

	}
}
