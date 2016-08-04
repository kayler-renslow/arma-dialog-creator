package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 @author Kayler
 A Project holds the its location to where all saved data is, the current display the {@link com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasEditor} is editing,
 the {@link MacroRegistry} instance, as well as all ExternalResources.
 Created on 07/19/2016. */
public class Project {
	private String projectName;
	private String projectDescription;
	private final File appSaveDirectory;
	private final File projectSaveDirectory;
	
	private final ValueObserver<ArmaDisplay> editingDisplayObserver = new ValueObserver<>(new ArmaDisplay(0));
	private final MacroRegistry macroRegistry = new MacroRegistry();
	private final ResourceRegistry resourceRegistry = new ResourceRegistry();
	
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
	
	@Override
	public String toString() {
		return projectName;
	}
}
