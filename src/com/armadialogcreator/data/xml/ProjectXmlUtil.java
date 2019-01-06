package com.armadialogcreator.data.xml;

import com.armadialogcreator.core.*;
import com.armadialogcreator.core.sv.SVRaw;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.data.CustomControlClassRegistry;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
import com.armadialogcreator.util.XmlUtil;
import com.armadialogcreator.util.XmlWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.*;
import java.util.function.Function;

/**
 Common XML writing/loading handling for save files.

 @author Kayler
 @since 11/12/2016. */
public class ProjectXmlUtil {

	private static final ResourceBundle bundle = Lang.getBundle("ProjectXmlParseBundle");

	/**
	 Loads a {@link ControlClassSpecification} from xml.

	 @param containerElement element that contains the xml tags written by {@link #writeControlClassSpecification(XmlWriter, ControlClassSpecification, Element)}
	 @param context used for loading {@link ControlPropertySpecification} via {@link #loadControlProperty(String, Element, DataContext, XmlErrorRecorder)}
	 */
	@NotNull
	public static List<ControlClassSpecification> loadControlClassSpecifications(@NotNull Element containerElement, @Nullable DataContext context, @NotNull XmlErrorRecorder recorder) {
		List<Element> classSpecElements = XmlUtil.getChildElementsWithTagName(containerElement, "class-spec");
		List<ControlClassSpecification> specs = new ArrayList<>(classSpecElements.size());

		for (Element classSpecElement : classSpecElements) {
			ControlClassSpecification specification = loadControlClassSpecification(classSpecElement, context, recorder);
			specs.add(specification);
		}
		return specs;
	}

	@NotNull
	public static ControlClassSpecification loadControlClassSpecification(@NotNull Element classSpecElement, @Nullable DataContext context, @NotNull XmlErrorRecorder recorder) {
		String className = classSpecElement.getAttribute("name");
		String extend = classSpecElement.getAttribute("extend").trim();
		if (extend.length() == 0) {
			extend = null;
		}
		List<ControlPropertySpecification> requiredProperties = new LinkedList<>();
		List<ControlPropertySpecification> optionalProperties = new LinkedList<>();
		List<ControlPropertyLookup> inheritProperties = new LinkedList<>();
		List<ControlClassSpecification> requiredClasses = ControlClassSpecification.EMPTY;
		List<ControlClassSpecification> optionalClasses = ControlClassSpecification.EMPTY;

		final String controlPropertyTagName = "property";

		//required control properties
		List<Element> requiredPropertyElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "required-properties");
		if (requiredPropertyElementGroups.size() > 0) {
			loadPropertyList(className, requiredProperties, context, recorder, controlPropertyTagName, requiredPropertyElementGroups.get(0));
		}

		//optional control properties
		List<Element> optionalPropertyElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "optional-properties");
		if (optionalPropertyElementGroups.size() > 0) {
			loadPropertyList(className, optionalProperties, context, recorder, controlPropertyTagName, optionalPropertyElementGroups.get(0));
		}

		//overridden control properties
		List<Element> inheritPropertyElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "inherit-properties");
		if (inheritPropertyElementGroups.size() > 0) {
			inheritProperties = loadInheritedControlProperties(inheritPropertyElementGroups.get(0), recorder);
		}


		//required sub classes
		List<Element> requiredClassElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "required-classes");
		if (requiredClassElementGroups.size() > 0) {
			Element requiredClassElementGroup = requiredClassElementGroups.get(0);
			requiredClasses = loadControlClassSpecifications(requiredClassElementGroup, context, recorder);
		}

		//optional sub classes
		List<Element> optionalClassElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "optional-classes");
		if (optionalClassElementGroups.size() > 0) {
			Element optionalClassElementGroup = optionalClassElementGroups.get(0);
			requiredClasses = loadControlClassSpecifications(optionalClassElementGroup, context, recorder);
		}

		ControlClassSpecification specification = new ControlClassSpecification(className,
				requiredProperties,
				optionalProperties,
				requiredClasses,
				optionalClasses
		);
		specification.setExtendClass(extend);
		specification.getInheritedProperties().addAll(inheritProperties);

		return specification;
	}

	private static void loadPropertyList(@NotNull String requester, @NotNull List<ControlPropertySpecification> list,
										 @Nullable DataContext context, @NotNull XmlErrorRecorder recorder,
										 @NotNull String controlPropertyTagName, @NotNull Element propertyElementGroup) {
		List<Element> propertyElements = XmlUtil.getChildElementsWithTagName(propertyElementGroup, controlPropertyTagName);
		List<Element> missingPropertyElements = XmlUtil.getChildElementsWithTagName(propertyElementGroup, "undefined");

		for (Element propertyElement : propertyElements) {
			ControlPropertySpecification p = loadControlProperty(requester, propertyElement, context, recorder);
			if (p != null) {
				list.add(p);
			}
		}
		final String lookupId = "id";
		final String macroKey = "macro-key";
		for (Element missingPropertyElement : missingPropertyElements) {
			String idAttr = missingPropertyElement.getAttribute(lookupId);
			String macroId = missingPropertyElement.getAttribute(macroKey);
			ControlPropertyLookup lookup = getLookup(idAttr, missingPropertyElement, recorder);
			if (lookup == null) {
				continue;
			}

			ControlPropertySpecification newSpec = new ControlPropertySpecification(lookup);
			if (macroId.length() > 0) {
				newSpec.setMacroKey(macroId);
			}
			list.add(newSpec);
		}

	}

	public static void writeCustomControls(@NotNull XmlWriter writer, @NotNull CustomControlClassRegistry registry, @NotNull Element addToEle) {
		Element customControlsEle = writer.appendElementToElement("custom-controls", addToEle);

		for (CustomControlClass customClass : registry.getControlClassList()) {
			Element controlClassEle = writer.appendElementToElement("custom-control", customControlsEle);
			ProjectXmlUtil.writeControlClassSpecification(writer, customClass.newSpecification(), controlClassEle);
			if (customClass.getComment() != null) {
				Element commentEle = writer.appendElementToElement("comment", controlClassEle);
				writer.appendTextNode(customClass.getComment(), commentEle);
			}
		}
	}

	/**
	 Used for loading {@link CustomControlClass} instances.

	 @param controlsElement root element that contains all written custom control classes
	 @param context used for {@link #loadControlClassSpecification(Element, DataContext, XmlErrorRecorder)}
	 @param recorder used for {@link #loadControlClassSpecification(Element, DataContext, XmlErrorRecorder)}
	 @param customClassParsed this function is invoked when a {@link ControlClassSpecification} is loaded that will
	 be used to construct a {@link CustomControlClass}
	 */
	public static void loadCustomControlClasses(@NotNull Element controlsElement,
												@Nullable DataContext context,
												@NotNull XmlErrorRecorder recorder,
												@NotNull Function<ControlClassSpecification, ?> customClassParsed
	) {
		List<Element> customControlsElementGroups = XmlUtil.getChildElementsWithTagName(controlsElement, "custom-controls");
		for (Element customControlClassesGroup : customControlsElementGroups) {
			List<Element> customControlElements = XmlUtil.getChildElementsWithTagName(customControlClassesGroup, "custom-control");
			for (Element customControlElement : customControlElements) {
				List<Element> controlClassSpecs = XmlUtil.getChildElementsWithTagName(customControlElement, "class-spec");
				if (controlClassSpecs.size() <= 0) {
					continue;
				}
				ControlClassSpecification spec = loadControlClassSpecification(controlClassSpecs.get(0), context, recorder);
				List<Element> commentElements = XmlUtil.getChildElementsWithTagName(customControlElement, "comment");
				String commentContent = null;
				if (commentElements.size() > 0) {
					commentContent = XmlUtil.getImmediateTextContent(commentElements.get(0));
				}
				spec.setComment(commentContent);

				customClassParsed.apply(spec);
			}
		}
	}

	/**
	 Writes a {@link ControlClassSpecification} to xml.

	 @param writer xml writer
	 @param specification specification to write
	 @param addToEle element to append the xml to
	 */
	public static void writeControlClassSpecification(@NotNull XmlWriter writer, @NotNull ControlClassSpecification specification, @NotNull Element addToEle) {
		Element classSpecEle = writer.appendElementToElement("class-spec", addToEle);
		classSpecEle.setAttribute("name", specification.getClassName());
		if (specification.getExtendClassName() != null) {
			classSpecEle.setAttribute("extend", specification.getExtendClassName());
		}

		//required control properties
		if (specification.getRequiredProperties().size() > 0) {
			Element requiredPropertiesEle = writer.appendElementToElement("required-properties", classSpecEle);
			for (ControlPropertySpecification property : specification.getRequiredControlProperties()) {
				if (property.getValue() == null) {
					writeMissingControlPropertyValue(writer, property, requiredPropertiesEle);
				} else {
					writeControlPropertySpecification(writer, property, requiredPropertiesEle);
				}
			}
		}

		//optional control properties
		if (specification.getOptionalProperties().size() > 0) {
			Element optionalPropertiesEle = writer.appendElementToElement("optional-properties", classSpecEle);
			for (ControlPropertySpecification property : specification.getOptionalControlProperties()) {
				if (property.getValue() == null) {
					writeMissingControlPropertyValue(writer, property, optionalPropertiesEle);
				} else {
					writeControlPropertySpecification(writer, property, optionalPropertiesEle);
				}
			}
		}

		//overridden properties
		if (specification.getInheritedProperties().size() > 0) {
			Element inheritPropertiesEle = writer.appendElementToElement("inherit-properties", classSpecEle);
			for (ControlPropertyLookupConstant propertyLookup : specification.getInheritedProperties()) {
				writeInheritControlPropertyLookup(writer, propertyLookup, inheritPropertiesEle);
			}
		}

		//required sub classes
		if (specification.getRequiredNestedClasses().size() > 0) {
			Element requiredClassesEle = writer.appendElementToElement("required-classes", classSpecEle);
			for (ControlClassSpecification s : specification.getRequiredNestedClasses()) {
				writeControlClassSpecification(writer, s, requiredClassesEle);
			}
		}

		//optional sub classes
		if (specification.getOptionalNestedClasses().size() > 0) {
			Element optionalClassesEle = writer.appendElementToElement("optional-classes", classSpecEle);
			for (ControlClassSpecification s : specification.getOptionalNestedClasses()) {
				writeControlClassSpecification(writer, s, optionalClassesEle);
			}
		}
	}

	/**
	 Writes a list of {@link ControlProperty} that are <b>not</b> overridden via {@link ControlClass#overrideProperty(ControlPropertyLookupConstant)}.
	 Only {@link ControlProperty#getPropertyLookup()} is written.
	 This method simply invokes {@link #writeInheritControlPropertyLookup(XmlWriter, ControlPropertyLookupConstant, Element)}
	 for each {@link ControlProperty} in <code>properties</code>

	 @param writer xml writer
	 @param properties properties to write
	 @param addToEle element to add XML to
	 */
	public static void writeInheritedControlProperties(@NotNull XmlWriter writer, @NotNull Iterable<ControlProperty> properties,
													   @NotNull Element addToEle) {
		for (ControlProperty property : properties) {
			writeInheritControlPropertyLookup(writer, property.getPropertyLookup(), addToEle);
		}
	}

	/**
	 Writes a {@link ControlPropertyLookupConstant} that is <b>not</b> overridden via {@link ControlClass#overrideProperty(ControlPropertyLookupConstant)}.
	 Only {@link ControlPropertyLookupConstant#getPropertyType()} is written.

	 @param writer xml writer
	 @param lookup lookup to write
	 @param addToEle element to add XML to
	 */
	public static void writeInheritControlPropertyLookup(@NotNull XmlWriter writer, @NotNull ControlPropertyLookupConstant lookup, @NotNull Element addToEle) {
		Element inheritPropertyEle = writer.appendElementToElement("inherit-property", addToEle);
		inheritPropertyEle.setAttribute("id", lookup.getPropertyId() + "");
	}

	@NotNull
	public static List<ControlPropertyLookup> loadInheritedControlProperties(@NotNull Element parent, @NotNull XmlErrorRecorder recorder) {
		List<Element> inheritPropertyElements = XmlUtil.getChildElementsWithTagName(parent, "inherit-property");
		List<ControlPropertyLookup> list = new LinkedList<>();
		final String lookupId = "id";
		for (Element inheritPropertyElement : inheritPropertyElements) {
			ControlPropertyLookup lookup = getLookup(inheritPropertyElement.getAttribute(lookupId), inheritPropertyElement, recorder);
			if (lookup != null) {
				list.add(lookup);
			}
		}
		return list;
	}

	private static void writeMissingControlPropertyValue(@NotNull XmlWriter writer, @NotNull ControlPropertySpecification property,
														 @NotNull Element addToEle) {
		Element undefinedEle = writer.appendElementToElement("undefined", addToEle);
		undefinedEle.setAttribute("id", property.getPropertyLookup().getPropertyId() + "");
		if (property.getMacroKey() != null) {
			undefinedEle.setAttribute("macro-key", property.getMacroKey());
		}
	}


	public static void writeControlProperty(@NotNull XmlWriter writer, @NotNull ControlProperty cprop, @NotNull Element addToEle) {
		writeControlProperty(
				writer,
				cprop.getPropertyLookup(),
				cprop.getMacro() == null ? null : cprop.getMacro().getKey(),
				cprop.getValue(),
				addToEle
		);
	}

	public static void writeControlPropertySpecification(@NotNull XmlWriter writer, @NotNull ControlPropertySpecification specification, @NotNull Element addToEle) {
		writeControlProperty(writer, specification.getPropertyLookup(), specification.getMacroKey(), specification.getValue(), addToEle);
	}

	private static void writeControlProperty(@NotNull XmlWriter stm, @NotNull ControlPropertyLookupConstant lookup,
											 @Nullable String macroKey, @Nullable SerializableValue value, @NotNull Element addToEle) {
		if (value == null) {
			return;
		}
		Element propertyEle = stm.appendElementToElement("property", addToEle);
		propertyEle.setAttribute("id", lookup.getPropertyId() + "");
		if (macroKey != null) {
			propertyEle.setAttribute("macro-key", macroKey);
		}
		if (value.getPropertyType() != lookup.getPropertyType()) {
			propertyEle.setAttribute("ptype", value.getPropertyType().getId() + "");
		}

		writeValue(stm, value, propertyEle);
	}

	/**
	 Loads a {@link ControlPropertySpecification} from the given xml element.

	 @param requester a name to use for error reporting that explains what/why is invoking this method
	 @param controlPropertyElement xml element (should be a &lt;property&gt; tag)
	 @param context used for fetching {@link SerializableValue} instances inside the {@link ControlProperty} xml text. See
	 {@link #loadValue(String, Element, PropertyType, DataContext, XmlErrorRecorder)}
	 @param recorder recorder to use for reporting errors
	 @return the instance, or null if couldn't be loaded
	 */
	@Nullable
	public static ControlPropertySpecification loadControlProperty(@NotNull String requester, @NotNull Element controlPropertyElement,
																   @Nullable DataContext context,
																   @NotNull XmlErrorRecorder recorder) {
		String lookupIdAttr = controlPropertyElement.getAttribute("id");
		String macroKeyAttr = controlPropertyElement.getAttribute("macro-key");
		String propertyTypeAttr = controlPropertyElement.getAttribute("ptype");
		PropertyType convertToPropertyType = null;
		if (propertyTypeAttr.length() != 0) {
			try {
				convertToPropertyType = PropertyType.findById(Integer.parseInt(propertyTypeAttr));
			} catch (IllegalArgumentException ignore) {

			}
		}
		ControlPropertyLookup lookup = getLookup(lookupIdAttr, controlPropertyElement.getParentNode(), recorder);
		if (lookup == null) {
			return null; //uncertain whether or not the control can be properly edited/rendered. So just skip control entirely.
		}
		requester = requester + ";" + lookup.getPropertyName();
		SerializableValue value;
		if (convertToPropertyType != null) {
			if (convertToPropertyType == PropertyType.Raw) {
				value = loadRawValue(requester, lookup.getPropertyType(), controlPropertyElement, recorder);
			} else {
				value = loadValue(requester, controlPropertyElement, convertToPropertyType, context, recorder);
				if (value == null) {
					recorder.addError(
							new ParseError(
									String.format(
											bundle.getString("ProjectLoad.ptype_bad_f"),
											convertToPropertyType
									)
							)
					);
				}
			}
		} else {
			value = loadValue(requester, controlPropertyElement, lookup.getPropertyType(), context, recorder);
		}
		//Don't worry about value being null.
		//If a value failed to be created, and thus a null value was returned, the user will be notified of such error

		return new ControlPropertySpecification(lookup, value, macroKeyAttr);
	}

	/**
	 Writes the given {@link SerializableValue} as a series of &lt;v&gt; tags. Example:"&lt;v&gt;1&lt;/v&gt;&lt;v&gt;0&lt;/v&gt;"
	 */
	public static void writeValue(@NotNull XmlWriter stm, @NotNull SerializableValue svalue, @NotNull Element addToEle) {
		for (String value : svalue.getAsStringArray()) {
			Element vEle = stm.appendElementToElement("v", addToEle);
			stm.appendTextNode(value, vEle);
		}
	}

	/**
	 Converts an xml element where it's children are value tags.<br>
	 Sample:<br>
	 <code>
	 &lt;parentElement&gt;<br>
	 &nbsp;&nbsp;&lt;v&gt;value text&lt;/v&gt;<br>
	 &nbsp;&nbsp;&lt;v&gt;value text&lt;/v&gt;<br>
	 &lt;/parentElement&gt;
	 </code>

	 @param requester the name of the thing that is requesting the value creation
	 @param parentElement element that is the parent of the value tags
	 @param propertyType type that will be used to determine the type of {@link ValueConverter}
	 @param dataContext context to use for when converting text values to Java objects. See {@link ValueConverter#convert(DataContext, String...)}
	 @return the {@link SerializableValue}, or null if couldn't be created (will log errors in errors).
	 */
	@Nullable
	public static SerializableValue loadValue(@NotNull String requester, @NotNull Element parentElement, @NotNull PropertyType propertyType,
											  @Nullable DataContext dataContext, @NotNull XmlErrorRecorder recorder) {
		List<Element> valueElements = XmlUtil.getChildElementsWithTagName(parentElement, "v");
		if (valueElements.size() < propertyType.getPropertyValuesSize()) {
			recorder.addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_value_creation_count_f"), parentElement.getTagName(), valueElements.size())));
			return null;
		}
		String[] values = new String[propertyType.getPropertyValuesSize()];
		int valueInd = 0;
		for (Element valueElement : valueElements) {
			if (valueInd >= values.length) {
				System.out.println(
						"WARNING: ProjectXmlUtil.loadValue: too many v XML elements for parent"
								+ parentElement.getTagName()
								+ " (" + parentElement.getTextContent().replaceAll("[\r\n]+", " ") + ")"
				);
				break;
			}
			values[valueInd++] = XmlUtil.getImmediateTextContent(valueElement);
		}
		SerializableValue value;
		try {
			value = SerializableValue.constructNew(dataContext, propertyType, values);
		} catch (Exception e) {
			recorder.addError(
					new ParseError(
							String.format(bundle.getString("ProjectLoad.could_not_create_value_f"), requester, Arrays.toString(values)),
							e
					)
			);
			return null;
		}
		return value;
	}


	@Nullable
	public static SVRaw loadRawValue(@NotNull String requester, @Nullable PropertyType substituteType, @NotNull Element parentElement,
									 @NotNull XmlErrorRecorder recorder) {
		List<Element> valueElements = XmlUtil.getChildElementsWithTagName(parentElement, "v");
		if (valueElements.size() < 1) {
			recorder.addError(
					new ParseError(
							String.format(
									bundle.getString("ProjectLoad.bad_value_creation_count_f"),
									requester,
									valueElements.size())
					)
			);
			return null;
		}

		return new SVRaw(XmlUtil.getImmediateTextContent(valueElements.get(0)), substituteType);
	}

	@Nullable
	public static ControlPropertyLookup getLookup(@NotNull String lookupIdStr, @NotNull Node parentNode, @NotNull XmlErrorRecorder recorder) {
		ControlPropertyLookup lookup;
		try {
			int id = Integer.parseInt(lookupIdStr);
			lookup = ControlPropertyLookup.findById(id);
			return lookup;
		} catch (IllegalArgumentException e) {
			recorder.addError(
					new ParseError(
							String.format(
									bundle.getString("ProjectLoad.bad_control_property_lookup_id_f"),
									lookupIdStr,
									parentNode.getNodeName()
							)
					)
			);
			return null;
		}
	}
}
