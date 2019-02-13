package com.armadialogcreator.core;

import com.armadialogcreator.core.old.ControlClassOld;
import com.armadialogcreator.core.old.SpecificationRegistry;
import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author Kayler
 @since 08/05/2017 */
public class EmptySpecRegistry implements SpecificationRegistry {


	@Override
	@Nullable
	public Macro findMacroByKey(@NotNull String macroKey) {
		return null;
	}

	@Override
	@Nullable
	public ControlClassOld findControlClassByName(@NotNull String className) {
		return null;
	}

	@Override
	@Nullable
	public SerializableValue getDefaultValue(@NotNull ConfigPropertyLookupConstant lookup) {
		return null;
	}

	@Override
	public void prefetchValues(@NotNull List<ConfigPropertyLookupConstant> tofetch, @Nullable DefaultValueProvider.Context context) {

	}

	@Override
	public void cleanup() {

	}

}
