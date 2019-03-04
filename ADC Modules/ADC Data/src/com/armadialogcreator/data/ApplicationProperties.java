package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.util.ApplicationSingleton;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 @author K
 @since 3/3/19 */
@ApplicationSingleton
public class ApplicationProperties implements ApplicationStateSubscriber {
	public static final ApplicationProperties instance = new ApplicationProperties();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	private final PropertySettings settings = new PropertySettings();

	public final Settings.FileSetting lastWorkspace = new Settings.FileSetting(Workspace.DEFAULT_WORKSPACE_DIRECTORY);

	@Override
	public void applicationDataInitializing() {
		ApplicationDataManager.getInstance().getDataList().add(settings);

		Map<String, Settings.Setting> map = settings.map;
		map.put("last_workspace_folder", lastWorkspace);
	}

	private static class PropertySettings extends Settings implements ApplicationData {

		@Override
		@NotNull
		public String getDataID() {
			return "application-properties";
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			this.setFromConfigurable(config);
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable configurable) {
			configurable.addNestedConfigurable(this.copyToConfigurable());
		}
	}
}
