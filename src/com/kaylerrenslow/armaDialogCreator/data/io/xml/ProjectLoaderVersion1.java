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
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.io.export.HeaderFileType;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlGroupTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.FolderTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 A project loader for save-verison='1'
 @author Kayler
 @since 08/07/2016. */
public class ProjectLoaderVersion1 extends ProjectVersionLoader {

	private final LinkedList<AfterLoadJob> jobs = new LinkedList<>();

	protected ProjectLoaderVersion1(ProjectXmlLoader loader) throws XmlParseException {
		super(loader);
	}

	@Override
	public void parseDocument() throws XmlParseException {
		loadProject();

		for (AfterLoadJob job : jobs) {
			job.doWork(project, this);
		}
	}

	private void loadProject() throws XmlParseException {
		try {
			String projectName = document.getDocumentElement().getAttribute("name");
			project = new Project(projectName, ArmaDialogCreator.getApplicationDataManager().getAppSaveDataDirectory());
			loadMacroRegistry();
			loadCustomControlClassRegistry();

			ArmaDisplay editingDisplay = fetchEditingDisplay(project.getMacroRegistry().getMacros());
			if (editingDisplay != null) {
				project.setEditingDisplay(editingDisplay);
			}
			project.setProjectDescription(getProjectDescription());

			fetchExportConfiguration();
			loadResourceRegistry();

		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(e.getMessage());
		}
	}

	private void fetchExportConfiguration() {
		List<Element> exportConfigElementList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "export-config");
		if (exportConfigElementList.size() <= 0) {
			return;
		}
		Element exportConfigElement = exportConfigElementList.get(0);
		List<Element> configAttributeElementList = XmlUtil.getChildElementsWithTagName(exportConfigElement, "config-attribute");
		if (configAttributeElementList.size() == 0) {
			return;
		}
		final ProjectExportConfiguration exportConfiguration = project.getExportConfiguration();
		for (Element configAttributeElement : configAttributeElementList) {
			String attributeName = configAttributeElement.getAttribute("name");
			switch (attributeName) {
				case "export-class-name": {
					String exportClassName = XmlUtil.getImmediateTextContent(configAttributeElement);
					exportConfiguration.setExportClassName(exportClassName.trim());
					break;
				}
				case "export-location": {
					String exportLocation = XmlUtil.getImmediateTextContent(configAttributeElement);
					File exportLocationFile = new File(exportLocation.trim());
					if (!exportLocationFile.isDirectory()) {
						return;
					}
					exportConfiguration.setExportLocation(exportLocationFile);
					break;
				}
				case "place-adc-notice": {
					boolean placeAdcNotice = XmlUtil.getImmediateTextContent(configAttributeElement).trim().equalsIgnoreCase("true");
					exportConfiguration.setPlaceAdcNotice(placeAdcNotice);
					break;
				}
				case "export-macros-to-file": {
					boolean exportMacrosToFile = XmlUtil.getImmediateTextContent(configAttributeElement).trim().equalsIgnoreCase("true");
					exportConfiguration.setExportMacrosToFile(exportMacrosToFile);
					break;
				}
				case "export-file-type-ext": {
					String fileTypeExt = XmlUtil.getImmediateTextContent(configAttributeElement).trim();
					for (HeaderFileType type : HeaderFileType.values()) {
						if (type.getExtension().equalsIgnoreCase(fileTypeExt)) {
							exportConfiguration.setFileType(type);
							break;
						}
					}
					break;
				}
				default: {
					break;
				}
			}
		}
	}

	private void loadResourceRegistry() {
		List<Element> externalResourcesElementGroups = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "external-resources");
		for (Element externalResourcesElementGroup : externalResourcesElementGroups) {
			ResourceRegistryXmlLoader.loadRegistryFromElement(project.getResourceRegistry(), externalResourcesElementGroup);
		}
	}

	private void loadCustomControlClassRegistry() throws IOException {
		List<Element> customControlClassElementGroups = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "custom-control-classes");
		for (Element customControlClassesGroup : customControlClassElementGroups) {
			List<ControlClassSpecification> specs = ProjectXmlUtil.loadControlClassSpecifications(customControlClassesGroup, dataContext, this.loader);
			for (ControlClassSpecification spec : specs) {
				project.getCustomControlClassRegistry().addControlClass(spec);
			}
		}
	}

	private void loadMacroRegistry() {
		List<Element> macrosGroupElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "macros");
		List<Element> macroElements;
		for (Element macrosGroupElement : macrosGroupElements) {
			macroElements = XmlUtil.getChildElementsWithTagName(macrosGroupElement, "macro");
			for (Element macroElement : macroElements) {
				String key = macroElement.getAttribute("key");
				String propertyTypeAttr = macroElement.getAttribute("property-type-id");
				String comment = macroElement.getAttribute("comment");
				if (key.length() == 0 || propertyTypeAttr.length() == 0) {
					addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_macro_key_or_type_f"), key, propertyTypeAttr)));
					continue;
				}
				PropertyType propertyType;
				try {
					propertyType = PropertyType.findById(Integer.parseInt(propertyTypeAttr));
				} catch (IllegalArgumentException e) { //will catch number format exception
					addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_macro_property_type_f"), propertyTypeAttr)));
					continue;
				}
				SerializableValue value = getValue(propertyType, macroElement);
				if (value == null) {
					continue;
				}
				Macro<?> macro = new Macro<>(key, value, propertyType);
				project.getMacroRegistry().getMacros().add(macro);
				macro.setComment(comment);
			}
		}

	}

	private String getProjectDescription() {
		List<Element> descriptionElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "project-description");
		if (descriptionElements.size() > 0) {
			return XmlUtil.getImmediateTextContent(descriptionElements.get(0));
		}
		return null;
	}

	private ArmaDisplay fetchEditingDisplay(List<Macro> macros) {
		List<Element> displayElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "display");
		if (displayElements.size() <= 0) {
			return null;
		}
		Element displayElement = displayElements.get(0);
		ArmaDisplay display = new ArmaDisplay();

		List<Element> displayPropertyElements = XmlUtil.getChildElementsWithTagName(displayElement, "display-property");
		for (Element displayPropertyElement : displayPropertyElements) {
			String lookupId = displayPropertyElement.getAttribute("lookup-id");
			try {
				int id = Integer.parseInt(lookupId);
				DisplayPropertyLookup lookup = DisplayPropertyLookup.findById(id);
				SerializableValue value = getValue(lookup.getPropertyType(), displayPropertyElement);
				switch (lookup) {
					case IDD: {
						display.getIddProperty().setValue(value);
						break;
					}
					default: {
						display.getDisplayProperties().add(new DisplayProperty(lookup, value));
						break;
					}
				}

			} catch (IllegalArgumentException e) {
				addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_display_property_lookup_id_f"), lookupId), ParseError.genericRecover("-1")));
			}
		}

		List<Element> displayControlElements = XmlUtil.getChildElementsWithTagName(displayElement, "display-controls");
		List<ArmaControl> controls;
		String controlsType;
		for (Element displayControlElement : displayControlElements) {
			controlsType = displayControlElement.getAttribute("type");
			switch (controlsType) {
				case "background": {
					controls = buildStructureAndGetControls(treeStructureBg.getRoot(), displayControlElement, macros);
					for (ArmaControl control : controls) {
						display.getBackgroundControls().add(control);
					}
					break;
				}
				case "main": {
					controls = buildStructureAndGetControls(treeStructureMain.getRoot(), displayControlElement, macros);
					for (ArmaControl control : controls) {
						display.getControls().add(control);
					}
					break;
				}
			}
		}
		return display;
	}

	private List<ArmaControl> buildStructureAndGetControls(TreeStructure.TreeNode<TreeItemEntry> parent, Element parentElement, List<Macro> macros) {
		List<ArmaControl> controls = new LinkedList<>();
		List<Element> tagElements = XmlUtil.getChildElementsWithTagName(parentElement, "*");
		ArmaControl control;
		TreeStructure.TreeNode<TreeItemEntry> treeNode;
		for (Element controlElement : tagElements) {
			switch (controlElement.getTagName()) {
				case "control": {
					control = getControl(controlElement, false, macros);
					if (control == null) {
						return controls;
					}
					treeNode = new TreeStructure.TreeNode<>(new ControlTreeItemEntry(control));
					parent.getChildren().add(treeNode);
					break;
				}
				case "control-group": {
					control = getControl(controlElement, true, macros);
					if (control == null) {
						return controls;
					}
					ArmaControlGroup group = (ArmaControlGroup) control;
					treeNode = new TreeStructure.TreeNode<>(new ControlGroupTreeItemEntry(group));
					parent.getChildren().add(treeNode);
					List<ArmaControl> controlsToAdd = buildStructureAndGetControls(treeNode, controlElement, macros);
					for (ArmaControl add : controlsToAdd) {
						group.getControls().add(add);
					}
					break;
				}
				case "folder": {
					treeNode = new TreeStructure.TreeNode<>(new FolderTreeItemEntry(controlElement.getAttribute("name")));
					parent.getChildren().add(treeNode);
					controls.addAll(buildStructureAndGetControls(treeNode, controlElement, macros));
					continue;
				}
				default: {
					continue;
				}
			}
			controls.add(control);
		}

		return controls;
	}

	private ArmaControl getControl(Element controlElement, boolean isControlGroup, List<Macro> macros) {
		String controlClassName = controlElement.getAttribute("class-name");
		if (controlClassName.trim().length() == 0) {
			addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.missing_control_name"), controlElement.getTextContent())));
			return null;
		}

		ControlType controlType;
		String controlTypeStr = controlElement.getAttribute("control-type-id");
		try {
			int controlTypeId = Integer.parseInt(controlTypeStr);
			controlType = ControlType.getById(controlTypeId);
		} catch (IllegalArgumentException e) { //will catch number format exception as well
			addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_control_type_f"), controlTypeStr, controlClassName)));
			return null;
		}

		RendererLookup rendererLookup;
		String rendererStr = controlElement.getAttribute("renderer-id");
		try {
			rendererLookup = RendererLookup.getById(Integer.parseInt(rendererStr));
		} catch (IllegalArgumentException e) {
			addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_renderer_f"), rendererStr, controlClassName)));
			return null;
		}
		List<Element> controlPropertyElements = XmlUtil.getChildElementsWithTagName(controlElement, "control-property");

		LinkedList<ControlPropertySpecification> properties = new LinkedList<>();
		for (Element controlPropertyElement : controlPropertyElements) {
			ControlPropertySpecification property = ProjectXmlUtil.loadControlProperty(controlPropertyElement, dataContext, this.loader);
			if (property != null) {
				properties.add(property);
			}
		}

		ArmaControlSpecRequirement specProvider = ArmaControlLookup.findByControlType(controlType).specProvider;
		boolean containsAll = containsAllProperties(controlClassName, specProvider.getRequiredProperties(), properties);
		if (!containsAll) {
			return null;
		}

		ArmaControl control;
		if (isControlGroup) {
			control = new ArmaControlGroup(controlClassName, controlType, DataKeys.ARMA_RESOLUTION.get(dataContext), rendererLookup, DataKeys.ENV.get(dataContext));
		} else {
			control = new ArmaControl(
					controlClassName, controlType, specProvider, DataKeys.ARMA_RESOLUTION.get(dataContext), rendererLookup,
					DataKeys.ENV.get(dataContext)
			);
		}

		for (ControlPropertyLookup lookup : specProvider.getRequiredProperties()) {
			for (ControlPropertySpecification config : properties) {
				if (config.getLookup() == lookup) {
					ControlProperty property = control.findRequiredProperty(lookup);
					setControlPropertyValue(macros, config, property);
				}
			}
		}

		for (ControlPropertyLookup lookup : specProvider.getOptionalProperties()) {
			for (ControlPropertySpecification config : properties) {
				if (config.getLookup() == lookup) {
					ControlProperty property = control.findOptionalProperty(lookup);
					setControlPropertyValue(macros, config, property);
				}
			}
		}

		String extendClassName = controlElement.getAttribute("extend-class");
		if (extendClassName.length() > 0) {
			jobs.add(new ControlExtendJob(extendClassName, control));
		}

		return control;
	}

	private SerializableValue getValue(@NotNull PropertyType propertyType, @NotNull Element controlPropertyElement) {
		return ProjectXmlUtil.getValue(dataContext, propertyType, controlPropertyElement, this.loader);
	}

	private void setControlPropertyValue(List<Macro> macros, ControlPropertySpecification config, ControlProperty property) {
		property.setValue(config.getValue());
		if (config.getMacroKey() != null) {
			for (Macro macro : macros) {
				if (macro.getKey().equals(config.getMacroKey())) {
					property.setValueToMacro(macro);
				}
			}
		}
	}

	private boolean containsAllProperties(String controlClassName, ControlPropertyLookup[] toMatch, LinkedList<ControlPropertySpecification> master) {
		for (ControlPropertyLookup toMatchLookup : toMatch) {
			boolean matched = false;
			for (ControlPropertySpecification masterLookup : master) {
				if (masterLookup.getLookup() == toMatchLookup) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.missing_control_property_f"), toMatchLookup.getPropertyName(), controlClassName)));
				return false;
			}
		}
		return true;
	}


	private interface AfterLoadJob {
		void doWork(@NotNull Project project, @NotNull ProjectVersionLoader loader);
	}

	private static class ControlExtendJob implements AfterLoadJob {
		private final String controlClassName;
		private final ArmaControl setMyExtend;

		public ControlExtendJob(String controlClassName, ArmaControl setMyExtend) {
			this.controlClassName = controlClassName;
			this.setMyExtend = setMyExtend;
		}


		@Override
		public void doWork(@NotNull Project project, @NotNull ProjectVersionLoader loader) {
			ArmaDisplay display = project.getEditingDisplay();
			ArmaControl match = display.findControlByClassName(controlClassName);
			if (match != null) {
				setMyExtend.extendControlClass(match);
			} else {
				loader.addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.couldnt_match_extend_class"), controlClassName, setMyExtend.getClassName())));
			}
		}
	}

}
