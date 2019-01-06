package com.armadialogcreator.application;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 @author K
 @since 01/03/2019 */
public class Project {
	public static final String PROJECT_SAVE_FILE_NAME = "project.xml";

	private String projectName;
	private String projectDescription;
	private File projectSaveDirectory;
	private File projectSaveFile;

	private Workspace workspace;
	private final ListObserver<ProjectData> projectDataList = new ListObserver<>(new ArrayList<>());

	public Project(@NotNull ProjectDescriptor descriptor) {
		this.projectName = descriptor.getProjectName();
		this.workspace = descriptor.getWorkspace();
		this.projectSaveDirectory = descriptor.getProjectDirectory();

		projectSaveFile = descriptor.getProjectXmlFile();
	}

	@NotNull
	public ListObserver<ProjectData> getProjectDataList() {
		return projectDataList;
	}

	/** @return {@link ApplicationDataManager#getCurrentProject()} */
	@NotNull
	public static Project getCurrentProject() {
		return ApplicationDataManager.getInstance().getCurrentProject();
	}

	/**
	 Get the .xml file for the project. This file is not guaranteed to exist.
	 It is guaranteed to exist when the project is saved.

	 @return the .xml file
	 */
	@NotNull
	public File getProjectSaveFile() {
		return projectSaveFile;
	}

	/**
	 Get the path for the fileName that is based inside the {@link #getProjectSaveDirectory()}

	 @param fileName name of the file
	 @return File instance that is project_path\fileName
	 */
	public File getFileForName(@NotNull String fileName) {
		return new File(projectSaveDirectory.getPath() + File.separator + fileName);
	}

	/** @return the user's name for the project */
	@NotNull
	public String getProjectName() {
		return projectName;
	}

	/**
	 Set the project name, which is stored in the project.xml file

	 @param projectName the new project name
	 */
	public void setProjectName(@NotNull String projectName) throws IllegalArgumentException {
		this.projectName = makeProjectNameSafe(projectName);
	}

	/** @return a name that is safe to use for a directory name */
	@NotNull
	public static String makeProjectNameSafe(@NotNull String projectName) {
		final char[] illegalChars = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < projectName.length(); i++) {
			boolean illegal = false;
			for (char c : illegalChars) {
				if (projectName.charAt(i) == c) {
					illegal = true;
					break;
				}
			}
			if (!illegal) {
				sb.append(projectName.charAt(i));
			}
		}
		return sb.toString();
	}


	/**
	 Set the directory where the project is saved.
	 The project's save path will be relative to {@link Workspace#getWorkspaceDirectory()}
	 <p>
	 If the project directory name already exists, a (x) will be appended to the name

	 @param directoryName the new directory name for the project.
	 @throws IOException when the file system couldn't do the move
	 */
	public void setProjectDirectoryName(@NotNull String directoryName) throws IOException {
		File dest = workspace.getFileForName(directoryName);
		int c = 1;
		while (dest.exists()) {
			dest = workspace.getFileForName(directoryName + " (" + c + ")");
		}
		if (projectSaveFile.exists()) {
			Files.move(projectSaveDirectory.toPath(), dest.toPath(), StandardCopyOption.ATOMIC_MOVE);
		}
		this.projectSaveDirectory = dest;
		this.projectSaveFile = getFileForName(PROJECT_SAVE_FILE_NAME);
	}

	/** @return the project's user description */
	@Nullable
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 Set the project's description

	 @param projectDescription the description
	 */
	public void setProjectDescription(@Nullable String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/** @return the directory which {@link #getProjectSaveFile()} exists in */
	@NotNull
	public File getProjectSaveDirectory() {
		return projectSaveDirectory;
	}

	@Override
	@NotNull
	public String toString() {
		return projectName;
	}

	/**
	 @return the {@link Workspace} that owns this {@link Project}
	 (may be different from {@link Workspace#getWorkspace()})
	 */
	@NotNull
	public Workspace getWorkspace() {
		return workspace;
	}
}
