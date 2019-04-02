package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.util.ApplicationSingleton;
import com.armadialogcreator.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 @author K
 @since 02/15/2019 */
@ApplicationSingleton
public class SettingsManager implements Registry<String, Settings.Setting> {
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

	@Nullable
	@Override
	public Settings.Setting get(@NotNull String key) {
		Settings.Setting setting = appSettings.getMap().get(key);
		if (setting != null) {
			return setting;
		}
		return projectSettings.getMap().get(key);
	}

	@Nullable
	@Override
	public Settings.Setting get(@NotNull String key, @NotNull DataLevel dataLevel) {
		if (dataLevel == DataLevel.Project) {
			return projectSettings.getMap().get(key);
		} else if (dataLevel == DataLevel.Application) {
			return appSettings.getMap().get(key);
		}
		return null;
	}

	@Override
	@NotNull
	public Map<DataLevel, List<Settings.Setting>> copyAllToMap() {
		Map<DataLevel, List<Settings.Setting>> map = new HashMap<>();
		map.put(DataLevel.Application, new ArrayList<>(appSettings.getMap().values()));
		map.put(DataLevel.Project, new ArrayList<>(projectSettings.getMap().values()));
		return map;
	}

	@Override
	@NotNull
	public Map<DataLevel, List<KeyValue<String, Configurable>>> copyAllToConfigurableMap() {
		Map<DataLevel, List<KeyValue<String, Configurable>>> map = new HashMap<>();
		map.put(DataLevel.Application, appSettings.toKeyValueList());
		map.put(DataLevel.Project, projectSettings.toKeyValueList());
		return map;
	}

	@Override
	public int getEntryCount() {
		return appSettings.getMap().size() + projectSettings.getMap().size();
	}

}
