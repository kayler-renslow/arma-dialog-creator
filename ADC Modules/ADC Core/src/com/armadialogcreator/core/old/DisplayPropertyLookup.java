package com.armadialogcreator.core.old;

import com.armadialogcreator.core.sv.SVBoolean;
import com.armadialogcreator.core.sv.SVDouble;
import com.armadialogcreator.core.sv.SVInteger;
import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 Created by Kayler on 09/15/2016.
 */
public enum DisplayPropertyLookup implements ControlPropertyLookupConstant {
	IDD(0, "idd", PropertyType.Int, "The unique id to the display."),
	MOVING_ENABLE(1, "movingEnable", PropertyType.Boolean, "True if the display can be moved with mouse, false otherwise."),
	ENABLE_SIMULATION(2, "enableSimulation", PropertyType.Boolean, "True if the display will allow user interaction (e.g. buttons can be pressed), false otherwise.");

	public static final DisplayPropertyLookup[] EMPTY = new DisplayPropertyLookup[0];

	private final @Nullable ControlPropertyOptionOld[] options;
	private final String propertyName;
	private final PropertyType propertyType;
	private final String about;
	private final int propertyId;

	DisplayPropertyLookup(int propertyId, @NotNull String propertyName, @NotNull PropertyType propertyType, @NotNull String about, @Nullable ControlPropertyOptionOld... options) {
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
		this.options = options;
	}

	@Override
	public String toString() {
		return propertyName;
	}

	@Nullable
	@Override
	public ControlPropertyOptionOld[] getOptions() {
		return options;
	}

	@NotNull
	@Override
	public String getPropertyName() {
		return propertyName;
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

	@NotNull
	public String getAbout() {
		return about;
	}

	@NotNull
	public DisplayProperty newEmptyProperty() {
		return new DisplayProperty(this);
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

	public DisplayProperty getIntProperty(int defaultValue) {
		if (getPropertyType() != PropertyType.Int) {
			throw new IllegalStateException("can't get int property when property type isn't int");
		}
		return new DisplayProperty(this, new SVInteger(defaultValue));
	}

	public DisplayProperty getFloatProperty(double defaultValue) {
		if (getPropertyType() != PropertyType.Float) {
			throw new IllegalStateException("can't get float property when property type isn't float");
		}
		return new DisplayProperty(this, new SVDouble(defaultValue));
	}

	public DisplayProperty getBooleanProperty(boolean defaultValue) {
		if (getPropertyType() != PropertyType.Boolean) {
			throw new IllegalStateException("can't get boolean property when property type isn't boolean");
		}
		return new DisplayProperty(this, SVBoolean.get(defaultValue));
	}

	public DisplayProperty getProperty(SerializableValue defaultValue) {
		return new DisplayProperty(this, defaultValue);
	}

	private static class PropertiesLookupDataVerifier {
		static ArrayList<Integer> usedIds = new ArrayList<>();
	}
}
