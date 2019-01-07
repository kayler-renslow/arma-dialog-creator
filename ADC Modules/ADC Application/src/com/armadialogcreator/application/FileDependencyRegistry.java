package com.armadialogcreator.application;

import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.ListsIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 A FileDependencyRegistry is a location for storing file dependencies that ADC uses (image files, scripts, etc).

 @author Kayler
 @since 07/19/2016. */
public class FileDependencyRegistry implements Registry {
	private static final FileDependencyRegistry instance = new FileDependencyRegistry();

	static {
		ApplicationDataManager.getInstance().addStateSubscriber(instance);
	}

	@NotNull
	public static FileDependencyRegistry getInstance() {
		return instance;
	}

	@NotNull
	private final ApplicationDependencies applicationDependencies = new ApplicationDependencies();
	@NotNull
	private WorkspaceDependencies workspaceDependencies = new WorkspaceDependencies();
	@NotNull
	private ProjectDependencies projectDependencies = new ProjectDependencies();

	@Nullable
	public FileDependency getDependencyInstanceByFile(@NotNull File f) {
		FileDependency fd = projectDependencies.getDependencyInstanceByFile(f);
		if (fd != null) {
			return fd;
		}
		fd = workspaceDependencies.getDependencyInstanceByFile(f);
		if (fd != null) {
			return fd;
		}
		return applicationDependencies.getDependencyInstanceByFile(f);
	}

	@NotNull
	public ApplicationDependencies getApplicationDependencies() {
		return applicationDependencies;
	}

	@NotNull
	public WorkspaceDependencies getWorkspaceDependencies() {
		return workspaceDependencies;
	}

	@NotNull
	public ProjectDependencies getProjectDependencies() {
		return projectDependencies;
	}

	@Override
	public void applicationInitializing() {
		ApplicationDataManager.getInstance().getApplicationDataList().add(applicationDependencies);
	}

	@Override
	public void applicationDataLoaded() {
	}

	@Override
	public void applicationExit() {

	}

	@Override
	public void projectInitializing(@NotNull Project project) {

	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		for (ProjectData d : project.getProjectDataList()) {
			if (d instanceof ProjectDependencies) {
				this.projectDependencies = (ProjectDependencies) d;
				break;
			}
		}
	}

	@Override
	public void projectReady(@NotNull Project project) {

	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {

	}

	@Override
	public void workspaceReady(@NotNull Workspace workspace) {

	}

	@Override
	public void workspaceDataLoaded(@NotNull Workspace workspace) {
		for (WorkspaceData d : workspace.getWorkspaceDataList()) {
			if (d instanceof WorkspaceDependencies) {
				this.workspaceDependencies = (WorkspaceDependencies) d;
				break;
			}
		}
	}

	@NotNull
	public Iterable<FileDependency> iterateAllDependencies() {
		List<List<FileDependency>> lists = new ArrayList<>(3);
		lists.add(getProjectDependencies().getDependencyList());
		lists.add(getWorkspaceDependencies().getDependencyList());
		lists.add(getApplicationDependencies().getDependencyList());
		return new ListsIterator<>(lists);
	}

	private abstract static class Base<D extends ADCData> implements ADCDataRenewable<D>, ADCData {
		private final ListObserver<FileDependency> dependencyList = new ListObserver<>(new ArrayList<>());

		@NotNull
		public ListObserver<FileDependency> getDependencyList() {
			return dependencyList;
		}

		@Nullable
		public FileDependency getDependencyInstanceByFile(@NotNull File f) {
			for (FileDependency resource : dependencyList) {
				if (resource.getExternalFileObserver().getValue().equals(f)) {
					return resource;
				}
			}
			return null;
		}

		@Override
		@NotNull
		public String getDataID() {
			return "file-dependencies";
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {

		}

		@Override
		public @NotNull Configurable exportToConfigurable() {
			for (FileDependency d : dependencyList) {
				if (d instanceof RemappedFileDependency) {

				} else if (d instanceof ExportableFileDependency) {

				} else {

				}
			}
			return null;
		}
	}

	public static class ApplicationDependencies extends Base<ApplicationData> implements ApplicationData {

		@NotNull
		@Override
		public ApplicationData constructNew() {
			return this;
		}
	}

	public static class WorkspaceDependencies extends Base<WorkspaceData> implements WorkspaceData {

		@NotNull
		@Override
		public WorkspaceData constructNew() {
			return new WorkspaceDependencies();
		}

	}

	public static class ProjectDependencies extends Base<ProjectData> implements ProjectData {

		@NotNull
		@Override
		public ProjectData constructNew() {
			return new ProjectDependencies();
		}
	}
}
