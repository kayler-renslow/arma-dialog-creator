package com.kaylerrenslow.armaDialogCreator.data.xml;

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
			String popertyNameAttr = propertyElement.getAttribute("name");
			if (popertyNameAttr.equals(constantToFetch.getPropertyName())) {
				return ProjectXmlUtil.loadValue(constantToFetch.getPropertyName(), propertyElement, constantToFetch.getPropertyType(), dataContext, this);
			}
		}
		return null;
	}
}
