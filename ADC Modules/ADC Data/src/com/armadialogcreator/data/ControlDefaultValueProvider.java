package com.armadialogcreator.data;

import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.control.ArmaControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 3/31/19 */
public class ControlDefaultValueProvider {
	public static void addDefaultValuesFromSystemSheet(@NotNull ArmaControl control) {
		DefaultValueSheet sheet = getSystemSheet(control, null);
		addDefaultValues(control, sheet);
	}

	public static void addDefaultValues(@NotNull ArmaControl control, @NotNull DefaultValueSheet sheet) {
		ConfigClassDefaultValueProvider.addDefaultValues(control, sheet, configClass -> {
			return getSystemSheet(control, configClass.getClassName());
		});
	}

	@NotNull
	private static DefaultValueSheet getSystemSheet(@NotNull ArmaControl control, @Nullable String nestedClassName) {
		DefaultValueSheetRegistry registry = DefaultValueSheetRegistry.instance;
		String name = registry.getSystemSheetName("Control." + control.getControlType().name());
		if (nestedClassName != null) {
			if (control.findNestedClassNullable(nestedClassName) != null) {
				name += "." + nestedClassName;
			}
		}
		DefaultValueSheet sheet = registry.get(name, DataLevel.System);
		if (sheet == null) {
			throw new IllegalStateException(name);
		}
		return sheet;
	}
}
