package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 @author K
 @since 01/06/2019 */
public class RequirementsConfigClass extends ConfigClass {

	private final List<ConfigPropertyKey> userProperties = new ArrayList<>();

	public RequirementsConfigClass(@NotNull String className) {
		super(className);
	}

	@NotNull
	public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
		return new ReadOnlyList<>(Collections.emptyList());
	}

	@NotNull
	public ReadOnlyList<ConfigPropertyLookupConstant> getOptionalProperties() {
		return new ReadOnlyList<>(Collections.emptyList());
	}

	@NotNull
	public List<ConfigPropertyKey> getUserSpecifiedProperties() {
		return userProperties;
	}
}
