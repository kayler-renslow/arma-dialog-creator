package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.List;

/**
 @author Kayler
 @since 11/22/2016 */
public class DefaultValueXmlLoader extends XmlLoader {
	public DefaultValueXmlLoader(@NotNull InputStream is, @Nullable DataContext context) throws XmlParseException {
		super(is, context, DataKeys.ENV);
	}

	public SerializableValue fetchValue(@NotNull ControlPropertyLookupConstant constantToFetch) {
		List<Element> propertyElements = XmlUtil.getChildElementsWithTagName(getDocumentElement(), "property");
		for (Element propertyElement : propertyElements) {
			String lookupIdAttr = propertyElement.getAttribute("lookup-id");
			if (lookupIdAttr.equals(constantToFetch.getPropertyId() + "")) {
				ControlPropertyLookup lookup = ProjectXmlUtil.getLookup(lookupIdAttr, getDocumentElement(), this);
				if (lookup == null) {
					return null;
				}
				return ProjectXmlUtil.loadValue(propertyElement, lookup.getPropertyType(), dataContext, this);
			}
		}
		return null;
	}

	public static SerializableValue fetchValue(@NotNull InputStream is, @NotNull DataContext context, @NotNull ControlPropertyLookupConstant constantToFetch) throws XmlParseException {
		DefaultValueXmlLoader loader = new DefaultValueXmlLoader(is, context);
		return loader.fetchValue(constantToFetch);
	}
}
