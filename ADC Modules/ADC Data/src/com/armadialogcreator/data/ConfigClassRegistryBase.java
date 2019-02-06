package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.core.ConfigClass;
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
abstract class ConfigClassRegistryBase<C extends ConfigClass> implements Registry {

	private final ConfigClassConfigurableHandler configurableHandler;
	@NotNull
	private ProjectClasses<C> projectClasses = new ProjectClasses<>(this);
	@NotNull
	private WorkspaceClasses<C> workspaceClasses = new WorkspaceClasses<>(this);
	@NotNull
	private ApplicationClasses<C> applicationClasses = new ApplicationClasses<>(this);
	@NotNull
	private final SystemClasses<C> systemClasses = new SystemClasses<>(this);

	protected ConfigClassRegistryBase(@NotNull ConfigClassConfigurableHandler configurableHandler) {
		this.configurableHandler = configurableHandler;
	}

	/** @return a {@link ConfigClass} instance from the given name. Will return null if className couldn't be matched */
	@Nullable
	public C findConfigClassByName(@NotNull String className) {
		C c = projectClasses.findConfigClassByName(className);
		if (c != null) {
			return c;
		}
		c = workspaceClasses.findConfigClassByName(className);
		if (c != null) {
			return c;
		}
		c = applicationClasses.findConfigClassByName(className);
		if (c != null) {
			return c;
		}
		return systemClasses.findConfigClassByName(className);
	}

	@Override
	public void applicationInitializing() {
		systemClasses.loadSystemConfigClasses();
		ApplicationDataManager.getInstance().getDataList().add(applicationClasses);
	}

	@Override
	public void projectInitializing(@NotNull Project project) {
		project.getDataList().add(new ProjectClasses<>(this));
	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {
		workspace.getDataList().add(new WorkspaceClasses<>(this));
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		for (ProjectData data : project.getDataList()) {
			if (data instanceof ProjectClasses) {
				this.projectClasses = (ProjectClasses<C>) data;
			}
		}
	}

	@Override
	public void projectClosed(@NotNull Project project) {
		this.projectClasses.getClasses().invalidate();
	}

	@Override
	public void workspaceDataLoaded(@NotNull Workspace workspace) {
		for (WorkspaceData data : workspace.getDataList()) {
			if (data instanceof WorkspaceClasses) {
				this.workspaceClasses = (WorkspaceClasses<C>) data;
			}
		}
	}

	@Override
	public void workspaceClosed(@NotNull Workspace workspace) {
		this.workspaceClasses.getClasses().invalidate();
	}

	/**
	 @return project level {@link ConfigClass} instances
	 */
	@NotNull
	public ProjectClasses<C> getProjectClasses() {
		return projectClasses;
	}

	/**
	 @return workspace level {@link ConfigClass} instances
	 */
	@NotNull
	public WorkspaceClasses<C> getWorkspaceClasses() {
		return workspaceClasses;
	}

	/**
	 @return project level {@link ConfigClass} instances
	 */
	@NotNull
	public ApplicationClasses<C> getApplicationClasses() {
		return applicationClasses;
	}

	@NotNull
	public SystemClasses<C> getSystemClasses() {
		return systemClasses;
	}

	@NotNull
	public Iterable<C> iterateAllConfigClasses() {
		List<List<C>> lists = new ArrayList<>(4);
		lists.add(getProjectClasses().getClasses());
		lists.add(getWorkspaceClasses().getClasses());
		lists.add(getApplicationClasses().getClasses());
		lists.add(getSystemClasses().getClasses());
		return new ListsIterator<>(lists);
	}

	private static abstract class Base<C extends ConfigClass, D extends ADCData> implements ADCData {

		protected final DataLevel myLevel;
		private final ListObserver<C> classes = new ListObserver<>(new LinkedList<>());
		protected final ConfigClassRegistryBase<C> registry;

		protected Base(@NotNull ConfigClassRegistryBase<C> registry, @NotNull DataLevel myLevel) {
			this.registry = registry;
			this.myLevel = myLevel;
		}

		@Nullable
		public C findConfigClassByName(@NotNull String className) {
			for (C c : classes) {
				if (c.getClassName().equals(className)) {
					return c;
				}
			}
			return null;
		}

		@NotNull
		public ListObserver<C> getClasses() {
			return classes;
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			registry.configurableHandler.loadFromConfigurable(config, classes);
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable configurable) {
			registry.configurableHandler.exportToConfigurable(configurable, classes, myLevel);
		}

		@Override
		@NotNull
		public String getDataID() {
			return "config-classes";
		}
	}

	interface ConfigClassConfigurableHandler<C extends ConfigClass> {
		void loadFromConfigurable(@NotNull Configurable config, @NotNull List<C> classes);

		void exportToConfigurable(@NotNull Configurable config, @NotNull List<C> classes, @NotNull DataLevel level);
	}

	static abstract class BaseConfigClassConfigurableHandler<C extends ConfigClass> implements ConfigClassConfigurableHandler<C> {
		@Override
		public void loadFromConfigurable(@NotNull Configurable config, @NotNull List<C> classes) {
			for (Configurable nested : config.getNestedConfigurables()) {
				if (nested.getConfigurableName().equals("config-class")) {
					//todo
				}
			}
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable config, @NotNull List<C> classes, @NotNull DataLevel level) {
			config.addAttribute("level", level.name());
			for (ConfigClass configClass : classes) {
				//todo
			}
		}
	}

	public static class SystemClasses<C extends ConfigClass> extends Base<C, SystemData> implements SystemData {

		public SystemClasses(@NotNull ConfigClassRegistryBase<C> registry) {
			super(registry, DataLevel.System);
		}

		public void loadSystemConfigClasses() {

		}
	}

	public static class ApplicationClasses<C extends ConfigClass> extends Base<C, ApplicationData> implements ApplicationData {

		public ApplicationClasses(@NotNull ConfigClassRegistryBase<C> registry) {
			super(registry, DataLevel.Application);
		}
	}

	public static class WorkspaceClasses<C extends ConfigClass> extends Base<C, WorkspaceData> implements WorkspaceData {

		protected WorkspaceClasses(@NotNull ConfigClassRegistryBase<C> registry) {
			super(registry, DataLevel.Workspace);
		}
	}

	public static class ProjectClasses<C extends ConfigClass> extends Base<C, ProjectData> implements ProjectData {

		public ProjectClasses(@NotNull ConfigClassRegistryBase<C> registry) {
			super(registry, DataLevel.Project);
		}
	}

}
