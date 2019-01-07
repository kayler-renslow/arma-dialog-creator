package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationDataManager;
import com.armadialogcreator.core.ControlClass;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/04/2019 */
public class ControlClassRegistry extends ConfigClassRegistryBase<ControlClass> {

	private static final ControlClassRegistry instance = new ControlClassRegistry();

	static {
		ApplicationDataManager.getInstance().addStateSubscriber(instance);
	}

	@NotNull
	public static ControlClassRegistry getInstance() {
		return instance;
	}


}
