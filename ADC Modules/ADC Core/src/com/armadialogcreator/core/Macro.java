package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Holds a simple macro. The macro is referenced by a key and the result is text that is appended into the ending .h file.

 @author Kayler
 @since 07/05/2016. */
public interface Macro {

	/** Get the key */
	@NotNull
	default String getKey() {
		return getKeyObserver().getValue();
	}

	/** Set the key */
	default void setKey(@NotNull String key) {
		getKeyObserver().updateValue(key);
	}

	@NotNull
	NotNullValueObserver<String> getKeyObserver();

	/** Get the value */
	@NotNull
	default SerializableValue getValue() {
		return getValueObserver().getValue();
	}

	/** Set the value */
	default void setValue(@NotNull SerializableValue value) {
		getValueObserver().updateValue(value);
	}

	/**
	 @return the {@link NotNullValueObserver}.
	 */
	@NotNull
	NotNullValueObserver<SerializableValue> getValueObserver();

	/** @return the comment that describes the macro. Is user defined. */
	@Nullable
	String getComment();

	/** Set the comment */
	void setComment(@Nullable String comment);

	/** Get the {@link PropertyType} of the macro */
	@NotNull
	default PropertyType getPropertyType() {
		return getValue().getPropertyType();
	}

	@NotNull
	static Macro newMacro(@NotNull String key, @NotNull SerializableValue value) {
		return new BasicMacro<>(key, value);
	}

	class BasicMacro<T extends SerializableValue> implements Macro {

		private final NotNullValueObserver<String> keyObserver;
		private final NotNullValueObserver<SerializableValue> valueObserver;
		private String comment;

		/**
		 A macro is referenced by a key and the result is text that is appended into the ending .h file.

		 @param key the key (prefered to be all caps)
		 @param value the value (Object.toString() will be used to get end result)
		 */
		public BasicMacro(@NotNull String key, @NotNull T value) {
			keyObserver = new NotNullValueObserver<>(key);
			this.valueObserver = new NotNullValueObserver<>(value);
		}

		@Override
		@NotNull
		public NotNullValueObserver<SerializableValue> getValueObserver() {
			return valueObserver;
		}

		@Override
		@Nullable
		public String getComment() {
			return comment;
		}

		@Override
		public void setComment(@Nullable String comment) {
			this.comment = comment;
		}

		@Override
		public String toString() {
			return getKey();
		}

		@NotNull
		@Override
		public NotNullValueObserver<String> getKeyObserver() {
			return keyObserver;
		}

		/**
		 Checks if key and value are equal

		 @param o to check
		 @return true if equal, false otherwise
		 */
		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof Macro) {
				Macro other = (Macro) o;
				return this.getKey().equals(other.getKey()) && this.getValue().equals(other.getValue());
			}
			return false;
		}
	}


}
