package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.core.sv.SerializableValueConstructionException;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 @author K
 @since 3/7/19 */
public class DefaultValueSheet {

	private final String name;
	private final Map<String, Property> properties = new HashMap<>();

	public DefaultValueSheet(@NotNull String name) {
		this.name = name;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setFromConfigurable(@NotNull Configurable config) {
		properties.clear();
		for (Configurable nested : config.getNestedConfigurables()) {
			Property p = Property.newFromConfigurable(nested);
			properties.put(p.getPropertyName(), p);
		}
	}

	@NotNull
	public Configurable exportToConfigurable() {
		Configurable.Simple sheet = new Configurable.Simple("sheet");
		sheet.addAttribute("name", name);

		for (Map.Entry<String, Property> entry : properties.entrySet()) {
			sheet.addNestedConfigurable(entry.getValue().exportToConfigurable());
		}

		return sheet;
	}

	@NotNull
	public Map<String, Property> getProperties() {
		return properties;
	}

	public static class Property {
		protected final String propertyName;
		protected final @NotNull String[] values;
		protected final Map<String, String> attributes;

		public Property(@NotNull String propertyName, @NotNull String[] values) {
			this.propertyName = propertyName;
			this.values = values;
			attributes = new HashMap<>();
		}

		protected Property(String propertyName, @NotNull String[] values, @NotNull Map<String, String> attributes) {
			this.propertyName = propertyName;
			this.values = values;
			this.attributes = attributes;
		}

		@NotNull
		public String getPropertyName() {
			return propertyName;
		}

		@NotNull
		public Map<String, String> getAttributes() {
			return attributes;
		}

		@NotNull
		public SerializableValue toNewSerializableValue(@NotNull PropertyType propertyType, @NotNull Env env)
				throws SerializableValueConstructionException {
			return SerializableValue.constructNew(env, propertyType, values);
		}

		@NotNull
		protected Configurable exportToConfigurable() {
			Configurable.Simple property = new Configurable.Simple("property");
			property.addAttribute("name", propertyName);
			Configurable.Simple vs = new Configurable.Simple("vs");
			property.addNestedConfigurable(vs);
			for (String s : values) {
				Configurable.Simple v = new Configurable.Simple("v");
				v.setBody(s);
				vs.addNestedConfigurable(v);
			}
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				Configurable.Simple a = new Configurable.Simple("a");
				a.addAttribute("name", entry.getKey());
				a.setBody(entry.getKey());
				property.addNestedConfigurable(a);
			}
			return property;
		}

		@NotNull
		protected static Property newFromConfigurable(@NotNull Configurable c) {
			String name = c.getAttributeValue("name");
			String[] values = null;
			Iterable<Configurable> nestedConfigs = c.getNestedConfigurables();
			Map<String, String> atts = new HashMap<>();
			for (Configurable nested : nestedConfigs) {
				switch (nested.getConfigurableName()) {
					case "vs": {
						int nestedCount = nested.getNestedConfigurableCount();
						values = new String[nestedCount];
						int i = 0;
						for (Configurable v : nested.getNestedConfigurables()) {
							values[i++] = v.getConfigurableBody();
						}
						break;
					}
					case "a": {
						String attName = nested.getAttributeValue("name");
						atts.put(attName, nested.getConfigurableBody());
						break;
					}
				}
			}
			if (values == null) {
				throw new IllegalStateException();
			}
			return new Property(name, values, atts);
		}
	}
}
