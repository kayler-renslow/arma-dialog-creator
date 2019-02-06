package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.ListsArrayIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 @author K
 @since 01/04/2019 */
public class MacroRegistry implements Registry {

	private static final MacroRegistry instance = new MacroRegistry();

	static {
		ApplicationManager.getInstance().addStateSubscriber(instance);
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
		ApplicationDataManager.getInstance().getDataList().add(applicationMacros);
	}

	@Override
	public void projectInitializing(@NotNull Project project) {
		project.getDataList().add(new ProjectMacros());
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		for (ProjectData d : project.getDataList()) {
			if (d instanceof ProjectMacros) {
				this.projectMacros = (ProjectMacros) d; //update to new macros
				break;
			}
		}
	}

	@Override
	public void projectClosed(@NotNull Project project) {
		projectMacros.getMacros().invalidate();
	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {
		workspace.getDataList().add(new WorkspaceMacros());
	}

	@Override
	public void workspaceDataLoaded(@NotNull Workspace workspace) {
		for (WorkspaceData d : workspace.getDataList()) {
			if (d instanceof WorkspaceMacros) {
				this.workspaceMacros = (WorkspaceMacros) d; //update to new macros
				break;
			}
		}
	}

	@Override
	public void workspaceClosed(@NotNull Workspace workspace) {
		this.workspaceMacros.getMacros().invalidate();
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
		List<Macro>[] lists = new List[4];
		lists[0] = getProjectMacros().getMacros();
		lists[1] = getWorkspaceMacros().getMacros();
		lists[2] = getApplicationMacros().getMacros();
		lists[3] = getSystemMacros().getMacros();
		return new ListsArrayIterator<>(lists);
	}

	private static abstract class Base implements ADCData {

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
			for (Configurable nested : config.getNestedConfigurables()) {
				if (nested.getConfigurableName().equals("macros")) {
					//todo
				}
			}
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable config) {
			config.addAttribute("level", myLevel.name());
			for (Macro macro : macros) {
				Configurable.Simple mc = new Configurable.Simple("macro");
				mc.addAttribute("key", macro.getKey());
				if (macro.getComment() != null) {
					mc.addAttribute("comment", macro.getComment());
				}
				mc.addAttribute("type", macro.getPropertyType().getId() + "");
				mc.addNestedConfigurable(new SerializableValueConfigurable(macro.getValue()));
				//todo
			}
		}

		@Override
		@NotNull
		public String getDataID() {
			return "config-classes";
		}
	}

	public static class SystemMacros extends Base implements SystemData {

		public SystemMacros() {
			super(DataLevel.System);
		}

		public void loadSystemMacros() {

		}
	}

	public static class ApplicationMacros extends Base implements ApplicationData {

		public ApplicationMacros() {
			super(DataLevel.Application);
		}
	}

	public static class WorkspaceMacros extends Base implements WorkspaceData {

		protected WorkspaceMacros() {
			super(DataLevel.Workspace);
		}

	}

	public static class ProjectMacros extends Base implements ProjectData {

		public ProjectMacros() {
			super(DataLevel.Project);
		}

	}

}
