package com.armadialogcreator.application;

/**
 Indicates what level data is saved.

 @author K
 @since 01/06/2019 */
public enum DataLevel {
	/**
	 Pre-existing data on the user's computer.
	 This is used when data is being used from Arma 3 or from data built inside ADC itself
	 */
	System,
	/**
	 Saved at the Application level.
	 When the user launches ADC, no matter what project or workspace is loaded, the data will be present.
	 */
	Application,
	/** Data is dependent on what {@link Workspace} is loaded */
	Workspace,
	/** Data is dependent on what {@link Project} is loaded */
	Project
}
