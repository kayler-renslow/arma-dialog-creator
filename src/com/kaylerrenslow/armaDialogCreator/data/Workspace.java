package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 A {@link Workspace} is a directory for holding a set of {@link Project}s. It also has its own {@link ResourceRegistry},
 which is accessible via {@link WorkspaceResourceRegistry#getInstance()} or {@link #getGlobalResourceRegistry()}.

 @author Kayler
 @see Project
 @since 11/23/2016 */
public class Workspace {
	public static final File DEFAULT_WORKSPACE_DIRECTORY = new File(FileSystemView.getFileSystemView().getDefaultDirectory() + "/Arma Dialog Creator");

	private final File workspaceDirectory;
	private final WorkspaceResourceRegistry globalResourceRegistry;
	private final File adcDirectory;

	/**
	 Construct a new instance

	 @param workspaceDirectory the directory for the workspace. The directory must exist, or an exception will be thrown
	 */
	protected Workspace(@NotNull File workspaceDirectory) {
		if (!workspaceDirectory.isDirectory()) {
			throw new IllegalArgumentException("workspaceDirectory isn't a directory");
		}
		if (!workspaceDirectory.exists()) {
			throw new IllegalArgumentException("workspaceDirectory doesn't exist");
		}
		this.workspaceDirectory = workspaceDirectory;
		globalResourceRegistry = new WorkspaceResourceRegistry(this);

		adcDirectory = getFileForName(".adc");
		adcDirectory.mkdirs();
	}

	/** @return the File that is the workspace's directory that contains a bunch of {@link Project}'s */
	@NotNull
	public File getWorkspaceDirectory() {
		return workspaceDirectory;
	}

	/**
	 Get the current {@link Workspace} instance that is saved in {@link ApplicationDataManager#getWorkspace()}.

	 @return instance
	 */
	@NotNull
	public static Workspace getWorkspace() {
		return ApplicationDataManager.getInstance().getWorkspace();
	}

	/**
	 @return a File with {@link #getWorkspaceDirectory()} as the prefixed path.
	 @see #getFilePathForName(String)
	 */
	@NotNull
	public File getFileForName(@NotNull String fileName) {
		return new File(getFilePathForName(fileName));
	}

	/**
	 @return a file's path with {@link #getWorkspaceDirectory()} as the prefixed path.
	 @see #getFileForName(String)
	 */
	@NotNull
	public String getFilePathForName(@NotNull String fileName) {
		return workspaceDirectory.getPath() + "\\" + fileName;
	}

	/**
	 Get a {@link ResourceRegistry} that is contained in this {@link Workspace#getWorkspaceDirectory()}.

	 @return the instance
	 */
	@NotNull
	public WorkspaceResourceRegistry getGlobalResourceRegistry() {
		return globalResourceRegistry;
	}

	/** @return file in "{@link #getWorkspaceDirectory()}/.adc" */
	@NotNull
	public File getAdcDirectory() {
		return adcDirectory;
	}

	/** @return file in "{@link #getWorkspaceDirectory()}/.adc/<code>fileName</code>" */
	@NotNull
	public File getFileInAdcDirectory(@NotNull String fileName) {
		return getFileForName(".adc/" + fileName);
	}
}
