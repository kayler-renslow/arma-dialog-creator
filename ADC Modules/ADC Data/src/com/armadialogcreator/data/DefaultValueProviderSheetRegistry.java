package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.util.ApplicationSingleton;
import com.armadialogcreator.util.ListObserver;
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
public class DefaultValueProviderSheetRegistry implements Registry<String, DefaultValueSheet> {
	public static final DefaultValueProviderSheetRegistry instance = new DefaultValueProviderSheetRegistry();

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
		return null;
	}

	@Nullable
	@Override
	public DefaultValueSheet get(@NotNull String key, @NotNull DataLevel dataLevel) {
		return null;
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
	public void applicationDataInitializing() {
		ApplicationDataManager.getInstance().getDataList().add(applicationSheets);
	}

	@Override
	public void systemDataInitializing() {

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
					String name = sheet.getAttributeValue("name");
					if (name == null) {
						throw new IllegalStateException();
					}
					DefaultValueSheet valueSheet = new DefaultValueSheet(name);
					this.sheets.add(valueSheet);
					valueSheet.setFromConfigurable(sheet);
				}
			}

			Configurable extSheets = config.getConfigurable("ext-sheets");
			if (extSheets != null) {
				for (Configurable extSheet : extSheets.getNestedConfigurables()) {
					String name = extSheet.getAttributeValue("name");
					if (name == null) {
						throw new IllegalStateException();
					}
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
	}

	public static class SystemDefaultValueSheets extends Base {
		public SystemDefaultValueSheets() {
			super(DataLevel.System);
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
