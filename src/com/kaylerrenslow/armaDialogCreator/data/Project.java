package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.XmlWriteException;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 @author Kayler
 A Project holds the its location to where all saved data is, the current display the {@link com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasEditor} is editing,
 the {@link MacroRegistry} instance, as well as all ExternalResources.
 Created on 07/19/2016. */
public class Project {
	private String projectName;
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
			String date = String.format("%d-%d-%d %d-%d", year, month, day, hour, minute);
			projectName = "untitled " + date;
		}
		
		this.projectName = projectName;
		this.appSaveDirectory = appSaveDirectory;
		
		this.projectSaveDirectory = getProjectFile(projectName, appSaveDirectory);
	}
	
	public Project(@NotNull File projectFile, @NotNull File appSaveDirectory) {
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
	
	public void save() {
		//		if(!this.projectSaveDirectory.exists()){
		//			projectSaveDirectory.mkdir();
		//		}
		File projectSaveXml = getFileForName("project.xml");
		
	}
	
	private static class ProjectSaveWriter {
		/**
		 Current version of when the project was saved. If the export ever changes format, this number should change as well.
		 This is meant for ensuring the project is loaded correctly, despite an older save format (be sure to store each loader for each save version).
		 */
		private static final int SAVE_VERSION = 1;
		
		public ProjectSaveWriter(@NotNull Project project, @NotNull File projectSaveXml) throws IOException {
			write(project, projectSaveXml);
		}
		
		private void write(@NotNull Project project, @NotNull File projectSaveXml) throws IOException {
			FileOutputStream fos = new FileOutputStream(projectSaveXml);
			
			fos.write("<?xml version='1.0' encoding='UTF-8' ?>".getBytes());
			fos.write(String.format("<project name='%s' save-version='%d'>", project.getProjectName(), SAVE_VERSION).getBytes());
			
			writeMacros(fos, project);
			writeDisplay(fos, project.getEditingDisplay());
			
			fos.write("</project>".getBytes());
			
			
			fos.flush();
			fos.close();
		}
		
		private void writeDisplay(@NotNull FileOutputStream fos, @NotNull ArmaDisplay editingDisplay) throws IOException {
			fos.write(String.format("<display idd='%d'>", editingDisplay.getIdd()).getBytes());
			
			for (ArmaControl control : editingDisplay.getControls()) {
				writeControl(fos, control);
			}
			
			fos.write("</display>".getBytes());
		}
		
		private void writeControl(@NotNull FileOutputStream fos, @NotNull ArmaControl control) throws IOException {
			final String controlGroupStr = "control-group";
			final String controlStr = "control";
			boolean controlGroup = control instanceof ArmaControlGroup;
			
			fos.write(String.format("<%s idc='%d' renderer='%s' control-type-id='%d' class-name='%s' extend-class='%s'>",
					controlGroup ? controlGroupStr : controlStr,
					control.getIdc(),
					control.getRenderer().getClass().getName(),
					control.getType().typeId,
					control.getClassName(),
					control.getExtendControl() != null ? control.getExtendControl().getClassName() : ""
			).getBytes());
			
			//write control properties
			if (control.getMissingRequiredProperties().size() != 0) {
				throw new XmlWriteException(String.format(Lang.XmlWrite.ProjectSave.CONTROL_PROPERTIES_MISSING_F, control.getClassName()));
			}
			for (ControlProperty cprop : control.getAllDefinedProperties()) {
				fos.write(String.format("<control-property lookup-id='%d'>", cprop.getPropertyLookup().propertyId).getBytes());
				if (cprop.getValue() == null) {
					throw new IllegalStateException("control property value is not allowed to be null if it is defined (ArmaControl.getAllDefinedProperties())");
				}
				writeValue(fos, cprop.getValue());
				fos.write("</control-property>".getBytes());
			}
			
			if (controlGroup) {
				for (ArmaControl subControl : ((ArmaControlGroup) control).getControls()) {
					writeControl(fos, subControl);
				}
			}
			
			fos.write((controlGroup ? controlGroupStr : controlStr).getBytes());
		}
		
		private void writeMacros(@NotNull FileOutputStream fos, @NotNull Project project) throws IOException {
			fos.write("<macros>".getBytes());
			
			MacroRegistry registry = project.getMacroRegistry();
			for (Macro macro : registry.getMacros()) {
				fos.write(String.format("<macro key='%s' type='%s' comment='%s'>", macro.getKey(), macro.getPropertyType(), macro.getComment()).getBytes());
				writeValue(fos, macro.getValue());
				fos.write("</macro>".getBytes());
			}
			
			fos.write("</macros>".getBytes());
		}
		
		private void writeValue(@NotNull FileOutputStream fos, @NotNull SerializableValue svalue) throws IOException {
			for (String value : svalue.getAsStringArray()) {
				fos.write("<value>".getBytes());
				fos.write(value.getBytes());
				fos.write("</value>".getBytes());
			}
		}
	}
}
