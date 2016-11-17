/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVBoolean;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVDouble;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVInteger;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 Created by Kayler on 09/15/2016.
 */
public enum DisplayPropertyLookup implements ControlPropertyLookupConstant {
	IDD(0, "idd", PropertyType.INT, new String[]{"The unique id to the display."}),
	MOVING_ENABLE(1, "movingEnable", PropertyType.BOOLEAN, new String[]{"True if the display can be moved with mouse, false otherwise."}),
	ENABLE_SIMULATION(2, "enableSimulation", PropertyType.BOOLEAN, new String[]{"True if the display will allow user interaction (e.g. buttons can be pressed), false otherwise."});

	public static final DisplayPropertyLookup[] EMPTY = new DisplayPropertyLookup[0];

	private final @Nullable ControlPropertyOption[] options;
	private final String propertyName;
	private final PropertyType propertyType;
	private final String[] about;
	private final int propertyId;

	DisplayPropertyLookup(int propertyId, @NotNull String propertyName, @NotNull PropertyType propertyType, @NotNull String[] about, @Nullable ControlPropertyOption... options) {
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
	public ControlPropertyOption[] getOptions() {
		return options;
	}

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

	@Override
	public String[] getAbout() {
		return about;
	}

	public DisplayProperty getPropertyWithNoData() {
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
		if (getPropertyType() != PropertyType.INT) {
			throw new IllegalStateException("can't get int property when property type isn't int");
		}
		return new DisplayProperty(this, new SVInteger(defaultValue));
	}

	public DisplayProperty getFloatProperty(double defaultValue) {
		if (getPropertyType() != PropertyType.FLOAT) {
			throw new IllegalStateException("can't get float property when property type isn't float");
		}
		return new DisplayProperty(this, new SVDouble(defaultValue));
	}

	public DisplayProperty getBooleanProperty(boolean defaultValue) {
		if (getPropertyType() != PropertyType.BOOLEAN) {
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
