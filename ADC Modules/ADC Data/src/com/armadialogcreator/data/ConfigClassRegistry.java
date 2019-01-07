package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationDataManager;
import com.armadialogcreator.core.ConfigClass;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/04/2019 */
public class ConfigClassRegistry extends ConfigClassRegistryBase<ConfigClass> {

	private static final ConfigClassRegistry instance = new ConfigClassRegistry();

	static {
		ApplicationDataManager.getInstance().addStateSubscriber(instance);
	}

	@NotNull
	public static ConfigClassRegistry getInstance() {
		return instance;
	}

}
