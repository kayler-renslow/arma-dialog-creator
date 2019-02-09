package com.armadialogcreator.application;

import com.armadialogcreator.util.UpdateListenerGroup;
import com.armadialogcreator.util.XmlParseException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 @author K
 @since 01/03/2019 */
public class ApplicationManager {
	private static final String PROJECT_SAVE_FILE_NAME = "project.adc";
	private static final String WORKSPACE_SAVE_FILE_NAME = "workspace.adc";
	private static final String APPLICATION_DATA_SAVE_FILE_NAME = "applicationData.adc";

	private static final ApplicationManager instance = new ApplicationManager();

	@NotNull
	public static ApplicationManager getInstance() {
		return instance;
	}

	private final List<ApplicationStateSubscriber> subs = new ArrayList<>();
	private final UpdateListenerGroup<ApplicationState> stateUpdateGroup = new UpdateListenerGroup<>();
	private boolean applicationInitialized = false;

	private volatile Project project;
	private volatile Workspace workspace;
	
	public void initializeApplication() {
		if (applicationInitialized) {
			return;
		}

		for (ApplicationStateSubscriber sub : subs) {
			sub.applicationInitializing();
		}
		stateUpdateGroup.update(ApplicationState.ApplicationInitializing);

		for (ApplicationStateSubscriber sub : subs) {
			sub.systemDataInitializing();
		}
		stateUpdateGroup.update(ApplicationState.SystemDataInitializing);

		for (ApplicationStateSubscriber sub : subs) {
			sub.systemDataLoaded();
		}
		stateUpdateGroup.update(ApplicationState.SystemDataLoaded);

		//todo load configurables

		for (ApplicationStateSubscriber sub : subs) {
			sub.applicationDataLoaded();
		}
		stateUpdateGroup.update(ApplicationState.ApplicationDataLoaded);

		applicationInitialized = true;
	}


	public void addStateSubscriber(@NotNull ApplicationStateSubscriber sub) {
		subs.add(sub);
	}

	@NotNull
	protected List<ApplicationStateSubscriber> getApplicationStateSubs() {
		return subs;
	}

	/** A way of subscribing to state changes without needing to implement {@link ApplicationStateSubscriber} */
	@NotNull
	public UpdateListenerGroup<ApplicationState> getApplicationStateUpdateGroup() {
		return stateUpdateGroup;
	}

	@NotNull
	public Workspace getCurrentWorkspace() {
		if (workspace == null) {
			throw new IllegalStateException("workspace never initialized");
		}
		return workspace;
	}

	@NotNull
	public Project getCurrentProject() {
		if (project == null) {
			throw new IllegalStateException("project never initialized");
		}
		return project;
	}

	@NotNull
	public List<ProjectPreview> getProjectsForWorkspace(@NotNull Workspace workspace) {
		List<ProjectPreview> previews = new ArrayList<>();
		//todo
		return previews;
	}

	@NotNull
	public ProjectPreview getPreviewForProjectFile(@NotNull File f) throws XmlParseException {
		//todo
		return null;
	}

	public void loadWorkspace(@NotNull File workspaceDirectory) {
		if (workspace != null) {
			workspace.getDataList().invalidate();
			for (ApplicationStateSubscriber sub : subs) {
				sub.workspaceClosed(workspace);
			}
			stateUpdateGroup.update(ApplicationState.WorkspaceClosed);
		}

		Workspace newWorkspace = new Workspace(workspaceDirectory);
		newWorkspace.initialize();

		for (ApplicationStateSubscriber sub : subs) {
			sub.workspaceInitializing(newWorkspace);
		}
		stateUpdateGroup.update(ApplicationState.WorkspaceInitializing);

		//todo load configurables

		for (ApplicationStateSubscriber sub : subs) {
			sub.workspaceDataLoaded(newWorkspace);
		}
		stateUpdateGroup.update(ApplicationState.WorkspaceDataLoaded);

		this.workspace = newWorkspace;

		for (ApplicationStateSubscriber sub : subs) {
			sub.workspaceReady(newWorkspace);
		}
		stateUpdateGroup.update(ApplicationState.WorkspaceReady);
		
	}

	public void loadProject(@NotNull ProjectDescriptor descriptor) {
		if (project != null) {
			project.getDataList().invalidate();
			for (ApplicationStateSubscriber sub : subs) {
				sub.projectClosed(project);
			}
			stateUpdateGroup.update(ApplicationState.ProjectClosed);
		}

		Project newProject = new Project(descriptor);

		for (ApplicationStateSubscriber sub : subs) {
			sub.projectInitializing(newProject);
		}
		stateUpdateGroup.update(ApplicationState.ProjectInitializing);

		//todo load configurables

		for (ApplicationStateSubscriber sub : subs) {
			sub.projectDataLoaded(newProject);
		}
		stateUpdateGroup.update(ApplicationState.ProjectDataLoaded);

		this.project = newProject;

		for (ApplicationStateSubscriber sub : subs) {
			sub.projectReady(newProject);
		}
		stateUpdateGroup.update(ApplicationState.ProjectReady);

	}

	public void saveProject() {

	}

	/**
	 Set the directory where the project is saved.
	 The project's save path will be relative to {@link Workspace#getWorkspaceDirectory()}
	 <p>
	 If the project directory name already exists, a (x) will be appended to the name

	 @param project the project to change
	 @param directoryName the new directory name for the project.
	 @throws IOException when the file system couldn't do the move
	 */
	public void setProjectDirectoryName(@NotNull Project project, @NotNull String directoryName) throws IOException {
		Workspace workspace = project.getWorkspace();
		File dest = workspace.getFileForName(directoryName);
		int c = 1;
		while (dest.exists()) {
			dest = workspace.getFileForName(directoryName + " (" + c + ")");
		}
		if (project.getFileForName(PROJECT_SAVE_FILE_NAME).exists()) {
			Files.move(project.getProjectSaveDirectory().toPath(), dest.toPath(), StandardCopyOption.ATOMIC_MOVE);
		}
		project.setProjectSaveFile(new File(dest.getAbsolutePath() + File.separator + PROJECT_SAVE_FILE_NAME));
	}

	public void closeApplication() {
		if (!applicationInitialized) {
			return;
		}

		if (workspace != null) {
			for (ApplicationStateSubscriber sub : subs) {
				sub.workspaceClosed(workspace);
			}
			stateUpdateGroup.update(ApplicationState.WorkspaceClosed);
		}

		if (project != null) {
			for (ApplicationStateSubscriber sub : subs) {
				sub.projectClosed(project);
			}
			stateUpdateGroup.update(ApplicationState.ProjectClosed);
		}

		for (ApplicationStateSubscriber sub : subs) {
			sub.applicationExit();
		}
		stateUpdateGroup.update(ApplicationState.ApplicationExit);
	}

	public void restartApplication() {
		//todo
		//		try {
		//			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		//			final File currentJar = new File(ArmaDialogCreator.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		//
		//			/* is it a jar file? */
		//			if (!currentJar.getName().endsWith(".jar")) {
		//				return;
		//			}
		//
		//			/* Build command: java -jar application.jar */
		//			final ArrayList<String> command = new ArrayList<>();
		//			command.add(javaBin);
		//			command.add("-jar");
		//			command.add(currentJar.getPath());
		//
		//			final ProcessBuilder builder = new ProcessBuilder(command);
		//			if (askToSave) {
		//				ERROR todo
		//				//ApplicationDataManager.getInstance().askSaveAll();
		//			}
		//			builder.start();
		//			System.exit(0);
		//		} catch (Exception e) {
		//			ExceptionHandler.error(e);
		//		}
	}

	@NotNull
	public static File getFileInApplicationDirectory(@NotNull String file) {
		final String append = File.separator + "Arma Dialog Creator" + File.separator + file;
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN")) {
			return new File(System.getenv("APPDATA") + append);
		} else if (OS.contains("MAC")) {
			return new File(System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + append);
		} else if (OS.contains("NUX")) {
			return new File(System.getProperty("user.home") + append);
		}
		return new File(System.getProperty("user.dir") + append);
	}
}
