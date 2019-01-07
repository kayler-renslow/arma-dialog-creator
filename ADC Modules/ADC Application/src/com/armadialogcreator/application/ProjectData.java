package com.armadialogcreator.application;

/**
 When the project initializes, place your new implemented instance of {@link ProjectData}
 in {@link Project#getProjectDataList()}.
 <p>
 When the project is done initializing, the method {@link #loadFromConfigurable(Configurable)} will be invoked.
 This is where you can populate your data with values.
 <p>
 When the project closes, all instances in {@link Project#getProjectDataList()} will automatically be saved in the project
 save file via {@link #exportToConfigurable()}.
 <p>
 The method {@link #getDataID()} will be used to identify in the saved config file which {@link ProjectData} instance gets
 what {@link Configurable}.
 <p>
 The method {@link #constructNew()} will be invoked every time a new {@link Project} is loaded and thus a new instance
 of {@link ProjectData} should be created that is independent from the previous project.

 @author K
 @see DataLevel#Project
 @see ApplicationStateSubscriber
 @since 01/04/2019 */
public interface ProjectData extends ADCData, ADCDataRenewable<ProjectData> {

}
