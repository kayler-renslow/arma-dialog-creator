package com.armadialogcreator.application;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 When the application initializes, reflection will be used to find all implementing classes of this interface.
 The {@link #constructNew()} method will be invoked to instantiate the class and then the instance will be placed
 in {@link ApplicationDataManager#getApplicationDataList()}.
 <p>
 When the application is done initializing, the method {@link #loadFromConfigurable(Configurable)} will be invoked.
 This is where you can populate your data with values.
 <p>
 When the application closes, all instances in the list will automatically be saved in the Application level
 config.xml file via {@link #exportToConfigurable()}.
 <p>
 The method {@link #getDataID()} will be used to identify in the saved config file which {@link ApplicationData} instance gets
 what {@link Configurable}.

 @see DataLevel#Application
 @author K
 @since 01/04/2019 */
public interface ApplicationData extends ADCData<ApplicationData> {
	/**
	 This method is invoked automatically once when the Application initializes.

	 @return a new instance of whatever you want to be placed on {@link ApplicationDataManager#getApplicationDataList()}
	 */
	@Override
	@NotNull ApplicationData constructNew();

	/**
	 This method is invoked automatically when the Application is done initializing.

	 @param config the config previously saved from last Application config save (or it's empty meaning no config present)
	 */
	@Override
	void loadFromConfigurable(@NotNull Configurable config);

	/**
	 This method is invoked automatically when the Application is closing.

	 @return config the config used to place in the config save
	 */
	@Override
	@NotNull Configurable exportToConfigurable();

	/** A universally unique id used for identifying which part of the config belongs to which {@link ApplicationData} instance */
	@Override
	@NonNls
	@NotNull String getDataID();
}
