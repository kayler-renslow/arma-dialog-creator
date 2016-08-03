package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.MacroRegistry;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 Created by Kayler on 08/02/2016.
 */
public class ProjectSaveXmlWriter {
	/**
	 Current version of when the project was saved. If the export ever changes format, this number should change as well.
	 This is meant for ensuring the project is loaded correctly, despite an older save format (be sure to store each loader for each save version).
	 */
	private static final int SAVE_VERSION = 1;
	
	private final Project project;
	private final File projectSaveXml;
	
	public ProjectSaveXmlWriter(@NotNull Project project, @NotNull File projectSaveXml) {
		this.project = project;
		this.projectSaveXml = projectSaveXml;
	}
	
	public void write() throws IOException {
		FileOutputStream fos = new FileOutputStream(projectSaveXml);
		
		fos.write("<?xml version='1.0' encoding='UTF-8' ?>".getBytes());
		fos.write(String.format("<project name='%s' save-version='%d'>", project.getProjectName(), SAVE_VERSION).getBytes());
		
		fos.write("<project-description>".getBytes());
		fos.write((project.getProjectDescription() != null ? project.getProjectDescription() : "").getBytes());
		fos.write("</project-description>".getBytes());
		
		writeMacros(fos);
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
	
	private void writeMacros(@NotNull FileOutputStream fos) throws IOException {
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
