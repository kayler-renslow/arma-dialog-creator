package com.armadialogcreator.application;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

/**
 A {@link Workspace} is a directory for holding a set of {@link Project}s.

 @author Kayler
 @see Project
 @since 11/23/2016 */
public class Workspace implements ADCDataListManager<WorkspaceData> {
	public static final File DEFAULT_WORKSPACE_DIRECTORY = new File(System.getProperty("user.home") + File.separator + "Arma Dialog Creator");

	static {
		if (!DEFAULT_WORKSPACE_DIRECTORY.exists()) {
			DEFAULT_WORKSPACE_DIRECTORY.mkdirs();
		}
	}

	private final File workspaceDirectory;
	private final File adcDirectory;
	private final ListObserver<WorkspaceData> dataList = new ListObserver<>(new ArrayList<>());

	/**
	 Construct a new instance

	 @param workspaceDirectory the directory for the workspace. The directory must exist, or an exception will be thrown
	 */
	public Workspace(@NotNull File workspaceDirectory) {
		if (!workspaceDirectory.exists()) {
			throw new IllegalArgumentException();
		}
		if (!workspaceDirectory.isDirectory()) {
			throw new IllegalArgumentException();
		}
		this.workspaceDirectory = workspaceDirectory;
		adcDirectory = getFileForName(".adc");
	}

	void initialize() {
		if (!adcDirectory.exists()) {
			adcDirectory.mkdirs();
		}
		File cacheDirectory = getFileInAdcDirectory("cache");
		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}
	}

	@Override
	@NotNull
	public ListObserver<WorkspaceData> getDataList() {
		return dataList;
	}

	/** @return the File that is the workspace's directory that contains a bunch of {@link Project}'s */
	@NotNull
	public File getWorkspaceDirectory() {
		return workspaceDirectory;
	}

	/**
	 Get the current {@link Workspace} instance that is saved in {@link ApplicationManager#getCurrentWorkspace()}.

	 @return instance
	 */
	@NotNull
	public static Workspace getWorkspace() {
		return ApplicationManager.instance.getCurrentWorkspace();
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
		return workspaceDirectory.getPath() + File.separator + fileName;
	}

	/** @return file in "{@link #getWorkspaceDirectory()}/.adc" */
	@NotNull
	public File getAdcDirectory() {
		return adcDirectory;
	}

	/** @return file in "{@link #getWorkspaceDirectory()}/.adc/<code>fileName</code>" */
	@NotNull
	public File getFileInAdcDirectory(@NotNull String fileName) {
		return getFileForName(".adc" + File.separator + fileName);
	}

	/** @return file in "{@link #getWorkspaceDirectory()}/.adc/cache/<code>fileName</code>" */
	@NotNull
	public File getFileInCacheDirectory(@NotNull String fileName) {
		return getFileInAdcDirectory("cache" + File.separator + fileName);
	}

	/**
	 This is different from {@link #equals(Object)} as this checks the underlying {@link #getWorkspaceDirectory()}
	 rather than the content of this object

	 @return true if this workspace has the same {@link #getWorkspaceDirectory()} as the other
	 */
	public boolean sameAs(@NotNull Workspace other) {
		return workspaceDirectory.getAbsoluteFile().equals(other.workspaceDirectory.getAbsoluteFile());
	}

}
