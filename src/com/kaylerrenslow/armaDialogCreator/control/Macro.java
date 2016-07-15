package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Holds a simple macro. The macro is referenced by a key and the result is text that is appended into the ending .h file.
 Created on 07/05/2016. */
public class Macro<T extends SerializableValue> {

	private final String key;
	private final ControlProperty.PropertyType propertyType;
	protected ValueObserver<T> valueObserver;
	protected String comment;

	/**
	 A macro is referenced by a key and the result is text that is appended into the ending .h file.

	 @param key the key (prefered to be all caps)
	 @param value the value (Object.toString() will be used to get end result)
	 */
	public Macro(@NotNull String key, @NotNull T value, @NotNull ControlProperty.PropertyType propertyType) {
		this.key = key;
		this.valueObserver = new ValueObserver<>(value);
		this.propertyType = propertyType;
	}

	/** Get the key */
	@NotNull
	public String getKey() {
		return key;
	}

	/** Get the value */
	@NotNull
	public T getValue() {
		return valueObserver.getValue();
	}

	/** Set the value */
	public void setValue(@NotNull T value) {
		this.valueObserver.updateValue(value);
	}

	@NotNull
	public ValueObserver<T> getValueObserver() {
		return valueObserver;
	}

	@Nullable
	public String getComment() {
		return comment;
	}

	public void setComment(@Nullable String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return key;
	}

	public ControlProperty.PropertyType getPropertyType() {
		return propertyType;
	}
}
