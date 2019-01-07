package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/06/2019 */
public interface ADCDataRenewable<D extends ADCData> {
	@NotNull D constructNew();
}
