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

import java.io.File;

/**
 @author Kayler
 @since 11/23/2016 */
public class ProjectInfo {
	private final String projectName;
	private final File projectXmlFile;
	private final File projectDirectory;

	public ProjectInfo(@NotNull String projectName, @NotNull File projectDirectory) {
		this(projectName, new File(projectDirectory.getPath() + "/" + Project.PROJECT_SAVE_FILE_NAME), projectDirectory);
	}

	public ProjectInfo(@NotNull String projectName, @NotNull File projectXmlFile, @NotNull File projectDirectory) {
		this.projectName = projectName;
		this.projectXmlFile = projectXmlFile;
		this.projectDirectory = projectDirectory;
	}

	@NotNull
	public File getProjectXmlFile() {
		return projectXmlFile;
	}

	@NotNull
	public String getProjectName() {
		return projectName;
	}

	@NotNull
	public File getProjectDirectry() {
		return projectDirectory;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ProjectInfo that = (ProjectInfo) o;

		if (!projectName.equals(that.projectName)) {
			return false;
		}
		return projectDirectory.equals(that.projectDirectory);
	}

	public String toString() {
		return projectName;
	}

}
