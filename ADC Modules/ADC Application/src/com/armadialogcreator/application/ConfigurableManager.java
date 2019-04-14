package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/06/2019 */
public interface ConfigurableManager {
	void loadFromConfigurable(@NotNull Configurable config);

	void exportToConfigurable(@NotNull Configurable configurable);
}
