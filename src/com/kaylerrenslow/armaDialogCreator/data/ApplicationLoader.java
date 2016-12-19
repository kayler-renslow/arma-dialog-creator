
package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.gui.main.popup.projectInit.ADCProjectInitWindow;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 Created by Kayler on 08/07/2016.
 */
public class ApplicationLoader {
	public static final ApplicationLoader INSTANCE = new ApplicationLoader();

	public enum LoadType {
		NEW_PROJECT, LOAD, IMPORT
	}

	private ApplicationLoader() {
	}

	public static ApplicationLoader getInstance() {
		return INSTANCE;
	}

	public ApplicationLoadConfig getNewLoadConfig() {
		return createConfig(ArmaDialogCreator.getApplicationDataManager());
	}

	private ApplicationLoadConfig createConfig(ApplicationDataManager applicationDataManager) {
		ADCProjectInitWindow projectInitWindow = new ADCProjectInitWindow();
		projectInitWindow.showAndWait();

		applicationDataManager.loadWorkspace(projectInitWindow.getWorkspaceDirectory());

		ADCProjectInitWindow.ProjectInit init = projectInitWindow.getProjectInit();
		ProjectInfo project;

		LoadType loadType;

		if (init instanceof ADCProjectInitWindow.ProjectInit.OpenProject) {
			ADCProjectInitWindow.ProjectInit.OpenProject openProject = (ADCProjectInitWindow.ProjectInit.OpenProject) init;
			project = openProject.getProjectXmlInfo();
			loadType = LoadType.LOAD;
		} else {
			loadType = LoadType.NEW_PROJECT;
			String projectName = null;
			if (init instanceof ADCProjectInitWindow.ProjectInit.NewProject) {
				ADCProjectInitWindow.ProjectInit.NewProject newProject = (ADCProjectInitWindow.ProjectInit.NewProject) init;
				projectName = newProject.getProjectName();
			}
			if (init == null) {
				System.err.println("WARNING: project init type wasn't matched with any known types. Just creating new project from scratch.");
			}

			projectName = (projectName != null && projectName.length() > 0) ? projectName : getTemplateProjectName();
			projectName = projectName.trim();
			project = new ProjectInfo(projectName, Workspace.getWorkspace().getFileForName(projectName + "/"));
		}

		return new ApplicationLoadConfig(project, loadType);
	}

	private String getTemplateProjectName() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int hour = Calendar.getInstance().get(Calendar.HOUR);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
		String date = String.format("%d-%d-%d %d-%d%s", year, month, day, hour, minute, am_pm == Calendar.AM ? "am" : "pm");
		return "untitled " + date;
	}


	public static class ApplicationLoadConfig {
		private final ProjectInfo newProject;
		private final LoadType loadType;

		private ApplicationLoadConfig(@NotNull ProjectInfo newProject, @NotNull LoadType loadType) {
			this.newProject = newProject;
			this.loadType = loadType;
		}

		@NotNull
		public LoadType getLoadType() {
			return loadType;
		}

		@NotNull
		public ProjectInfo getProjectInfo() {
			return newProject;
		}
	}


}
