package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
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
}
