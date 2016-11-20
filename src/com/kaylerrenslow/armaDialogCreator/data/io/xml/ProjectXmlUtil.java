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

import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 Common XML writing/loading handling for save files.

 @author Kayler
 @since 11/12/2016. */
public class ProjectXmlUtil {

	/**
	 Loads a {@link ControlClassSpecification} from xml.

	 @param containerElement element that contains the xml tags written by {@link #writeControlClassSpecification(XmlWriterOutputStream, ControlClassSpecification)}
	 @param context used for loading {@link ControlPropertySpecification} via {@link #loadControlProperty(Element, DataContext, XmlErrorRecorder)}
	 */
	public static List<ControlClassSpecification> loadControlClassSpecifications(@NotNull Element containerElement, @Nullable DataContext context, @NotNull XmlErrorRecorder recorder) {
		List<Element> classSpecElements = XmlUtil.getChildElementsWithTagName(containerElement, "class-spec");
		List<ControlClassSpecification> specs = new ArrayList<>(classSpecElements.size());

		for (Element classSpecElement : classSpecElements) {
			ControlClassSpecification specification = loadControlClassSpecification(classSpecElement, context, recorder);
			if (specification != null) {
				specs.add(specification);
			}
		}
		return specs;
	}

	public static ControlClassSpecification loadControlClassSpecification(@NotNull Element classSpecElement, @Nullable DataContext context, @NotNull XmlErrorRecorder recorder) {
		String className = classSpecElement.getAttribute("name");
		String extend = classSpecElement.getAttribute("extend");
		ControlPropertySpecification[] requiredProperties = ControlPropertySpecification.EMPTY;
		ControlPropertySpecification[] optionalProperties = ControlPropertySpecification.EMPTY;
		List<ControlPropertyLookup> overriddenProperties = new LinkedList<>();
		ControlClassSpecification[] requiredClasses = ControlClassSpecification.EMPTY;
		ControlClassSpecification[] optionalClasses = ControlClassSpecification.EMPTY;

		final String controlProperty = "control-property";

		//required control properties
		List<Element> requiredPropertyElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "required-properties");
		if (requiredPropertyElementGroups.size() > 0) {
			requiredProperties = loadPropertyArray(context, recorder, controlProperty, requiredPropertyElementGroups.get(0));
		}

		//optional control properties
		List<Element> optionalPropertyElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "optional-properties");
		if (optionalPropertyElementGroups.size() > 0) {
			optionalProperties = loadPropertyArray(context, recorder, controlProperty, optionalPropertyElementGroups.get(0));
		}

		//overridden control properties
		List<Element> overriddenPropertyElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "overridden-properties");
		if (overriddenPropertyElementGroups.size() > 0) {
			overriddenProperties = loadInheritedControlProperties(overriddenPropertyElementGroups.get(0), recorder);
		}


		//required sub classes
		List<Element> requiredClassElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "required-classes");
		if (requiredClassElementGroups.size() > 0) {
			Element requiredClassElementGroup = requiredClassElementGroups.get(0);
			requiredClasses = loadControlClassSpecifications(requiredClassElementGroup, context, recorder).toArray(new ControlClassSpecification[0]);
		}

		//optional sub classes
		List<Element> optionalClassElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "optional-classes");
		if (optionalClassElementGroups.size() > 0) {
			Element optionalClassElementGroup = optionalClassElementGroups.get(0);
			requiredClasses = loadControlClassSpecifications(optionalClassElementGroup, context, recorder).toArray(new ControlClassSpecification[0]);
		}

		ControlClassSpecification specification = new ControlClassSpecification(className, requiredProperties, optionalProperties, requiredClasses, optionalClasses);
		for (ControlPropertyLookup overriden : overriddenProperties) {
			specification.overrideProperty(overriden);
		}

		specification.setExtendClass(extend);

		return specification;
	}

	private static ControlPropertySpecification[] loadPropertyArray(@Nullable DataContext context, @NotNull XmlErrorRecorder recorder, String controlProperty, Element propertyElementGroup) {
		List<Element> propertyElements = XmlUtil.getChildElementsWithTagName(propertyElementGroup, controlProperty);
		List<Element> missingPropertyElements = XmlUtil.getChildElementsWithTagName(propertyElementGroup, "undefined");

		ControlPropertySpecification[] propertiesArray = new ControlPropertySpecification[propertyElements.size() + missingPropertyElements.size()];
		int i = 0;
		for (Element propertyElement : propertyElements) {
			propertiesArray[i++] = loadControlProperty(propertyElement, context, recorder);
		}
		final String lookupId = "lookup-id";
		final String macroKey = "macro-key";
		for (Element missingPropertyElement : missingPropertyElements) {
			String idAttr = missingPropertyElement.getAttribute(lookupId);
			String macroId = missingPropertyElement.getAttribute(macroKey);
			ControlPropertyLookup lookup = getLookup(idAttr, missingPropertyElement, recorder);
			if (lookup == null) {
				return ControlPropertySpecification.EMPTY;
			}

			ControlPropertySpecification newSpec = new ControlPropertySpecification(lookup);
			if (macroId.length() > 0) {
				newSpec.setMacroKey(macroId);
			}
			propertiesArray[i++] = newSpec;
		}
		return propertiesArray;
	}

	/**
	 Writes a {@link ControlClassSpecification} to xml.

	 @param stm xml writer stream
	 @param specification specification to write
	 */
	public static void writeControlClassSpecification(@NotNull XmlWriterOutputStream stm, @NotNull ControlClassSpecification specification) throws IOException {
		stm.writeBeginTag(String.format(
				"class-spec name='%s'%s", specification.getClassName(),
				specification.getExtendClassName() != null ? String.format(" extend='%s'", specification.getExtendClassName()) : "")
		);

		//required control properties
		if (specification.getRequiredProperties().length > 0) {
			final String requiredProperties = "required-properties";
			stm.writeBeginTag(requiredProperties);
			for (ControlPropertySpecification property : specification.getRequiredControlProperties()) {
				if (property.getValue() == null) {
					writeMissingControlPropertyValue(stm, property);
				} else {
					writeControlPropertySpecification(stm, property);
				}
			}
			stm.writeCloseTag(requiredProperties);
		}

		//optional control properties
		if (specification.getOptionalProperties().length > 0) {
			final String optionalProperties = "optional-properties";
			stm.writeBeginTag(optionalProperties);
			for (ControlPropertySpecification property : specification.getOptionalControlProperties()) {
				if (property.getValue() == null) {
					writeMissingControlPropertyValue(stm, property);
				} else {
					writeControlPropertySpecification(stm, property);
				}
			}
			stm.writeCloseTag(optionalProperties);
		}

		//overridden properties
		if (specification.getOverriddenProperties().size() > 0) {
			final String overriddenProperties = "overridden-properties";
			stm.writeBeginTag(overriddenProperties);
			for (ControlPropertySpecification property : specification.getOverriddenProperties()) {
				writeInheritControlPropertyLookup(stm, property.getPropertyLookup());
			}
			stm.writeCloseTag(overriddenProperties);
		}

		//required sub classes
		if (specification.getRequiredNestedClasses().length > 0) {
			final String requiredClasses = "required-classes";
			stm.writeBeginTag(requiredClasses);
			for (ControlClassSpecification s : specification.getRequiredNestedClasses()) {
				writeControlClassSpecification(stm, s);
			}
			stm.writeCloseTag(requiredClasses);
		}

		//optional sub classes
		if (specification.getOptionalNestedClasses().length > 0) {
			final String optionalClasses = "optional-classes";
			stm.writeBeginTag(optionalClasses);
			for (ControlClassSpecification s : specification.getOptionalNestedClasses()) {
				writeControlClassSpecification(stm, s);
			}
			stm.writeCloseTag(optionalClasses);
		}

		stm.writeCloseTag("class-spec");
	}

	/**
	 Writes a list of {@link ControlProperty} that are <b>not</b> overridden via {@link ControlClass#overrideProperty(ControlPropertyLookupConstant)}. Only {@link ControlProperty#getPropertyLookup()}
	 is written. This method simply invokes {@link #writeInheritControlPropertyLookup(XmlWriterOutputStream, ControlPropertyLookupConstant)} for each {@link ControlProperty} in
	 <code>properties</code>

	 @param stm xml writer stream
	 @param properties properties to write
	 @throws IOException
	 */
	public static void writeInheritedControlProperties(@NotNull XmlWriterOutputStream stm, @NotNull List<ControlProperty> properties) throws IOException {
		for (ControlProperty property : properties) {
			writeInheritControlPropertyLookup(stm, property.getPropertyLookup());
		}
	}

	/**
	 Writes a {@link ControlPropertyLookupConstant} that is <b>not</b> overridden via {@link ControlClass#overrideProperty(ControlPropertyLookupConstant)}. Only
	 {@link ControlPropertyLookupConstant#getPropertyType()} is written.

	 @param stm xml writer stream
	 @param lookup lookup to write
	 @throws IOException
	 */
	public static void writeInheritControlPropertyLookup(@NotNull XmlWriterOutputStream stm, @NotNull ControlPropertyLookupConstant lookup) throws IOException {
		stm.write("<inherit-property lookup-id='" + lookup.getPropertyId() + "' />");
	}

	public static List<ControlPropertyLookup> loadInheritedControlProperties(@NotNull Element parent, @NotNull XmlErrorRecorder recorder) {
		List<Element> inheritPropertyElements = XmlUtil.getChildElementsWithTagName(parent, "inherit-property");
		List<ControlPropertyLookup> list = new LinkedList<>();
		final String lookupId = "lookup-id";
		for (Element inheritPropertyElement : inheritPropertyElements) {
			ControlPropertyLookup lookup = getLookup(inheritPropertyElement.getAttribute(lookupId), inheritPropertyElement, recorder);
			if (lookup != null) {
				list.add(lookup);
			}
		}
		return list;
	}

	private static void writeMissingControlPropertyValue(@NotNull XmlWriterOutputStream stm, @NotNull ControlPropertySpecification property) throws IOException {
		stm.write("<undefined lookup-id='" + property.getPropertyLookup().getPropertyId() + "'");
		if (property.getMacroKey() != null) {
			stm.write(" macro-key='" + property.getMacroKey() + "'");
		}
		stm.write("/>");
	}


	public static void writeControlProperty(@NotNull XmlWriterOutputStream stm, @NotNull ControlProperty cprop) throws IOException {
		writeControlProperty(stm, cprop.getPropertyLookup(), cprop.getMacro() == null ? null : cprop.getMacro().getKey(), cprop.getValue());
	}

	public static void writeControlPropertySpecification(@NotNull XmlWriterOutputStream stm, @NotNull ControlPropertySpecification specification) throws IOException {
		writeControlProperty(stm, specification.getPropertyLookup(), specification.getMacroKey(), specification.getValue());
	}

	private static void writeControlProperty(@NotNull XmlWriterOutputStream stm, @NotNull ControlPropertyLookupConstant lookup, @Nullable String macroKey, @Nullable SerializableValue value)
			throws IOException {
		if (value == null) {
			return;
		}
		stm.writeBeginTag(String.format("control-property lookup-id='%d'%s",
				lookup.getPropertyId(),
				macroKey == null ? "" : String.format(" macro-key='%s'", macroKey)
				)
		);
		writeValueTags(stm, value);
		stm.writeCloseTag("control-property");
	}

	/**
	 Loads a {@link ControlPropertySpecification} from the given xml element.

	 @param controlPropertyElement xml element (should be a &lt;control-property&gt; tag)
	 @param context used for fetching {@link SerializableValue} instances inside the {@link ControlProperty} xml text. See {@link #getValue(DataContext, PropertyType, Element, XmlErrorRecorder)}
	 @param recorder recorder to use for reporting errors
	 */
	public static ControlPropertySpecification loadControlProperty(@NotNull Element controlPropertyElement, @Nullable DataContext context, @NotNull XmlErrorRecorder recorder) {
		String lookupIdStr = controlPropertyElement.getAttribute("lookup-id");
		String macroKey = controlPropertyElement.getAttribute("macro-key");
		ControlPropertyLookup lookup = getLookup(lookupIdStr, controlPropertyElement.getParentNode(), recorder);
		if (lookup == null) {
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

	@Nullable
	private static ControlPropertyLookup getLookup(@NotNull String lookupIdStr, @NotNull Node parentNode, @NotNull XmlErrorRecorder recorder) {
		ControlPropertyLookup lookup;
		try {
			int id = Integer.parseInt(lookupIdStr);
			lookup = ControlPropertyLookup.findById(id);
			return lookup;
		} catch (IllegalArgumentException e) {
			recorder.addError(
					new ParseError(
							String.format(
									Lang.ApplicationBundle().getString("XmlParse.ProjectLoad.bad_control_property_lookup_id_f"),
									lookupIdStr,
									parentNode.getNodeName()
							)
					)
			);
			return null;
		}
	}
}
