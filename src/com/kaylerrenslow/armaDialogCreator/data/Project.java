package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.SpecificationRegistry;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 A Project holds the its location to where all saved data is, the current display the {@link com.kaylerrenslow.armaDialogCreator.gui.main.editor.UICanvasEditor} is editing,
 the {@link ProjectMacroRegistry} instance, as well as all ExternalResources.

 @author Kayler
 @since 07/19/2016. */
public class Project implements SpecificationRegistry {
	public static final String PROJECT_SAVE_FILE_NAME = "project.xml";

	private String projectName;
	private String projectDescription;
	private final File projectSaveDirectory;
	private File projectSaveFile;

	private final ValueObserver<ArmaDisplay> editingDisplayObserver;
	private final ProjectMacroRegistry macroRegistry;
	private final ResourceRegistry resourceRegistry;
	private final CustomControlClassRegistry controlRegistry;
	private ProjectExportConfiguration exportConfiguration;

	private ProjectDefaultValueProvider defaultValueProvider;

	public Project(@NotNull ProjectInfo info) {
		this.projectName = info.getProjectName();
		this.projectSaveDirectory = info.getProjectDirectry();

		exportConfiguration = ProjectExportConfiguration.getDefaultConfiguration(this);

		editingDisplayObserver = new ValueObserver<>(new ArmaDisplay());
		macroRegistry = new ProjectMacroRegistry();
		resourceRegistry = new ResourceRegistry(this);
		controlRegistry = new CustomControlClassRegistry();

		projectSaveFile = info.getProjectXmlFile();
	}

	@NotNull
	public static Project getCurrentProject() {
		return ApplicationDataManager.getInstance().getCurrentProject();
	}

	/** Get the .xml file for the project */
	@NotNull
	public File getProjectSaveFile() {
		return projectSaveFile;
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
	public ProjectMacroRegistry getMacroRegistry() {
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

	@Override
	@Nullable
	public ControlClass findControlClassByName(@NotNull String className) {
		ControlClass controlClass = getEditingDisplay().findControlByClassName(className);
		if (controlClass != null) {
			return controlClass;
		}

		return controlRegistry.findControlClassByName(className);
	}

	@Nullable
	@Override
	public Macro findMacroByKey(@NotNull String macroKey) {
		return getMacroRegistry().findMacroByKey(macroKey);
	}

	@Nullable
	@Override
	public SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup) {
		return defaultValueProvider.getDefaultValue(lookup);
	}

	@Override
	public void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch) {
		defaultValueProvider = new ProjectDefaultValueProvider();
		defaultValueProvider.prefetchValues(tofetch);
	}
}
