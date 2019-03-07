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

	private final String propertyName;
	private final NotNullValueObserver<SerializableValue> observer;
	private SerializableValue valueWhenPropertyAbsent;
	@Nullable
	private ConfigProperty configProperty;
	@Nullable
	private Macro macro;

	private final NotNullValueListener<SerializableValue> valueListener = new NotNullValueListener<>() {
		@Override
		public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> valueObserver, @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
			valueObserver.updateValue(newValue);
		}
	};
	private final UpdateListenerGroup<ConfigPropertyUpdate> updateGroup = new UpdateListenerGroup<>();


	public ConfigPropertyProxy(@NotNull String propertyName, @NotNull SerializableValue valueWhenPropertyAbsent) {
		this.propertyName = propertyName;
		this.observer = new NotNullValueObserver<>(valueWhenPropertyAbsent);
		this.valueWhenPropertyAbsent = valueWhenPropertyAbsent;
	}

	void setConfigProperty(@Nullable ConfigProperty property) {
		if (property != null) {
			property.setValue(observer.getValue());
			if (property.getBoundMacro() != macro) {
				if (macro == null) {
					property.clearMacro();
				} else {
					property.bindToMacro(macro);

					//remove the redundant listener from macro as it gets added back at :addValueListener
					macro.getValueObserver().removeListener(valueListener);
				}

				//:addValueListener
				property.addValueListener(valueListener);
			} else {
				observer.updateValue(property.getValue());
			}
		} else if (this.configProperty != null) {
			configProperty.removeValueListener(valueListener);
			if (macro != null) {
				macro.getValueObserver().addListener(valueListener);
			} else {
				observer.updateValue(valueWhenPropertyAbsent);
			}
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
		this.macro = m;
		if (configProperty != null) {
			configProperty.bindToMacro(m);
		} else {
			m.getValueObserver().addListener(valueListener);
		}
	}

	@Override
	public boolean clearMacro() {
		if (macro == null) {
			return false;
		}
		macro.getValueObserver().removeListener(valueListener);
		if (configProperty != null) {
			return configProperty.clearMacro();
		}
		return true;
	}

	@Override
	public boolean isBoundToMacro() {
		return macro != null;
	}

	@Override
	@Nullable
	public Macro getBoundMacro() {
		return macro;
	}

	@Override
	@NotNull
	public String getName() {
		return propertyName;
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

	@Override
	public int hashCode() {
		return propertyName.hashCode();
	}
}