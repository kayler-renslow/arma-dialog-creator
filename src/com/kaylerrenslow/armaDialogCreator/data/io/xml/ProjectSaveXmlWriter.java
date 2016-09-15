/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.MacroRegistry;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.ResourceRegistry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.FolderTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
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
	private final TreeStructure<? extends TreeItemEntry> treeStructureMain;
	private final TreeStructure<? extends TreeItemEntry> treeStructureBg;
	private final File projectSaveXml;
	
	public ProjectSaveXmlWriter(@NotNull Project project, @NotNull TreeStructure<? extends TreeItemEntry> treeStructureMain,
								@NotNull TreeStructure<? extends TreeItemEntry> treeStructureBg, @NotNull File projectSaveXml) {
		this.project = project;
		this.treeStructureMain = treeStructureMain;
		this.treeStructureBg = treeStructureBg;
		this.projectSaveXml = projectSaveXml;
	}
	
	public void write() throws IOException {
		FileOutputStream fos = new FileOutputStream(projectSaveXml);
		
		fos.write("<?xml version='1.0' encoding='UTF-8' ?>".getBytes());
		fos.write(String.format("<project name='%s' save-version='%d'>", esc(project.getProjectName()), SAVE_VERSION).getBytes());
		
		fos.write("<project-description>".getBytes());
		fos.write(esc(project.getProjectDescription() != null ? project.getProjectDescription() : "").getBytes());
		fos.write("</project-description>".getBytes());

		writeResources(fos, project.getResourceRegistry());
		writeMacros(fos);
		writeDisplay(fos, project.getEditingDisplay());
		
		fos.write("</project>".getBytes());
		
		
		fos.flush();
		fos.close();
	}

	private void writeResources(FileOutputStream fos, @NotNull ResourceRegistry resourceRegistry) throws IOException{
		new ResourceRegistryXmlWriter(resourceRegistry).write(fos);
	}

	private void writeDisplay(@NotNull FileOutputStream fos, @NotNull ArmaDisplay editingDisplay) throws IOException {
		fos.write(String.format("<display idd='%d'>", editingDisplay.getIdd()).getBytes());
		
		fos.write("<display-controls type='background'>".getBytes());
		writeControls(fos, treeStructureBg.getRoot());
		fos.write("</display-controls>".getBytes());
		
		fos.write("<display-controls type='main'>".getBytes());
		writeControls(fos, treeStructureMain.getRoot());
		fos.write("</display-controls>".getBytes());
		
		fos.write("</display>".getBytes());
	}
	
	private void writeControls(@NotNull FileOutputStream fos, @NotNull TreeStructure.TreeNode<? extends TreeItemEntry> parent) throws IOException {
		for (TreeStructure.TreeNode<? extends TreeItemEntry> treeNode : parent.getChildren()) {
			if (treeNode.getData() instanceof FolderTreeItemEntry) {
				FolderTreeItemEntry folderTreeItemEntry = (FolderTreeItemEntry) treeNode.getData();
				writeFolder(fos, treeNode, folderTreeItemEntry);
			} else if (treeNode.getData() instanceof ControlTreeItemEntry) { //control group tree item entry should extend this class
				ControlTreeItemEntry controlTreeItemEntry = (ControlTreeItemEntry) treeNode.getData();
				writeControl(fos, treeNode, controlTreeItemEntry.getMyArmaControl());
			} else {
				throw new IllegalStateException("unknown tree node data class");
			}
		}
		
	}
	
	private void writeFolder(@NotNull FileOutputStream fos, @NotNull TreeStructure.TreeNode<? extends TreeItemEntry> treeNode, @NotNull FolderTreeItemEntry folder) throws IOException {
		fos.write(String.format("<folder name='%s'>", esc(folder.getText())).getBytes());
		writeControls(fos, treeNode);
		fos.write("</folder>".getBytes());
	}
	
	private void writeControl(@NotNull FileOutputStream fos, @NotNull TreeStructure.TreeNode<? extends TreeItemEntry> treeNode, @NotNull ArmaControl control) throws IOException {
		final String controlGroupStr = "control-group";
		final String controlStr = "control";
		boolean controlGroup = control instanceof ArmaControlGroup;
		
		fos.write(String.format("<%s idc='%d' renderer-id='%d' control-type-id='%d' class-name='%s' extend-class='%s'>",
				controlGroup ? controlGroupStr : controlStr,
				control.getIdc(),
				control.getRendererLookup().id,
				control.getType().typeId,
				control.getClassName(),
				control.getExtendClass() != null ? control.getExtendClass().getClassName() : ""
				).getBytes()
		);
		
		//write control properties
		if (control.getMissingRequiredProperties().size() != 0) {
			throw new XmlWriteException(String.format(Lang.XmlWrite.ProjectSave.CONTROL_PROPERTIES_MISSING_F, control.getClassName()));
		}
		for (ControlProperty cprop : control.getAllDefinedProperties()) {
			fos.write(String.format("<control-property lookup-id='%d' macro-key='%s'>",
					cprop.getPropertyLookup().getPropertyId(),
					cprop.getMacro() == null ? "" : cprop.getMacro().getKey()
					).getBytes()
			);
			if (cprop.getValue() == null) {
				throw new IllegalStateException("control property value is not allowed to be null if it is defined (ArmaControl.getAllDefinedProperties())");
			}
			writeValue(fos, cprop.getValue());
			fos.write("</control-property>".getBytes());
		}
		
		if (controlGroup) {
			writeControls(fos, treeNode);
		}
		
		fos.write(("</" + (controlGroup ? controlGroupStr : controlStr) + ">").getBytes());
	}
	
	private void writeMacros(@NotNull FileOutputStream fos) throws IOException {
		fos.write("<macros>".getBytes());
		
		MacroRegistry registry = project.getMacroRegistry();
		for (Macro macro : registry.getMacros()) {
			fos.write(String.format("<macro key='%s' property-type-id='%d' comment='%s'>", macro.getKey(), macro.getPropertyType().id, esc(macro.getComment())).getBytes());
			writeValue(fos, macro.getValue());
			fos.write("</macro>".getBytes());
		}
		
		fos.write("</macros>".getBytes());
	}
	
	private void writeValue(@NotNull FileOutputStream fos, @NotNull SerializableValue svalue) throws IOException {
		for (String value : svalue.getAsStringArray()) {
			fos.write("<value>".getBytes());
			fos.write(esc(value).getBytes());
			fos.write("</value>".getBytes());
		}
	}

	private static String esc(String value){
		return value.replaceAll("'", "&#39;").replaceAll("<","&lt;").replaceAll(">", "&gt;");
	}
}
