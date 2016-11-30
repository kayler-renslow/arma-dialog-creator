package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 A ResourceRegistry is a per-project storage of all resources the project needs.

 @author Kayler
 @since 07/19/2016. */
public class ResourceRegistry {
	private final List<ExternalResource> externalResourceList = new ArrayList<>();

	public static final String RESOURCES_FILE_NAME = ".resources";

	private final File resourcesFile;

	protected ResourceRegistry(@NotNull Project project) {
		resourcesFile = project.getFileForName(RESOURCES_FILE_NAME);
	}

	protected ResourceRegistry(@NotNull File resourcesFile) {
		this.resourcesFile = resourcesFile;
	}


	@NotNull
	public File getResourcesFile() {
		return resourcesFile;
	}

	/** Get the path for the given filename relative to the {@link #getResourcesFile()} path. */
	public File getResourcesFilePathForName(@NotNull String fileName) {
		return new File(resourcesFile + "/" + fileName);
	}

	public List<ExternalResource> getExternalResourceList() {
		return externalResourceList;
	}

}
