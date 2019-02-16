package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.util.ApplicationSingleton;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/15/2019 */
@ApplicationSingleton
public class ApplicationSettingsManager implements ApplicationStateSubscriber {
	public static final ApplicationSettingsManager instance = new ApplicationSettingsManager();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	private final ApplicationSettings settings = new ApplicationSettings();

	@NotNull
	public ApplicationSettings getSettings() {
		return settings;
	}

}
