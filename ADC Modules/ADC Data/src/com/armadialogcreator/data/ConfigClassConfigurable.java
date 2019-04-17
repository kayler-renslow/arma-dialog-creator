package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 @author kayler
 @since 4/16/19 */
public class ConfigClassConfigurable implements Configurable {
	private final List<KeyValueString> atts = new ArrayList<>(2);
	private final List<Configurable> nestedConfs;

	public ConfigClassConfigurable(@NotNull ConfigClass configClass) {
		atts.add(new KeyValueString("name", configClass.getClassName()));
		if (configClass.isExtending()) {
			atts.add(new KeyValueString("extend", configClass.getExtendClassName()));
		}
		nestedConfs = new ArrayList<>(configClass.getNonInheritedPropertyCount());
		for (ConfigProperty property : configClass.iterateProperties()) {
			if (!configClass.propertyIsInherited(property.getName())) {
				Configurable.Simple propertyConf = new Configurable.Simple("property");
				propertyConf.addAttribute("priority", property.priority() + "");
				nestedConfs.add(propertyConf);
				propertyConf.addAttribute("name", property.getName());
				if (property.isBoundToMacro()) {
					propertyConf.addAttribute("macro", property.getBoundMacro().getKey());
				}
				propertyConf.addNestedConfigurable(new SerializableValueConfigurable(property.getValue()));

			}
		}
	}

	@Override
	@NotNull
	public Iterable<Configurable> getNestedConfigurables() {
		return nestedConfs;
	}

	@Override
	public int getNestedConfigurableCount() {
		return nestedConfs.size();
	}

	@Override
	@NotNull
	public Iterable<KeyValueString> getConfigurableAttributes() {
		return atts;
	}

	@Override
	public int getConfigurableAttributeCount() {
		return atts.size();
	}

	@Override
	public void addNestedConfigurable(@NotNull Configurable c) {
		nestedConfs.add(c);
	}

	@Override
	public void addAttribute(@NotNull String key, @NotNull String value) {
		atts.add(new KeyValueString(key, value));
	}

	@Override
	@NotNull
	public String getConfigurableName() {
		return "config-class";
	}

	@Override
	@NotNull
	public String getConfigurableBody() {
		return "";
	}
}
