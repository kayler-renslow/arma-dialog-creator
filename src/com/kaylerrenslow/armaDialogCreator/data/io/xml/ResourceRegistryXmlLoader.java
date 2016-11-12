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

import com.kaylerrenslow.armaDialogCreator.data.ExternalResource;
import com.kaylerrenslow.armaDialogCreator.data.ResourceRegistry;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.KeyValueString;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import java.io.File;
import java.util.List;

/**
 @author Kayler
 Used to load external resources from an xml file
 Created on 09/10/2016. */
public class ResourceRegistryXmlLoader extends XmlLoader {

	/**
	 Construct a loader for an xml file that has the root tag {@link ResourceRegistryXmlWriter#EXTERNAL_RESOURCES_TAG_NAME}.
	 This constructed loader is used explicitly for {@link com.kaylerrenslow.armaDialogCreator.data.io.xml.ResourceRegistryXmlWriter.GlobalResourceRegistryXmlWriter}.
	 <br>
	 If you wish to load resources into an existing registry, use {@link #loadRegistryFromElement(ResourceRegistry, Element)}

	 @param xmlFile file that contains the resource registry external resources
	 @param context data context
	 @throws XmlParseException
	 */
	public ResourceRegistryXmlLoader(@NotNull File xmlFile, @Nullable DataContext context) throws XmlParseException {
		super(xmlFile, context, null);
	}

	public static void loadRegistryFromElement(@NotNull ResourceRegistry resourceRegistry, @NotNull Element externalResourcesTag) {
		if (!externalResourcesTag.getTagName().equals("external-resources")) {
			throw new IllegalArgumentException("externalResourcesTag does not have the tag name '" + "external-resources" + "'");
		}
		List<Element> externalResourceList = XmlUtil.getChildElementsWithTagName(externalResourcesTag, "external-resource");
		for (Element externalResourceElement : externalResourceList) {
			List<Element> externalResourcePropertyElements = XmlUtil.getChildElementsWithTagName(externalResourceElement, "resource-property");
			KeyValueString[] keyValues = new KeyValueString[externalResourcePropertyElements.size()];
			for (int i = 0; i < externalResourcePropertyElements.size(); i++) {
				Element resourcePropertyElement = externalResourcePropertyElements.get(i);
				String key = resourcePropertyElement.getAttribute("key");
				keyValues[i] = new KeyValueString(key, XmlUtil.getImmediateTextContent(resourcePropertyElement).trim());
			}
			ExternalResource externalResource = new ExternalResource(new File(XmlUtil.getImmediateTextContent(externalResourceElement).trim()), keyValues);
			resourceRegistry.getExternalResourceList().add(externalResource);
		}
	}

	public void load(ResourceRegistry registry) {
		loadRegistryFromElement(registry, this.getDocumentElement());
	}
}
