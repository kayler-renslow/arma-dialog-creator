package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationDataManager;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.core.ControlClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 @author K
 @since 01/04/2019 */
public class ControlClassRegistry extends ConfigClassRegistryBase<ControlClass> {

	private static final ControlClassRegistry instance = new ControlClassRegistry(new ConfigClassConfigurableHandler() {
		@Override
		public void loadFromConfigurable(@NotNull Configurable config, @NotNull List classes) {

		}

		@Override
		public @NotNull Configurable exportToConfigurable(@NotNull List classes, @NotNull DataLevel level) {
			return null;
		}
	});

	static {
		ApplicationDataManager.getInstance().addStateSubscriber(instance);
	}

	@NotNull
	public static ControlClassRegistry getInstance() {
		return instance;
	}


	protected ControlClassRegistry(@NotNull ConfigClassConfigurableHandler configurableHandler) {
		super(configurableHandler);
	}
}
