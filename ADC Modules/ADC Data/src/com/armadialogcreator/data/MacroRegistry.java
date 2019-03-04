package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 @author K
 @since 01/04/2019 */
@ApplicationSingleton
public class MacroRegistry implements Registry {

	public static final MacroRegistry instance = new MacroRegistry();
	private static final Key<DataLevel> KEY_MACRO_DATA_LEVEL = new Key<>("MacroRegistry.dataLevel", null);

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
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
	public void applicationDataInitializing() {
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
		return new QuadIterable<>(
				getProjectMacros().getMacros(),
				getWorkspaceMacros().getMacros(),
				getApplicationMacros().getMacros(),
				getSystemMacros().getMacros()
		);
	}

	public void removeMacro(@NotNull Macro macro) {
		boolean remove = projectMacros.getMacros().remove(macro);
		if (remove) {
			macro.invalidate();
			return;
		}
		remove = workspaceMacros.getMacros().remove(macro);
		if (remove) {
			macro.invalidate();
			return;
		}
		remove = applicationMacros.getMacros().remove(macro);
		if (remove) {
			macro.invalidate();
		}
	}

	@Nullable
	public DataLevel getDataLevel(@NotNull Macro m) {
		return KEY_MACRO_DATA_LEVEL.get(m.getUserData());
	}

	@NotNull
	public Map<DataLevel, List<Macro>> copyAllMacrosToMap() {
		Map<DataLevel, List<Macro>> map = new HashMap<>();
		map.put(DataLevel.Project, projectMacros.getMacros());
		map.put(DataLevel.Workspace, workspaceMacros.getMacros());
		map.put(DataLevel.Application, applicationMacros.getMacros());
		map.put(DataLevel.System, systemMacros.getMacros());
		return map;
	}

	private static abstract class Base implements ADCData {

		protected final DataLevel myLevel;
		private final ListObserver<Macro> macros = new ListObserver<>(new ArrayList<>());

		protected Base(@NotNull DataLevel myLevel) {
			this.myLevel = myLevel;
			macros.addListener((list, change) -> {
				switch (change.getChangeType()) {
					case Add: {
						ListObserverChangeAdd<Macro> added = change.getAdded();
						DataContext userData = added.getAdded().getUserData();
						KEY_MACRO_DATA_LEVEL.put(userData, myLevel);
						break;
					}
					case Clear: {
						for (Macro m : list) {
							DataContext userData = m.getUserData();
							KEY_MACRO_DATA_LEVEL.put(userData, myLevel);
						}
						break;
					}
					case Remove: {
						ListObserverChangeRemove<Macro> removed = change.getRemoved();
						DataContext userData = removed.getRemoved().getUserData();
						KEY_MACRO_DATA_LEVEL.put(userData, null);
						break;
					}
					case Set: {
						ListObserverChangeSet<Macro> set = change.getSet();
						DataContext userData = set.getOld().getUserData();
						KEY_MACRO_DATA_LEVEL.put(userData, null);
						userData = set.getNew().getUserData();
						KEY_MACRO_DATA_LEVEL.put(userData, myLevel);
						break;
					}
					case Move: {
						ListObserverChangeMove<Macro> moved = change.getMoved();
						DataContext userData = moved.getMoved().getUserData();
						if (moved.isSourceListChange()) {
							KEY_MACRO_DATA_LEVEL.put(userData, null);
						} else {
							KEY_MACRO_DATA_LEVEL.put(userData, myLevel);
						}
						break;
					}
					default: {
						throw new IllegalStateException();
					}
				}
			});
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

		public void addMacro(@NotNull Macro macro) {
			macros.add(macro);
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
