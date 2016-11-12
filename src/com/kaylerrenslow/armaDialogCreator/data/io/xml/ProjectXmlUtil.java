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

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertySpecification;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 @author Kayler
 Commmon XML writing/loading handling for save files.
 Created on 11/12/2016. */
public class ProjectXmlUtil {

	public static void writeControlProperty(@NotNull XmlWriterOutputStream stm, @NotNull ControlProperty cprop) throws IOException {
		stm.write(String.format("<control-property lookup-id='%d'%s>",
				cprop.getPropertyLookup().getPropertyId(),
				cprop.getMacro() == null ? "" : String.format(" macro-key='%s'", cprop.getMacro().getKey())
				)
		);
		if (cprop.getValue() == null) {
			throw new IllegalStateException("control property value is not allowed to be null if it is defined (ArmaControl.getDefinedProperties())");
		}
		writeValueTags(stm, cprop.getValue());
		stm.write("</control-property>");
	}

	/**
	 Loads a {@link ControlPropertySpecification} from the given xml element.

	 @param controlPropertyElement xml element (should be a &lt;control-property&gt; tag)
	 @param context used for fetching {@link SerializableValue} instances inside the {@link ControlProperty} xml text. See {@link #getValue(DataContext, PropertyType, Element, XmlErrorRecorder)}
	 @param recorder recorder to use for reporting errors
	 */
	public static ControlPropertySpecification loadControlProperty(@NotNull Element controlPropertyElement, @Nullable DataContext context, @NotNull XmlErrorRecorder recorder) {
		ControlPropertyLookup lookup;
		String lookupIdStr = controlPropertyElement.getAttribute("lookup-id");
		String macroKey = controlPropertyElement.getAttribute("macro-key");
		try {
			int id = Integer.parseInt(lookupIdStr);
			lookup = ControlPropertyLookup.findById(id);
		} catch (IllegalArgumentException e) {
			recorder.addError(
					new ParseError(
							String.format(
									Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_control_property_lookup_id_f"),
									lookupIdStr,
									controlPropertyElement.getParentNode().toString()
							)
					)
			);
			return null; //uncertain whether or not the control can be properly edited/rendered. So just skip control entirely.
		}
		SerializableValue value = getValue(context, lookup.getPropertyType(), controlPropertyElement, recorder);
		if (value == null) {
			return null; //uncertain whether or not the control can be properly edited/rendered. So just skip control entirely.
		}
		return new ControlPropertySpecification(lookup, value, macroKey);
	}

	/**
	 Writes the given {@link SerializableValue} as a series of &lt;value&gt; tags. Example:"&lt;value&gt;1&lt;/value&gt;&lt;value&gt;0&lt;/value&gt;"
	 */
	public static void writeValueTags(@NotNull XmlWriterOutputStream stm, @NotNull SerializableValue svalue) throws IOException {
		for (String value : svalue.getAsStringArray()) {
			stm.write("<value>");
			stm.write(XmlWriterOutputStream.esc(value));
			stm.write("</value>");
		}
	}

	/**
	 Converts an xml element where it's children are value tags.<br>
	 Sample:<br>
	 <code>
	 &lt;parentElement&gt;<br>
	 &nbsp;&nbsp;&lt;value&gt;value text&lt;/value&gt;<br>
	 &nbsp;&nbsp;&lt;value&gt;value text&lt;/value&gt;<br>
	 &lt;/parentElement&gt;
	 </code>

	 @param dataContext context to use for when converting text values to Java objects. See {@link ValueConverter#convert(DataContext, String...)}
	 @param propertyType type that will be used to determine the type of {@link ValueConverter}
	 @param parentElement element that is the parent of the value tags
	 @return the {@link SerializableValue}, or null if couldn't be created (will log errors in errors).
	 */
	public static SerializableValue getValue(@Nullable DataContext dataContext, @NotNull PropertyType propertyType, @NotNull Element parentElement, @NotNull XmlErrorRecorder recorder) {
		List<Element> valueElements = XmlUtil.getChildElementsWithTagName(parentElement, "value");
		if (valueElements.size() < propertyType.propertyValuesSize) {
			recorder.addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_value_creation_count_f"), parentElement.getTagName(), valueElements.size())));
			return null;
		}
		String[] values = new String[propertyType.propertyValuesSize];
		int valueInd = 0;
		for (Element valueElement : valueElements) {
			values[valueInd++] = XmlUtil.getImmediateTextContent(valueElement);
		}
		SerializableValue value;
		try {
			value = propertyType.converter.convert(dataContext, values);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			recorder.addError(new ParseError(String.format(Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_values_f"), Arrays.toString(values))));
			return null;
		}
		return value;
	}
}
