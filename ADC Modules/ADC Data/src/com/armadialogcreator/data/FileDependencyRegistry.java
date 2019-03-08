package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.util.ApplicationSingleton;
import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.TripleIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 A FileDependencyRegistry is a location for storing file dependencies that ADC uses (image files, scripts, etc).

 @author Kayler
 @since 07/19/2016. */
@ApplicationSingleton
public class FileDependencyRegistry implements Registry<File, FileDependency> {
	public static final FileDependencyRegistry instance = new FileDependencyRegistry();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
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
	public void applicationDataInitializing() {
		ApplicationDataManager.getInstance().getDataList().add(applicationDependencies);
	}

	@Override
	public void projectInitializing(@NotNull Project project) {
		project.getDataList().add(new ProjectDependencies());
	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {
		workspace.getDataList().add(new WorkspaceDependencies());
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		for (ProjectData d : project.getDataList()) {
			if (d instanceof ProjectDependencies) {
				this.projectDependencies = (ProjectDependencies) d;
				break;
			}
		}
	}

	@Override
	public void projectClosed(@NotNull Project project) {
		projectDependencies.getDependencyList().invalidate();
	}


	@Override
	public void workspaceDataLoaded(@NotNull Workspace workspace) {
		for (WorkspaceData d : workspace.getDataList()) {
			if (d instanceof WorkspaceDependencies) {
				this.workspaceDependencies = (WorkspaceDependencies) d;
				break;
			}
		}
	}

	@Override
	public void workspaceClosed(@NotNull Workspace workspace) {
		workspaceDependencies.getDependencyList().invalidate();
	}

	@NotNull
	public Iterable<FileDependency> iterateAllDependencies() {
		return new TripleIterable<>(getProjectDependencies().getDependencyList(),
				getWorkspaceDependencies().getDependencyList(),
				getApplicationDependencies().getDependencyList()
		);
	}

	@Nullable
	@Override
	public FileDependency get(@NotNull File key) {
		return getDependencyInstanceByFile(key);
	}

	@Nullable
	@Override
	public FileDependency get(@NotNull File key, @NotNull DataLevel dataLevel) {
		switch (dataLevel) {
			case Project: {
				return projectDependencies.getDependencyInstanceByFile(key);
			}
			case Workspace: {
				return workspaceDependencies.getDependencyInstanceByFile(key);
			}
			case Application: {
				return applicationDependencies.getDependencyInstanceByFile(key);
			}
		}
		return null;
	}

	@Override
	@NotNull
	public Map<DataLevel, List<FileDependency>> copyAllToMap() {
		Map<DataLevel, List<FileDependency>> map = new HashMap<>();
		map.put(DataLevel.Application, applicationDependencies.getDependencyList());
		map.put(DataLevel.Workspace, workspaceDependencies.getDependencyList());
		map.put(DataLevel.Project, projectDependencies.getDependencyList());
		return map;
	}

	private abstract static class Base<D extends ADCData> implements ADCData {
		private final ListObserver<FileDependency> dependencyList = new ListObserver<>(new ArrayList<>());

		@NotNull
		public ListObserver<FileDependency> getDependencyList() {
			return dependencyList;
		}

		@Nullable
		public FileDependency getDependencyInstanceByFile(@NotNull File f) {
			for (FileDependency resource : dependencyList) {
				if (resource.getOriginalFileObserver().getValue().equals(f)) {
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
		public void exportToConfigurable(@NotNull Configurable configurable) {
			for (FileDependency d : dependencyList) {

			}
		}
	}

	public static class ApplicationDependencies extends Base<ApplicationData> implements ApplicationData {

	}

	public static class WorkspaceDependencies extends Base<WorkspaceData> implements WorkspaceData {

	}

	public static class ProjectDependencies extends Base<ProjectData> implements ProjectData {

	}
}
