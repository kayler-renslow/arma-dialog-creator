package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 @author Kayler
 @see ControlClass
 @since 05/22/2016. */
public class ControlProperty {
	public static final ControlProperty[] EMPTY = new ControlProperty[0];

	/** Truncates the double to remove insignificant decimal places */
	public static double truncate(double x) {
		return MathUtil.truncate(x, 8);
	}

	private final ValueListener<?> macroListener = new ValueListener<Object>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
			if (myMacro == null) {
				throw new IllegalStateException("myMacro shouldn't be null");
			}
			setValue(myMacro.getValue());
		}
	};

	private final ControlPropertyLookupConstant propertyLookup;
	private final ControlPropertyValueObserver valueObserver;
	private SerializableValue defaultValue;

	/** The custom data instance for when {@link #setCustomDataValue(Object)} is invoked */
	private Object customData;
	/** True when {@link #setUsingCustomData(boolean)} with true passed in parameter, false otherwise. */
	private boolean usingCustomData = false;
	private ValueObserver<Object> customDataValueObserver;

	/** Value to switch to when the set macro becomes null. */
	private SerializableValue beforeMacroValue;
	private @Nullable Macro myMacro;

	private ControlProperty inherited;
	private ControlProperty beforeInherit;

	private final UpdateListenerGroup<ControlPropertyUpdate> controlPropertyUpdateGroup = new UpdateListenerGroup<>();
	private final UpdateGroupListener<ControlPropertyUpdate> inheritListener = new UpdateGroupListener<ControlPropertyUpdate>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ControlPropertyUpdate> group, ControlPropertyUpdate data) {
			ControlProperty.this.update(data, false);
		}
	};


	/**
	 A control property is something like "idc" or "colorBackground". The current implementation has all values a {@link SerializableValue}.
	 This constructor also sets the default value (retrievable via {@link #getDefaultValue()}) equal to null.

	 @param propertyLookup unique lookup for the property.
	 @param value current value of the property
	 */
	protected ControlProperty(@NotNull ControlPropertyLookupConstant propertyLookup, @Nullable SerializableValue value) {
		this.propertyLookup = propertyLookup;
		valueObserver = new ControlPropertyValueObserver(this, value);
		defaultValue = null;
		beforeMacroValue = value;
	}

	protected ControlProperty(@NotNull ControlPropertySpecification specification, @NotNull MacroRegistry registry) {
		this(specification.getPropertyLookup(), specification.getValue());
		setTo(specification, registry);
	}

	/**
	 This constructor is used for when the values of the property are not set but the number of values stored is determined.
	 For more information on this class, see constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}

	 @param propertyLookup propertyLookup
	 */
	protected ControlProperty(ControlPropertyLookupConstant propertyLookup) {
		this(propertyLookup, (SerializableValue) null);
	}

	/**
	 Creates a control property of type {@link SVInteger}<br>
	 See constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 */
	protected ControlProperty(ControlPropertyLookupConstant propertyLookup, int value) {
		this(propertyLookup, new SVInteger(value));
	}

	/**
	 Creates a control property of type {@link SVDouble}<br>
	 See constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 */
	protected ControlProperty(ControlPropertyLookupConstant propertyLookup, double value) {
		this(propertyLookup, new SVDouble(value));
	}

	/**
	 Creates a control property of type {@link SVBoolean}<br>
	 See constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 */
	protected ControlProperty(ControlPropertyLookupConstant propertyLookup, boolean value) {
		this(propertyLookup, SVBoolean.get(value));
	}

	/**
	 Creates a control property of type {@link SVString}<br>
	 See constructor {@link #ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 */
	protected ControlProperty(ControlPropertyLookupConstant propertyLookup, String value) {
		this(propertyLookup, new SVString(value));
	}

	/** Set ControlProperty's value. If a macro is set to the control property, the macro's value will be undisturbed, however, the control property's value will be set to this value. */
	public void setValue(@Nullable SerializableValue v) {
		beforeMacroValue = v;
		valueObserver.updateValue(v);
	}

	private void setValue(@Nullable SerializableValue v, ControlPropertyValueUpdate.ValueOrigin origin) {
		beforeMacroValue = v;
		valueObserver.updateValue(v, origin);
	}

	/**
	 Set the custom data value.

	 @see #isUsingCustomData()
	 */
	public void setCustomDataValue(@Nullable Object customData) {
		if (customData == this.customData || (customData != null && customData.equals(this.customData))) {
			//either both null (or same references for that matter) or customData is potentially null, but may still equal this.customData
			return;
		}
		Object oldData = this.customData;
		this.customData = customData;
		if (this.customDataValueObserver == null) {
			customDataValueObserver = new ValueObserver<>(customData);
		}
		controlPropertyUpdateGroup.update(new ControlPropertyCustomDataUpdate(this, oldData, customData, true, this.isUsingCustomData()));
	}

	/** Does nothing except mark that the {@link ControlProperty} is/ins't using custom data. @see #isCustomData() */
	public void setUsingCustomData(boolean custom) {
		if (custom == this.usingCustomData) {
			return;
		}
		this.usingCustomData = custom;
		controlPropertyUpdateGroup.update(new ControlPropertyCustomDataUpdate(this, customData, customData, false, this.isUsingCustomData()));
	}

	/**
	 Set the default value.

	 @param setValue if true, the ControlProperty value will also be set to the given defaultValue
	 @param defaultValue new default value
	 */
	public void setDefaultValue(boolean setValue, SerializableValue defaultValue) {
		this.defaultValue = defaultValue;
		if (setValue) {
			setValue(defaultValue);
		}
	}

	/**
	 Sets the default value to an integer (uses {@link SVInteger}) (same thing as calling setDefaultValue(new SVInteger(defaultValue)))

	 @see #setDefaultValue(boolean, SerializableValue)
	 */
	public void setDefaultValue(boolean setValue, int defaultValue) {
		setDefaultValue(setValue, new SVInteger(defaultValue));
	}

	/**
	 Sets the default value to an integer (uses {@link SVDouble}) (same thing as calling setDefaultValue(new SVDouble(defaultValue)))

	 @see #setDefaultValue(boolean, SerializableValue)
	 */
	public void setDefaultValue(boolean setValue, double defaultValue) {
		setDefaultValue(setValue, new SVDouble(defaultValue));
	}

	/**
	 Sets the default value to a String (uses {@link SVString}) (same thing as calling setDefaultValue(new SVString(defaultValue)))

	 @see #setDefaultValue(boolean, SerializableValue)
	 */
	public void setDefaultValue(boolean setValue, @Nullable String defaultValue) {
		setDefaultValue(setValue, new SVString(defaultValue));
	}

	/**
	 Set the control property's values equal to a macro. The properties prior to being set to the macro will be preserved.
	 If this method is invoked again with the macro=null, the preserved values will be inserted. In either scenario, the values observer will be notified of the change.

	 @param newMacro the macro to set to, or null if not to set to macro
	 */
	public void setValueToMacro(@Nullable Macro newMacro) {
		if (newMacro == this.myMacro || (newMacro != null && newMacro.equals(this.myMacro))) { //either both null (or same references for that matter) or m might still equal this.myMacro)
			return;
		}
		Macro oldMacro = this.myMacro;
		if (newMacro == null) {
			myMacro.getValueObserver().removeListener(macroListener);
			this.myMacro = null;
			setValue(beforeMacroValue, ControlPropertyValueUpdate.ValueOrigin.MACRO);
		} else {
			this.myMacro = newMacro;
			this.myMacro.getValueObserver().addListener(macroListener);
			beforeMacroValue = valueObserver.getValue();
			setValue(this.myMacro.getValue(), ControlPropertyValueUpdate.ValueOrigin.MACRO);
		}
		controlPropertyUpdateGroup.update(new ControlPropertyMacroUpdate(this, oldMacro, newMacro));
	}

	/** Get the custom data set from {@link #setCustomDataValue(Object)} */
	@Nullable
	public Object getCustomData() {
		return customData;
	}

	/** Get the {@link ValueObserver} instance for the {@link #getCustomData()} value. Will be null when the custom data is never set. */
	@Nullable
	public ReadOnlyValueObserver<Object> getCustomDataValueObserver() {
		return customDataValueObserver.getReadOnlyValueObserver();
	}

	/** Return {@link ControlPropertyLookupConstant#getPropertyName()} with instance {@link #getPropertyLookup()} */
	@NotNull
	public String getName() {
		return propertyLookup.getPropertyName();
	}

	/**
	 Return true if the given type is equal to this instance's property type, false otherwise.
	 (This is effectively doing the same thing as getPropertyType() == PropertyType.something)

	 @return true if equal, false if not equal or {@link #getPropertyType()} == null
	 */
	public boolean isPropertyType(@NotNull PropertyType type) {
		return getPropertyType() == type;
	}

	/**
	 Get the current {@link PropertyType}.

	 @return the current {@link PropertyType}, or null if <code>{@link #getValue()}==null</code>
	 */
	@Nullable
	public PropertyType getPropertyType() {
		return getValue() != null ? getValue().getPropertyType() : null;
	}

	/** @return {@link ControlPropertyLookupConstant#getPropertyType()} */
	@NotNull
	public PropertyType getInitialPropertyType() {
		return propertyLookup.getPropertyType();
	}

	@Nullable
	public SerializableValue getValue() {
		return valueObserver.getValue();
	}

	@NotNull
	public ControlPropertyLookupConstant getPropertyLookup() {
		return propertyLookup;
	}

	/**
	 Return true if the data may not match the type of the control property (i.e. placing a String in the property when {@link #getPropertyType()} is {@link PropertyType#INT}). This is set by
	 invoking {@link #setCustomDataValue(Object)}. This will not affect {@link #getValue()}.
	 */
	public boolean isUsingCustomData() {
		return usingCustomData;
	}

	/** Get the default value for the property */
	@Nullable
	public SerializableValue getDefaultValue() {
		return defaultValue;
	}

	/** Get the macro that the control property is using, or null if not using a macro */
	@Nullable
	public Macro getMacro() {
		return myMacro;
	}

	/**
	 Get the ControlProperty's value as an int. If the value is of type {@link SVInteger} or {@link Expression}, this method will succeed.

	 @throws IllegalStateException when ControlProperty's value isn't of type {@link SVInteger} or {@link Expression}
	 */
	public int getIntValue() {
		if (getValue() == null) {
			throw new NullPointerException("value is null");
		}
		if (getValue() instanceof SVInteger) {
			return ((SVInteger) getValue()).getInt();
		}
		if (getValue() instanceof Expression) {
			return (int) ((Expression) getValue()).getNumVal();
		}
		throw new IllegalStateException("Incompatible type fetching. My serializable value class name=" + getValue().getClass().getName());
	}

	/**
	 Get the ControlProperty's value as an double. If the value is of type {@link SVDouble} or {@link Expression}, this method will succeed.

	 @throws IllegalStateException when ControlProperty's value isn't of type {@link SVDouble} or {@link Expression}
	 */
	public double getFloatValue() {
		if (getValue() == null) {
			throw new NullPointerException("value is null");
		}
		if (getValue() instanceof SVDouble) {
			return ((SVDouble) getValue()).getDouble();
		}
		if (getValue() instanceof Expression) {
			return ((Expression) getValue()).getNumVal();
		}
		throw new IllegalStateException("Incompatible type fetching. My serializable value class name=" + getValue().getClass().getName());
	}

	/**
	 Get the ControlProperty's value as an boolean. If the value is of type {@link SVBoolean}, this method will succeed.

	 @throws IllegalStateException when ControlProperty's value isn't of type {@link SVBoolean}
	 */
	public boolean getBooleanValue() {
		if (getValue() == null) {
			throw new NullPointerException("value is null");
		}
		if (getValue() instanceof SVBoolean) {
			return ((SVBoolean) getValue()).isTrue();
		}
		throw new IllegalStateException("Incompatible type fetching. My serializable value class name=" + getValue().getClass().getName());
	}


	/** Get the observer that observers the values inside this property. Whenever the values get updated, the observer and it's listener will be told so. */
	@NotNull
	public ValueObserver<SerializableValue> getValueObserver() {
		return valueObserver;
	}

	/**
	 Get an {@link UpdateListenerGroup} instance that invokes {@link UpdateListenerGroup#update(Object)} whenever one of these operations have been done:<br>
	 <ul>
	 <li>Value update</li>
	 <li>Custom data update</li>
	 <li>Set to a {@link Macro}, or removed it</li>
	 </ul>
	 */
	@NotNull
	public UpdateListenerGroup<ControlPropertyUpdate> getControlPropertyUpdateGroup() {
		return controlPropertyUpdateGroup;
	}

	/** Set the first value to int. This will just wrap the int in {@link SVInteger} */
	public void setValue(int v) {
		setValue(new SVInteger(v));
	}

	/** Set the first value to int. This will just wrap the int in {@link SVDouble} */
	public void setValue(double v) {
		setValue(new SVDouble(v));
	}

	/** Set the first value to int. This will just wrap the int in {@link SVBoolean} */
	public void setValue(boolean v) {
		setValue(SVBoolean.get(v));
	}

	/** Set the first value to String. This will just wrap the String in {@link SVString} */
	public void setValue(@Nullable String v) {
		if (v == null) {
			setValue((SerializableValue) null);
		} else {
			setValue(new SVString(v));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ControlProperty)) {
			return false;
		}
		ControlProperty other = (ControlProperty) o;

		if (getPropertyLookup() != other.getPropertyLookup()) {
			return false;
		}

		boolean eq;
		if (getValue() == null) {
			eq = other.getValue() == null;
		} else {
			eq = getValue().equals(other.getValue());
		}

		if (!eq) {
			return false;
		}

		if (getMacro() == null) {
			eq = other.getMacro() == null;
		} else {
			eq = getMacro().equals(other.getMacro());
		}

		if (!eq) {
			return false;
		}

		if (getDefaultValue() == null) {
			eq = other.getDefaultValue() == null;
		} else {
			eq = getDefaultValue().equals(other.getDefaultValue());
		}

		if (!eq) {
			return false;
		}

		if (isUsingCustomData()) {
			eq = other.isUsingCustomData();
		} else {
			eq = !other.isUsingCustomData();
		}

		if (!eq) {
			return false;
		}

		if (getCustomData() == null) {
			return other.getCustomData() == null;
		} else {
			return getCustomData().equals(other.getCustomData());
		}
	}

	@Override
	public String toString() {
		return "ControlProperty{" +
				"propertyLookup=" + propertyLookup +
				", value=" + (getValue() != null ? Arrays.toString(getValue().getAsStringArray()) : "null") +
				'}';
	}

	/**
	 Sets this {@link ControlProperty} to the given update.

	 @param update update to use
	 @param deepCopyValue if <code>update</code> is an instance of {@link ControlPropertyValueUpdate} and if <code>deepCopyValue</code> is true, the value returned by
	 {@link ControlPropertyValueUpdate#getNewValue()} will be copied via {@link SerializableValue#deepCopy()}, otherwise if <code>deepCopyValue</code> == false, the value will not be deep copied.
	 If <code>update</code> is <b>not</b> an instance of {@link ControlPropertyValueUpdate}, <code>deepCopyValue</code> will have no effect.
	 */
	public void update(@NotNull ControlPropertyUpdate update, boolean deepCopyValue) {
		if (update instanceof ControlPropertyValueUpdate) {
			ControlPropertyValueUpdate update1 = (ControlPropertyValueUpdate) update;
			if (update1.getNewValue() == null) {
				setValue((SerializableValue) null);
			} else if (deepCopyValue) {
				setValue(update1.getNewValue().deepCopy());
			} else {
				setValue(update1.getNewValue());
			}
		} else if (update instanceof ControlPropertyMacroUpdate) {
			ControlPropertyMacroUpdate update1 = (ControlPropertyMacroUpdate) update;
			setValueToMacro(update1.getNewMacro());
		} else if (update instanceof ControlPropertyCustomDataUpdate) {
			ControlPropertyCustomDataUpdate update1 = (ControlPropertyCustomDataUpdate) update;
			setCustomDataValue(update1.getNewCustomData());
			setUsingCustomData(update1.isUsingCustomData());
		} else if (update instanceof ControlPropertyInheritUpdate) {
			ControlPropertyInheritUpdate update1 = (ControlPropertyInheritUpdate) update;
			inherit(update1.getInheritedProperty());
		} else {
			throw new IllegalArgumentException("WARNING: ControlProperty.update(): unknown control property update:" + update);
		}
	}

	/** Construct a new {@link ControlProperty} that will copy the lookup and deep copy the value. The macro and custom data will also be shallow copied over */
	@NotNull
	public ControlProperty deepCopy() {
		ControlProperty copy = new ControlProperty(getPropertyLookup(), getValue() != null ? getValue().deepCopy() : null);
		copy.setCustomDataValue(getCustomData());
		copy.setUsingCustomData(isUsingCustomData());
		copy.setDefaultValue(false, getDefaultValue());
		copy.setValueToMacro(getMacro());
		return copy;
	}

	/**
	 Will set this property equal to the given one only if {@link #getPropertyLookup()} matches with this and <code>property</code>.
	 This method is used in conjunction with {@link #inherit(ControlProperty)}.
	 Note: {@link ControlProperty#getValue()} will not be deep copied. If the desire is to deep copy the given property, use {@link ControlProperty#deepCopy()}.

	 @param property property to set to
	 */
	public void setTo(@NotNull ControlProperty property) {
		setTo(property, ControlPropertyValueUpdate.ValueOrigin.OTHER);
	}

	private void setTo(@NotNull ControlProperty property, @NotNull ControlPropertyValueUpdate.ValueOrigin origin) {
		if (property.getPropertyLookup() != getPropertyLookup()) {
			throw new IllegalArgumentException("not same property lookup");
		}
		setValue(property.getValue(), origin);
		setValueToMacro(property.getMacro()); //do after set value
		setUsingCustomData(property.isUsingCustomData());
		setCustomDataValue(property.getCustomData());
		setDefaultValue(false, property.getDefaultValue());
	}

	/**
	 Will set this property equal to the given specification only if {@link #getPropertyLookup()} matches with this and <code>property</code>. Note: {@link ControlPropertySpecification#getValue()} will not be
	 deep copied. The value returned by {@link ControlPropertySpecification#getValue()} will also become the default value ({@link #getDefaultValue()})

	 @param specification specification to set to
	 @param registry registry that contains the {@link Macro} instances
	 */
	public void setTo(@NotNull ControlPropertySpecification specification, @NotNull MacroRegistry registry) {
		if (specification.getPropertyLookup() != getPropertyLookup()) {
			throw new IllegalArgumentException("not same property lookup");
		}
		setDefaultValue(true, specification.getValue());
		if (specification.getMacroKey() != null) {
			setValueToMacro(registry.findMacroByKey(specification.getMacroKey())); //do after set value
		}
		setUsingCustomData(specification.isCustomData());
		setCustomDataValue(specification.getCustomData());
	}

	/**
	 Inherit values, macro, and custom data from the given {@link ControlProperty}. If the property is inherited and then this method is invoked again with <code>inherit</code>==null, the
	 previous values, macro, and custom data will be given back to this property

	 @param inherit property to inherit, or null to remove any inheritance
	 */
	public void inherit(@Nullable ControlProperty inherit) {
		if (inherited == inherit) {
			return;
		}
		if (inherit != null && inherit.inherited == this) {
			throw new IllegalArgumentException("circular inheritance");
		}
		ControlProperty oldInherited = inherited;
		inherited = inherit;
		if (inherit == null) {
			if (beforeInherit != null) {
				setTo(beforeInherit, ControlPropertyValueUpdate.ValueOrigin.INHERIT);
			}
			oldInherited.getControlPropertyUpdateGroup().removeListener(inheritListener);
		} else {
			beforeInherit = this.deepCopy();
			setTo(inherit, ControlPropertyValueUpdate.ValueOrigin.INHERIT);
			inherit.getControlPropertyUpdateGroup().addListener(inheritListener);
		}

		controlPropertyUpdateGroup.update(new ControlPropertyInheritUpdate(this, inherit));
	}

	/**
	 Get if this property is inheriting another property

	 @return true if inheriting, false otherwise
	 @see #inherit(ControlProperty)
	 */
	public boolean isInherited() {
		return inherited != null;
	}

	/** Get the {@link ControlProperty} this property is inheriting, or null if not inheriting anything */
	@Nullable
	public ControlProperty getInherited() {
		return inherited;
	}

	/**
	 Set's the default value (if <code>setDefaultValue==true</code>) and current value equal to <code>value</code>
	 if they are null.

	 @param setDefaultValue if true, will set the default value equal to <code>value</code> if default value is null
	 @param value value to set if the value is null
	 */
	public void setValueIfAbsent(boolean setDefaultValue, SerializableValue value) {
		if (getValue() == null) {
			setValue(value);
		}
		if (defaultValue == null && setDefaultValue) {
			setDefaultValue(false, value);
		}
	}

	/**
	 A custom {@link ValueObserver} instance for handling {@link ControlProperty#setValue(SerializableValue)} and
	 {@link ControlProperty#setValue(SerializableValue, ControlPropertyValueUpdate.ValueOrigin)}.
	 When the value is updated, {@link ControlProperty#getControlPropertyUpdateGroup()} will be properly notified.
	 */
	private static class ControlPropertyValueObserver extends ValueObserver<SerializableValue> {

		private final ControlProperty property;
		private boolean disableUpdate = false;

		public ControlPropertyValueObserver(@NotNull ControlProperty property, SerializableValue value) {
			super(value);
			this.property = property;
		}

		/**
		 Used for updating the value with origin={@link ControlPropertyValueUpdate.ValueOrigin#OTHER}

		 @param newValue new value to set to
		 */
		@Override
		public void updateValue(SerializableValue newValue) {
			SerializableValue old = this.getValue();
			super.updateValue(newValue);
			if (!disableUpdate) {
				property.controlPropertyUpdateGroup.update(new ControlPropertyValueUpdate(property, old, newValue, ControlPropertyValueUpdate.ValueOrigin.OTHER));
			}
		}

		/**
		 Used for updating the value with origin <b>not equal to</b> {@link ControlPropertyValueUpdate.ValueOrigin#OTHER}

		 @param newValue new value to set to
		 @param origin where the value was updated from
		 */
		public void updateValue(@Nullable SerializableValue newValue, @NotNull ControlPropertyValueUpdate.ValueOrigin origin) {
			SerializableValue old = this.getValue();
			disableUpdate = true;
			updateValue(newValue);
			disableUpdate = false;
			property.controlPropertyUpdateGroup.update(new ControlPropertyValueUpdate(property, old, newValue, origin));
		}
	}
}
