package com.armadialogcreator.core;

import com.armadialogcreator.core.old.ControlClassOld;
import com.armadialogcreator.core.old.ControlPropertyLookupConstant;
import com.armadialogcreator.core.old.DefaultValueProvider;
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
	public SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup) {
		return null;
	}

	@Override
	public void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch, @Nullable DefaultValueProvider.Context context) {

	}

	@Override
	public void cleanup() {

	}

}
