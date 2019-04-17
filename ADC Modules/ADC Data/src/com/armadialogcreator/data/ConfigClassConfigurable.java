package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.data.ConfigClassJob.ExtendConfigClassJob;
import com.armadialogcreator.data.ConfigClassJob.SetMacroJob;
import com.armadialogcreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 @author kayler
 @since 4/16/19 */
public class ConfigClassConfigurable implements Configurable {
	@NotNull
	public static ConfigClass fromConfigurable(@NotNull Configurable configurable, @Nullable ConfigClass configClass,
											   @NotNull Consumer<ConfigClassJob> jobConsumer) {
		String name = configurable.getAttributeValue("name");
		if (name == null) {
			throw new IllegalStateException();
		}
		String extend = configurable.getAttributeValue("extend");
		if (configClass == null) {
			configClass = new ConfigClass(name);
		} else {
			configClass.setClassName(name);
		}
		if (extend != null) {
			jobConsumer.accept(new ExtendConfigClassJob(configClass, extend));
		}
		for (Configurable nested : configurable.getNestedConfigurables()) {
			String propertyName = nested.getAttributeValue("name");
			String macro = nested.getAttributeValue("macro");
			String priorityStr = nested.getAttributeValue("priority");

			if (propertyName == null) {
				throw new IllegalStateException();
			}
			if (macro != null) {
				jobConsumer.accept(new SetMacroJob(configClass, propertyName, macro));
			}
			Configurable svConf = nested.getConfigurable(SerializableValueConfigurable.CONFIGURABLE_NAME);
			if (svConf == null) {
				throw new IllegalStateException();
			}
			SerializableValue sv = SerializableValueConfigurable.createFromConfigurable(
					svConf,
					ExpressionEnvManager.instance.getEnv()
			);
			ConfigProperty property = configClass.addProperty(propertyName, sv);
			if (priorityStr != null && priorityStr.length() > 0) {
				try {
					property.setPriority(Integer.parseInt(priorityStr));
				} catch (NumberFormatException ignore) {

				}
			}
		}
		return configClass;
	}

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
