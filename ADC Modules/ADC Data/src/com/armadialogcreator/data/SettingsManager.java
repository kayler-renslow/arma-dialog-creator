package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.util.ApplicationSingleton;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/15/2019 */
@ApplicationSingleton
public class SettingsManager implements ApplicationStateSubscriber {
	public static final SettingsManager instance = new SettingsManager();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	private final ApplicationSettings appSettings = new ApplicationSettings();
	private final ProjectSettings projectSettings = new ProjectSettings();

	@NotNull
	public ApplicationSettings getApplicationSettings() {
		return appSettings;
	}

	@NotNull
	public ProjectSettings getProjectSettings() {
		return projectSettings;
	}

	@Override
	public void projectReady(@NotNull Project project) {

	}

	private static abstract class Base implements ADCData {
		private final Settings settings;

		public Base(@NotNull Settings settings) {
			this.settings = settings;
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			//todo
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable configurable) {
			//todo
		}
	}
}
