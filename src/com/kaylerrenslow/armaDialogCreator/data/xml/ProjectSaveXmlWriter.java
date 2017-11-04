package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.ProjectMacroRegistry;
import com.kaylerrenslow.armaDialogCreator.data.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.util.XmlWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.File;

/**
 Class for writing {@link Project} to an XML file

 @author Kayler
 @since 08/02/2016 */
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
	 @throws TransformerException when file couldn't be written
	 */
	public void write(@Nullable File saveFile) throws TransformerException {
		saveFile = saveFile == null ? projectSaveXml : saveFile;

		XmlWriter writer = new XmlWriter(saveFile, "project");
		writer.getRootElement().setAttribute("name", project.getProjectName());
		writer.getRootElement().setAttribute("save-version", SAVE_VERSION + "");
		writer.getRootElement().setAttribute("save-time", System.currentTimeMillis() + "");

		Element projectDescriptionEle = writer.appendElementToRoot("project-description");
		writer.appendTextNode(project.getProjectDescription() != null ? project.getProjectDescription() : "", projectDescriptionEle);

		if (project.getStringTable() != null) {
			Element stringtableEle = writer.appendElementToRoot("stringtable");
			writer.appendTextNode(project.getStringTable().getFile().getAbsolutePath(), stringtableEle);
		}

		ResourceRegistryXmlWriter.write(project.getResourceRegistry(), writer, writer.getRootElement());

		writeMacros(writer);
		writeDisplay(writer, project.getEditingDisplay());

		ProjectXmlUtil.writeCustomControls(writer, project.getProjectCustomControlClassRegistry(), writer.getRootElement());

		{//write the project export configuration
			ProjectExportConfiguration c = project.getExportConfiguration();
			Element exportConfigEle = writer.appendElementToRoot("export-config");

			writeProjectExportConfigurationAttribute(writer, exportConfigEle, "export-class-name", c.getExportClassName());
			writeProjectExportConfigurationAttribute(writer, exportConfigEle, "export-location", c.getExportDirectory().getPath());
			writeProjectExportConfigurationAttribute(writer, exportConfigEle, "place-adc-notice", c.shouldPlaceAdcNotice() + "");
			writeProjectExportConfigurationAttribute(writer, exportConfigEle, "export-macros-to-file", c.shouldExportMacrosToFile() + "");
			writeProjectExportConfigurationAttribute(writer, exportConfigEle, "export-file-type-ext", c.getHeaderFileType().getExtension());
		}

		writer.writeToFile(-1);

		writeWorkspaceCustomControlClassRegistry();
	}

	private void writeWorkspaceCustomControlClassRegistry() throws TransformerException {
		XmlWriter writer = new XmlWriter(project.getWorkspaceCustomControlClassesFile(), "custom-classes");
		ProjectXmlUtil.writeCustomControls(writer, project.getWorkspaceCustomControlClassRegistry(), writer.getRootElement());
		writer.writeToFile(-1);
	}

	private void writeProjectExportConfigurationAttribute(@NotNull XmlWriter writer, @NotNull Element exportConfig,
														  @NotNull String attributeName, @NotNull String value) {
		Element configAttributeEle = writer.appendElementToElement("config-attribute", exportConfig);
		configAttributeEle.setAttribute("name", attributeName);
		writer.appendTextNode(value, configAttributeEle);
	}


	private void writeDisplay(@NotNull XmlWriter writer, @NotNull ArmaDisplay editingDisplay) {
		Element displayEle = writer.appendElementToRoot("display");

		//write display properties
		for (DisplayProperty property : editingDisplay.getDisplayProperties()) {
			if (property.getValue() == null) {
				continue;
			}
			Element displayPropertyEle = writer.appendElementToElement("display-property", displayEle);
			displayPropertyEle.setAttribute("id", property.getPropertyLookup().getPropertyId() + "");

			writeValue(writer, property.getValue(), displayPropertyEle);
		}

		{
			Element displayControlsEle = writer.appendElementToElement("display-controls", displayEle);
			displayControlsEle.setAttribute("type", "background");
			writeControls(writer, treeStructureBg.getRoot(), displayControlsEle);
		}

		{
			Element displayControlsEle = writer.appendElementToElement("display-controls", displayEle);
			displayControlsEle.setAttribute("type", "main");
			writeControls(writer, treeStructureMain.getRoot(), displayControlsEle);
		}

	}

	private void writeControls(@NotNull XmlWriter writer, @NotNull TreeNode<ArmaControl> parent, @NotNull Element addToEle) {
		for (TreeNode<ArmaControl> treeNode : parent.getChildren()) {
			if (treeNode.isFolder()) {
				Element folderEle = writer.appendElementToElement("folder", addToEle);
				folderEle.setAttribute("name", treeNode.getName());
				writeControls(writer, treeNode, folderEle);
			} else {
				writeControl(writer, treeNode, treeNode.getData(), addToEle);
			}
		}

	}

	private void writeControl(@NotNull XmlWriter writer, @NotNull TreeNode<ArmaControl> treeNode,
							  @NotNull ArmaControl control, @NotNull Element addToEle) {
		final boolean isControlGroup = control instanceof ArmaControlGroup;

		Element controlEle = writer.appendElementToElement(isControlGroup ? "control-group" : "control", addToEle);
		controlEle.setAttribute("control-id", control.getControlType().getTypeId() + "");
		controlEle.setAttribute("class-name", control.getClassName());
		if (control.getExtendClass() != null) {
			controlEle.setAttribute("extend-class", control.getExtendClass().getClassName());
		}
		if (!control.getRenderer().isEnabled()) {
			controlEle.setAttribute("enabled", "f");
		}
		if (control.getRenderer().isGhost()) {
			controlEle.setAttribute("ghost", "t");
		}

		//write control properties
		for (ControlProperty cprop : control.getDefinedProperties()) {
			if (control.getTempPropertiesReadOnly().contains(cprop)) {
				continue;
			}
			ProjectXmlUtil.writeControlProperty(writer, cprop, controlEle);
		}

		ProjectXmlUtil.writeInheritedControlProperties(writer, control.getInheritedProperties(), controlEle);

		if (isControlGroup) {
			writeControls(writer, treeNode, controlEle);
		}

		if (control.getRequiredNestedClasses().size() > 0) {
			Element nestedRequiredEle = writer.appendElementToElement("nested-required", controlEle);

			for (ControlClass nested : control.getRequiredNestedClasses()) {
				if (control.getTempNestedClassesReadOnly().contains(nested)) {
					continue;
				}
				ProjectXmlUtil.writeControlClassSpecification(writer, new ControlClassSpecification(nested, false), nestedRequiredEle);
			}
		}

		if (control.getOptionalNestedClasses().size() > 0) {
			Element nestedOptionalEle = writer.appendElementToElement("nested-optional", controlEle);
			for (ControlClass nested : control.getOptionalNestedClasses()) {
				if (control.getTempNestedClassesReadOnly().contains(nested)) {
					continue;
				}
				ProjectXmlUtil.writeControlClassSpecification(writer, new ControlClassSpecification(nested, false), nestedOptionalEle);
			}
		}

	}

	private void writeMacros(@NotNull XmlWriter writer) {
		Element macrosEle = writer.appendElementToRoot("macros");

		ProjectMacroRegistry registry = project.getMacroRegistry();
		for (Macro macro : registry.getMacros()) {
			Element macroEle = writer.appendElementToElement("macro", macrosEle);
			macroEle.setAttribute("key", macro.getKey());
			macroEle.setAttribute("property-type", macro.getPropertyType().getId() + "");
			macroEle.setAttribute("comment", macro.getComment());
			writeValue(writer, macro.getValue(), macroEle);
		}
	}

	private void writeValue(@NotNull XmlWriter writer, @NotNull SerializableValue svalue, @NotNull Element addToEle) {
		ProjectXmlUtil.writeValue(writer, svalue, addToEle);
	}
}
