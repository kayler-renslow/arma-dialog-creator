package com.armadialogcreator.data;

import com.armadialogcreator.application.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 Created by Kayler on 09/13/2016.
 */
public class ClassicProjectExportConfiguration {

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
	private String customControlClassesExportFileName;

	public ClassicProjectExportConfiguration(
			@NotNull String exportClassName,
			@NotNull File exportDirectory,
			@NotNull Project project,
			boolean placeAdcNotice,
			boolean exportMacrosToFile,
			@NotNull HeaderFileType fileType
	) {
		this(exportClassName, exportDirectory, project, placeAdcNotice, exportMacrosToFile, fileType, null);
	}

	public ClassicProjectExportConfiguration(
			@NotNull String exportClassName,
			@NotNull File exportDirectory,
			@NotNull Project project,
			boolean placeAdcNotice,
			boolean exportMacrosToFile,
			@NotNull HeaderFileType fileType,
			@Nullable String exportConfigName
	) {
		this.exportClassName = exportClassName;
		this.exportDirectory = exportDirectory;
		this.project = project;
		this.placeAdcNotice = placeAdcNotice;
		this.exportMacrosToFile = exportMacrosToFile;
		this.fileType = fileType;
		setExportConfigName(exportConfigName);

		this.customControlClassesExportFileName = "CustomControlClasses" + fileType.getExtension();
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

	@NotNull
	public File getFileForExportDirectory(@NotNull String fileName) {
		return new File(exportDirectory.getAbsolutePath() + File.separator + fileName);
	}

	/** @return the file name that a {@link Project}'s {@link CustomControlClass} instances will be written to. */
	@NotNull
	public String getCustomClassesExportFileName() {
		return customControlClassesExportFileName;
	}

	public void setCustomControlClassesExportFile(@NotNull String customControlClassesExportFileName) {
		this.customControlClassesExportFileName = customControlClassesExportFileName;
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
	public static ClassicProjectExportConfiguration newDefaultConfiguration(@NotNull Project project) {
		return new ClassicProjectExportConfiguration(
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
	public ClassicProjectExportConfiguration copy() {
		return new ClassicProjectExportConfiguration(this.exportClassName, exportDirectory, project, placeAdcNotice, exportMacrosToFile, fileType, exportConfigName);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClassicProjectExportConfiguration)) {
			return false;
		}

		ClassicProjectExportConfiguration that = (ClassicProjectExportConfiguration) o;

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
