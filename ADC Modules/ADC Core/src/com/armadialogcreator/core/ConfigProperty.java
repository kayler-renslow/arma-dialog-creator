package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.util.NotNullValueListener;
import com.armadialogcreator.util.NotNullValueObserver;
import javafx.beans.InvalidationListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 @author K
 @since 01/03/2019 */
public class ConfigProperty {
	private final String name;
	private Macro boundMacro;
	private final ReroutableValueObserver valueObserver;
	private final NotNullValueListener<SerializableValue> macroValueListener = new NotNullValueListener<>() {
		@Override
		public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> observer, @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
			valueObserver.updateValue(newValue);
		}
	};
	private boolean persistent;

	public ConfigProperty(@NotNull String name, @NotNull SerializableValue initialValue) {
		this.name = name;
		this.valueObserver = new ReroutableValueObserver(initialValue);
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

	public void addValueListener(@NotNull NotNullValueListener<SerializableValue> l) {
		valueObserver.addListener(l);
	}

	public void removeValueListener(@NotNull NotNullValueListener<SerializableValue> l) {
		valueObserver.removeListener(l);
	}

	void mime(@NotNull ConfigProperty property) {
		valueObserver.setRerouted(property.getValueObserver());
	}

	void clearMime() {
		valueObserver.setRerouted(null);
	}

	boolean isMiming() {
		return valueObserver.rerouted != null;
	}

	public void setPersistent(boolean p) {
		this.persistent = p;
		if (!p) {
			valueObserver.setRerouted(null);
		}
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistentValue(@NotNull SerializableValue value) {
		valueObserver.setPersistentValue(value);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	private static class ReroutableValueObserver extends NotNullValueObserver<SerializableValue> {

		private final LinkedList<NotNullValueListener<SerializableValue>> listeners = new LinkedList<>();
		private final LinkedList<InvalidationListener> invalidationListeners = new LinkedList<>();

		private NotNullValueObserver<SerializableValue> rerouted;

		private final NotNullValueListener<SerializableValue> reroutedValueListener = new NotNullValueListener<>() {
			@Override
			public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> observer,
									 @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
				for (NotNullValueListener<SerializableValue> l : listeners) {
					l.valueUpdated(ReroutableValueObserver.this, oldValue, newValue);
				}
			}
		};

		public ReroutableValueObserver(@NotNull SerializableValue value) {
			super(value);

			super.addListener(new NotNullValueListener<>() {
				@Override
				public void valueUpdated(@NotNull NotNullValueObserver<SerializableValue> observer,
										 @NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
					for (NotNullValueListener<SerializableValue> l : listeners) {
						l.valueUpdated(ReroutableValueObserver.this, oldValue, newValue);
					}
				}
			});
		}

		public void setRerouted(@Nullable NotNullValueObserver<SerializableValue> rerouted) {
			if (rerouted == this.rerouted) {
				//either both are null or the same instance
				return;
			}
			NotNullValueObserver<SerializableValue> old = this.rerouted;
			this.rerouted = rerouted;
			if (rerouted == null) {
				old.removeListener(reroutedValueListener);
				return;
			}
			this.rerouted.addListener(reroutedValueListener);
			if (!this.rerouted.getValue().equals(this.getValue())) {
				//fake a value update
				for (NotNullValueListener<SerializableValue> l : listeners) {
					l.valueUpdated(ReroutableValueObserver.this, this.getValue(), this.rerouted.getValue());
				}
			}
		}

		@NotNull
		@Override
		public SerializableValue getValue() {
			return rerouted != null ? rerouted.getValue() : super.getValue();
		}

		@Override
		public void updateValue(@NotNull SerializableValue newValue) {
			if (rerouted != null) {
				rerouted.updateValue(newValue);
			} else {
				super.updateValue(newValue);
			}
		}

		@Override
		public void addListener(@NotNull NotNullValueListener<SerializableValue> listener) {
			if (listeners.contains(listener)) {
				return;
			}
			listeners.add(listener);
		}

		@Override
		public void removeListener(@NotNull NotNullValueListener<SerializableValue> listener) {
			listeners.remove(listener);
		}

		@Override
		public void addListener(@NotNull InvalidationListener listener) {
			invalidationListeners.add(listener);
		}

		@Override
		public void removeListener(@NotNull InvalidationListener listener) {
			invalidationListeners.remove(listener);
		}

		@Override
		public void clearListeners() {
			for (NotNullValueListener l : listeners) {
				l.listenerDetached();
			}
			listeners.clear();
			invalidationListeners.clear();
		}

		@Override
		public void invalidate() {
			for (NotNullValueListener l : listeners) {
				l.observerInvalidated();
			}
			clearListeners();
			if (rerouted != null) {
				rerouted.removeListener(reroutedValueListener);
			}
			rerouted = null;
		}

		public void setPersistentValue(@NotNull SerializableValue value) {
			NotNullValueObserver<SerializableValue> old = rerouted;
			rerouted = null;
			updateValue(value);
			rerouted = old;
		}
	}
}
