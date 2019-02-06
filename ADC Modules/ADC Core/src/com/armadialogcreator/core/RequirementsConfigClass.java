package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 @author K
 @since 01/06/2019 */
public class RequirementsConfigClass extends ConfigClass {

	public RequirementsConfigClass(@NotNull String className) {
		super(className);
	}

	@NotNull
	public ReadOnlyList<String> getRequiredProperties() {
		return null;
	}

	@NotNull
	public ReadOnlyList<String> getOptionalProperties() {
		return null;
	}

	@NotNull
	public List<String> getUserSpecifiedProperties() {
		return null;
	}
}
