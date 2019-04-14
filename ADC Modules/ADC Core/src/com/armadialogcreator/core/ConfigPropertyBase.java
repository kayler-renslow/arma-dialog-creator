package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.util.NotNullValueListener;
import com.armadialogcreator.util.NotNullValueObserver;
import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 03/05/2019 */
public abstract class ConfigPropertyBase implements ConfigPropertyKey {

	protected final String name;
	protected int priority = Integer.MAX_VALUE;

	public ConfigPropertyBase(@NotNull String propertyName) {
		this.name = propertyName;
	}

	@NotNull
	public abstract NotNullValueObserver<SerializableValue> getValueObserver();

	@NotNull
	public abstract SerializableValue getValue();

	public abstract void bindToMacro(@NotNull Macro m);

	public abstract boolean clearMacro();

	public abstract boolean isBoundToMacro();

	@Nullable
	public abstract Macro getBoundMacro();

	@NotNull
	public String getName() {
		return name;
	}

	@Override
	@NotNull
	public String getPropertyName() {
		return name;
	}

	public abstract void setValue(@NotNull SerializableValue value);

	public void setValue(int i) {
		setValue(new SVInteger(i));
	}

	public void setValue(boolean b) {
		setValue(SVBoolean.get(b));
	}

	public void setValue(double d) {
		setValue(new SVDouble(d));
	}

	public void setValue(@NotNull String s) {
		setValue(new SVString(s));
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

	public abstract void addValueListener(@NotNull NotNullValueListener<SerializableValue> l);

	public abstract void removeValueListener(@NotNull NotNullValueListener<SerializableValue> l);

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
		return getValue().getPropertyType();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ConfigPropertyBase)) {
			return false;
		}
		ConfigPropertyBase other = (ConfigPropertyBase) obj;
		return this.nameEquals(other);
	}

	@NotNull
	public abstract UpdateListenerGroup<ConfigPropertyUpdate> getPropertyUpdateGroup();

	void invalidate() {
		clearMacro();
		getValueObserver().invalidate();
		getPropertyUpdateGroup().clearListeners();
	}

	@Override
	public int priority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
