/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */


package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.projectInit.ADCProjectInitWindow;
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
