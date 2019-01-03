
package com.armadialogcreator.data;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.data.xml.ProjectInit;
import com.armadialogcreator.gui.main.popup.projectInit.ADCProjectInitWindow;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 Created by Kayler on 08/07/2016.
 */
public class ApplicationLoader {
	public static final ApplicationLoader INSTANCE = new ApplicationLoader();

	public enum LoadType {
		NEW_PROJECT, LOAD
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

		Workspace workspace = applicationDataManager.loadWorkspace(projectInitWindow.getWorkspaceDirectory());

		ProjectInit init = projectInitWindow.getProjectInit();
		ProjectInfo project;

		LoadType loadType;

		if (init instanceof ProjectInit.OpenProject) {
			ProjectInit.OpenProject openProject = (ProjectInit.OpenProject) init;
			project = openProject.getProjectXmlInfo();
			loadType = LoadType.LOAD;
		} else {
			loadType = LoadType.NEW_PROJECT;
			String projectName = null;
			if (init instanceof ProjectInit.NewProject) {
				ProjectInit.NewProject newProject = (ProjectInit.NewProject) init;
				projectName = newProject.getProjectName();
			}
			if (init == null) {
				System.err.println("WARNING: project init type wasn't matched with any known types. Just creating new project from scratch.");
			}

			projectName = (projectName != null && projectName.length() > 0) ? projectName : getTemplateProjectName();
			projectName = projectName.trim();
			project = new ProjectInfo(projectName, projectName, workspace);
		}

		return new ApplicationLoadConfig(project, loadType, workspace);
	}

	private String getTemplateProjectName() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1; //month starts at 0
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
		private final Workspace workspace;

		private ApplicationLoadConfig(@NotNull ProjectInfo newProject, @NotNull LoadType loadType,
									  @NotNull Workspace workspace) {
			this.newProject = newProject;
			this.loadType = loadType;
			this.workspace = workspace;
		}

		@NotNull
		public LoadType getLoadType() {
			return loadType;
		}

		@NotNull
		public ProjectInfo getProjectInfo() {
			return newProject;
		}

		@NotNull
		public Workspace getWorkspace() {
			return workspace;
		}
	}


}
