/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCProjectInitWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 Created by Kayler on 08/07/2016.
 */
public class ApplicationLoader {
	public static final ApplicationLoader INSTANCE = new ApplicationLoader();
	private ApplicationLoader(){}
	
	static ApplicationLoader getInstance() {
		return INSTANCE;
	}
	
	public ApplicationLoadConfig getLoadConfig(){
		ApplicationLoadConfig config = createConfig(ArmaDialogCreator.getApplicationDataManager());
		config.request.complete(config);
		return config;
	}
	
	private ApplicationLoadConfig createConfig(ApplicationDataManager applicationDataManager) {
		ApplicationLoadRequest request = new ApplicationLoadRequest();
		ApplicationData applicationData = new ApplicationData(request);
		
		ADCProjectInitWindow projectInitWindow = new ADCProjectInitWindow(applicationData, applicationDataManager.getAppSaveDataDirectory());
		projectInitWindow.showAndWait();
		
		ADCProjectInitWindow.ProjectInit init = projectInitWindow.getProjectInit();
		Project project;
		TreeStructure treeStructure = null;
		if (init instanceof ADCProjectInitWindow.ProjectInit.NewProject) {
			ADCProjectInitWindow.ProjectInit.NewProject newProject = (ADCProjectInitWindow.ProjectInit.NewProject) init;
			String projectName = newProject.getProjectName();
			project = new Project(projectName, applicationDataManager.getAppSaveDataDirectory());
		} else if (init instanceof ADCProjectInitWindow.ProjectInit.OpenProject) {
			ADCProjectInitWindow.ProjectInit.OpenProject openProject = (ADCProjectInitWindow.ProjectInit.OpenProject) init;
			project = openProject.getProject();
			treeStructure = openProject.getParseResult().getTreeStructure();
			
		} else {
			System.err.println("WARNING: project init type wasn't matched with any known types. Just creating new project from scratch.");
			project = new Project(null, applicationDataManager.getAppSaveDataDirectory());
		}
		
		return new ApplicationLoadConfig(applicationData, request, project, treeStructure);
	}
	
	public static class ApplicationLoadRequest{
		private final LinkedList<ApplicationLoadListener> listeners = new LinkedList<>();
		
		private ApplicationLoadRequest(){}
		
		public void addOnComplete(ApplicationLoadListener listener) {
			listeners.add(listener);
		}
		
		private void complete(ApplicationLoadConfig config){
			for(ApplicationLoadListener listener : listeners){
				listener.loaded(config);
			}
		}
	}
	
	public static class ApplicationLoadConfig {
		private final ApplicationData applicationData;
		private final ApplicationLoadRequest request;
		private final Project newProject;
		private final TreeStructure newTreeStructure;
		
		private ApplicationLoadConfig(ApplicationData applicationData, ApplicationLoadRequest request, Project newProject, TreeStructure newTreeStructure) {
			this.applicationData = applicationData;
			this.request = request;
			this.newProject = newProject;
			this.newTreeStructure = newTreeStructure;
		}
		
		@NotNull
		public ApplicationData getApplicationData() {
			return applicationData;
		}
		
		@NotNull
		public Project getNewProject() {
			return newProject;
		}
		
		@Nullable
		public TreeStructure getNewTreeStructure() {
			return newTreeStructure;
		}
	}
	
	
}
