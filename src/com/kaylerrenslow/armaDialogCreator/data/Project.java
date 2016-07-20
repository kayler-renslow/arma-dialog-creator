package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 @author Kayler
 A Project holds the its location to where all saved data is, the current display the {@link com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasEditor} is editing,
 the {@link MacroRegistry} instance, as well as //todo finish this sentence
 Created on 07/19/2016. */
public class Project {
	private String projectName;
	private final File appSaveDirectory;
	private final File projectSaveDirectory;
	
	private final ValueObserver<ArmaDisplay> editingDisplayObserver = new ValueObserver<>(new ArmaDisplay(0));
	private final MacroRegistry macroRegistry = new MacroRegistry();
	private final ResourceRegistry resourceRegistry = new ResourceRegistry();
	
	public Project(@NotNull String projectName, @NotNull File appSaveDirectory) {
		this.projectName = projectName;
		this.appSaveDirectory = appSaveDirectory;
		
		this.projectSaveDirectory = getProjectFile(projectName, appSaveDirectory);
		this.projectSaveDirectory.mkdir();
	}
	
	private File getProjectFile(String projectName, File appSaveDirectory){
		return new File(appSaveDirectory.getPath() + "\\" + projectName);
	}
		
	@NotNull
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(@NotNull String projectName) throws IOException{
		this.projectName = projectName;
		Files.move(projectSaveDirectory.toPath(), getProjectFile(projectName, appSaveDirectory).toPath(), StandardCopyOption.ATOMIC_MOVE);
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
}
