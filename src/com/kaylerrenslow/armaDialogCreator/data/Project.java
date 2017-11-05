package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 A Project holds the its location to where all saved data is, the current display the {@link com.kaylerrenslow.armaDialogCreator.gui.main.editor.UICanvasEditor} is editing,
 the {@link ProjectMacroRegistry} instance, as well as all {@link ExternalResource}s.
 <p>
 A project needs an {@link ApplicationData} instance for the project's {@link com.kaylerrenslow.armaDialogCreator.control.DefaultValueProvider} instance.

 @author Kayler
 @since 07/19/2016. */
public class Project implements SpecificationRegistry {
	public static final String PROJECT_SAVE_FILE_NAME = "project.xml";

	private final ApplicationData applicationData;
	private String projectName;
	private String projectDescription;
	private File projectSaveDirectory;
	private File projectSaveFile;

	private final ValueObserver<ArmaDisplay> editingDisplayObserver;
	private final ProjectMacroRegistry macroRegistry;
	private final ResourceRegistry resourceRegistry;
	private final CustomControlClassRegistry projectCustomControlClassRegistry;
	private ProjectExportConfiguration exportConfiguration;

	private ProjectDefaultValueProvider defaultValueProvider;
	private StringTable stringTable;
	private Language defaultLanguage;
	private CustomControlClassRegistry workspaceCustomControlClassRegistry;
	private Workspace workspace;

	/**
	 Create a new instance.

	 @param applicationData the {@link ApplicationData} instance to use. See class level doc for more info.
	 @param info info to create {@link Project} with
	 */
	public Project(@NotNull ApplicationData applicationData, @NotNull ProjectInfo info) {
		this.applicationData = applicationData;
		this.projectName = info.getProjectName();
		this.workspace = info.getWorkspace();
		this.projectSaveDirectory = info.getProjectDirectory();

		exportConfiguration = ProjectExportConfiguration.newDefaultConfiguration(this);

		editingDisplayObserver = new ValueObserver<>(new ArmaDisplay());
		macroRegistry = new ProjectMacroRegistry();
		resourceRegistry = new ResourceRegistry(this);
		projectCustomControlClassRegistry = new CustomControlClassRegistry(CustomControlClass.Scope.Project, this);
		workspaceCustomControlClassRegistry = new CustomControlClassRegistry(CustomControlClass.Scope.Workspace, this);

		projectSaveFile = info.getProjectXmlFile();
	}

	/** @return {@link ApplicationDataManager#getCurrentProject()} */
	@NotNull
	public static Project getCurrentProject() {
		return ApplicationDataManager.getInstance().getCurrentProject();
	}

	/**
	 Get the .xml file for the project. This file is not guaranteed to exist.
	 It is guaranteed to exist when the project is saved.

	 @return the .xml file
	 */
	@NotNull
	public File getProjectSaveFile() {
		return projectSaveFile;
	}

	/** @return the {@link StringTable} instance for the project, or null if not set */
	@Nullable
	public StringTable getStringTable() {
		return stringTable;
	}

	/**
	 Set the {@link StringTable} instance for the project or remove it

	 @param stringTable the instance to use
	 */
	public void setStringTable(@Nullable StringTable stringTable) {
		this.stringTable = stringTable;
		if (stringTable != null) {
			stringTable.setDefaultLanguage(defaultLanguage);
		}
	}

	/**
	 Get the path for the fileName that is based inside the {@link #getProjectSaveDirectory()}

	 @param fileName name of the file
	 @return File instance that is project_path\fileName
	 */
	public File getFileForName(@NotNull String fileName) {
		return new File(projectSaveDirectory.getPath() + "\\" + fileName);
	}

	/** @return the user's name for the project */
	@NotNull
	public String getProjectName() {
		return projectName;
	}

	/**
	 Set the project name, which is stored in the project.xml file

	 @param projectName the new project name
	 */
	public void setProjectName(@NotNull String projectName) throws IllegalArgumentException {
		this.projectName = makeProjectNameSafe(projectName);
	}

	/** @return a name that is safe to use for a directory name */
	@NotNull
	public static String makeProjectNameSafe(@NotNull String projectName) {
		final char[] illegalChars = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < projectName.length(); i++) {
			boolean illegal = false;
			for (char c : illegalChars) {
				if (projectName.charAt(i) == c) {
					illegal = true;
					break;
				}
			}
			if (!illegal) {
				sb.append(projectName.charAt(i));
			}
		}
		return sb.toString();
	}


	/**
	 Set the directory where the project is saved.
	 The project's save path will be relative to {@link Workspace#getWorkspaceDirectory()}
	 <p>
	 If the project directory name already exists, a (x) will be appended to the name

	 @param directoryName the new directory name for the project.
	 @throws IOException when the file system couldn't do the move
	 */
	public void setProjectDirectoryName(@NotNull String directoryName) throws IOException {
		File dest = workspace.getFileForName(directoryName);
		int c = 1;
		while (dest.exists()) {
			dest = workspace.getFileForName(directoryName + " (" + c + ")");
		}
		if (projectSaveFile.exists()) {
			Files.move(projectSaveDirectory.toPath(), dest.toPath(), StandardCopyOption.ATOMIC_MOVE);
		}
		this.projectSaveDirectory = dest;
		this.projectSaveFile = getFileForName(PROJECT_SAVE_FILE_NAME);
	}

	/** @return the project's user description */
	@Nullable
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 Set the project's description

	 @param projectDescription the description
	 */
	public void setProjectDescription(@Nullable String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/** @return the directory which {@link #getProjectSaveFile()} exists in */
	@NotNull
	public File getProjectSaveDirectory() {
		return projectSaveDirectory;
	}

	/** @return the display that the dialog creator is editing right now. */
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

	/**
	 This returns a {@link CustomControlClassRegistry} where each {@link CustomControlClass} that is only for this
	 project. The {@link CustomControlClass} instances <b>are</b> saved within the project's save file.

	 @return the registry
	 @see #getWorkspaceCustomControlClassRegistry()
	 */
	@NotNull
	public CustomControlClassRegistry getProjectCustomControlClassRegistry() {
		return projectCustomControlClassRegistry;
	}

	/**
	 This returns a {@link CustomControlClassRegistry} where each {@link CustomControlClass} that is shared across a
	 {@link Workspace}. The {@link CustomControlClass} instances are not saved within the project's save file.

	 @return the registry
	 @see #getProjectCustomControlClassRegistry()
	 */
	@NotNull
	public CustomControlClassRegistry getWorkspaceCustomControlClassRegistry() {
		return workspaceCustomControlClassRegistry;
	}

	/**
	 @return all {@link CustomControlClass} instances across {@link #getProjectCustomControlClassRegistry()} and
	 {@link #getWorkspaceCustomControlClassRegistry()} in a new list
	 */
	@NotNull
	public List<CustomControlClass> getAllCustomControlClasses() {
		ReadOnlyList<CustomControlClass> pList = getProjectCustomControlClassRegistry().getControlClassList();
		ReadOnlyList<CustomControlClass> wList = getWorkspaceCustomControlClassRegistry().getControlClassList();
		List<CustomControlClass> list = new ArrayList<>(pList.size() + wList.size());
		list.addAll(pList);
		list.addAll(wList);

		return list;
	}

	/**
	 Adds a {@link CustomControlClass} to its appropriate {@link ControlClassRegistry} dependent upon
	 {@link CustomControlClass#getScope()}

	 @param customControlClass to add
	 */
	public void addCustomControlClass(@NotNull CustomControlClass customControlClass) {
		switch (customControlClass.getScope()) {
			case Project: {
				projectCustomControlClassRegistry.addControlClass(customControlClass);
				return;
			}
			case Workspace: {
				workspaceCustomControlClassRegistry.addControlClass(customControlClass);
				return;
			}
			default:
				throw new IllegalStateException("unhandled scope:" + customControlClass.getScope());
		}
	}

	public void removeCustomControlClass(@NotNull CustomControlClass customControlClass) {
		switch (customControlClass.getScope()) {
			case Project: {
				projectCustomControlClassRegistry.removeControlClass(customControlClass);
				return;
			}
			case Workspace: {
				workspaceCustomControlClassRegistry.removeControlClass(customControlClass);
				return;
			}
			default:
				throw new IllegalStateException("unhandled scope:" + customControlClass.getScope());
		}
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

	/**
	 Will get the {@link ControlClass} instance by name. First will search {@link #getProjectCustomControlClassRegistry()}.
	 If that returns null, will search {@link #getEditingDisplay()} via {@link ArmaDisplay#findControlByClassName(String)}*

	 @return the matched class, or null if couldn't be located
	 */
	@Override
	@Nullable
	public ControlClass findControlClassByName(@NotNull String className) {
		ControlClass controlClass = projectCustomControlClassRegistry.findControlClassByName(className);
		if (controlClass != null) {
			return controlClass;
		}
		controlClass = workspaceCustomControlClassRegistry.findControlClassByName(className);
		if (controlClass != null) {
			return controlClass;
		}
		return getEditingDisplay().findControlByClassName(className);
	}

	@Nullable
	@Override
	public Macro findMacroByKey(@NotNull String macroKey) {
		if (stringTable != null && macroKey.toLowerCase().startsWith("str_")) {
			Macro m = stringTable.getKeyById(macroKey);
			if (m != null) {
				return m;
			}
		}
		return getMacroRegistry().findMacroByKey(macroKey);
	}

	@Nullable
	@Override
	public SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup) {
		return defaultValueProvider.getDefaultValue(lookup);
	}

	@Override
	public void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch, @Nullable Context context) {
		if (defaultValueProvider == null) {
			defaultValueProvider = new ProjectDefaultValueProvider(applicationData);
		}
		defaultValueProvider.prefetchValues(tofetch, context);
	}

	@Override
	public void cleanup() {
		defaultValueProvider.cleanup();
	}

	/**
	 Set the default language for {@link #getStringTable()}. If {@link #getStringTable()} is null, the value will be cached and when {@link #setStringTable(StringTable)} is invoked,
	 {@link StringTable#setDefaultLanguage(Language)} will automatically be invoked.

	 @param language default language
	 */
	public void setDefaultLanguage(@Nullable Language language) {
		this.defaultLanguage = language;
		if (stringTable != null) {
			stringTable.setDefaultLanguage(defaultLanguage);
		}
	}

	/** @return the file that stores {@link Project#getWorkspaceCustomControlClassRegistry()} */
	@NotNull
	public File getWorkspaceCustomControlClassesFile() {
		return workspace.getFileInAdcDirectory("custom_controls.xml");
	}

	/**
	 @return the {@link Workspace} that owns this {@link Project}
	 (may be different from {@link Workspace#getWorkspace()})
	 */
	@NotNull
	public Workspace getWorkspace() {
		return workspace;
	}
}
