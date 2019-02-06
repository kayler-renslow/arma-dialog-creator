package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.core.ConfigClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 @author K
 @since 01/04/2019 */
public class ConfigClassRegistry extends ConfigClassRegistryBase<ConfigClass> {

	private static final ConfigClassRegistry instance = new ConfigClassRegistry(new ConfigClassConfigurableHandler<>() {
		@Override
		public void loadFromConfigurable(@NotNull Configurable config, @NotNull List<ConfigClass> classes) {

		}

		@Override
		public void exportToConfigurable(@NotNull Configurable config, @NotNull List<ConfigClass> classes, @NotNull DataLevel level) {

		}

	});

	static {
		ApplicationManager.getInstance().addStateSubscriber(instance);
	}

	@NotNull
	public static ConfigClassRegistry getInstance() {
		return instance;
	}

	protected ConfigClassRegistry(@NotNull ConfigClassConfigurableHandler<ConfigClass> configurableHandler) {
		super(configurableHandler);
	}
}
