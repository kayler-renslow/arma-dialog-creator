package com.armadialogcreator.application;

/**
 When the {@link Workspace} initializes, place your new implemented instance of {@link WorkspaceData}
 in {@link Workspace#getWorkspaceDataList()}.
 <p>
 When the workspace is done initializing, the method {@link #loadFromConfigurable(Configurable)} will be invoked.
 This is where you can populate your data with values.
 <p>
 When the workspace closes, all instances in {@link Workspace#getWorkspaceDataList()} will automatically be saved in the workspace
 save file via {@link #exportToConfigurable()}.
 <p>
 The method {@link #getDataID()} will be used to identify in the saved config file which {@link WorkspaceData} instance gets
 what {@link Configurable}.
 <p>
 The method {@link #constructNew()} will be invoked every time a new {@link Workspace} is loaded and thus a new instance
 of {@link WorkspaceData} should be created that is independent from the previous workspace.

 @author K
 @see DataLevel#Workspace
 @see ApplicationStateSubscriber
 @since 01/04/2019 */
public interface WorkspaceData extends ADCData, ADCDataRenewable<WorkspaceData> {

}
