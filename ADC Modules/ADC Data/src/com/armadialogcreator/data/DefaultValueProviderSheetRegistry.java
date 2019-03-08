package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.util.ApplicationSingleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 @author K
 @since 3/7/19 */
@ApplicationSingleton
public class DefaultValueProviderSheetRegistry implements Registry<String, DefaultValueSheet> {
	public static final DefaultValueProviderSheetRegistry instance = new DefaultValueProviderSheetRegistry();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	@Nullable
	@Override
	public DefaultValueSheet get(@NotNull String key) {
		return null;
	}

	@Nullable
	@Override
	public DefaultValueSheet get(@NotNull String key, @NotNull DataLevel dataLevel) {
		return null;
	}

	@Override
	@NotNull
	public Map<DataLevel, List<DefaultValueSheet>> copyAllToMap() {
		return null;
	}
}
