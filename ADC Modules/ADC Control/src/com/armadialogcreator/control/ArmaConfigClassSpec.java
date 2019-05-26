package com.armadialogcreator.control;

import com.armadialogcreator.core.AllowedStyleProvider;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.ReadOnlyList;
import com.armadialogcreator.util.ReadOnlyMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 Created by Kayler on 07/07/2016.
 */
public interface ArmaConfigClassSpec extends AllowedStyleProvider {
	ArmaConfigClassSpec BASE = new ArmaConfigClassSpec() {
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

	/** @return a new list of {@link #getRequiredProperties()} and {@link #getOptionalProperties()} concatenated together */
	@NotNull
	default ReadOnlyList<ConfigPropertyLookupConstant> getAllProperties() {
		int size = getOptionalProperties().size() + getRequiredProperties().size();
		if (size > 0) {
			ArrayList<ConfigPropertyLookupConstant> props = new ArrayList<>(size);
			props.addAll(getOptionalProperties());
			props.addAll(getRequiredProperties());
			return new ReadOnlyList<>(props);
		}
		return getRequiredProperties();
	}

	@NotNull
	default ReadOnlyMap<String, ArmaConfigClassSpec> getNestedConfigClasses() {
		return new ReadOnlyMap<>(new HashMap<>());
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
