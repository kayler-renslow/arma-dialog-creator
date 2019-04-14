package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 Indicates what level data is saved.

 @author K
 @since 01/06/2019 */
public enum DataLevel {
	/**
	 Pre-existing data on the user's computer.
	 This is used when data is being used from Arma 3 or from data built inside ADC itself
	 */
	System((byte) 0),
	/**
	 Saved at the Application level.
	 When the user launches ADC, no matter what project or workspace is loaded, the data will be present.
	 */
	Application((byte) 1),
	/** Data is dependent on what {@link Workspace} is loaded */
	Workspace((byte) 2),
	/** Data is dependent on what {@link Project} is loaded */
	Project((byte) 3);

	private final byte loadOrder;

	DataLevel(byte loadOrder) {
		this.loadOrder = loadOrder;
	}

	/**
	 The load order of a {@link DataLevel} is the order in which data in a level will be loaded.
	 The order is as follows:
	 <ol>
	 <li>{@link #System} (loaded first)</li>
	 <li>{@link #Application}</li>
	 <li>{@link #Workspace}</li>
	 <li>{@link #Project} (loaded last)</li>
	 </ol>
	 <p>
	 This number is ranged 0-3 (inclusive, inclusive). This all means that if you have data at the {@link #System} level,
	 it will be loaded first. If you have it at {@link #Application}, it will be loaded after all {@link #System} data is loaded.
	 Etc.

	 @return the load order.
	 */
	public byte getLoadOrder() {
		return loadOrder;
	}

	/** @return if this instance's {@link #getLoadOrder()} is less than the other */
	public boolean loadsBefore(@NotNull DataLevel other) {
		return loadOrder < other.loadOrder;
	}

	/** @return if this instance's {@link #getLoadOrder()} is greater than the other */
	public boolean loadsAfter(@NotNull DataLevel other) {
		return loadOrder > other.loadOrder;
	}
}


