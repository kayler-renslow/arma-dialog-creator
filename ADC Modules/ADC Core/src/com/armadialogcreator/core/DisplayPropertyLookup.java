package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 Created by Kayler on 09/15/2016.
 */
public enum DisplayPropertyLookup implements ConfigPropertyLookupConstant {
	IDD(0, "idd", PropertyType.Int, "The unique id to the display."),
	MOVING_ENABLE(1, "movingEnable", PropertyType.Boolean, "True if the display can be moved with mouse, false otherwise."),
	ENABLE_SIMULATION(2, "enableSimulation", PropertyType.Boolean, "True if the display will allow user interaction (e.g. buttons can be pressed), false otherwise.");

	public static final DisplayPropertyLookup[] EMPTY = new DisplayPropertyLookup[0];

	private final @Nullable ReadOnlyArray<ConfigPropertyValueOption> options;
	private final String propertyName;
	private final PropertyType propertyType;
	private final String about;
	private final int propertyId;
	private final ConfigPropertyKey key;

	DisplayPropertyLookup(int propertyId, @NotNull String propertyName, @NotNull PropertyType propertyType, @NotNull String about, @NotNull ConfigPropertyValueOption... options) {
		if (PropertiesLookupDataVerifier.usedIds.contains(propertyId)) {
			throw new IllegalArgumentException("display propertyId for " + name() + " is not unique");
		}
		if (propertyId < 0) {
			throw new IllegalArgumentException("propertyId for " + name() + " can't be <0");
		}
		PropertiesLookupDataVerifier.usedIds.add(propertyId);
		this.propertyId = propertyId;
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.about = about;
		this.options = options.length == 0 ? ReadOnlyArray.EMPTY : new ReadOnlyArray<>(options);
		key = new ConfigPropertyKey.Simple(propertyName);
	}

	@Override
	public String toString() {
		return propertyName;
	}

	@Nullable
	@Override
	public ReadOnlyArray<ConfigPropertyValueOption> getOptions() {
		return options;
	}

	@NotNull
	@Override
	public String getPropertyName() {
		return propertyName;
	}

	@Override
	@NotNull
	public ConfigPropertyKey getHashSafeKey() {
		return key;
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return propertyType;
	}

	@Override
	public int getPropertyId() {
		return propertyId;
	}

	@Override
	public boolean isEvent() {
		return false;
	}

	@NotNull
	public String getAbout() {
		return about;
	}

	/** @throws IllegalArgumentException when id couldn't be matched */
	@NotNull
	public static DisplayPropertyLookup findById(int id) {
		for (DisplayPropertyLookup lookup : values()) {
			if (lookup.propertyId == id) {
				return lookup;
			}
		}
		throw new IllegalArgumentException("id " + id + " couldn't be matched");
	}


	private static class PropertiesLookupDataVerifier {
		static ArrayList<Integer> usedIds = new ArrayList<>();
	}
}
