package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.NotNullValueListener;
import com.armadialogcreator.util.NotNullValueObserver;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 01/03/2019 */
public class ConfigProperty extends ConfigPropertyBase {
	private Macro boundMacro;
	private final NotNullValueObserver<SerializableValue> valueObserver;
	private final NotNullValueListener<SerializableValue> macroValueListener = new NotNullValueListener<>() {
		@Override
		public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> observer, @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
			valueObserver.updateValue(newValue);
		}
	};
	private final UpdateListenerGroup<ConfigPropertyUpdate> updateGroup = new UpdateListenerGroup<>();

	public ConfigProperty(@NotNull String name, @NotNull SerializableValue initialValue) {
		super(name);
		this.valueObserver = new NotNullValueObserver<>(initialValue);
	}

	@Override
	@NotNull
	public NotNullValueObserver<SerializableValue> getValueObserver() {
		return valueObserver;
	}

	@Override
	public void bindToMacro(@NotNull Macro m) {
		if (boundMacro != null) {
			m.getValueObserver().removeListener(macroValueListener);
		}
		boundMacro = m;
		m.getValueObserver().addListener(macroValueListener);
	}

	@Override
	public boolean clearMacro() {
		if (boundMacro == null) {
			return false;
		}
		boundMacro.getValueObserver().removeListener(macroValueListener);
		boundMacro = null;
		return true;
	}

	@Override
	public boolean isBoundToMacro() {
		return boundMacro != null;
	}

	@Override
	@NotNull
	public SerializableValue getValue() {
		return valueObserver.getValue();
	}

	@Override
	@Nullable
	public Macro getBoundMacro() {
		return boundMacro;
	}

	@Override
	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public ConfigProperty deepCopy() {
		ConfigProperty property = new ConfigProperty(name, getValue().deepCopy());
		Macro boundMacro = getBoundMacro();
		if (boundMacro != null) {
			property.bindToMacro(boundMacro);
		}
		return property;
	}

	@Override
	public void setValue(@NotNull SerializableValue value) {
		valueObserver.updateValue(value);
	}

	@Override
	public void addValueListener(@NotNull NotNullValueListener<SerializableValue> l) {
		valueObserver.addListener(l);
	}

	@Override
	public void removeValueListener(@NotNull NotNullValueListener<SerializableValue> l) {
		valueObserver.removeListener(l);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	@NotNull
	public UpdateListenerGroup<ConfigPropertyUpdate> getPropertyUpdateGroup() {
		return updateGroup;
	}


}
