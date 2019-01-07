package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.util.KeyValueString;
import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.ListsIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 @author K
 @since 01/04/2019 */
public class MacroRegistry implements Registry {

	private static final MacroRegistry instance = new MacroRegistry();

	static {
		ApplicationDataManager.getInstance().addStateSubscriber(instance);
	}

	@NotNull
	public static MacroRegistry getInstance() {
		return instance;
	}

	@NotNull
	private ProjectMacros projectMacros = new ProjectMacros();
	@NotNull
	private WorkspaceMacros workspaceMacros = new WorkspaceMacros();
	@NotNull
	private final ApplicationMacros applicationMacros = new ApplicationMacros();
	@NotNull
	private final SystemMacros systemMacros = new SystemMacros();

	/** @return a {@link Macro} instance from the given name. Will return null if className couldn't be matched */
	@Nullable
	public Macro findMacroByName(@NotNull String macroKey) {
		Macro m = projectMacros.findMacroByName(macroKey);
		if (m != null) {
			return m;
		}
		m = workspaceMacros.findMacroByName(macroKey);
		if (m != null) {
			return m;
		}
		m = applicationMacros.findMacroByName(macroKey);
		if (m != null) {
			return m;
		}
		return systemMacros.findMacroByName(macroKey);
	}

	@Override
	public void applicationInitializing() {
		systemMacros.loadSystemMacros();
		ApplicationDataManager.getInstance().getApplicationDataList().add(applicationMacros);
	}

	@Override
	public void applicationDataLoaded() {
	}

	@Override
	public void applicationExit() {

	}

	@Override
	public void projectInitializing(@NotNull Project project) {
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		for (ProjectData d : project.getProjectDataList()) {
			if (d instanceof ProjectMacros) {
				this.projectMacros = (ProjectMacros) d;
				break;
			}
		}
	}

	@Override
	public void projectReady(@NotNull Project project) {

	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {
	}

	@Override
	public void workspaceReady(@NotNull Workspace workspace) {

	}

	@Override
	public void workspaceDataLoaded(@NotNull Workspace workspace) {
		for (WorkspaceData d : workspace.getWorkspaceDataList()) {
			if (d instanceof WorkspaceMacros) {
				this.workspaceMacros = (WorkspaceMacros) d;
				break;
			}
		}
	}

	/**
	 @return project level {@link Macro} instances
	 @throws IllegalStateException when this method is invoked before a project has been loaded
	 */
	@NotNull
	public ProjectMacros getProjectMacros() {
		return projectMacros;
	}

	/**
	 @return workspace level {@link Macro} instances
	 @throws IllegalStateException when this method is invoked before a workspace has been loaded
	 */
	@NotNull
	public WorkspaceMacros getWorkspaceMacros() {
		return workspaceMacros;
	}

	/**
	 @return project level {@link Macro} instances
	 @throws IllegalStateException when this method is invoked before the application's {@link Macro} instances has been loaded
	 */
	@NotNull
	public ApplicationMacros getApplicationMacros() {
		return applicationMacros;
	}

	@NotNull
	public SystemMacros getSystemMacros() {
		return systemMacros;
	}

	@NotNull
	public Iterable<Macro> iterateAllMacros() {
		List<List<Macro>> lists = new ArrayList<>(4);
		lists.add(getProjectMacros().getMacros());
		lists.add(getWorkspaceMacros().getMacros());
		lists.add(getApplicationMacros().getMacros());
		lists.add(getSystemMacros().getMacros());
		return new ListsIterator<>(lists);
	}

	private static abstract class Base<T extends ADCData> implements ADCData {

		protected final DataLevel myLevel;
		private final ListObserver<Macro> macros = new ListObserver<>(new LinkedList<>());

		protected Base(@NotNull DataLevel myLevel) {
			this.myLevel = myLevel;
		}

		@Nullable
		public Macro findMacroByName(@NotNull String className) {
			for (Macro c : macros) {
				if (c.getKey().equals(className)) {
					return c;
				}
			}
			return null;
		}

		@NotNull
		public ListObserver<Macro> getMacros() {
			return macros;
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			List<Configurable> nestedConfigs = config.getNestedConfigurables();
			for (Configurable nested : nestedConfigs) {
				if (nested.getConfigurableName().equals("macros")) {
					//todo
				}
			}
		}

		@Override
		@NotNull
		public Configurable exportToConfigurable() {
			Configurable config = new Configurable.Simple("macro-registry");
			config.getConfigurableAttributes().add(new KeyValueString("level", myLevel.name()));
			for (Macro macro : macros) {
				Configurable.Simple mc = new Configurable.Simple("macro");
				mc.getConfigurableAttributes().add(new KeyValueString("key", macro.getKey()));
				mc.getConfigurableAttributes().add(new KeyValueString("comment", macro.getComment()));
				mc.getConfigurableAttributes().add(new KeyValueString("type", macro.getPropertyType().getId() + ""));
				mc.getNestedConfigurables().add(new SerializableValueConfigurable(macro.getValue()));
				//todo
			}
			return config;
		}

		@Override
		@NotNull
		public String getDataID() {
			return "config-classes";
		}
	}

	public static class SystemMacros extends Base<SystemData> implements SystemData {

		public SystemMacros() {
			super(DataLevel.System);
		}

		public void loadSystemMacros() {

		}
	}

	public static class ApplicationMacros extends Base<ApplicationData> implements ApplicationData {

		public ApplicationMacros() {
			super(DataLevel.Application);
		}
	}

	public static class WorkspaceMacros extends Base<WorkspaceData> implements WorkspaceData {

		protected WorkspaceMacros() {
			super(DataLevel.Workspace);
		}

		@Override
		@NotNull
		public WorkspaceData constructNew() {
			getMacros().invalidate();
			return new WorkspaceMacros();
		}

	}

	public static class ProjectMacros extends Base<ProjectData> implements ProjectData {

		public ProjectMacros() {
			super(DataLevel.Project);
		}

		@Override
		@NotNull
		public ProjectData constructNew() {
			getMacros().invalidate();
			return new ProjectMacros();
		}

	}

}
