package com.armadialogcreator.data;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 4/2/19 */
public class ConfigClassDefaultValueProvider {

	public static void addPropertyWithDefaultValue(@NotNull ConfigClass configClass, @NotNull DefaultValueSheet sheet,
												   @NotNull String property) {
		Env env = ExpressionEnvManager.instance.getEnv();
		DefaultValueSheet.Property dproperty = sheet.getProperties().get(property);
		if (dproperty != null) {
			ConfigPropertyLookupConstant lookup = configClass.getLookup(property);
			if (lookup == null) {
				return;
			}
			configClass.addProperty(property,
					dproperty.toNewSerializableValue(lookup.getPropertyType(), env)
			);
		}
	}
}
