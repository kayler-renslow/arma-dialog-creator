package com.armadialogcreator.application;

/**
 While {@link ApplicationState#ApplicationDataInitializing} is active, place your new implemented instance of {@link ApplicationData}
 in {@link ApplicationDataManager#getDataList()}.
 <p>
 When the application data is done initializing, the method {@link #loadFromConfigurable(Configurable)} will be invoked.
 This is where you can populate your data with values.
 <p>
 When the application closes, all instances in the list will automatically be saved in the Application level
 config.xml file via {@link ConfigurableManager#exportToConfigurable(Configurable)}.
 <p>
 The method {@link #getDataID()} will be used to identify in the saved config file which {@link ApplicationData} instance gets
 what {@link Configurable}.

 @author K
 @see DataLevel#Application
 @see ApplicationStateSubscriber
 @since 01/04/2019 */
public interface ApplicationData extends ADCData {

}
