package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used with {@link ControlClassOld#getPropertyUpdateGroup()}

 @author Kayler
 @since 08/11/2016. */
public class ControlPropertyValueUpdate implements ControlPropertyUpdate {
	public enum ValueOrigin {
		/** Caused by {@link Macro} value update via {@link ControlProperty#setValueToMacro(Macro)} */
		MACRO,
		/** Caused by {@link ControlProperty#inherit(ControlProperty)} */
		INHERIT,
		/** By user or program */
		OTHER
	}

	private final ControlProperty controlProperty;
	private final SerializableValue oldValue;
	private final SerializableValue newValue;
	private final ValueOrigin origin;

	public ControlPropertyValueUpdate(@NotNull ControlProperty controlProperty, SerializableValue oldValue, SerializableValue newValue, @NotNull ValueOrigin origin) {
		this.controlProperty = controlProperty;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.origin = origin;
	}

	/** Get the ControlProperty that recieved an update */
	@NotNull
	public ControlProperty getControlProperty() {
		return controlProperty;
	}

	/** Get the ControlProperty's old value */
	@Nullable
	public SerializableValue getOldValue() {
		return oldValue;
	}

	/** Get the ControlProperty's new value */
	@Nullable
	public SerializableValue getNewValue() {
		return newValue;
	}

	/** Get the origin of the value update. */
	@NotNull
	public ValueOrigin getOrigin() {
		return origin;
	}
}
