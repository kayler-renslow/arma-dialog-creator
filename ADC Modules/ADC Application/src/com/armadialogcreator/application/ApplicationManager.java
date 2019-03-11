package com.armadialogcreator.application;

import com.armadialogcreator.application.xml.ApplicationDataXmlReader;
import com.armadialogcreator.application.xml.ProjectDataXmlReader;
import com.armadialogcreator.application.xml.WorkspaceDataXmlReader;
import com.armadialogcreator.application.xml.XmlConfigurableWriter_2019_1_0;
import com.armadialogcreator.util.UpdateListenerGroup;
import com.armadialogcreator.util.XmlParseException;
import org.jetbrains.annotations.NotNull;

import javax.xml.transform.TransformerException;
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
	public static final String PROJECT_SAVE_FILE_NAME = "project.adc";
	public static final String WORKSPACE_SAVE_FILE_NAME = "workspace.adc";
	public static final String APPLICATION_DATA_SAVE_FILE_NAME = "applicationData.adc";

	public static final ApplicationManager instance = new ApplicationManager();

	private final List<ApplicationStateSubscriber> subs = new ArrayList<>();

	private final UpdateListenerGroup<ApplicationState> stateUpdateGroup = new UpdateListenerGroup<>();
	private boolean applicationInitialized = false;
	private volatile Project project;

	private volatile Workspace workspace;
	public void initializeADC() throws ADCDataLoadException {
		if (applicationInitialized) {
			return;
		}

		for (ApplicationStateSubscriber sub : subs) {
			System.out.println("ApplicationManager.initializeADC - Sub:" + sub.getClass().getName());
			sub.adcInitializing();
		}
		stateUpdateGroup.update(ApplicationState.ADCInitializing);

		for (ApplicationStateSubscriber sub : subs) {
			sub.systemDataInitializing();
		}
		stateUpdateGroup.update(ApplicationState.SystemDataInitializing);

		for (ApplicationStateSubscriber sub : subs) {
			sub.systemDataLoaded();
		}
		stateUpdateGroup.update(ApplicationState.SystemDataLoaded);


		for (ApplicationStateSubscriber sub : subs) {
			sub.applicationDataInitializing();
		}
		stateUpdateGroup.update(ApplicationState.ApplicationDataInitializing);

		File applicationDataSave = getApplicationDataSaveFile();
		if (applicationDataSave.exists()) {
			ApplicationDataXmlReader reader = new ApplicationDataXmlReader(applicationDataSave);
			try {
				reader.read();
			} catch (XmlParseException e) {
				throw new ADCDataLoadException(e);
			}
		}

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
			// workspace never initialized
			throw new IllegalStateException();
		}
		return workspace;
	}

	@NotNull
	public Project getCurrentProject() {
		if (project == null) {
			//project not initialized
			throw new IllegalStateException();
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

	public void loadWorkspace(@NotNull Workspace newWorkspace) throws ADCDataLoadException {
		if (workspace != null) {
			if (workspace.sameAs(newWorkspace)) {
				return;
			}
			workspace.getDataList().invalidate();
			for (ApplicationStateSubscriber sub : subs) {
				sub.workspaceClosed(workspace);
			}
			stateUpdateGroup.update(ApplicationState.WorkspaceClosed);
		}

		newWorkspace.initialize();

		for (ApplicationStateSubscriber sub : subs) {
			sub.workspaceInitializing(newWorkspace);
		}
		stateUpdateGroup.update(ApplicationState.WorkspaceInitializing);

		File workspaceSave = getWorkspaceDataSaveFile(newWorkspace);
		if (workspaceSave.exists()) {
			WorkspaceDataXmlReader reader = new WorkspaceDataXmlReader(newWorkspace, workspaceSave);
			try {
				reader.read();
			} catch (XmlParseException e) {
				throw new ADCDataLoadException(e);
			}
		}

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

	public void loadProject(@NotNull ProjectDescriptor descriptor) throws ADCDataLoadException {
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

		File projectSave = getProjectSaveFile(newProject);
		if (projectSave.exists()) {
			ProjectDataXmlReader reader = new ProjectDataXmlReader(newProject, projectSave);
			try {
				reader.read();
			} catch (XmlParseException e) {
				throw new ADCDataLoadException(e);
			}
		}

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

	public void saveProject() throws ADCDataWriteException {
		saveApplicationData();
		try {
			new XmlConfigurableWriter_2019_1_0<>(workspace, getWorkspaceDataSaveFile(workspace)).write();
		} catch (IOException | TransformerException e) {
			throw new ADCDataWriteException(e);
		}
		try {
			new XmlConfigurableWriter_2019_1_0<>(project, getProjectSaveFile(project)).write();
		} catch (IOException | TransformerException e) {
			throw new ADCDataWriteException(e);
		}
	}

	public void saveApplicationData() throws ADCDataWriteException {
		try {
			new XmlConfigurableWriter_2019_1_0<>(ApplicationDataManager.getInstance(), getApplicationDataSaveFile()).write();
		} catch (IOException | TransformerException e) {
			throw new ADCDataWriteException(e);
		}
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
			sub.ADCExit();
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
	public static File getApplicationDirectory() {
		return new File(getApplicationDirectoryPath());
	}

	@NotNull
	public static String getApplicationDirectoryPath() {
		final String append = File.separator + "Arma Dialog Creator";
		final String dotADC = File.separator + ".adc";
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN")) {
			return System.getenv("APPDATA") + append;
		} else if (OS.contains("MAC")) {
			return System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + append;
		} else if (OS.contains("NUX")) {
			return System.getProperty("user.home") + dotADC + append;
		}
		return System.getProperty("user.dir") + dotADC + append;
	}

	@NotNull
	public static File getFileInApplicationDirectory(@NotNull String file) {
		return new File(getApplicationDirectoryPath() + File.separator + file);
	}

	@NotNull
	public static File getProjectSaveFile(@NotNull Workspace workspace, @NotNull String projectName) {
		return workspace.getFileForName(projectName + File.separator + PROJECT_SAVE_FILE_NAME);
	}

	@NotNull
	public static File getProjectSaveFile(@NotNull Project project) {
		return project.getFileForName(PROJECT_SAVE_FILE_NAME);
	}

	@NotNull
	public static File getWorkspaceDataSaveFile(@NotNull Workspace workspace) {
		return workspace.getFileInAdcDirectory(ApplicationManager.WORKSPACE_SAVE_FILE_NAME);
	}

	@NotNull
	public static File getFileInApplicationDataDirectory(@NotNull String file) {
		return ApplicationManager.getFileInApplicationDirectory("applicationData" + File.separator + file);
	}

	@NotNull
	public static File getApplicationDataSaveFile() {
		return getFileInApplicationDataDirectory(APPLICATION_DATA_SAVE_FILE_NAME);
	}
}
