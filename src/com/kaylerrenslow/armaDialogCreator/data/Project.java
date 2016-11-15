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

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 A Project holds the its location to where all saved data is, the current display the {@link com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasEditor} is editing,
 the {@link MacroRegistry} instance, as well as all ExternalResources.

 @author Kayler
 @since 07/19/2016. */
public class Project {
	private String projectName;
	private String projectDescription;
	private final File appSaveDirectory;
	private final File projectSaveDirectory;

	private final ValueObserver<ArmaDisplay> editingDisplayObserver = new ValueObserver<>(new ArmaDisplay());
	private final MacroRegistry macroRegistry = new MacroRegistry();
	private final ResourceRegistry resourceRegistry = new ResourceRegistry();
	private final CustomControlClassRegistry controlRegistry = new CustomControlClassRegistry();
	private ProjectExportConfiguration exportConfiguration;

	public Project(@Nullable String projectName, @NotNull File appSaveDirectory) {
		if (projectName == null || projectName.trim().length() == 0) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			int hour = Calendar.getInstance().get(Calendar.HOUR);
			int minute = Calendar.getInstance().get(Calendar.MINUTE);
			int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
			String date = String.format("%d-%d-%d %d-%d%s", year, month, day, hour, minute, am_pm == Calendar.AM ? "am" : "pm");
			projectName = "untitled " + date;
		}

		this.projectName = projectName;
		this.appSaveDirectory = appSaveDirectory;

		this.projectSaveDirectory = getProjectFile(projectName, appSaveDirectory);

		exportConfiguration = ProjectExportConfiguration.getDefaultConfiguration(this);
	}

	private File getProjectFile(String projectName, File appSaveDirectory) {
		return new File(appSaveDirectory.getPath() + "\\" + projectName);
	}

	/**
	 Get the path for the fileName that is based inside the Project path

	 @param fileName name of the file
	 @return File instance that is project_path\fileName
	 */
	public File getFileForName(@NotNull String fileName) {
		return new File(projectSaveDirectory.getPath() + "\\" + fileName);
	}

	@NotNull
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(@NotNull String projectName) throws IOException {
		this.projectName = projectName;
		//		Files.move(projectSaveDirectory.toPath(), getProjectFile(projectName, appSaveDirectory).toPath(), StandardCopyOption.ATOMIC_MOVE);
	}

	@Nullable
	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(@Nullable String projectDescription) {
		this.projectDescription = projectDescription;
	}

	@NotNull
	public File getProjectSaveDirectory() {
		return projectSaveDirectory;
	}

	/** Get the display that the dialog creator is editing right now. */
	@NotNull
	public ArmaDisplay getEditingDisplay() {
		return editingDisplayObserver.getValue();
	}

	@NotNull
	public ValueObserver<ArmaDisplay> getEditingDisplayObserver() {
		return editingDisplayObserver;
	}

	/** Set the display that the dialog creator is to edit (notifies the valueObserver for the display as well). */
	public void setEditingDisplay(@NotNull ArmaDisplay display) {
		this.editingDisplayObserver.updateValue(display);
	}

	@NotNull
	public MacroRegistry getMacroRegistry() {
		return macroRegistry;
	}

	@NotNull
	public ResourceRegistry getResourceRegistry() {
		return resourceRegistry;
	}

	@NotNull
	public CustomControlClassRegistry getCustomControlClassRegistry() {
		return controlRegistry;
	}

	@Override
	@NotNull
	public String toString() {
		return projectName;
	}

	@NotNull
	public ProjectExportConfiguration getExportConfiguration() {
		return exportConfiguration;
	}

	public void setExportConfiguration(@NotNull ProjectExportConfiguration exportConfiguration) {
		this.exportConfiguration = exportConfiguration;
	}

	@Nullable
	public ControlClass findControlClassByName(@NotNull String className) {
		ControlClass controlClass = getEditingDisplay().findControlByClassName(className);
		if (controlClass != null) {
			return controlClass;
		}

		return controlRegistry.findControlClassByName(className);
	}
}
