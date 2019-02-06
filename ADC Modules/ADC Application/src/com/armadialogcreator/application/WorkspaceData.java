package com.armadialogcreator.application;

/**
 When the {@link Workspace} initializes, place your new implemented instance of {@link WorkspaceData}
 in {@link Workspace#getDataList()}.
 <p>
 When the workspace is done initializing, the method {@link #loadFromConfigurable(Configurable)} will be invoked.
 This is where you can populate your data with values.
 <p>
 When the workspace closes, all instances in {@link Workspace#getDataList()} will automatically be saved in the workspace
 save file via {@link ConfigurableManager#exportToConfigurable(Configurable)}.
 <p>
 The method {@link #getDataID()} will be used to identify in the saved config file which {@link WorkspaceData} instance gets
 what {@link Configurable}.

 @author K
 @see DataLevel#Workspace
 @see ApplicationStateSubscriber
 @since 01/04/2019 */
public interface WorkspaceData extends ADCData {

}
