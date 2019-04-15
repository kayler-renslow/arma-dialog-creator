package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.core.sv.SerializableValueConstructionException;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 @author K
 @since 01/06/2019 */
public class SerializableValueConfigurable implements Configurable {
	public static final String CONFIGURABLE_NAME = "sv";
	public static final String PROPERTY_TYPE_ATTR_NAME = "type";

	private final SerializableValue sv;

	public SerializableValueConfigurable(@NotNull SerializableValue sv) {
		this.sv = sv;
	}

	@Override
	@NotNull
	public Iterable<Configurable> getNestedConfigurables() {
		String[] stringArray = sv.getAsStringArray();
		List<Configurable> vs = new ArrayList<>(stringArray.length);
		for (String s : stringArray) {
			Simple c = new Simple("v");
			c.setBody(s);
			vs.add(c);
		}
		return vs;
	}

	@Override
	public int getNestedConfigurableCount() {
		return sv.getPropertyType().getPropertyValuesSize();
	}

	@Override
	@NotNull
	public Iterable<KeyValueString> getConfigurableAttributes() {
		List<KeyValueString> list = new ArrayList<>(1);
		list.add(new KeyValueString(PROPERTY_TYPE_ATTR_NAME, sv.getPropertyType().getId() + ""));
		return list;
	}

	@Override
	public int getConfigurableAttributeCount() {
		return 1;
	}

	@Override
	public void addNestedConfigurable(@NotNull Configurable c) {

	}

	@Override
	public void addAttribute(@NotNull String key, @NotNull String value) {

	}

	@Override
	@NotNull
	public String getConfigurableName() {
		return CONFIGURABLE_NAME;
	}

	@Override
	@NotNull
	public String getConfigurableBody() {
		return "";
	}

	@NotNull
	public static SerializableValue createFromConfigurable(@NotNull Configurable svConfigurable, @NotNull Env env) throws SerializableValueConstructionException {
		String propertyTypeIdText = svConfigurable.getAttributeValue(PROPERTY_TYPE_ATTR_NAME);
		if (propertyTypeIdText == null) {
			throw new SerializableValueConstructionException();
		}
		PropertyType propertyType;
		try {
			propertyType = PropertyType.findById(Integer.parseInt(propertyTypeIdText));
		} catch (Exception e) {
			throw new SerializableValueConstructionException(e);
		}
		List<Configurable> valueElements = new ArrayList<>(7);
		for (Configurable nested : svConfigurable.getNestedConfigurables()) {
			if (nested.getConfigurableName().equals("v")) {
				valueElements.add(nested);
			}
		}
		if (valueElements.size() < propertyType.getPropertyValuesSize()) {
			throw new SerializableValueConstructionException();
			//recorder.addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_value_creation_count_f"), parentElement.getTagName(), valueElements.size())));
		}
		String[] values = new String[propertyType.getPropertyValuesSize()];
		int valueInd = 0;
		for (Configurable valueElement : valueElements) {
			if (valueInd >= values.length) {
				//				System.out.println(
				//						"WARNING: ProjectXmlUtil.loadValue: too many v XML elements for parent"
				//								+ parentElement.getTagName()
				//								+ " (" + parentElement.getTextContent().replaceAll("[\r\n]+", " ") + ")"
				//				);
				break;
			}
			values[valueInd++] = valueElement.getConfigurableBody();
		}
		SerializableValue value;
		try {
			value = SerializableValue.constructNew(env, propertyType, values);
		} catch (Exception e) {
			//			recorder.addError(
			//					new ParseError(
			//							String.format(bundle.getString("ProjectLoad.could_not_create_value_f"), requester, Arrays.toString(values)),
			//							e
			//					)
			//			);
			throw new SerializableValueConstructionException(e);
		}
		return value;
	}
}
