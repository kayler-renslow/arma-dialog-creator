package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Holds a simple macro. The macro is referenced by a key and the result is text that is appended into the ending .h file.

 @author Kayler
 @since 07/05/2016. */
public interface Macro<T extends SerializableValue> {

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
	ValueObserver<String> getKeyObserver();

	/** Get the value */
	@NotNull
	default T getValue() {
		return getValueObserver().getValue();
	}

	/** Set the value */
	default void setValue(@NotNull T value) {
		getValueObserver().updateValue(value);
	}

	/**
	 @return the {@link ValueObserver}.
	 */
	@NotNull
	ValueObserver<T> getValueObserver();

	/** Get the comment that describes the macro. Is user defined. */
	@Nullable
	String getComment();

	/** Set the comment */
	void setComment(@Nullable String comment);

	/** Get the {@link PropertyType} of the macro */
	@NotNull
	default PropertyType getPropertyType() {
		return getValue().getPropertyType();
	}

	/** Get the macro type */
	@NotNull
	MacroType getMacroType();

	/** Set the macro type */
	void setMacroType(@NotNull MacroType myType);

	static <T extends SerializableValue> Macro<T> newMacro(@NotNull String key, @NotNull T value) {
		return new BasicMacro<>(key, value);
	}

	class BasicMacro<T extends SerializableValue> implements Macro<T> {

		private ValueObserver<String> keyObserver = new ValueObserver<>(null);
		protected ValueObserver<T> valueObserver;
		protected String comment;
		protected MacroType myType = MacroType.USER_DEFINED;

		/**
		 A macro is referenced by a key and the result is text that is appended into the ending .h file.

		 @param key the key (prefered to be all caps)
		 @param value the value (Object.toString() will be used to get end result)
		 */
		public BasicMacro(@NotNull String key, @NotNull T value) {
			getKeyObserver().updateValue(key); //do not change to setKey
			this.valueObserver = new ValueObserver<>(value);
		}

		@Override
		@NotNull
		public ValueObserver<T> getValueObserver() {
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
		public ValueObserver<String> getKeyObserver() {
			return keyObserver;
		}

		@Override
		@NotNull
		public MacroType getMacroType() {
			return myType;
		}

		@Override
		public void setMacroType(@NotNull MacroType myType) {
			this.myType = myType;
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
