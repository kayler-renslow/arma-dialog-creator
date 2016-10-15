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
