package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.NotNullValueListener;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 01/07/2019 */
public class ConfigPropertyValue {
	private final NotNullValueObserver<SerializableValue> valueObserver;
	private final NotNullValueListener<SerializableValue> macroValueListener = new NotNullValueListener<>() {
		@Override
		public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> observer, @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
			valueObserver.updateValue(newValue);
		}
	};
	private Macro boundMacro;

	public ConfigPropertyValue(@NotNull SerializableValue initial) {
		valueObserver = new NotNullValueObserver<>(initial);
	}

	@NotNull
	public NotNullValueObserver<SerializableValue> getValueObserver() {
		return valueObserver;
	}

	public void bindToMacro(@NotNull Macro m) {
		boundMacro = m;
		m.getValueObserver().addListener(macroValueListener);
	}

	public boolean clearMacro() {
		if (boundMacro == null) {
			return false;
		}
		boundMacro.getValueObserver().removeListener(macroValueListener);
		boundMacro = null;
		return true;
	}

	public boolean isBoundToMacro() {
		return boundMacro != null;
	}

	@NotNull
	public SerializableValue getValue() {
		return valueObserver.getValue();
	}

	@Nullable
	public Macro getBoundMacro() {
		return boundMacro;
	}
}
