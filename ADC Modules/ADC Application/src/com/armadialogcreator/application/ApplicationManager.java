package com.armadialogcreator.application;

import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author K
 @since 01/03/2019 */
public class ApplicationManager {
	private boolean applicationInitialized = false;
	private boolean workspaceLoaded = false;
	private boolean projectLoaded = false;

	public void initializeApplication() {
		if (applicationInitialized) {
			return;
		}
		ApplicationDataManager adm = ApplicationDataManager.getInstance();
		UpdateListenerGroup<ApplicationState> group = adm.getApplicationStateUpdateGroup();

		for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
			sub.applicationInitializing();
		}
		group.update(ApplicationState.ApplicationInitializing);

		//todo load configurables

		for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
			sub.applicationDataLoaded();
		}
		group.update(ApplicationState.ApplicationDataLoaded);

		applicationInitialized = true;
	}

	public void loadWorkspace(@NotNull File workspaceDirectory) {
		ApplicationDataManager adm = ApplicationDataManager.getInstance();
		UpdateListenerGroup<ApplicationState> group = adm.getApplicationStateUpdateGroup();

		if (workspaceLoaded) {
			Workspace w = adm.getCurrentWorkspace();
			w.getWorkspaceDataList().invalidate();
			for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
				sub.workspaceClosed(w);
			}
			group.update(ApplicationState.WorkspaceClosed);
		}

		Workspace w = new Workspace(workspaceDirectory);

		for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
			sub.workspaceInitializing(w);
		}
		group.update(ApplicationState.WorkspaceInitializing);

		//todo load configurables

		for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
			sub.workspaceDataLoaded(w);
		}
		group.update(ApplicationState.WorkspaceDataLoaded);

		adm.setWorkspace(w);

		for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
			sub.workspaceReady(w);
		}
		group.update(ApplicationState.WorkspaceReady);

		workspaceLoaded = true;
	}

	public void loadProject(@NotNull ProjectDescriptor descriptor) {
		ApplicationDataManager adm = ApplicationDataManager.getInstance();
		UpdateListenerGroup<ApplicationState> group = adm.getApplicationStateUpdateGroup();

		if (projectLoaded) {
			Project p = adm.getCurrentProject();
			p.getProjectDataList().invalidate();
			for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
				sub.projectClosed(p);
			}
			group.update(ApplicationState.ProjectClosed);
		}

		Project p = new Project(descriptor);

		for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
			sub.projectInitializing(p);
		}
		group.update(ApplicationState.ProjectInitializing);

		//todo load configurables

		for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
			sub.projectDataLoaded(p);
		}
		group.update(ApplicationState.ProjectDataLoaded);

		adm.setProject(p);

		for (ApplicationStateSubscriber sub : adm.getApplicationStateSubs()) {
			sub.projectReady(p);
		}
		group.update(ApplicationState.ProjectReady);

		projectLoaded = true;

	}
}
