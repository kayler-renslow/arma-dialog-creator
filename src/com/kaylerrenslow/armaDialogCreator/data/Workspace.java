package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 @author Kayler
 @since 11/23/2016 */
public class Workspace {
	public static final File DEFAULT_WORKSPACE_DIRECTORY = new File(FileSystemView.getFileSystemView().getDefaultDirectory() + "/Arma Dialog Creator");

	private final File workspaceDirectory;
	private final WorkspaceResourceRegistry globalResourceRegistry;

	protected Workspace(@NotNull File workspaceDirectory) {
		if (!workspaceDirectory.isDirectory()) {
			throw new IllegalArgumentException("workspaceDirectory isn't a directory");
		}
		if (!workspaceDirectory.exists()) {
			throw new IllegalArgumentException("workspaceDirectory doesn't exist");
		}
		this.workspaceDirectory = workspaceDirectory;
		globalResourceRegistry = new WorkspaceResourceRegistry(this);
	}

	@NotNull
	public File getWorkspaceDirectory() {
		return workspaceDirectory;
	}

	/**
	 Get the current {@link Workspace} instance

	 @return instance
	 */
	@NotNull
	public static Workspace getWorkspace() {
		return ApplicationDataManager.getInstance().getWorkspace();
	}

	@NotNull
	public File getFileForName(@NotNull String fileName) {
		return new File(getFilePathForName(fileName));
	}

	@NotNull
	public String getFilePathForName(@NotNull String fileName) {
		return workspaceDirectory.getPath() + "\\" + fileName;
	}

	@NotNull
	public WorkspaceResourceRegistry getGlobalResourceRegistry() {
		return globalResourceRegistry;
	}
}
