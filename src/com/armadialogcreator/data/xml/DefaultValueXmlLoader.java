package com.armadialogcreator.data.xml;

import com.armadialogcreator.core.ControlPropertyLookupConstant;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.XmlLoader;
import com.armadialogcreator.util.XmlParseException;
import com.armadialogcreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import java.io.Reader;
import java.util.List;

/**
 @author Kayler
 @since 11/22/2016 */
public class DefaultValueXmlLoader extends XmlLoader {
	public DefaultValueXmlLoader(@NotNull Reader r, @Nullable DataContext context) throws XmlParseException {
		super(r, context, Env.ENV);
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
