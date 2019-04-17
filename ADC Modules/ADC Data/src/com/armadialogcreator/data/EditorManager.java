package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.util.ApplicationSingleton;
import com.armadialogcreator.util.ScreenDimension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 @author K
 @since 02/09/2019 */
@ApplicationSingleton
public class EditorManager implements ApplicationStateSubscriber {
	public static final EditorManager instance = new EditorManager();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	private ArmaDisplay editingDisplay = new ArmaDisplay();
	private final ArmaResolution resolution = new ArmaResolution(ScreenDimension.D848);

	@NotNull
	public ArmaDisplay getEditingDisplay() {
		return editingDisplay;
	}

	@NotNull
	public ArmaResolution getResolution() {
		return resolution;
	}

	public int getBackgroundImage() {
		return 0;
	}


	@Override
	public void projectInitializing(@NotNull Project project) {
		project.getDataList().add(new EditorProjectData());
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		for (ProjectData pd : project.getDataList()) {
			if (pd instanceof EditorProjectData) {
				((EditorProjectData) pd).doJobs();
			}
		}
	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	public void loadDisplayFromConfigurable(@NotNull Configurable c) {
		EditorProjectData data = new EditorProjectData();
		data.loadFromConfigurable(c);
		data.doJobs();
	}

	private static class EditorProjectData implements ProjectData {
		private List<ConfigClassRegistry.ConfigClassJob> jobs;

		@Override
		@NotNull
		public String getDataID() {
			return "EditorManagerData";
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			jobs = new ArrayList<>();
			Configurable ccConf = config.getConfigurable("config-class");
			if (ccConf != null) {
				ArmaDisplay armaDisplay = new ArmaDisplay("");
				ConfigClassRegistry.fromConfigurable(ccConf, armaDisplay, configClassJob -> {
					jobs.add(configClassJob);
				});
				EditorManager.instance.editingDisplay = armaDisplay;
			}
			Configurable controls = config.getConfigurable("controls");
			if (controls != null) {
				Configurable bgConf = controls.getConfigurable("background");
				Configurable mainConf = controls.getConfigurable("main");
			}
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable configurable) {
			// todo
		}

		public void doJobs() {
			if (jobs == null) {
				return; //nothing was loaded
			}
			for (ConfigClassRegistry.ConfigClassJob job : jobs) {
				job.doWork();
			}
			jobs = null; //help GC
		}
	}
}
