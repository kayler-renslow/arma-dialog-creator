package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Holds a simple macro. The macro is referenced by a key and the result is text that is appended into the ending .h file.

 @author Kayler
 @since 07/05/2016. */
public class Macro<T extends SerializableValue> {

	public enum MacroType {
		USER_DEFINED(Lang.ApplicationBundle().getString("Macros.Type.user_defined")),
		SYSTEM(Lang.ApplicationBundle().getString("Macros.Type.system")),
		GLOBAL(Lang.ApplicationBundle().getString("Macros.Type.global"));

		private final String displayText;

		MacroType(String displayText) {
			this.displayText = displayText;
		}

		@NotNull
		public String getDisplayText() {
			return displayText;
		}
	}

	private String key;
	private final PropertyType propertyType;
	protected ValueObserver<T> valueObserver;
	protected String comment;
	protected MacroType myType = MacroType.USER_DEFINED;

	/**
	 A macro is referenced by a key and the result is text that is appended into the ending .h file.

	 @param key the key (prefered to be all caps)
	 @param value the value (Object.toString() will be used to get end result)
	 */
	public Macro(@NotNull String key, @NotNull T value, @NotNull PropertyType propertyType) {
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

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setKey(@NotNull String key) {
		this.key = key;
	}

	@NotNull
	public MacroType getMacroType() {
		return myType;
	}

	public void setMacroType(@NotNull MacroType myType) {
		this.myType = myType;
	}

	/**
	 Checks if key and value are equal

	 @param o to check
	 @return true if equal, false otherwise
	 */
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
