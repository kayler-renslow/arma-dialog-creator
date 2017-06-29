package com.kaylerrenslow.armaDialogCreator.data.xml;

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
 Used to load external resources from an xml file
 @author Kayler
 @since 09/10/2016. */
public class ResourceRegistryXmlLoader extends XmlLoader {

	/**
	 Construct a loader for an xml file that has the root tag "external-resources".
	 This constructed loader is used explicitly for {@link ResourceRegistryXmlWriter.WorkspaceResourceRegistryXmlWriter}.
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
			throw new IllegalArgumentException("externalResourcesTag does not have the tag name 'external-resources'");
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
			resourceRegistry.addResource(externalResource);
		}
	}

	public void load(ResourceRegistry registry) {
		loadRegistryFromElement(registry, this.getDocumentElement());
	}
}
