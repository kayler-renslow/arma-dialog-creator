package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

/**
 Created by Kayler on 07/28/2016.
 */
public class ProjectXmlLoader {
	private final Document document;
	
	private ProjectXmlLoader(@NotNull Document document) {
		this.document = document;
	}
	
	@Nullable
	public static Project parse(@NotNull File projectSaveXml) throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(projectSaveXml);
		doc.getDocumentElement().normalize();
		ProjectXmlLoader loader = new ProjectXmlLoader(doc);
		return loader.parseDocument();
	}
	
	@Nullable
	private Project parseDocument() {
		String projectName = document.getDocumentElement().getAttribute("name");
		Project project = new Project(projectName, ArmaDialogCreator.getApplicationDataManager().getAppSaveDataDirectory());
		fetchMacros(project.getMacroRegistry().getMacros());
		
		return project;
	}
	
	private List<Macro> fetchMacros(List<Macro> macros) {
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
					continue;
				}
				PropertyType propertyType = PropertyType.get(type);
				if (propertyType == null) {
					continue;
				}
				NodeList macroValueNodeList = macroElement.getElementsByTagName("value");
				if (propertyType.propertyValuesSize > macroValueNodeList.getLength()) { //missing entries
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
					value = propertyType.valueClass.getConstructor(String[].class).newInstance(values);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				Macro<?> macro = new Macro<>(key, value, propertyType);
				macros.add(macro);
				macro.setComment(comment);
			}
		}
		
		return macros;
	}
}
