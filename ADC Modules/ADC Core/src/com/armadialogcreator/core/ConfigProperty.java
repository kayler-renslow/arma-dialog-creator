package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.util.NotNullValueListener;
import com.armadialogcreator.util.NotNullValueObserver;
import com.armadialogcreator.util.ReadOnlyArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 01/03/2019 */
public class ConfigProperty {
	private final String name;
	private Macro boundMacro;
	private final NotNullValueObserver<SerializableValue> valueObserver;
	private final NotNullValueListener<SerializableValue> macroValueListener = new NotNullValueListener<>() {
		@Override
		public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> observer, @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
			valueObserver.updateValue(newValue);
		}
	};

	public ConfigProperty(@NotNull String name, @NotNull SerializableValue initialValue) {
		this.name = name;
		this.valueObserver = new NotNullValueObserver<>(initialValue);
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

	public void setValue(@NotNull SerializableValue value) {
		valueObserver.updateValue(value);
	}

	public void setValue(int i) {
		valueObserver.updateValue(new SVInteger(i));
	}

	public void setValue(boolean b) {
		valueObserver.updateValue(SVBoolean.get(b));
	}

	public void setValue(double d) {
		valueObserver.updateValue(new SVDouble(d));
	}

	public void setValue(@NotNull String s) {
		valueObserver.updateValue(new SVString(s));
	}

	public double getFloatValue() {
		SerializableValue v = getValue();
		if (v instanceof SVNumericValue) {
			return ((SVNumericValue) v).toDouble();
		}
		throw new IllegalStateException();
	}

	public int getIntValue() {
		SerializableValue v = getValue();
		if (v instanceof SVNumericValue) {
			return ((SVNumericValue) v).toInt();
		}
		throw new IllegalStateException();
	}

	public boolean getBooleanValue() {
		SerializableValue v = getValue();
		if (v instanceof SVBoolean) {
			return v == SVBoolean.TRUE;
		}
		throw new IllegalStateException();
	}

	public void addValueListener(@NotNull NotNullValueListener<SerializableValue> l) {
		valueObserver.addListener(l);
	}

	public void removeValueListener(@NotNull NotNullValueListener<SerializableValue> l) {
		valueObserver.removeListener(l);
	}

	@NotNull
	public ReadOnlyArray<ConfigPropertyValueOption> getValueOptions() {
		return ReadOnlyArray.EMPTY;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@NotNull
	public PropertyType getPropertyType() {
		return valueObserver.getValue().getPropertyType();
	}

	public boolean nameEquals(@NotNull ConfigPropertyKey key) {
		return this.name.equals(key.getPropertyName());
	}

	public boolean nameEquals(@NotNull String name) {
		return this.name.equals(name);
	}

	void invalidate() {
		clearMacro();
		valueObserver.invalidate();
	}
}
