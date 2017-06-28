package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.ProjectControlClassRegistry;
import com.kaylerrenslow.armaDialogCreator.data.ProjectMacroRegistry;
import com.kaylerrenslow.armaDialogCreator.data.ResourceRegistry;
import com.kaylerrenslow.armaDialogCreator.data.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	private final TreeStructure<ArmaControl> treeStructureMain;
	private final TreeStructure<ArmaControl> treeStructureBg;
	private final File projectSaveXml;

	/**
	 Creates a new writer.

	 @param project project to write
	 @param treeStructureMain the {@link TreeStructure} used for saving the controls and folders in the foreground
	 @param treeStructureBg the {@link TreeStructure} used for saving the controls and folders in the background
	 */
	public ProjectSaveXmlWriter(@NotNull Project project, @NotNull TreeStructure<ArmaControl> treeStructureMain,
								@NotNull TreeStructure<ArmaControl> treeStructureBg) {
		this.project = project;
		this.treeStructureMain = treeStructureMain;
		this.treeStructureBg = treeStructureBg;
		this.projectSaveXml = project.getProjectSaveFile();
	}

	/**
	 Write the xml.

	 @param saveFile if null, will use {@link Project#getProjectSaveFile()} as the write file, otherwise will write
	 to this file
	 @throws IOException
	 */
	public void write(@Nullable File saveFile) throws IOException {
		saveFile = saveFile == null ? projectSaveXml : saveFile;

		XmlWriterOutputStream stm = new XmlWriterOutputStream(saveFile);

		stm.writeDefaultProlog();
		stm.write(
				String.format(
						"<project name='%s' save-version='%d' save-time='%d'>",
						esc(project.getProjectName()),
						SAVE_VERSION,
						System.currentTimeMillis()
				)
		);

		stm.write("<project-description>");
		stm.write(esc(project.getProjectDescription() != null ? project.getProjectDescription() : ""));
		stm.write("</project-description>");

		if (project.getStringTable() != null) {
			stm.writeBeginTag("stringtable");
			stm.write(project.getStringTable().getFile().getAbsolutePath());
			stm.writeCloseTag("stringtable");
		}

		writeResources(stm, project.getResourceRegistry());
		writeMacros(stm);
		writeDisplay(stm, project.getEditingDisplay());

		writeCustomControls(stm, project.getCustomControlClassRegistry());

		writeProjectExportConfiguration(stm, project.getExportConfiguration());

		stm.write("</project>");

		stm.flush();
		stm.close();
	}

	private void writeCustomControls(@NotNull XmlWriterOutputStream stm, @NotNull ProjectControlClassRegistry registry) throws IOException {
		String customControlClasses = "custom-controls";
		stm.writeBeginTag(customControlClasses);
		final String customControl = "custom-control";
		final String comment = "comment";
		for (CustomControlClass customClass : registry.getControlClassList()) {
			stm.writeBeginTag(customControl);
			ProjectXmlUtil.writeControlClassSpecification(stm, customClass.newSpecification());
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
		writeProjectExportConfigurationAttribute(stm, "export-location", configuration.getExportDirectory().getPath());
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
			stm.write(String.format("<display-property id='%s'>", property.getPropertyLookup().getPropertyId()));
			writeValue(stm, property.getValue());
			stm.write("</display-property>");
		}
	}

	private void writeControls(@NotNull XmlWriterOutputStream stm, @NotNull TreeNode<ArmaControl> parent) throws IOException {
		for (TreeNode<ArmaControl> treeNode : parent.getChildren()) {
			if (treeNode.isFolder()) {
				writeFolder(stm, treeNode);
			} else {
				writeControl(stm, treeNode, treeNode.getData());
			}
		}

	}

	private void writeFolder(@NotNull XmlWriterOutputStream stm, @NotNull TreeNode<ArmaControl> treeNode) throws IOException {
		stm.write(String.format("<folder name='%s'>", esc(treeNode.getName())));
		writeControls(stm, treeNode);
		stm.write("</folder>");
	}

	private void writeControl(@NotNull XmlWriterOutputStream stm, @NotNull TreeNode<ArmaControl> treeNode, @NotNull ArmaControl control) throws IOException {
		final String controlGroupStr = "control-group";
		final String controlStr = "control";
		boolean controlGroup = control instanceof ArmaControlGroup;
		boolean enabled = control.getRenderer().isEnabled();
		boolean ghost = control.getRenderer().isGhost();

		stm.write(String.format("<%s control-id='%d' class-name='%s'%s%s%s>",
				controlGroup ? controlGroupStr : controlStr,
				control.getControlType().getTypeId(),
				control.getClassName(),
				control.getExtendClass() != null ? String.format(" extend-class='%s'", control.getExtendClass()
						.getClassName()) : "",
				!enabled ? " enabled='f'" : "",
				ghost ? " ghost='t'" : ""
				)
		);

		//write control properties
		for (ControlProperty cprop : control.getDefinedProperties()) {
			if (control.getTempPropertiesReadOnly().contains(cprop)) {
				continue;
			}
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
				if (control.getTempNestedClassesReadOnly().contains(nested)) {
					continue;
				}
				ProjectXmlUtil.writeControlClassSpecification(stm, new ControlClassSpecification(nested, false));
			}
			stm.writeCloseTag(reqNestedClasses);
		}

		if (control.getOptionalNestedClasses().size() > 0) {
			final String optNestedClasses = "nested-optional";
			stm.writeBeginTag(optNestedClasses);
			for (ControlClass nested : control.getOptionalNestedClasses()) {
				if (control.getTempNestedClassesReadOnly().contains(nested)) {
					continue;
				}
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
