package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVRaw;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.CustomControlClassRegistry;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
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
		String extend = classSpecElement.getAttribute("extend").trim();
		if (extend.length() == 0) {
			extend = null;
		}
		List<ControlPropertySpecification> requiredProperties = new LinkedList<>();
		List<ControlPropertySpecification> optionalProperties = new LinkedList<>();
		List<ControlPropertyLookup> overriddenProperties = new LinkedList<>();
		List<ControlClassSpecification> requiredClasses = ControlClassSpecification.EMPTY;
		List<ControlClassSpecification> optionalClasses = ControlClassSpecification.EMPTY;

		final String controlPropertyTagName = "property";

		//required control properties
		List<Element> requiredPropertyElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "required-properties");
		if (requiredPropertyElementGroups.size() > 0) {
			loadPropertyList(requiredProperties, context, recorder, controlPropertyTagName, requiredPropertyElementGroups.get(0));
		}

		//optional control properties
		List<Element> optionalPropertyElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "optional-properties");
		if (optionalPropertyElementGroups.size() > 0) {
			loadPropertyList(optionalProperties, context, recorder, controlPropertyTagName, optionalPropertyElementGroups.get(0));
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
			requiredClasses = loadControlClassSpecifications(requiredClassElementGroup, context, recorder);
		}

		//optional sub classes
		List<Element> optionalClassElementGroups = XmlUtil.getChildElementsWithTagName(classSpecElement, "optional-classes");
		if (optionalClassElementGroups.size() > 0) {
			Element optionalClassElementGroup = optionalClassElementGroups.get(0);
			requiredClasses = loadControlClassSpecifications(optionalClassElementGroup, context, recorder);
		}

		ControlClassSpecification specification = new ControlClassSpecification(className, requiredProperties, optionalProperties, requiredClasses, optionalClasses);
		for (ControlPropertyLookup overriden : overriddenProperties) {
			specification.overrideProperty(overriden);
		}

		specification.setExtendClass(extend);

		return specification;
	}

	private static void loadPropertyList(@NotNull List<ControlPropertySpecification> list, @Nullable DataContext context, @NotNull XmlErrorRecorder recorder,
										 @NotNull String controlPropertyTagName, @NotNull Element propertyElementGroup) {
		List<Element> propertyElements = XmlUtil.getChildElementsWithTagName(propertyElementGroup, controlPropertyTagName);
		List<Element> missingPropertyElements = XmlUtil.getChildElementsWithTagName(propertyElementGroup, "undefined");

		for (Element propertyElement : propertyElements) {
			ControlPropertySpecification p = loadControlProperty(propertyElement, context, recorder);
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

	public static void writeCustomControls(@NotNull XmlWriterOutputStream stm,
										   @NotNull CustomControlClassRegistry registry) throws IOException {
		String customControlClasses = "custom-controls";
		stm.writeBeginTag(customControlClasses);
		final String customControl = "custom-control";
		final String comment = "comment";
		for (CustomControlClass customClass : registry.getControlClassList()) {
			stm.writeBeginTag(customControl);
			ProjectXmlUtil.writeControlClassSpecification(stm, customClass.newSpecification());
			if (customClass.getComment() != null) {
				stm.writeBeginTag(comment);
				stm.write(customClass.getComment());
				stm.writeCloseTag(comment);
			}
			stm.writeCloseTag(customControl);
		}
		stm.writeCloseTag(customControlClasses);
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
	) throws IOException {
		final String customControls = "custom-controls";
		final String customControl = "custom-control";
		final String comment = "comment";
		final String classSpec = "class-spec";
		List<Element> customControlsElementGroups = XmlUtil.getChildElementsWithTagName(controlsElement, customControls);
		for (Element customControlClassesGroup : customControlsElementGroups) {
			List<Element> customControlElements = XmlUtil.getChildElementsWithTagName(customControlClassesGroup, customControl);
			for (Element customControlElement : customControlElements) {
				List<Element> controlClassSpecs = XmlUtil.getChildElementsWithTagName(customControlElement, classSpec);
				if (controlClassSpecs.size() <= 0) {
					continue;
				}
				ControlClassSpecification spec = loadControlClassSpecification(controlClassSpecs.get(0), context, recorder);
				List<Element> commentElements = XmlUtil.getChildElementsWithTagName(customControlElement, comment);
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

	 @param stm xml writer stream
	 @param specification specification to write
	 */
	public static void writeControlClassSpecification(@NotNull XmlWriterOutputStream stm, @NotNull ControlClassSpecification specification) throws IOException {
		stm.writeBeginTag(String.format(
				"class-spec name='%s'%s", specification.getClassName(),
				specification.getExtendClassName() != null ? String.format(" extend='%s'", specification.getExtendClassName()) : "")
		);

		//required control properties
		if (specification.getRequiredProperties().size() > 0) {
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
		if (specification.getOptionalProperties().size() > 0) {
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
		if (specification.getInheritedProperties().size() > 0) {
			final String inheritedProperties = "inherit-properties";
			stm.writeBeginTag(inheritedProperties);
			for (ControlPropertySpecification property : specification.getInheritedProperties()) {
				writeInheritControlPropertyLookup(stm, property.getPropertyLookup());
			}
			stm.writeCloseTag(inheritedProperties);
		}

		//required sub classes
		if (specification.getRequiredNestedClasses().size() > 0) {
			final String requiredClasses = "required-classes";
			stm.writeBeginTag(requiredClasses);
			for (ControlClassSpecification s : specification.getRequiredNestedClasses()) {
				writeControlClassSpecification(stm, s);
			}
			stm.writeCloseTag(requiredClasses);
		}

		//optional sub classes
		if (specification.getOptionalNestedClasses().size() > 0) {
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
	public static void writeInheritedControlProperties(@NotNull XmlWriterOutputStream stm, @NotNull Iterable<ControlProperty> properties) throws IOException {
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
		stm.write("<inherit-property id='" + lookup.getPropertyId() + "' />");
	}

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

	private static void writeMissingControlPropertyValue(@NotNull XmlWriterOutputStream stm, @NotNull ControlPropertySpecification property) throws IOException {
		stm.write("<undefined id='" + property.getPropertyLookup().getPropertyId() + "'");
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
		stm.writeBeginTag(String.format("property id='%d'%s%s",
				lookup.getPropertyId(),
				macroKey == null ? "" : String.format(" macro-key='%s'", macroKey),
				value.getPropertyType() != lookup.getPropertyType() ? String.format(" ptype='%s'", value.getPropertyType().getId()) : ""
				)
		);

		writeValue(stm, value);
		stm.writeCloseTag("property");
	}

	/**
	 Loads a {@link ControlPropertySpecification} from the given xml element.

	 @param controlPropertyElement xml element (should be a &lt;property&gt; tag)
	 @param context used for fetching {@link SerializableValue} instances inside the {@link ControlProperty} xml text. See
	 {@link #loadValue(String, Element, PropertyType, DataContext, XmlErrorRecorder)}
	 @param recorder recorder to use for reporting errors
	 @return the instance, or null if couldn't be loaded
	 */
	@Nullable
	public static ControlPropertySpecification loadControlProperty(@NotNull Element controlPropertyElement, @Nullable DataContext context, @NotNull XmlErrorRecorder recorder) {
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
		SerializableValue value;
		if (convertToPropertyType != null) {
			if (convertToPropertyType == PropertyType.Raw) {
				value = loadRawValue(lookup.getPropertyType(), controlPropertyElement, recorder);
			} else {
				value = loadValue(lookup.getPropertyName(), controlPropertyElement, convertToPropertyType, context, recorder);
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
			value = loadValue(lookup.getPropertyName(), controlPropertyElement, lookup.getPropertyType(), context, recorder);
		}
		if (value == null) {
			return null;
		}
		return new ControlPropertySpecification(lookup, value, macroKeyAttr);
	}

	/**
	 Writes the given {@link SerializableValue} as a series of &lt;v&gt; tags. Example:"&lt;v&gt;1&lt;/v&gt;&lt;v&gt;0&lt;/v&gt;"
	 */
	public static void writeValue(@NotNull XmlWriterOutputStream stm, @NotNull SerializableValue svalue) throws IOException {
		for (String value : svalue.getAsStringArray()) {
			stm.write("<v>");
			stm.write(XmlWriterOutputStream.esc(value));
			stm.write("</v>");
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
			e.printStackTrace(System.out);
			recorder.addError(new ParseError(String.format(bundle.getString("ProjectLoad.could_not_create_value_f"), requester, Arrays.toString(values))));
			return null;
		}
		return value;
	}


	@Nullable
	public static SVRaw loadRawValue(@Nullable PropertyType substituteType, @NotNull Element parentElement,
									 @NotNull XmlErrorRecorder recorder) {
		List<Element> valueElements = XmlUtil.getChildElementsWithTagName(parentElement, "v");
		if (valueElements.size() < 1) {
			recorder.addError(
					new ParseError(
							String.format(
									bundle.getString("ProjectLoad.bad_value_creation_count_f"),
									parentElement.getTagName(),
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
