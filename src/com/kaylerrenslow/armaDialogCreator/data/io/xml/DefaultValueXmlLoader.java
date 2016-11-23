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
