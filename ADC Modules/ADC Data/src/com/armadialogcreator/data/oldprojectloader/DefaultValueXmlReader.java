package com.armadialogcreator.data.oldprojectloader;

import com.armadialogcreator.core.old.ControlPropertyLookupConstant;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.XmlParseException;
import com.armadialogcreator.util.XmlReader;
import com.armadialogcreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.io.Reader;
import java.util.List;

/**
 @author Kayler
 @since 11/22/2016 */
public class DefaultValueXmlReader extends XmlReader {
	public DefaultValueXmlReader(@NotNull Reader r) throws XmlParseException {
		super(r);
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
