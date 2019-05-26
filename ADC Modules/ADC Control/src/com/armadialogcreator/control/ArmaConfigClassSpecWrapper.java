package com.armadialogcreator.control;

import com.armadialogcreator.core.ConfigPropertyCategory;
import com.armadialogcreator.core.ConfigPropertyKey;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.RequirementsConfigClass;
import com.armadialogcreator.util.DoubleIterable;
import com.armadialogcreator.util.ReadOnlyIterable;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 5/26/19 */
public class ArmaConfigClassSpecWrapper extends RequirementsConfigClass {
	private final ArmaConfigClassSpec spec;

	public ArmaConfigClassSpecWrapper(@NotNull String className, @NotNull ArmaConfigClassSpec spec) {
		super(className);
		this.spec = spec;
	}

	@Override
	@NotNull
	public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
		return spec.getRequiredProperties();
	}

	@Override
	@NotNull
	public ReadOnlyList<ConfigPropertyLookupConstant> getOptionalProperties() {
		return spec.getOptionalProperties();
	}

	@Override
	@Nullable
	public ReadOnlyIterable<ConfigPropertyLookupConstant> iterateLookupProperties() {
		return new ReadOnlyIterable<>(new DoubleIterable<>(getRequiredProperties(), getOptionalProperties()));
	}

	@NotNull
	@Override
	public ConfigPropertyCategory getPropertyCategory(@NotNull ConfigPropertyKey property) {
		ReadOnlyList<ConfigPropertyLookupConstant> plist = spec.getOptionalProperties();
		for (ConfigPropertyLookupConstant c : plist) {
			if (property.nameEquals(c)) {
				if (c.isEvent()) {
					return ConfigPropertyCategory.Event;
				}
				return ConfigPropertyCategory.Optional;
			}
		}

		plist = spec.getRequiredProperties();
		for (ConfigPropertyLookupConstant c : plist) {
			if (property.nameEquals(c)) {
				if (c.isEvent()) {
					return ConfigPropertyCategory.Event;
				}
				return ConfigPropertyCategory.Required;
			}
		}

		return super.getPropertyCategory(property);
	}
}
