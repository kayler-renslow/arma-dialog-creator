package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 @author K
 @since 02/07/2019 */
public class ProjectPreview {
	private final String projectName;
	private final File saveFile;
	private final Workspace workspace;
	private final String projectDescription;
	private final File previewImageFile;
	private final long epochLastSave;

	public ProjectPreview(@NotNull String projectName, @NotNull File saveFile, @NotNull Workspace workspace, @NotNull String projectDescription,
						  @Nullable File previewImageFile, long epochLastSave) {
		this.projectName = projectName;
		this.saveFile = saveFile;
		this.workspace = workspace;
		this.projectDescription = projectDescription;
		this.previewImageFile = previewImageFile;
		this.epochLastSave = epochLastSave;
	}

	@NotNull
	public File getSaveFile() {
		return saveFile;
	}

	@NotNull
	public String getProjectName() {
		return projectName;
	}

	@NotNull
	public String getProjectDescription() {
		return projectDescription;
	}

	@Nullable
	public File getPreviewImageFile() {
		return previewImageFile;
	}

	public long getEpochLastSave() {
		return epochLastSave;
	}

	@NotNull
	public Workspace getWorkspace() {
		return workspace;
	}

	@NotNull
	ProjectDescriptor toDescriptor() {
		return new ProjectDescriptor(projectName, saveFile, workspace);
	}

	@NotNull
	public String toString() {
		return projectName;
	}
}
