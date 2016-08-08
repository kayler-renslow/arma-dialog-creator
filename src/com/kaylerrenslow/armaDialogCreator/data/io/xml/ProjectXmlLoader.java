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
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecProvider;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlGroupTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.FolderTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 Loads a project from a .xml save file (used with Project export save-version 1)
 Created on 07/28/2016. */
public class ProjectXmlLoader extends XmlLoader {
	
	/**
	 Parses the given file and returns the result with the Project instance.
	 
	 @param context must contain keys: {@link DataKeys#ARMA_RESOLUTION}, {@link DataKeys#ENV}
	 @param projectSaveXml file that contains the project save xml
	 @return result
	 @throws XmlParseException when the file could not be properly parsed
	 */
	@NotNull
	public static ProjectParseResult parse(@NotNull DataContext context, @NotNull File projectSaveXml) throws XmlParseException {
		ProjectXmlLoader loader = new ProjectXmlLoader(projectSaveXml, context, DataKeys.ENV, DataKeys.ARMA_RESOLUTION);
		return new ProjectParseResult(loader.parseDocument(), loader.treeStructureMain, loader.getErrors());
	}
	
	public static class ProjectParseResult extends XmlLoader.ParseResult {
		
		private final Project project;
		private final TreeStructure<TreeItemEntry> treeStructure;
		
		private ProjectParseResult(Project project, TreeStructure<TreeItemEntry> treeStructure, ArrayList<ParseError> errors) {
			super(errors);
			this.project = project;
			this.treeStructure = treeStructure;
		}
		
		public TreeStructure<TreeItemEntry> getTreeStructure() {
			return treeStructure;
		}
		
		public Project getProject() {
			return project;
		}
		
	}
	
	private TreeStructure<TreeItemEntry> treeStructureMain = new TreeStructure<>(new TreeStructure.TreeNode<>(null));
	private TreeStructure<TreeItemEntry> treeStructureBg = new TreeStructure<>(new TreeStructure.TreeNode<>(null));
	
	private ProjectXmlLoader(@NotNull File projectSaveXml, @NotNull DataContext context, @NotNull Key<?>... requiredKeys) throws XmlParseException {
		super(projectSaveXml, context, requiredKeys);
	}
	
	@Nullable
	private Project parseDocument() throws XmlParseException {
		try {
			String projectName = document.getDocumentElement().getAttribute("name");
			Project project = new Project(projectName, ArmaDialogCreator.getApplicationDataManager().getAppSaveDataDirectory());
			fetchMacros(project.getMacroRegistry().getMacros());
			ArmaDisplay editingDisplay = fetchEditingDisplay(project.getMacroRegistry().getMacros());
			if (editingDisplay != null) {
				project.setEditingDisplay(editingDisplay);
			}
			project.setProjectDescription(getProjectDescription());
			return project;
		} catch (Exception e) {
			throw new XmlParseException(e.getMessage());
		}
	}
	
	private String getProjectDescription() {
		List<Element> descriptionElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "project-description");
		if (descriptionElements.size() > 0) {
			return XmlUtil.getImmediateTextContent(descriptionElements.get(0));
		}
		return null;
	}
	
	private List<Macro> fetchMacros(List<Macro> macros) {
		List<Element> macrosGroupElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "macros");
		List<Element> macroElements;
		for (Element macrosGroupElement : macrosGroupElements) {
			macroElements = XmlUtil.getChildElementsWithTagName(macrosGroupElement, "macro");
			for (Element macroElement : macroElements) {
				String key = macroElement.getAttribute("key");
				String type = macroElement.getAttribute("type");
				String comment = macroElement.getAttribute("comment");
				if (key.length() == 0 || type.length() == 0) {
					addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_MACRO_KEY_OR_TYPE_F, key, type)));
					continue;
				}
				PropertyType propertyType = PropertyType.get(type);
				if (propertyType == null) {
					addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_MACRO_PROPERTY_TYPE_F, type)));
					continue;
				}
				SerializableValue value = getValue(propertyType, macroElement);
				if (value == null) {
					continue;
				}
				Macro<?> macro = new Macro<>(key, value, propertyType);
				macros.add(macro);
				macro.setComment(comment);
			}
		}
		
		return macros;
	}
	
	private ArmaDisplay fetchEditingDisplay(List<Macro> macros) {
		List<Element> displayElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "display");
		for (Element displayElement : displayElements) {
			int idd;
			String iddStr = displayElement.getAttribute("idd");
			try {
				idd = Integer.parseInt(iddStr);
			} catch (NumberFormatException ex) {
				addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_DISPLAY_IDD_F, iddStr), ParseError.genericRecover("-1")));
				idd = -1;
			}
			
			ArmaDisplay display = new ArmaDisplay(idd);
			List<Element> displayControlElements = XmlUtil.getChildElementsWithTagName(displayElement, "display-controls");
			List<ArmaControl> controls;
			String controlsType;
			for (Element displayControlElement : displayControlElements) {
				controlsType = displayControlElement.getAttribute("type");
				switch (controlsType) {
					case "background": {
						controls = buildStructureAndGetControls(treeStructureBg.getRoot(), displayControlElement, macros);
						for (ArmaControl control : controls) {
							display.addBackgroundControl(control);
						}
						break;
					}
					case "main": {
						controls = buildStructureAndGetControls(treeStructureMain.getRoot(), displayControlElement, macros);
						for (ArmaControl control : controls) {
							display.addControl(control);
						}
						break;
					}
				}
			}
			return display;
		}
		return null;
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
						group.addControl(add);
					}
					
					break;
				}
				case "folder": {
					treeNode = new TreeStructure.TreeNode<>(new FolderTreeItemEntry(controlElement.getAttribute("name")));
					parent.getChildren().add(treeNode);
					controls.addAll(buildStructureAndGetControls(parent, controlElement, macros));
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
			addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.MISSING_CONTROL_NAME, controlElement.getTextContent())));
			return null;
		}
		int idc;
		String idcStr = controlElement.getAttribute("idc");
		try {
			idc = Integer.parseInt(idcStr);
		} catch (NumberFormatException e) {
			addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_CONTROL_IDC_F, idcStr, controlClassName), ParseError.genericRecover("-1")));
			idc = -1;
		}
		ControlType controlType;
		String controlTypeStr = controlElement.getAttribute("control-type-id");
		try {
			int controlTypeId = Integer.parseInt(controlTypeStr);
			controlType = ControlType.getById(controlTypeId);
		} catch (IllegalArgumentException e) { //will catch number format exception as well
			addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_CONTROL_TYPE_F, controlTypeStr, controlClassName)));
			return null;
		}
		
		String extendClassName = controlElement.getAttribute("extend-class");
		RendererLookup rendererLookup;
		String rendererStr = controlElement.getAttribute("renderer-id");
		try {
			rendererLookup = RendererLookup.getById(Integer.parseInt(rendererStr));
		} catch (IllegalArgumentException e) {
			addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_RENDERER_F, rendererStr, controlClassName)));
			return null;
		}
		List<Element> controlPropertyElements = XmlUtil.getChildElementsWithTagName(controlElement, "control-property");
		
		LinkedList<ControlLoadConfig> properties = new LinkedList<>();
		for (Element controlPropertyElement : controlPropertyElements) {
			ControlPropertyLookup lookup;
			String lookupIdStr = controlPropertyElement.getAttribute("lookup-id");
			String macroKey = controlPropertyElement.getAttribute("macro-key");
			try {
				int id = Integer.parseInt(lookupIdStr);
				lookup = ControlPropertyLookup.findById(id);
			} catch (IllegalArgumentException e) {
				addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_LOOKUP_ID_F, lookupIdStr, controlClassName)));
				return null; //uncertain whether or not the control can be properly edited/rendered. So just skip control entirely.
			}
			SerializableValue value = getValue(lookup.propertyType, controlPropertyElement);
			if (value == null) {
				return null; //uncertain whether or not the control can be properly edited/rendered. So just skip control entirely.
			}
			properties.add(new ControlLoadConfig(lookup, value, macroKey));
		}
		
		
		ArmaControlSpecProvider specProvider = ArmaControlLookup.findByControlType(controlType).specProvider;
		boolean containsAll = containsAllProperties(controlClassName, specProvider.getRequiredProperties(), properties);
		if (!containsAll) {
			return null;
		}
		Expression x, y, w, h;
		x = y = w = h = null;
		for (ControlLoadConfig config : properties) {
			switch (config.lookup) {
				case X: {
					x = (Expression) config.value;
					break;
				}
				case Y: {
					y = (Expression) config.value;
					break;
				}
				case W: {
					w = (Expression) config.value;
					break;
				}
				case H: {
					h = (Expression) config.value;
					break;
				}
				default:
					break;
			}
		}
		if (x == null || y == null || w == null || h == null) {
			throw new IllegalStateException("at least one position value (x,y,h,w) is undefined when it should be defined at this point.");
		}
		ArmaControl control;
		if (isControlGroup) {
			control = new ArmaControlGroup(controlClassName, idc, controlType, ControlStyle.CENTER.styleGroup, x, y, w, h, DataKeys.ARMA_RESOLUTION.get(dataContext), rendererLookup, DataKeys.ENV.get(dataContext));
		} else {
			control = new ArmaControl(controlClassName, specProvider, idc, controlType, ControlStyle.CENTER.styleGroup, x, y, w, h, DataKeys.ARMA_RESOLUTION.get(dataContext), rendererLookup, DataKeys.ENV.get(dataContext));
		}
		
		for (ControlPropertyLookup lookup : specProvider.getRequiredProperties()) {
			for (ControlLoadConfig config : properties) {
				if (config.lookup == lookup) {
					ControlProperty property = control.findRequiredProperty(lookup);
					setControlPropertyValue(macros, config, property);
				}
			}
		}
		
		for (ControlPropertyLookup lookup : specProvider.getOptionalProperties()) {
			for (ControlLoadConfig config : properties) {
				if (config.lookup == lookup) {
					ControlProperty property = control.findOptionalProperty(lookup);
					setControlPropertyValue(macros, config, property);
				}
			}
		}
		
		//		for (ControlPropertyLookup lookup : specProvider.getEventProperties()) {
		//			for (Pair<ControlPropertyLookup, SerializableValue> saved : properties) {
		//				if (saved.getKey() == lookup) {
		//					control.findEventProperty(lookup).setValue(saved.getValue());
		//				}
		//			}
		//		}
		
		return control;
	}
	
	private void setControlPropertyValue(List<Macro> macros, ControlLoadConfig config, ControlProperty property) {
		property.setValue(config.value);
		if (config.macroKey != null) {
			for (Macro macro : macros) {
				if (macro.getKey().equals(config.macroKey)) {
					property.setValueToMacro(macro);
				}
			}
		}
	}
	
	private boolean containsAllProperties(String controlClassName, ControlPropertyLookup[] toMatch, LinkedList<ControlLoadConfig> master) {
		for (ControlPropertyLookup toMatchLookup : toMatch) {
			boolean matched = false;
			for (ControlLoadConfig masterLookup : master) {
				if (masterLookup.lookup == toMatchLookup) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.MISSING_CONTROL_PROPERTY_F, toMatchLookup.propertyName, controlClassName)));
				return false;
			}
		}
		return true;
	}
	
	/**
	 Converts an xml element where it's children are value tags.<br>
	 Sample:<br>
	 <code>
	 &lt;parentElement&gt;<br>
	 &nbsp;&nbsp;&lt;value&gt;value text&lt;/value&gt;<br>
	 &nbsp;&nbsp;&lt;value&gt;value text&lt;/value&gt;<br>
	 &lt;/parentElement&gt;
	 </code>
	 
	 @param propertyType type that will be used to determine the type of {@link com.kaylerrenslow.armaDialogCreator.util.ValueConverter}
	 @param parentElement element that is the parent of the value tags
	 @return the {@link SerializableValue}, or null if couldn't be created (will log errors in {@link #errors}).
	 */
	private SerializableValue getValue(PropertyType propertyType, Element parentElement) {
		List<Element> valueElements = XmlUtil.getChildElementsWithTagName(parentElement, "value");
		if (valueElements.size() < propertyType.propertyValuesSize) {
			addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_VALUE_CREATION_COUNT_F, parentElement.getTagName(), valueElements.size())));
			return null;
		}
		String[] values = new String[propertyType.propertyValuesSize];
		int valueInd = 0;
		for (Element valueElement : valueElements) {
			values[valueInd++] = XmlUtil.getImmediateTextContent(valueElement);
		}
		SerializableValue value;
		try {
			value = propertyType.converter.convert(dataContext, values);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_VALUES_F, Arrays.toString(values))));
			return null;
		}
		return value;
	}
	
	private static class ControlLoadConfig {
		private final ControlPropertyLookup lookup;
		private final SerializableValue value;
		private final String macroKey;
		
		public ControlLoadConfig(ControlPropertyLookup lookup, SerializableValue value, String macroKey) {
			this.lookup = lookup;
			this.value = value;
			if (macroKey.trim().length() == 0) {
				this.macroKey = null;
			} else {
				this.macroKey = macroKey.trim();
			}
		}
	}
}
