package com.armadialogcreator.data;

import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 @author K
 @since 3/31/19 */
public class ControlDefaultValueProvider {
	public static void addDefaultValuesFromSystemSheet(@NotNull ArmaControl control) {
		DefaultValueProviderSheetRegistry registry = DefaultValueProviderSheetRegistry.instance;
		String name = registry.getSystemSheetName("Control." + control.getControlType().name());
		DefaultValueSheet sheet = registry.get(name, DataLevel.System);
		if (sheet == null) {
			throw new IllegalStateException(name);
		}
		addDefaultValues(control, sheet);
	}

	public static void addDefaultValues(@NotNull ArmaControl control, @NotNull DefaultValueSheet sheet) {
		Env env = ExpressionEnvManager.instance.getEnv();
		for (Map.Entry<String, DefaultValueSheet.Property> entry : sheet.getProperties().entrySet()) {
			ConfigPropertyLookup lookup = control.getLookup(entry.getKey());
			if (lookup == null) {
				continue;
			}
			control.addProperty(entry.getKey(),
					entry.getValue().toNewSerializableValue(lookup.getPropertyType(), env)
			);
		}
	}
}
