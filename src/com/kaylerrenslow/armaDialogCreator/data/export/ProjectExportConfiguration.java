package com.kaylerrenslow.armaDialogCreator.data.export;

import com.kaylerrenslow.armaDialogCreator.data.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 Created by Kayler on 09/13/2016.
 */
public class ProjectExportConfiguration {

	private static final String DEFAULT_CLASS_NAME = "MyDialog";
	private static final String DEFAULT_EXPORT_CONFIG_NAME = "Default";
	private static final String UNSET_EXPORT_CONFIG_NAME = "Untitled config";

	private String exportClassName;
	private File exportDirectory;
	private Project project;
	private boolean placeAdcNotice;
	private boolean exportMacrosToFile;
	private HeaderFileType fileType;
	private String exportConfigName;

	public ProjectExportConfiguration(
			@NotNull String exportClassName,
			@NotNull File exportDirectory,
			@NotNull Project project,
			boolean placeAdcNotice,
			boolean exportMacrosToFile,
			@NotNull HeaderFileType fileType
	) {
		this(exportClassName, exportDirectory, project, placeAdcNotice, exportMacrosToFile, fileType, null);
	}

	public ProjectExportConfiguration(
			@NotNull String exportClassName,
			@NotNull File exportDirectory,
			@NotNull Project project,
			boolean placeAdcNotice,
			boolean exportMacrosToFile,
			@NotNull HeaderFileType fileType,
			@Nullable String exportConfigName
	) {
		this.exportClassName = exportClassName;
		if (!exportDirectory.exists()) {
			exportDirectory.mkdirs();
		}
		setExportDirectory(exportDirectory);
		this.project = project;
		this.placeAdcNotice = placeAdcNotice;
		this.exportMacrosToFile = exportMacrosToFile;
		this.fileType = fileType;
		setExportConfigName(exportConfigName);
	}

	@NotNull
	public String getExportConfigName() {
		return exportConfigName;
	}

	public void setExportConfigName(@Nullable String exportConfigName) {
		this.exportConfigName = exportConfigName == null ? UNSET_EXPORT_CONFIG_NAME : exportConfigName;
	}

	public boolean shouldExportMacrosToFile() {
		return exportMacrosToFile;
	}

	public boolean shouldPlaceAdcNotice() {
		return placeAdcNotice;
	}

	/** @return the directory to which to export the project to */
	@NotNull
	public File getExportDirectory() {
		return exportDirectory;
	}

	/** @return the dialog's export class name */
	@NotNull
	public String getExportClassName() {
		return exportClassName;
	}

	@NotNull
	public Project getProject() {
		return project;
	}

	@NotNull
	public HeaderFileType getHeaderFileType() {
		return fileType;
	}

	public void setFileType(@NotNull HeaderFileType fileType) {
		this.fileType = fileType;
	}

	public void setExportClassName(@NotNull String exportClassName) {
		this.exportClassName = exportClassName;
	}

	/**
	 Sets the export location

	 @throws IllegalArgumentException when exportDirectory isn't a directory
	 */
	public void setExportDirectory(@NotNull File exportDirectory) {
		if (!exportDirectory.isDirectory()) {
			throw new IllegalArgumentException("exportDirectory ('" + exportDirectory.getPath() + "') is not a directory");
		}
		this.exportDirectory = exportDirectory;
	}

	public void setProject(@NotNull Project project) {
		this.project = project;
	}

	public void setPlaceAdcNotice(boolean placeAdcNotice) {
		this.placeAdcNotice = placeAdcNotice;
	}

	public void setExportMacrosToFile(boolean exportMacrosToFile) {
		this.exportMacrosToFile = exportMacrosToFile;
	}

	@NotNull
	public static ProjectExportConfiguration getDefaultConfiguration(@NotNull Project project) {
		return new ProjectExportConfiguration(
				DEFAULT_CLASS_NAME,
				project.getProjectSaveDirectory(),
				project,
				false,
				false,
				HeaderFileType.DEFAULT,
				DEFAULT_EXPORT_CONFIG_NAME
		);
	}

	@NotNull
	public ProjectExportConfiguration copy() {
		return new ProjectExportConfiguration(this.exportClassName, exportDirectory, project, placeAdcNotice, exportMacrosToFile, fileType, exportConfigName);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ProjectExportConfiguration)) {
			return false;
		}

		ProjectExportConfiguration that = (ProjectExportConfiguration) o;

		if (placeAdcNotice != that.placeAdcNotice) {
			return false;
		}
		if (exportMacrosToFile != that.exportMacrosToFile) {
			return false;
		}
		if (!exportClassName.equals(that.exportClassName)) {
			return false;
		}
		if (!exportDirectory.equals(that.exportDirectory)) {
			return false;
		}
		if (project != that.project) {
			return false;
		}
		return fileType == that.fileType;
	}

}
