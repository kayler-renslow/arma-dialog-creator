package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.NotNullValueListener;
import com.armadialogcreator.util.NotNullValueObserver;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 3/5/19 */
public class ConfigPropertyProxy extends ConfigPropertyBase {

	private final NotNullValueObserver<SerializableValue> observer;
	private SerializableValue valueWhenPropertyAbsent;
	@Nullable
	private ConfigProperty configProperty;
	private final NotNullValueListener<SerializableValue> valueListener = new NotNullValueListener<>() {
		@Override
		public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> valueObserver, @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
			valueObserver.updateValue(newValue);
		}
	};
	private final UpdateListenerGroup<ConfigPropertyUpdate> updateGroup = new UpdateListenerGroup<>();


	public ConfigPropertyProxy(@NotNull String propertyName, @NotNull SerializableValue valueWhenPropertyAbsent) {
		super(propertyName);
		this.observer = new NotNullValueObserver<>(valueWhenPropertyAbsent);
		this.valueWhenPropertyAbsent = valueWhenPropertyAbsent;
	}

	void setConfigProperty(@Nullable ConfigProperty property) {
		if (property != null) {
			observer.updateValue(property.getValue());
			property.addValueListener(valueListener);
		} else if (this.configProperty != null) {
			configProperty.removeValueListener(valueListener);
			observer.updateValue(valueWhenPropertyAbsent);
		}
		this.configProperty = property;
	}

	public boolean propertyAbsent() {
		return configProperty == null;
	}

	@Override
	@NotNull
	public NotNullValueObserver<SerializableValue> getValueObserver() {
		return observer;
	}

	@Override
	@NotNull
	public SerializableValue getValue() {
		return observer.getValue();
	}

	@Override
	public void bindToMacro(@NotNull Macro m) {
		if (configProperty != null) {
			configProperty.bindToMacro(m);
		} else {
			m.getValueObserver().addListener(valueListener);
		}
	}

	@Override
	public boolean clearMacro() {
		if (configProperty != null) {
			return configProperty.clearMacro();
		}
		return true;
	}

	@Override
	public boolean isBoundToMacro() {
		return configProperty != null && configProperty.isBoundToMacro();
	}

	@Override
	@Nullable
	public Macro getBoundMacro() {
		return configProperty != null ? configProperty.getBoundMacro() : null;
	}

	@Override
	public void setValue(@NotNull SerializableValue value) {
		observer.updateValue(value);
		if (configProperty != null) {
			configProperty.setValue(value);
		}
	}

	@Override
	public void addValueListener(@NotNull NotNullValueListener<SerializableValue> l) {
		observer.addListener(l);
	}

	@Override
	public void removeValueListener(@NotNull NotNullValueListener<SerializableValue> l) {
		observer.removeListener(l);
	}

	@Override
	@NotNull
	public UpdateListenerGroup<ConfigPropertyUpdate> getPropertyUpdateGroup() {
		return updateGroup;
	}

	@Override
	void invalidate() {
		super.invalidate();
		if (configProperty != null) {
			configProperty.invalidate();
		}
	}
}
