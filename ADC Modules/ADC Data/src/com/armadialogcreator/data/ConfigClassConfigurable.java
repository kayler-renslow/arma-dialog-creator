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
		String name = configurable.getAttributeValueNotNull("name");
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
			switch (nested.getConfigurableName()) {
				case "property": {
					String propertyName = nested.getAttributeValueNotNull("name");
					String macro = nested.getAttributeValue("macro");
					String priorityStr = nested.getAttributeValue("priority");

					if (macro != null) {
						jobConsumer.accept(new SetMacroJob(configClass, propertyName, macro));
					}
					Configurable svConf = nested.getConfigurableNotNull(SerializableValueConfigurable.CONFIGURABLE_NAME);
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
					break;
				}
				case "comment": {
					configClass.setUserComment(nested.getConfigurableBody());
					break;
				}
				case "nested-class": {
					ConfigClass myNested = fromConfigurable(nested, null, jobConsumer);
					configClass.addNestedClass(myNested);
					break;
				}
			}
		}
		return configClass;
	}

	private final List<KeyValueString> atts = new ArrayList<>(2);
	private final List<Configurable> nestedConfs;

	public ConfigClassConfigurable(@NotNull ConfigClass configClass, boolean appendInherited) {
		atts.add(new KeyValueString("name", configClass.getClassName()));
		if (configClass.isExtending()) {
			atts.add(new KeyValueString("extend", configClass.getExtendClassName()));
		}
		nestedConfs = new ArrayList<>(configClass.getNonInheritedPropertyCount() + configClass.getNestedClassesCount() + 1);
		for (ConfigProperty property : configClass.iterateProperties()) {
			final boolean propertyIsInherited = configClass.propertyIsInherited(property.getName());
			if (appendInherited || !propertyIsInherited) {
				Configurable.Simple propertyConf = new Configurable.Simple("property");
				propertyConf.addAttribute("priority", property.priority() + "");
				nestedConfs.add(propertyConf);
				propertyConf.addAttribute("name", property.getName());
				if (property.isBoundToMacro()) {
					propertyConf.addAttribute("macro", property.getBoundMacro().getKey());
				}
				propertyConf.addNestedConfigurable(new SerializableValueConfigurable(property.getValue()));
				if (appendInherited) {
					propertyConf.addAttribute("inherited", propertyIsInherited);
				}
			}
		}
		String userComment = configClass.getUserComment();
		if (userComment != null) {
			nestedConfs.add(new Simple("comment", userComment));
		}
		for (ConfigClass nested : configClass.iterateNestedClasses()) {
			final boolean nestedIsInherited = configClass.nestedConfigClassIsInherited(nested.getClassName());
			if (appendInherited || !nestedIsInherited) {
				ConfigClassConfigurable c = new ConfigClassConfigurable(nested, appendInherited);
				nestedConfs.add(c);
				c.addAttribute("inherited", nestedIsInherited);
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
