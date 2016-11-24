/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
	private final GlobalResourceRegistry globalResourceRegistry;

	protected Workspace(@NotNull File workspaceDirectory) {
		if (!workspaceDirectory.isDirectory()) {
			throw new IllegalArgumentException("workspaceDirectory isn't a directory");
		}
		if (!workspaceDirectory.exists()) {
			throw new IllegalArgumentException("workspaceDirectory doesn't exist");
		}
		this.workspaceDirectory = workspaceDirectory;
		globalResourceRegistry = new GlobalResourceRegistry(this);
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
	public GlobalResourceRegistry getGlobalResourceRegistry() {
		return globalResourceRegistry;
	}
}
