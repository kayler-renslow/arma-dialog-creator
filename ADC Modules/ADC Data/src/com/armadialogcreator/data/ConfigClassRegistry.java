package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 @author K
 @since 01/04/2019 */
@ApplicationSingleton
public class ConfigClassRegistry implements Registry<String, ConfigClass> {
	public static final ConfigClassRegistry instance = new ConfigClassRegistry();

	private static final Key<DataLevel> KEY_CONFIG_CLASS_DATA_LEVEL = new Key<>("ConfigClassRegistry.dataLevel", null);

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	@NotNull
	private ProjectClasses projectClasses = new ProjectClasses(this);
	@NotNull
	private WorkspaceClasses workspaceClasses = new WorkspaceClasses(this);
	@NotNull
	private ApplicationClasses applicationClasses = new ApplicationClasses(this);
	@NotNull
	private final SystemClasses systemClasses = new SystemClasses(this);

	/** @return a {@link ConfigClass} instance from the given name. Will return null if className couldn't be matched */
	@Nullable
	public ConfigClass findConfigClassByName(@NotNull String className) {
		ConfigClass c = projectClasses.findConfigClassByName(className);
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
	public void applicationDataInitializing() {
		systemClasses.loadSystemConfigClasses();
		ApplicationDataManager.getInstance().getDataList().add(applicationClasses);
		applicationClasses.doJobs();
	}

	@Override
	public void projectInitializing(@NotNull Project project) {
		project.getDataList().add(new ProjectClasses(this));
	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {
		workspace.getDataList().add(new WorkspaceClasses(this));
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		for (ProjectData data : project.getDataList()) {
			if (data instanceof ProjectClasses) {
				this.projectClasses = (ProjectClasses) data;
			}
		}
		projectClasses.doJobs();
	}

	@Override
	public void projectClosed(@NotNull Project project) {
		this.projectClasses.getClasses().invalidate();
	}

	@Override
	public void workspaceDataLoaded(@NotNull Workspace workspace) {
		for (WorkspaceData data : workspace.getDataList()) {
			if (data instanceof WorkspaceClasses) {
				this.workspaceClasses = (WorkspaceClasses) data;
			}
		}
		workspaceClasses.doJobs();
	}

	@Override
	public void workspaceClosed(@NotNull Workspace workspace) {
		this.workspaceClasses.getClasses().invalidate();
	}

	/**
	 @return project level {@link ConfigClass} instances
	 */
	@NotNull
	public ProjectClasses getProjectClasses() {
		return projectClasses;
	}

	/**
	 @return workspace level {@link ConfigClass} instances
	 */
	@NotNull
	public WorkspaceClasses getWorkspaceClasses() {
		return workspaceClasses;
	}

	/**
	 @return project level {@link ConfigClass} instances
	 */
	@NotNull
	public ApplicationClasses getApplicationClasses() {
		return applicationClasses;
	}

	@NotNull
	public SystemClasses getSystemClasses() {
		return systemClasses;
	}

	@NotNull
	public Iterable<ConfigClass> iterateAllConfigClasses() {
		return new QuadIterable<>(
				projectClasses.getClasses(),
				workspaceClasses.getClasses(),
				applicationClasses.getClasses(),
				systemClasses.getClasses()
		);
	}

	@Override
	public int getEntryCount() {
		return projectClasses.getClasses().size() +
				workspaceClasses.getClasses().size() +
				applicationClasses.getClasses().size() +
				systemClasses.getClasses().size();
	}

	@Nullable
	public DataLevel getDataLevel(@NotNull ConfigClass configClass) {
		return KEY_CONFIG_CLASS_DATA_LEVEL.get(configClass.getUserData());
	}

	@NotNull
	public Map<DataLevel, List<ConfigClass>> copyAllClassesToMap() {
		Map<DataLevel, List<ConfigClass>> map = new HashMap<>();
		map.put(DataLevel.Project, projectClasses.getClasses());
		map.put(DataLevel.Workspace, workspaceClasses.getClasses());
		map.put(DataLevel.Application, applicationClasses.getClasses());
		map.put(DataLevel.System, systemClasses.getClasses());

		return map;
	}

	@Nullable
	@Override
	public ConfigClass get(@NotNull String key) {
		return findConfigClassByName(key);
	}

	@Nullable
	@Override
	public ConfigClass get(@NotNull String key, @NotNull DataLevel dataLevel) {
		switch (dataLevel) {
			case System: {
				return systemClasses.findConfigClassByName(key);
			}
			case Application: {
				return applicationClasses.findConfigClassByName(key);
			}
			case Workspace: {
				return workspaceClasses.findConfigClassByName(key);
			}
			case Project: {
				return projectClasses.findConfigClassByName(key);
			}
		}
		return null;
	}

	@Override
	@NotNull
	public Map<DataLevel, List<ConfigClass>> copyAllToMap() {
		Map<DataLevel, List<ConfigClass>> map = new HashMap<>();
		map.put(DataLevel.System, systemClasses.getClasses());
		map.put(DataLevel.Application, applicationClasses.getClasses());
		map.put(DataLevel.Workspace, workspaceClasses.getClasses());
		map.put(DataLevel.Project, projectClasses.getClasses());
		return map;
	}

	@Override
	@NotNull
	public Map<DataLevel, List<KeyValue<String, Configurable>>> copyAllToConfigurableMap() {
		Map<DataLevel, List<KeyValue<String, Configurable>>> map = new HashMap<>();
		map.put(DataLevel.System, systemClasses.toKeyValueList());
		map.put(DataLevel.Application, applicationClasses.toKeyValueList());
		map.put(DataLevel.Workspace, workspaceClasses.toKeyValueList());
		map.put(DataLevel.Project, projectClasses.toKeyValueList());
		return map;
	}

	private static abstract class Base implements ADCData {

		protected final DataLevel myLevel;
		private final ListObserver<ConfigClass> classes = new ListObserver<>(new LinkedList<>());
		protected final ConfigClassRegistry registry;
		private final List<ConfigClassJob> jobs = new ArrayList<>();

		protected Base(@NotNull ConfigClassRegistry registry, @NotNull DataLevel myLevel) {
			this.registry = registry;
			this.myLevel = myLevel;

			classes.addListener((list, change) -> {
				switch (change.getChangeType()) {
					case Add: {
						ListObserverChangeAdd<ConfigClass> added = change.getAdded();
						DataContext userData = added.getAdded().getUserData();
						KEY_CONFIG_CLASS_DATA_LEVEL.put(userData, myLevel);
						break;
					}
					case Clear: {
						for (ConfigClass c : list) {
							DataContext userData = c.getUserData();
							KEY_CONFIG_CLASS_DATA_LEVEL.put(userData, null);
						}
						break;
					}
					case Move: {
						ListObserverChangeMove<ConfigClass> moved = change.getMoved();
						DataContext userData = moved.getMoved().getUserData();
						if (moved.isSourceListChange()) {
							// clear the value in the case it was moved out of the config class registry
							KEY_CONFIG_CLASS_DATA_LEVEL.put(userData, null);
						} else {
							// this list received an update even though it isn't source change, so this list
							// was the destination list
							KEY_CONFIG_CLASS_DATA_LEVEL.put(userData, myLevel);
						}
						break;
					}
					case Set: {
						ListObserverChangeSet<ConfigClass> set = change.getSet();
						DataContext userData = set.getNew().getUserData();
						KEY_CONFIG_CLASS_DATA_LEVEL.put(userData, myLevel);
						userData = set.getOld().getUserData();
						KEY_CONFIG_CLASS_DATA_LEVEL.put(userData, null);
						break;
					}
					case Remove: {
						ListObserverChangeRemove<ConfigClass> removed = change.getRemoved();
						DataContext userData = removed.getRemoved().getUserData();
						KEY_CONFIG_CLASS_DATA_LEVEL.put(userData, null);
						break;
					}
					default: {
						throw new IllegalStateException();
					}
				}
			});
		}

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

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			for (Configurable nested : config.getNestedConfigurables()) {
				if (nested.getConfigurableName().equals("config-class")) {
					classes.add(fromConfigurable(nested));
				}
			}
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable config) {
			config.addAttribute("level", getLevel().name());
			for (ConfigClass configClass : classes) {
				config.addNestedConfigurable(toConfigurable(configClass));
			}
		}

		@Override
		@NotNull
		public String getDataID() {
			return "config-classes";
		}

		public void addClass(@NotNull ConfigClass configClass) {
			classes.add(configClass);
		}

		abstract DataLevel getLevel();

		@NotNull
		public List<KeyValue<String, Configurable>> toKeyValueList() {
			List<KeyValue<String, Configurable>> list = new ArrayList<>();
			for (ConfigClass cc : classes) {
				list.add(new KeyValue<>(cc.getClassName(), toConfigurable(cc)));
			}
			return list;
		}

		void doJobs() {
			for (ConfigClassJob job : jobs) {
				job.doWork();
			}
			jobs.clear();
		}

		@NotNull
		public Configurable toConfigurable(@NotNull ConfigClass configClass) {
			Configurable.Simple conf = new Configurable.Simple("config-class");
			conf.addAttribute("name", configClass.getClassName());
			if (configClass.isExtending()) {
				conf.addAttribute("extend", configClass.getExtendClassName());
			}
			for (ConfigProperty property : configClass.iterateProperties()) {
				if (!configClass.propertyIsInherited(property.getName())) {
					Configurable.Simple propertyConf = new Configurable.Simple("property");
					conf.addNestedConfigurable(propertyConf);
					propertyConf.addAttribute("name", property.getName());
					if (property.isBoundToMacro()) {
						propertyConf.addAttribute("macro", property.getBoundMacro().getKey());
					}
					propertyConf.addNestedConfigurable(new SerializableValueConfigurable(property.getValue()));

				}
			}
			return conf;
		}

		@NotNull
		public ConfigClass fromConfigurable(@NotNull Configurable configurable) {
			String name = configurable.getAttributeValue("name");
			if (name == null) {
				throw new IllegalStateException();
			}
			String extend = configurable.getAttributeValue("extend");
			ConfigClass configClass = new ConfigClass(name);
			if (extend != null) {
				jobs.add(new ExtendConfigClassJob(configClass, extend));
			}
			for (Configurable nested : configurable.getNestedConfigurables()) {
				String propertyName = nested.getAttributeValue("name");
				String macro = nested.getAttributeValue("macro");

				if (propertyName == null) {
					throw new IllegalStateException();
				}
				if (macro != null) {
					jobs.add(new SetMacroJob(configClass, propertyName, macro));
				}
				Configurable svConf = nested.getConfigurable(SerializableValueConfigurable.CONFIGURABLE_NAME);
				if (svConf == null) {
					throw new IllegalStateException();
				}
				SerializableValue sv = SerializableValueConfigurable.createFromConfigurable(
						svConf,
						ExpressionEnvManager.instance.getEnv()
				);
				configClass.addProperty(propertyName, sv);
			}
			return configClass;
		}
	}

	public static class SystemClasses extends Base implements SystemData {

		public SystemClasses(@NotNull ConfigClassRegistry registry) {
			super(registry, DataLevel.System);
		}

		public void loadSystemConfigClasses() {

		}

		@Override
		DataLevel getLevel() {
			return DataLevel.System;
		}
	}

	public static class ApplicationClasses extends Base implements ApplicationData {

		public ApplicationClasses(@NotNull ConfigClassRegistry registry) {
			super(registry, DataLevel.Application);
		}

		@Override
		DataLevel getLevel() {
			return DataLevel.Application;
		}
	}

	public static class WorkspaceClasses extends Base implements WorkspaceData {

		protected WorkspaceClasses(@NotNull ConfigClassRegistry registry) {
			super(registry, DataLevel.Workspace);
		}

		@Override
		DataLevel getLevel() {
			return DataLevel.Workspace;
		}
	}

	public static class ProjectClasses extends Base implements ProjectData {

		public ProjectClasses(@NotNull ConfigClassRegistry registry) {
			super(registry, DataLevel.Project);
		}

		@Override
		DataLevel getLevel() {
			return DataLevel.Project;
		}
	}

	private interface ConfigClassJob {
		void doWork();
	}

	private static class ExtendConfigClassJob implements ConfigClassJob {

		private final ConfigClass cc;
		private final String extendClass;

		public ExtendConfigClassJob(@NotNull ConfigClass cc, @NotNull String extendClass) {
			this.cc = cc;
			this.extendClass = extendClass;
		}

		@Override
		public void doWork() {
			ConfigClass extend = ConfigClassRegistry.instance.findConfigClassByName(extendClass);
			if (extend == null) {
				throw new IllegalStateException();
			}
			cc.extendConfigClass(extend);
		}
	}

	private static class SetMacroJob implements ConfigClassJob {

		private final ConfigClass cc;
		private final String property;
		private final String macro;

		public SetMacroJob(@NotNull ConfigClass cc, @NotNull String property, @NotNull String macro) {
			this.cc = cc;
			this.property = property;
			this.macro = macro;
		}

		@Override
		public void doWork() {
			Macro macro = MacroRegistry.instance.findMacroByName(this.macro);
			if (macro == null) {
				throw new IllegalStateException();
			}
			cc.findProperty(property).bindToMacro(macro);
		}
	}

}
