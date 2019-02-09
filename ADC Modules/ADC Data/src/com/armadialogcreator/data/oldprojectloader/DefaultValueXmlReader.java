package com.armadialogcreator.data.oldprojectloader;

import com.armadialogcreator.core.old.ControlPropertyLookupConstant;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.XmlParseException;
import com.armadialogcreator.util.XmlReader;
import com.armadialogcreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.io.Reader;

/**
 @author Kayler
 @since 11/22/2016 */
public class DefaultValueXmlReader extends XmlReader {
	public DefaultValueXmlReader(@NotNull Reader r) throws XmlParseException {
		super(r);
	}

	public SerializableValue fetchValue(@NotNull ControlPropertyLookupConstant constantToFetch) {
		for (Element e : XmlUtil.iterateChildElements(getDocumentElement())) {
			if (e.getNodeName().equals("property")) {
				String popertyNameAttr = e.getAttribute("name");
				if (popertyNameAttr.equals(constantToFetch.getPropertyName())) {
					//					return ProjectXmlUtil.loadValue(constantToFetch.getPropertyName(), e, constantToFetch.getPropertyType(), dataContext, this);
				}
			}
		}
		return null;
	}
}
