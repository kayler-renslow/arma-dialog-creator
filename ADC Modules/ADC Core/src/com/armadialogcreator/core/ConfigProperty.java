package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
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
	private final ConfigPropertyValue cpv;
	private final ReroutableValueObserver cpvObserver;
	private ConfigProperty inherited;

	public ConfigProperty(@NotNull String name, @NotNull SerializableValue initialValue) {
		this.name = name;
		this.cpv = new ConfigPropertyValue(initialValue);
		cpvObserver = new ReroutableValueObserver(this.cpv);
	}

	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public NotNullValueObserver<SerializableValue> getValueObserver() {
		return cpvObserver;
	}

	@NotNull
	public SerializableValue getValue() {
		return cpvObserver.getValue();
	}

	public boolean isBoundToMacro() {
		if (inherited != null) {
			return inherited.isBoundToMacro();
		}
		return cpv.isBoundToMacro();
	}

	public void bindToMacro(@NotNull Macro m) {
		if (inherited != null) {
			inherited.bindToMacro(m);
			return;
		}
		cpv.bindToMacro(m);
	}

	public void clearMacro() {
		if (inherited != null) {
			inherited.clearMacro();
			return;
		}
		cpv.clearMacro();
	}

	public void inherit(@Nullable ConfigProperty property) {
		if (property == this.inherited) {
			//either both are null or equal so no point in continuing
			return;
		}
		this.inherited = property;
		if (property == null) {
			cpvObserver.setRerouted(null);
		} else {
			cpvObserver.setRerouted(property.cpv.getValueObserver());
		}
	}

	public boolean isInherited() {
		return this.inherited != null;
	}

	@Nullable
	public Macro getBoundMacro() {
		return this.inherited != null ? inherited.getBoundMacro() : cpv.getBoundMacro();
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

		public ReroutableValueObserver(@NotNull ConfigPropertyValue cpv) {
			super(cpv.getValueObserver().getValue());

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
			if (this.rerouted == null) {
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
		}
	}

}
