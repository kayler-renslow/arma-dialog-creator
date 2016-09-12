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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 A ResourceRegistry is a per-project storage of all resources the project needs.
 Created on 07/19/2016. */
public class ResourceRegistry {
	private final List<ExternalResource> externalResourceList = new ArrayList<>();

	public static final String RESOURCES_FILE_NAME = ".resources";
	private static final File resourcesFile = new File(ApplicationDataManager.getInstance().getAppSaveDataDirectory() + "/" + RESOURCES_FILE_NAME);

	public static GlobalResourceRegistry getGlobalRegistry(){
		return GlobalResourceRegistry.getInstance();
	}

	static {
		if (!resourcesFile.exists()) {
			resourcesFile.mkdirs();
		} else {
			if (!resourcesFile.isDirectory()) {
				throw new IllegalStateException("resourcesFile is not a directory");
			}
		}
	}

	public static File getResourcesFile() {
		return resourcesFile;
	}

	/** Get the path for the given filename relative to the {@link #getResourcesFile()} path ({@link ApplicationDataManager#getAppSaveDataDirectory()}/.resources/fileName). */
	public static File getResourcesFilePathForName(String fileName) {
		return new File(resourcesFile + "/" + fileName);
	}

	ResourceRegistry() {
	}

	public List<ExternalResource> getExternalResourceList() {
		return externalResourceList;
	}

	public static class GlobalResourceRegistry extends ResourceRegistry{
		private static final GlobalResourceRegistry instance = new GlobalResourceRegistry();

		public static GlobalResourceRegistry getInstance() {
			return instance;
		}

		private final File globalResourcesXmlFile = getResourcesFilePathForName("global-resources.xml");

		private GlobalResourceRegistry() {
		}

		/** {@link ApplicationDataManager#getAppSaveDataDirectory()}/.resources/global-resources.xml*/
		public File getGlobalResourcesXmlFile(){
			return globalResourcesXmlFile;
		}
	}
}
