package com.armadialogcreator.application;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @see ApplicationDataManager for {@link ApplicationData} instances
 @see Project for {@link ProjectData} instances
 @see Workspace for {@link WorkspaceData} instances
 @since 01/07/2019 */
public interface ADCDataListManager<D extends ADCData> {
	@NotNull ListObserver<D> getDataList();
}
