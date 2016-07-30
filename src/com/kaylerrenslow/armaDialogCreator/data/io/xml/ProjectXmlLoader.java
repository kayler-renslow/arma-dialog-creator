package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 Loads a project from a .xml save file
 Created on 07/28/2016. */
public class ProjectXmlLoader {
	private final Document document;
	private final DataContext dataContext;
	private ArrayList<String> errors = new ArrayList<>();
	
	private ProjectXmlLoader(@NotNull Document document, @NotNull DataContext context) {
		this.document = document;
		this.dataContext = context;
	}
	
	@Nullable
	public static ProjectParseResult parse(@NotNull DataContext context, @NotNull File projectSaveXml) throws XmlParseException {
		Document doc;
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(projectSaveXml);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(Lang.XmlParse.FAILED_TO_READ_XML);
		}
		ProjectXmlLoader loader = new ProjectXmlLoader(doc, context);
		return new ProjectParseResult(loader.parseDocument(), loader.errors);
	}
	
	public static class ProjectParseResult {
		private final Project project;
		private final ArrayList<String> errors;
		
		public ProjectParseResult(Project project, ArrayList<String> errors) {
			this.project = project;
			this.errors = errors;
		}
		
		public Project getProject() {
			return project;
		}
		
		public ArrayList<String> getErrors() {
			return errors;
		}
	}
	
	@Nullable
	private Project parseDocument() throws XmlParseException {
		String projectName = document.getDocumentElement().getAttribute("name");
		Project project = new Project(projectName, ArmaDialogCreator.getApplicationDataManager().getAppSaveDataDirectory());
		fetchMacros(project.getMacroRegistry().getMacros());
		ArmaDisplay editingDisplay = fetchEditingDisplay();
		return project;
	}
	
	private List<Macro> fetchMacros(List<Macro> macros) throws XmlParseException {
		NodeList macrosNodeList = document.getDocumentElement().getElementsByTagName("macros");
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
					errors.add(String.format(Lang.XmlParse.BAD_MACRO_KEY_OR_TYPE_F, key, type));
					continue;
				}
				PropertyType propertyType = PropertyType.get(type);
				if (propertyType == null) {
					errors.add(String.format(Lang.XmlParse.BAD_MACRO_PROPERTY_TYPE_F, type));
					continue;
				}
				NodeList macroValueNodeList = macroElement.getElementsByTagName("value");
				if (propertyType.propertyValuesSize > macroValueNodeList.getLength()) { //missing entries
					errors.add(String.format(Lang.XmlParse.BAD_MACRO_VALUE_COUNT_F, macroValueNodeList.getLength()));
					continue;
				}
				String[] values = new String[propertyType.propertyValuesSize];
				for (int macroValueInd = 0; macroValueInd < macroList.getLength(); macroValueInd++) {
					Node macroValueNode = macroValueNodeList.item(macroValueInd);
					if (macroValueNode.getNodeType() != Node.ELEMENT_NODE) {
						continue;
					}
					Element macroValueElement = (Element) macroValueNode;
					values[macroValueInd] = macroValueElement.getNodeValue();
				}
				
				SerializableValue value;
				try {
					value = propertyType.converter.convert(dataContext, values);
				} catch (Exception e) {
					e.printStackTrace(System.out);
					errors.add(String.format(Lang.XmlParse.BAD_MACRO_VALUES_F, Arrays.toString(values)));
					continue;
				}
				Macro<?> macro = new Macro<>(key, value, propertyType);
				macros.add(macro);
				macro.setComment(comment);
			}
		}
		
		return macros;
	}
	
	private ArmaDisplay fetchEditingDisplay() throws XmlParseException {
		NodeList displayNodeList = document.getDocumentElement().getElementsByTagName("display");
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
			errors.add(String.format(Lang.XmlParse.BAD_DISPLAY_IDD_F, iddStr));
			idd = -1;
		}
		
		ArmaDisplay display = new ArmaDisplay(idd);
		
		NodeList displayControlNodeList = displayElement.getElementsByTagName("control");
		Element controlElement;
		controls:
		for (int displayControlInd = 0; displayControlInd < displayControlNodeList.getLength(); displayControlInd++) {
			if (displayControlNodeList.item(displayControlInd).getNodeType() == Node.ELEMENT_NODE) {
				controlElement = (Element) displayControlNodeList.item(displayControlInd);
			} else {
				continue;
			}
			
			int idc;
			String idcStr = controlElement.getAttribute("idc");
			try {
				idc = Integer.parseInt(idcStr);
			} catch (NumberFormatException e) {
				errors.add(String.format(Lang.XmlParse.BAD_CONTROL_IDC_F, idcStr));
				idc = -1;
			}
			ControlType controlType;
			String controlTypeStr = controlElement.getAttribute("control-type");
			try {
				int controlTypeId = Integer.parseInt(controlTypeStr);
				controlType = ControlType.getById(controlTypeId);
				if (controlType == null) {
					errors.add(String.format(Lang.XmlParse.BAD_CONTROL_TYPE_F, controlTypeStr));
				}
			} catch (NumberFormatException e) {
				errors.add(String.format(Lang.XmlParse.BAD_CONTROL_TYPE_F, controlTypeStr));
			}
			String className = controlElement.getAttribute("class-name");
			String extendClassName = controlElement.getAttribute("extend-class");
			Class<? extends ArmaControlRenderer> rendererClass;
			String rendererStr = controlElement.getAttribute("renderer");
			try {
				rendererClass = (Class<? extends ArmaControlRenderer>) Class.forName(rendererStr);
			} catch (ClassNotFoundException | ClassCastException e) {
				errors.add(String.format(Lang.XmlParse.BAD_RENDERER_F, rendererStr));
				continue;
			}
			NodeList controlPropertiesNodeList = controlElement.getElementsByTagName("control-property");
			Element controlPropertyElement;
			List<ControlPropertyLookup> required = new LinkedList<>();
			List<ControlPropertyLookup> optional = new LinkedList<>();
			List<ControlPropertyLookup> events = new LinkedList<>();
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
					if (lookup == null) {
						errors.add(String.format(Lang.XmlParse.BAD_LOOKUP_ID_F, lookupIdStr));
						continue controls;
					}
				} catch (NumberFormatException e) {
					errors.add(String.format(Lang.XmlParse.BAD_LOOKUP_ID_F, lookupIdStr));
					continue controls;
				}
				String type = controlPropertyElement.getAttribute("type");
				if(type.equals("required")){
					
				}else if(type.equals("optional")){
					
				}else if(type.equals("event")){
					
				}else{
					errors.add(String.format(Lang.XmlParse.BAD_LOOKUP_TYPE_F, type));
					continue controls;
				}
			}
		}
		
		return display;
	}
}
