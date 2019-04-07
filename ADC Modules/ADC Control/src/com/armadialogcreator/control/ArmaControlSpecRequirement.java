package com.armadialogcreator.control;

import com.armadialogcreator.core.AllowedStyleProvider;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public interface ArmaControlSpecRequirement extends AllowedStyleProvider {
	ArmaControlSpecRequirement BASE = new ArmaControlSpecRequirement() {
	};

	/** Returns a new array of the properties that are required for all controls */
	ConfigPropertyLookupConstant[] defaultRequiredProperties = {
			ConfigPropertyLookup.TYPE,
			ConfigPropertyLookup.IDC,
			ConfigPropertyLookup.STYLE,
			ConfigPropertyLookup.X,
			ConfigPropertyLookup.Y,
			ConfigPropertyLookup.W,
			ConfigPropertyLookup.H};


	/** Returns a new array of properties that are optional for all controls */
	ConfigPropertyLookupConstant[] defaultOptionalProperties = {ConfigPropertyLookup.ACCESS};

	ReadOnlyList<ConfigPropertyLookupConstant> defaultRequiredPropertiesReadOnly = new ReadOnlyList<>(defaultRequiredProperties);
	ReadOnlyList<ConfigPropertyLookupConstant> defaultOptionalPropertiesReadOnly = new ReadOnlyList<>(defaultOptionalProperties);

	@NotNull
	default ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
		return defaultRequiredPropertiesReadOnly;
	}

	@NotNull
	default ReadOnlyList<ConfigPropertyLookupConstant> getOptionalProperties() {
		return defaultOptionalPropertiesReadOnly;
	}

	static ConfigPropertyLookup[] mergeArrays(@NotNull ConfigPropertyLookup[]... arrays) {
		int totalLengths = 0;
		for (ConfigPropertyLookup[] array : arrays) {
			totalLengths += array.length;
		}
		ConfigPropertyLookup[] neww = new ConfigPropertyLookup[totalLengths];
		int i = 0;
		for (ConfigPropertyLookup[] array : arrays) {
			for (ConfigPropertyLookup lookup : array) {
				neww[i++] = lookup;
			}
		}

		return neww;
	}

	@Override
	@NotNull
	default ReadOnlyArray<ControlStyle> getAllowedStyles() {
		return ReadOnlyArray.EMPTY;
	}
}
