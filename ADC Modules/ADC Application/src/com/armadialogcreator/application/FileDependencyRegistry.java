package com.armadialogcreator.application;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

/**
 A FileDependencyRegistry is a location for storing file dependencies that ADC uses (image files, scripts, etc).

 @author Kayler
 @since 07/19/2016. */
public abstract class FileDependencyRegistry implements Registry {
	private final ListObserver<FileDependency> dependencyList = new ListObserver<>(new ArrayList<>());

	private final File resourcesFile;

	public FileDependencyRegistry(@NotNull File resourcesFile) {
		this.resourcesFile = resourcesFile;
	}

	@NotNull
	public File getResourcesFile() {
		return resourcesFile;
	}

	@NotNull
	public File getResourcesDirectory() {
		return resourcesFile.getParentFile();
	}

	/** Get the path for the given filename relative to the {@link #getResourcesDirectory()} path. */
	@NotNull
	public File getFileForName(@NotNull String fileName) {
		return new File(getResourcesDirectory().getAbsolutePath() + File.separator + fileName);
	}

	@NotNull
	public ListObserver<FileDependency> getDependencyList() {
		return dependencyList;
	}

	@Nullable
	public FileDependency getDependencyInstanceByFile(@NotNull File f) {
		for (FileDependency resource : dependencyList) {
			if (resource.getExternalFileObserver().equals(f)) {
				return resource;
			}
		}
		return null;
	}

	public void addDependency(@NotNull FileDependency resource) {
		dependencyList.add(resource);
	}

	public void removeDependency(@NotNull FileDependency resource) {
		dependencyList.remove(resource);
	}
}
