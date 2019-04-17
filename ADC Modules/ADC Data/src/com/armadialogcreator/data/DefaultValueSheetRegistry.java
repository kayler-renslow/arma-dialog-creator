package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.util.ApplicationSingleton;
import com.armadialogcreator.util.KeyValue;
import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.XmlParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 @author K
 @since 3/7/19 */
@ApplicationSingleton
public class DefaultValueSheetRegistry implements Registry<String, DefaultValueSheet> {
	public static final DefaultValueSheetRegistry instance = new DefaultValueSheetRegistry();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	@NotNull
	private final SystemDefaultValueSheets systemSheets = new SystemDefaultValueSheets();
	@NotNull
	private final ApplicationDefaultValueSheets applicationSheets = new ApplicationDefaultValueSheets();
	@NotNull
	private WorkspaceDefaultValueSheets workspaceSheets = new WorkspaceDefaultValueSheets();
	@NotNull
	private ProjectDefaultValueSheets projectSheets = new ProjectDefaultValueSheets();

	@Nullable
	@Override
	public DefaultValueSheet get(@NotNull String key) {
		DefaultValueSheet sheet = get(key, DataLevel.Project);
		if (sheet != null) {
			return sheet;
		}
		sheet = get(key, DataLevel.Workspace);
		if (sheet != null) {
			return sheet;
		}
		sheet = get(key, DataLevel.Application);
		if (sheet != null) {
			return sheet;
		}

		return get(key, DataLevel.System);
	}

	@Nullable
	@Override
	public DefaultValueSheet get(@NotNull String key, @NotNull DataLevel dataLevel) {
		switch (dataLevel) {
			case System: {
				return systemSheets.getSheet(key);
			}
			case Application: {
				return applicationSheets.getSheet(key);
			}
			case Workspace: {
				return workspaceSheets.getSheet(key);
			}
			case Project: {
				return projectSheets.getSheet(key);
			}
		}
		return null;
	}

	@NotNull
	public String getSystemSheetName(@NotNull String name) {
		return '#' + name;
	}

	public boolean isValidUserSheetName(@NotNull String name) {
		return name.length() > 0 && name.charAt(0) != '#';
	}

	@Override
	@NotNull
	public Map<DataLevel, List<DefaultValueSheet>> copyAllToMap() {
		Map<DataLevel, List<DefaultValueSheet>> map = new HashMap<>();
		map.put(DataLevel.System, systemSheets.getSheets());
		map.put(DataLevel.Application, applicationSheets.getSheets());
		map.put(DataLevel.Workspace, workspaceSheets.getSheets());
		map.put(DataLevel.Project, projectSheets.getSheets());
		return map;
	}

	@Override
	@NotNull
	public Map<DataLevel, List<KeyValue<String, Configurable>>> copyAllToConfigurableMap() {
		Map<DataLevel, List<KeyValue<String, Configurable>>> map = new HashMap<>();
		map.put(DataLevel.System, systemSheets.toKeyValueList());
		map.put(DataLevel.Application, applicationSheets.toKeyValueList());
		map.put(DataLevel.Workspace, workspaceSheets.toKeyValueList());
		map.put(DataLevel.Project, projectSheets.toKeyValueList());
		return map;
	}

	@Override
	public int getEntryCount() {
		return systemSheets.getSheets().size() +
				applicationSheets.getSheets().size() +
				workspaceSheets.getSheets().size() +
				projectSheets.getSheets().size();
	}

	@Override
	public void applicationDataInitializing() {
		ApplicationDataManager.getInstance().getDataList().add(applicationSheets);
	}

	@Override
	public void systemDataInitializing() {
		try {
			systemSheets.loadSheets();
		} catch (XmlParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void applicationDataLoaded() {

	}

	@Override
	public void projectInitializing(@NotNull Project project) {

	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {

	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {

	}

	@Override
	public void workspaceDataLoaded(@NotNull Workspace workspace) {

	}

	private static class Base implements ADCData {
		private final ListObserver<DefaultValueSheet> sheets = new ListObserver<>(new ArrayList<>());
		private final DataLevel level;
		private final Map<String, FileDependency> externalSheets = new HashMap<>();
		private final Map<String, String> externalSheetsToLoadLater = new HashMap<>();

		public Base(@NotNull DataLevel level) {
			this.level = level;
		}

		@Override
		@NotNull
		public String getDataID() {
			return "default-value-sheets";
		}

		@NotNull
		public ListObserver<DefaultValueSheet> getSheets() {
			return sheets;
		}

		@NotNull
		public Map<String, FileDependency> getExternalSheets() {
			return externalSheets;
		}

		@NotNull
		Map<String, String> getExternalSheetsToLoadLater() {
			return externalSheetsToLoadLater;
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			Configurable sheets = config.getConfigurable("sheets");
			if (sheets != null) {
				for (Configurable sheet : sheets.getNestedConfigurables()) {
					String name = sheet.getAttributeValueNotNull("name");
					DefaultValueSheet valueSheet = new DefaultValueSheet(name);
					this.sheets.add(valueSheet);
					valueSheet.setFromConfigurable(sheet);
				}
			}

			Configurable extSheets = config.getConfigurable("ext-sheets");
			if (extSheets != null) {
				for (Configurable extSheet : extSheets.getNestedConfigurables()) {
					String name = extSheet.getAttributeValueNotNull("name");
					externalSheetsToLoadLater.put(name, extSheet.getConfigurableBody());
				}
			}
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable configurable) {
			configurable.addAttribute("level", level.name());
			Configurable.Simple sheets = new Configurable.Simple("sheets");
			configurable.addNestedConfigurable(sheets);
			{
				for (DefaultValueSheet sheet : this.sheets) {
					if (externalSheets.containsKey(sheet.getName())) {
						continue;
					}
					sheets.addNestedConfigurable(sheet.exportToConfigurable());
				}
			}

			Configurable.Simple externalSheets = new Configurable.Simple("ext-sheets");
			configurable.addNestedConfigurable(externalSheets);
			{
				for (Map.Entry<String, FileDependency> entry : this.externalSheets.entrySet()) {
					Configurable.Simple ext = new Configurable.Simple("ext-sheet");
					ext.addAttribute("name", entry.getKey());
					externalSheets.addNestedConfigurable(ext);
					ext.setBody(entry.getValue().getExportedPath());
				}
			}
		}

		@Nullable
		public DefaultValueSheet getSheet(@NotNull String key) {
			for (DefaultValueSheet sheet : sheets) {
				if (sheet.getName().equals(key)) {
					return sheet;
				}
			}
			return null;
		}

		@NotNull
		public List<KeyValue<String, Configurable>> toKeyValueList() {
			List<KeyValue<String, Configurable>> list = new ArrayList<>();
			for (DefaultValueSheet sheet : sheets) {
				list.add(new KeyValue<>(sheet.getName(), sheet.exportToConfigurable()));
			}
			return list;
		}
	}

	public static class SystemDefaultValueSheets extends Base {
		public SystemDefaultValueSheets() {
			super(DataLevel.System);
		}

		public void loadSheets() throws XmlParseException {
			ADCFile file = ADCFile.toADCJarFile("/com/armadialogcreator/data/defaultValues/SystemDefaultValues.xml", getClass().getModule().getName());
			Configurable root;
			try {
				root = XmlConfigurableLoader.load(file);
			} catch (XmlParseException e) {
				throw new RuntimeException(e);
			}
			Configurable prefixConf = root.getConfigurable("prefix");
			if (prefixConf == null) {
				throw new IllegalStateException();
			}
			String prefixValue = prefixConf.getAttributeValueNotNull("value");
			String prefixName = prefixConf.getAttributeValueNotNull("name");

			Configurable sheets = root.getConfigurableNotNull("sheets");
			for (Configurable sheet : sheets.getNestedConfigurables()) {
				String name = sheet.getAttributeValueNotNull("name");
				DefaultValueSheet valueSheet = new DefaultValueSheet(name);
				String displayName = sheet.getAttributeValue("display-name");
				valueSheet.setDisplayName(displayName == null ? "" : displayName);

				String systemSheetPath = sheet.getConfigurableBody().replace('$' + prefixName + '$', prefixValue);
				ADCFile systemSheetFile = ADCFile.toADCJarFile(systemSheetPath, getClass().getModule().getName());
				Configurable sheetConf = XmlConfigurableLoader.load(systemSheetFile);
				valueSheet.setFromConfigurable(sheetConf);

				getSheets().add(valueSheet);
			}
		}
	}

	public static class ApplicationDefaultValueSheets extends Base implements ApplicationData {
		public ApplicationDefaultValueSheets() {
			super(DataLevel.Application);
		}
	}

	public static class WorkspaceDefaultValueSheets extends Base implements WorkspaceData {
		public WorkspaceDefaultValueSheets() {
			super(DataLevel.Workspace);
		}
	}

	public static class ProjectDefaultValueSheets extends Base implements ProjectData {
		public ProjectDefaultValueSheets() {
			super(DataLevel.Project);
		}
	}
}
