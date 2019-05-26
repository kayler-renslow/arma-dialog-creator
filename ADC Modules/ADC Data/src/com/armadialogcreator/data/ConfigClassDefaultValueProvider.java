package com.armadialogcreator.data;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

/**
 @author K
 @since 4/2/19 */
public class ConfigClassDefaultValueProvider {

	public static void addDefaultValues(@NotNull ConfigClass configClass, @NotNull DefaultValueSheet sheet,
										@NotNull Function<ConfigClass, DefaultValueSheet> nestedClassSheetProvider) {
		Env env = ExpressionEnvManager.instance.getEnv();

		for (Map.Entry<String, DefaultValueSheet.Property> entry : sheet.getProperties().entrySet()) {
			ConfigPropertyLookupConstant lookup = configClass.getLookup(entry.getKey());
			if (lookup == null) {
				continue;
			}
			ConfigProperty configProperty = configClass.addProperty(entry.getKey(),
					entry.getValue().toNewSerializableValue(lookup.getPropertyType(), env)
			);
			configProperty.setPriority(lookup.priority());
		}
		for (ConfigClass cc : configClass.iterateNestedClasses()) {
			DefaultValueSheet s = nestedClassSheetProvider.apply(cc);
			if (s == null) {
				continue;
			}
			addDefaultValues(cc, s, nestedClassSheetProvider);
		}
	}

	public static void addPropertyWithDefaultValue(@NotNull ConfigClass configClass, @NotNull DefaultValueSheet sheet,
												   @NotNull String property) {
		Env env = ExpressionEnvManager.instance.getEnv();
		DefaultValueSheet.Property dproperty = sheet.getProperties().get(property);
		if (dproperty != null) {
			ConfigPropertyLookupConstant lookup = configClass.getLookup(property);
			if (lookup == null) {
				return;
			}
			ConfigProperty configProperty = configClass.addProperty(property,
					dproperty.toNewSerializableValue(lookup.getPropertyType(), env)
			);
			configProperty.setPriority(lookup.priority());
		}
	}
}
