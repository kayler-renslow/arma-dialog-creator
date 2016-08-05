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
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		return new ProjectParseResult(loader.parseDocument(), loader.getErrors());
	}
	
	public static class ProjectParseResult extends XmlLoader.ParseResult {
		
		private final Project project;
		
		private ProjectParseResult(Project project, ArrayList<ParseError> errors) {
			super(errors);
			this.project = project;
		}
		
		public Project getProject() {
			return project;
		}
		
	}
	
	private ProjectXmlLoader(@NotNull File projectSaveXml, @NotNull DataContext context, @NotNull Key<?>... requiredKeys) throws XmlParseException {
		super(projectSaveXml, context, requiredKeys);
	}
	
	@Nullable
	private Project parseDocument() throws XmlParseException {
		String projectName = document.getDocumentElement().getAttribute("name");
		Project project = new Project(projectName, ArmaDialogCreator.getApplicationDataManager().getAppSaveDataDirectory());
		fetchMacros(project.getMacroRegistry().getMacros());
		ArmaDisplay editingDisplay = fetchEditingDisplay();
		if (editingDisplay != null) {
			project.setEditingDisplay(editingDisplay);
		}
		project.setProjectDescription(getProjectDescription());
		return project;
	}
	
	private String getProjectDescription() {
		NodeList descriptionNodeList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "project-description");
		for (int i = 0; i < descriptionNodeList.getLength(); i++) {
			if (descriptionNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element descElement = (Element) descriptionNodeList.item(i);
				return XmlUtil.getImmediateTextContent(descElement);
			}
		}
		return null;
	}
	
	private List<Macro> fetchMacros(List<Macro> macros) {
		NodeList macrosNodeList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "macro");
		for (int i = 0; i < macrosNodeList.getLength(); i++) {
			Node macrosNode = macrosNodeList.item(i);
			if (macrosNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			NodeList macroList = macrosNode.getChildNodes();
			
			for (int macroInd = 0; macroInd < macroList.getLength(); macroInd++) {
				Node macroNode = macroList.item(macroInd);
				if (macroNode.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				Element macroElement = (Element) macroNode;
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
	
	private ArmaDisplay fetchEditingDisplay() {
		NodeList displayNodeList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "display");
		
		Element displayElement = null;
		for (int i = 0; i < displayNodeList.getLength(); i++) {
			if (displayNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				displayElement = (Element) displayNodeList.item(i);
				break;
			}
		}
		if (displayElement == null) {
			return null;
		}
		int idd;
		String iddStr = displayElement.getAttribute("idd");
		try {
			idd = Integer.parseInt(iddStr);
		} catch (NumberFormatException ex) {
			addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_DISPLAY_IDD_F, iddStr), ParseError.genericRecover("-1")));
			idd = -1;
		}
		
		ArmaDisplay display = new ArmaDisplay(idd);
		display.getControls().addAll(getControls(displayElement));
		
		return display;
	}
	
	private List<ArmaControl> getControls(Element parentElement) {
		List<ArmaControl> controls = new LinkedList<>();
		NodeList tagsNodeList = XmlUtil.getChildElementsWithTagName(parentElement, "*");
		Node tagNode;
		Element controlElement;
		for (int tagInd = 0; tagInd < tagsNodeList.getLength(); tagInd++) {
			tagNode = tagsNodeList.item(tagInd);
			if (tagNode.getNodeType() != Node.ELEMENT_NODE || (!tagNode.getNodeName().equals("control") && !tagNode.getNodeName().equals("control-group"))) {
				continue;
			}
			controlElement = (Element) tagNode;
			ArmaControl control;
			if (controlElement.getTagName().equals("control-group")) {
				control = getControl(controlElement, true);
				if (control == null) {
					return controls;
				}
				ArmaControlGroup group = (ArmaControlGroup) control;
				group.getControls().addAll(getControls(controlElement));
				
			} else {
				control = getControl(controlElement, false);
			}
			controls.add(control);
		}
		
		return controls;
	}
	
	private ArmaControl getControl(Element controlElement, boolean isControlGroup) {
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
		NodeList controlPropertiesNodeList = XmlUtil.getChildElementsWithTagName(controlElement, "control-property");
		Element controlPropertyElement;
		LinkedList<Pair<ControlPropertyLookup, SerializableValue>> properties = new LinkedList<>();
		for (int controlPropertyInd = 0; controlPropertyInd < controlPropertiesNodeList.getLength(); controlPropertyInd++) {
			if (controlPropertiesNodeList.item(controlPropertyInd).getNodeType() == Node.ELEMENT_NODE) {
				controlPropertyElement = (Element) controlPropertiesNodeList.item(controlPropertyInd);
			} else {
				continue;
			}
			ControlPropertyLookup lookup;
			String lookupIdStr = controlPropertyElement.getAttribute("lookup-id");
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
			properties.add(new Pair<>(lookup, value));
		}
		
		ArmaControlSpecProvider specProvider = ArmaControlLookup.findByControlType(controlType).specProvider;
		boolean containsAll = containsAllProperties(controlClassName, specProvider.getRequiredProperties(), properties);
		if (!containsAll) {
			return null;
		}
		Expression x, y, w, h;
		x = y = w = h = null;
		for (Pair<ControlPropertyLookup, SerializableValue> pair : properties) {
			switch (pair.getKey()) {
				case X: {
					x = (Expression) pair.getValue();
					break;
				}
				case Y: {
					y = (Expression) pair.getValue();
					break;
				}
				case W: {
					w = (Expression) pair.getValue();
					break;
				}
				case H: {
					h = (Expression) pair.getValue();
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
			control = new ArmaControlGroup(controlClassName, idc, controlType, ControlStyle.CENTER, x, y, w, h, DataKeys.ARMA_RESOLUTION.get(dataContext), rendererLookup, DataKeys.ENV.get(dataContext));
		} else {
			control = new ArmaControl(controlClassName, specProvider, idc, controlType, ControlStyle.CENTER, x, y, w, h, DataKeys.ARMA_RESOLUTION.get(dataContext), rendererLookup, DataKeys.ENV.get(dataContext));
		}
		
		for (ControlPropertyLookup lookup : specProvider.getRequiredProperties()) {
			for (Pair<ControlPropertyLookup, SerializableValue> saved : properties) {
				if (saved.getKey() == lookup) {
					control.findRequiredProperty(lookup).setValue(saved.getValue());
				}
			}
		}
		
		for (ControlPropertyLookup lookup : specProvider.getOptionalProperties()) {
			for (Pair<ControlPropertyLookup, SerializableValue> saved : properties) {
				if (saved.getKey() == lookup) {
					control.findOptionalProperty(lookup).setValue(saved.getValue());
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
	
	private boolean containsAllProperties(String controlClassName, ControlPropertyLookup[] toMatch, LinkedList<Pair<ControlPropertyLookup, SerializableValue>> master) {
		for (ControlPropertyLookup toMatchLookup : toMatch) {
			boolean matched = false;
			for (Pair<ControlPropertyLookup, SerializableValue> masterLookup : master) {
				if (masterLookup.getKey() == toMatchLookup) {
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
		NodeList valueNodeList = XmlUtil.getChildElementsWithTagName(parentElement, "value");
		if (propertyType.propertyValuesSize > valueNodeList.getLength()) { //missing entries
			addError(new ParseError(String.format(Lang.XmlParse.ProjectLoad.BAD_VALUE_CREATION_COUNT_F, parentElement.getTagName(), valueNodeList.getLength())));
			return null;
		}
		String[] values = new String[propertyType.propertyValuesSize];
		for (int valueInd = 0; valueInd < valueNodeList.getLength(); valueInd++) {
			Node macroValueNode = valueNodeList.item(valueInd);
			if (macroValueNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element macroValueElement = (Element) macroValueNode;
			values[valueInd] = XmlUtil.getImmediateTextContent(macroValueElement);
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
}
