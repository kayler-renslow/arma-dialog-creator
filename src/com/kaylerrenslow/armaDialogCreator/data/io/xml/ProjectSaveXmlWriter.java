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
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.CustomControlClassRegistry;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.ProjectMacroRegistry;
import com.kaylerrenslow.armaDialogCreator.data.ResourceRegistry;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.FolderTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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
		XmlWriterOutputStream stm = new XmlWriterOutputStream(projectSaveXml);

		stm.writeDefaultProlog();
		stm.write(String.format("<project name='%s' save-version='%d'>", esc(project.getProjectName()), SAVE_VERSION));

		stm.write("<project-description>");
		stm.write(esc(project.getProjectDescription() != null ? project.getProjectDescription() : ""));
		stm.write("</project-description>");

		writeResources(stm, project.getResourceRegistry());
		writeMacros(stm);
		writeDisplay(stm, project.getEditingDisplay());

		writeCustomControls(stm, project.getCustomControlClassRegistry());

		writeProjectExportConfiguration(stm, project.getExportConfiguration());

		stm.write("</project>");

		stm.flush();
		stm.close();
	}

	private void writeCustomControls(@NotNull XmlWriterOutputStream stm, @NotNull CustomControlClassRegistry registry) throws IOException {
		String customControlClasses = "custom-controls";
		stm.writeBeginTag(customControlClasses);
		final String customControl = "custom-control";
		final String comment = "comment";
		for (CustomControlClass customClass : registry.getControlClassList()) {
			stm.writeBeginTag(customControl);
			ProjectXmlUtil.writeControlClassSpecification(stm, customClass.getSpecification());
			if (customClass.getComment() != null) {
				stm.writeBeginTag(comment);
				stm.write(customClass.getComment());
				stm.writeCloseTag(comment);
			}
			stm.writeCloseTag(customControl);
		}
		stm.writeCloseTag(customControlClasses);
	}

	private void writeProjectExportConfiguration(XmlWriterOutputStream stm, @NotNull ProjectExportConfiguration configuration) throws IOException {
		stm.write("<export-config>");
		writeProjectExportConfigurationAttribute(stm, "export-class-name", configuration.getExportClassName());
		writeProjectExportConfigurationAttribute(stm, "export-location", configuration.getExportLocation().getPath());
		writeProjectExportConfigurationAttribute(stm, "place-adc-notice", configuration.shouldPlaceAdcNotice() + "");
		writeProjectExportConfigurationAttribute(stm, "export-macros-to-file", configuration.shouldExportMacrosToFile() + "");
		writeProjectExportConfigurationAttribute(stm, "export-file-type-ext", configuration.getHeaderFileType().getExtension());
		stm.write("</export-config>");
	}

	private void writeProjectExportConfigurationAttribute(XmlWriterOutputStream stm, @NotNull String attributeName, @NotNull String value) throws IOException {
		stm.write(String.format("<config-attribute name='%s'>", attributeName));
		stm.write(value);
		stm.write("</config-attribute>");
	}

	private void writeResources(XmlWriterOutputStream stm, @NotNull ResourceRegistry resourceRegistry) throws IOException {
		new ResourceRegistryXmlWriter(resourceRegistry).write(stm);
	}

	private void writeDisplay(@NotNull XmlWriterOutputStream stm, @NotNull ArmaDisplay editingDisplay) throws IOException {
		stm.write("<display>");

		writeDisplayProperties(stm, editingDisplay);

		stm.write("<display-controls type='background'>");
		writeControls(stm, treeStructureBg.getRoot());
		stm.write("</display-controls>");

		stm.write("<display-controls type='main'>");
		writeControls(stm, treeStructureMain.getRoot());
		stm.write("</display-controls>");

		stm.write("</display>");
	}

	private void writeDisplayProperties(@NotNull XmlWriterOutputStream stm, @NotNull ArmaDisplay display) throws IOException {
		for (DisplayProperty property : display.getDisplayProperties()) {
			if (property.getValue() == null) {
				continue;
			}
			stm.write(String.format("<display-property lookup-id='%s'>", property.getPropertyLookup().getPropertyId()));
			writeValue(stm, property.getValue());
			stm.write("</display-property>");
		}
	}

	private void writeControls(@NotNull XmlWriterOutputStream stm, @NotNull TreeStructure.TreeNode<? extends TreeItemEntry> parent) throws IOException {
		for (TreeStructure.TreeNode<? extends TreeItemEntry> treeNode : parent.getChildren()) {
			if (treeNode.getData() instanceof FolderTreeItemEntry) {
				FolderTreeItemEntry folderTreeItemEntry = (FolderTreeItemEntry) treeNode.getData();
				writeFolder(stm, treeNode, folderTreeItemEntry);
			} else if (treeNode.getData() instanceof ControlTreeItemEntry) { //control group tree item entry should extend this class
				ControlTreeItemEntry controlTreeItemEntry = (ControlTreeItemEntry) treeNode.getData();
				writeControl(stm, treeNode, controlTreeItemEntry.getMyArmaControl());
			} else {
				throw new IllegalStateException("unknown tree node data class");
			}
		}

	}

	private void writeFolder(@NotNull XmlWriterOutputStream stm, @NotNull TreeStructure.TreeNode<? extends TreeItemEntry> treeNode, @NotNull FolderTreeItemEntry folder) throws IOException {
		stm.write(String.format("<folder name='%s'>", esc(folder.getText())));
		writeControls(stm, treeNode);
		stm.write("</folder>");
	}

	private void writeControl(@NotNull XmlWriterOutputStream stm, @NotNull TreeStructure.TreeNode<? extends TreeItemEntry> treeNode, @NotNull ArmaControl control) throws IOException {
		final String controlGroupStr = "control-group";
		final String controlStr = "control";
		boolean controlGroup = control instanceof ArmaControlGroup;

		stm.write(String.format("<%s renderer-id='%d' control-id='%d' class-name='%s'%s>",
				controlGroup ? controlGroupStr : controlStr,
				control.getRendererLookup().id,
				control.getControlType().getTypeId(),
				control.getClassName(),
				control.getExtendClass() != null ? String.format(" extend-class='%s'", control.getExtendClass().getClassName()) : ""
				)
		);

		//write control properties
		for (ControlProperty cprop : control.getDefinedProperties()) {
			ProjectXmlUtil.writeControlProperty(stm, cprop);
		}

		ProjectXmlUtil.writeInheritedControlProperties(stm, control.getInheritedProperties());

		if (controlGroup) {
			writeControls(stm, treeNode);
		}

		if (control.getRequiredNestedClasses().size() > 0) {
			final String reqNestedClasses = "nested-required";
			stm.writeBeginTag(reqNestedClasses);
			for (ControlClass nested : control.getRequiredNestedClasses()) {
				ProjectXmlUtil.writeControlClassSpecification(stm, new ControlClassSpecification(nested, false));
			}
			stm.writeCloseTag(reqNestedClasses);
		}

		if (control.getOptionalNestedClasses().size() > 0) {
			final String optNestedClasses = "nested-optional";
			stm.writeBeginTag(optNestedClasses);
			for (ControlClass nested : control.getOptionalNestedClasses()) {
				ProjectXmlUtil.writeControlClassSpecification(stm, new ControlClassSpecification(nested, false));
			}
			stm.writeCloseTag(optNestedClasses);
		}

		stm.write(("</" + (controlGroup ? controlGroupStr : controlStr) + ">"));
	}

	private void writeMacros(@NotNull XmlWriterOutputStream stm) throws IOException {
		stm.write("<macros>");

		ProjectMacroRegistry registry = project.getMacroRegistry();
		for (Macro macro : registry.getMacros()) {
			stm.write(String.format("<macro key='%s' property-type-id='%d' comment='%s'>", macro.getKey(), macro.getPropertyType().getId(), esc(macro.getComment())));
			writeValue(stm, macro.getValue());
			stm.write("</macro>");
		}

		stm.write("</macros>");
	}

	private void writeValue(@NotNull XmlWriterOutputStream stm, @NotNull SerializableValue svalue) throws IOException {
		ProjectXmlUtil.writeValue(stm, svalue);
	}

	private static String esc(String value) {
		return value.replaceAll("'", "&#39;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
