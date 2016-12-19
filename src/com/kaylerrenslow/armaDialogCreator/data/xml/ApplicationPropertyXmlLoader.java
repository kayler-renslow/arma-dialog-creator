package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationProperty;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

/**
 Created by Kayler on 07/31/2016.
 */
public class ApplicationPropertyXmlLoader extends XmlLoader {
	
	public static ApplicationPropertyParseResult parse(@NotNull File applicationPropertiesFile) throws XmlParseException {
		ApplicationPropertyXmlLoader loader = new ApplicationPropertyXmlLoader(applicationPropertiesFile);
		return new ApplicationPropertyParseResult(loader.parseDocument(), loader.getErrors());
	}
	
	public static class ApplicationPropertyParseResult extends ParseResult {
		
		private final DataContext properties;
		
		public ApplicationPropertyParseResult(DataContext properties, ArrayList<ParseError> errors) {
			super(errors);
			this.properties = properties;
		}
		
		public DataContext getProperties() {
			return properties;
		}
	}
	
	protected ApplicationPropertyXmlLoader(@NotNull File xmlFile) throws XmlParseException {
		super(xmlFile, null, null);
	}
	
	private DataContext parseDocument() {
		DataContext propertiesDataContext = new DataContext();
		NodeList appPropertiesNodeList = getDocumentElement().getElementsByTagName("application-property");
		for (int appPropertyInd = 0; appPropertyInd < appPropertiesNodeList.getLength(); appPropertyInd++) {
			if (appPropertiesNodeList.item(appPropertyInd).getNodeType() == Node.ELEMENT_NODE) {
				Element appPropertyElement = (Element) appPropertiesNodeList.item(appPropertyInd);
				String name = appPropertyElement.getAttribute("name");
				String value = appPropertyElement.getTextContent();
				for (ApplicationProperty property : ApplicationProperty.values()) {
					if (property.getName().equals(name)) {
						if (value.trim().length() == 0) {
							propertiesDataContext.put(property, null);
						} else {
							try {
								propertiesDataContext.put(property, property.converter.convert(null, value));
							} catch (Exception e) {
								addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ApplicationPropertyLoad.bad_saved_value_f"), property.getName
										(), property
										.getDefaultValue()
										.toString())));
								propertiesDataContext.put(property, property.getDefaultValue());
							}
						}
					}
				}
				
			}
		}
		return propertiesDataContext;
	}
}
