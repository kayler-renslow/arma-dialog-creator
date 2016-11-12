/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 Base class for ArmaControl that may or may not be a control (could be missing properties like style or type, which are required for all controls)<br>
 This class is useful for creating base control classes and not having to type a bunch of redundant information.
 Created on 05/23/2016. */
public class ControlClass {
	public static final ControlClass[] EMPTY = new ControlClass[0];

	private final ControlClassSpecificationProvider specProvider;
	private ControlClass extend;


	private final List<ControlProperty> requiredProperties = new LinkedList<>();
	private final List<ControlProperty> optionalProperties = new LinkedList<>();
	private final List<ControlProperty> overrideProperties = new LinkedList<>();

	private final List<ControlClass> requiredSubClasses = new LinkedList<>();
	private final List<ControlClass> optionalSubClasses = new LinkedList<>();


	private final ReadOnlyList<ControlProperty> requiredPropertiesReadOnly = new ReadOnlyList<>(requiredProperties);
	private final ReadOnlyList<ControlProperty> optionalPropertiesReadOnly = new ReadOnlyList<>(optionalProperties);
	private final ReadOnlyList<ControlProperty> overridePropertiesReadOnly = new ReadOnlyList<>(overrideProperties);

	private final ReadOnlyList<ControlClass> requiredSubClassesReadOnly = new ReadOnlyList<>(requiredSubClasses);
	private final ReadOnlyList<ControlClass> optionalSubClassesReadOnly = new ReadOnlyList<>(optionalSubClasses);


	protected String className;

	private final UpdateListenerGroup<ControlPropertyUpdate> updateGroup = new UpdateListenerGroup<>();

	public ControlClass(@NotNull String name, @NotNull ControlClassSpecificationProvider provider) {
		this.className = name;
		this.specProvider = provider;

		addProperties(requiredProperties, specProvider.getRequiredProperties());
		addProperties(optionalProperties, specProvider.getOptionalProperties());
		addRequiredSubClasses(specProvider.getRequiredSubClasses());
		addOptionalSubClasses(specProvider.getOptionalSubClasses());

		for (ControlProperty controlProperty : requiredProperties) {
			controlProperty.getValueObserver().addValueListener(getControlPropertyListener(controlProperty));
		}
		for (ControlProperty controlProperty : optionalProperties) {
			controlProperty.getValueObserver().addValueListener(getControlPropertyListener(controlProperty));
		}
	}

	@NotNull
	private ValueListener<SerializableValue> getControlPropertyListener(final ControlProperty controlProperty) {
		return new ValueListener<SerializableValue>() {
			private final ControlPropertyUpdate update = new ControlPropertyUpdate(controlProperty, null, null);

			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				update.setOldValue(oldValue);
				update.setNewValue(newValue);
				updateGroup.update(update);
			}
		};
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public final void extendControlClass(@Nullable ControlClass armaControl) {
		if (armaControl == this) {
			throw new IllegalArgumentException("Extend class can't extend itself!");
		}
		this.extend = armaControl;
	}

	public final @Nullable ControlClass getExtendClass() {
		return extend;
	}

	/** Get the instance of this provider. It is best to not return a new instance each time and store the instance for later use. */
	public final ControlClassSpecificationProvider getSpecProvider() {
		return specProvider;
	}

	private void addRequiredSubClasses(@NotNull ControlClass... subClasses) {
		for (ControlClass subClass : subClasses) {
			if (subClass == this) {
				throw new IllegalArgumentException("Can't require a class as a subclass of itself");
			}
			requiredSubClasses.add(subClass);
		}
	}

	private void addOptionalSubClasses(@NotNull ControlClass... subClasses) {
		for (ControlClass subClass : subClasses) {
			if (subClass == this) {
				throw new IllegalArgumentException("Can't make a class as a subclass of itself");
			}
			optionalSubClasses.add(subClass);
		}
	}

	@NotNull
	public ReadOnlyList<ControlClass> getRequiredSubClasses() {
		return requiredSubClassesReadOnly;
	}

	@NotNull
	public ReadOnlyList<ControlClass> getOptionalSubClasses() {
		return optionalSubClassesReadOnly;
	}

	@NotNull
	public final List<ControlClass> getAllSubClasses() {
		List<ControlClass> all = new ArrayList<>();
		all.addAll(requiredSubClasses);
		all.addAll(optionalSubClasses);
		return all;
	}

	@NotNull
	public final List<ControlProperty> getMissingRequiredProperties() {
		List<ControlProperty> defined = getDefinedProperties();

		boolean found;
		for (ControlProperty req : requiredProperties) {
			found = false;
			for (ControlProperty d : defined) {
				if (req.equals(d)) {
					found = true;
					break;
				}
			}
			if (found) {
				defined.remove(req);
			}
		}
		return defined;
	}

	private void addProperties(List<ControlProperty> propertiesList, ControlPropertyLookup[] props) {
		for (ControlPropertyLookup lookup : props) {
			propertiesList.add(lookup.getPropertyWithNoData());
		}
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside the {@link ControlClass#getRequiredProperties()} return value.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup wasn't in required properties
	 */
	@NotNull
	public ControlProperty findRequiredProperty(@NotNull ControlPropertyLookup lookup) {
		for (ControlProperty controlProperty : getRequiredProperties()) {
			if (controlProperty.getPropertyLookup() == lookup) {
				return controlProperty;
			}
		}
		throw new IllegalArgumentException("Lookup element '" + lookup.name() + "' wasn't in required properties.");
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside the {@link ControlClass#getOptionalProperties()} return value.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup wasn't in optional properties
	 */
	@NotNull
	public ControlProperty findOptionalProperty(@NotNull ControlPropertyLookup lookup) {
		for (ControlProperty controlProperty : getOptionalProperties()) {
			if (controlProperty.getPropertyLookup() == lookup) {
				return controlProperty;
			}
		}
		throw new IllegalArgumentException("Lookup element '" + lookup.name() + "' wasn't in optional properties.");
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside {@link #getOptionalProperties()} and {@link #getRequiredProperties()}.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup doesn't exist in the {@link ControlClass}
	 */
	@NotNull
	public ControlProperty findProperty(@NotNull ControlPropertyLookup lookup) {
		try {
			return findRequiredProperty(lookup);
		} catch (IllegalArgumentException ignored) {
		}
		try {
			return findOptionalProperty(lookup);
		} catch (IllegalArgumentException ignored) {
		}
		throw new IllegalArgumentException("Lookup element '" + lookup.name() + "' wasn't in the control class");
	}

	/**
	 Override's a property that exists inside {@link #getExtendClass()}. When the property is found, a deep copy will be created and inserted into the list {@link #getOverriddenProperties()}

	 @throws IllegalArgumentException when the property doesn't exist in the extended class
	 @throws IllegalStateException    when {@link #getExtendClass()} is null
	 */
	public void overrideProperty(@NotNull ControlPropertyLookup property) throws IllegalArgumentException, IllegalStateException {
		if (getExtendClass() == null) {
			throw new IllegalStateException("no class has been extended");
		}
		ControlProperty toOverride = getExtendClass().findProperty(property);
		SerializableValue value = toOverride.getValue();
		if (value != null) {
			value = value.deepCopy();
		}
		ControlProperty newProp = new ControlProperty(toOverride.getPropertyLookup(), value);
		overrideProperties.add(newProp);
	}

	/** Will remove the given property from {@link #getOverriddenProperties()}. If the lookup isn't found, nothing will happen */
	public void removeOverrideProperty(@NotNull ControlPropertyLookup property) {
		int i = 0;
		while (i < overrideProperties.size()) {
			if (overrideProperties.get(i).getPropertyLookup() == property) {
				overrideProperties.remove(i);
				break;
			}
		}
	}

	@NotNull
	public final ReadOnlyList<ControlProperty> getRequiredProperties() {
		return requiredPropertiesReadOnly;
	}

	@NotNull
	public final ReadOnlyList<ControlProperty> getOptionalProperties() {
		return optionalPropertiesReadOnly;
	}

	@NotNull
	public final ReadOnlyList<ControlProperty> getOverriddenProperties() {
		return overridePropertiesReadOnly;
	}


	/** Will return all properties that are defined (excluding inherited properties that are defined) */
	@NotNull
	public List<ControlProperty> getDefinedProperties() {
		List<ControlProperty> properties = new ArrayList<>(getRequiredProperties().size() + getOptionalProperties().size());
		for (ControlProperty property : getRequiredProperties()) {
			if (property.getValue() != null) {
				properties.add(property);
			}
		}
		for (ControlProperty property : getOptionalProperties()) {
			if (property.getValue() != null) {
				properties.add(property);
			}
		}
		return properties;
	}

	/**
	 Will return all properties that are defined (including inherited properties that are defined and overridden). This will return a concatenation of {@link #getDefinedProperties()} and
	 {@link #getOverriddenDefinedProperties()}
	 */
	@NotNull
	public List<ControlProperty> getAllDefinedProperties() {
		List<ControlProperty> defined = getDefinedProperties();
		List<ControlProperty> override = getOverriddenDefinedProperties();
		List<ControlProperty> properties = new ArrayList<>(defined.size() + override.size());
		properties.addAll(defined);
		properties.addAll(override);

		return properties;
	}

	/** Will return all properties from {@link #getInheritedProperties()} that have defined properties ({@link ControlProperty#getValue()} != null) */
	@NotNull
	public List<ControlProperty> getDefinedInheritedProperties() {
		List<ControlProperty> inheritedProperties = getInheritedProperties();
		ArrayList<ControlProperty> definedProperties = new ArrayList<>(inheritedProperties.size());
		for (ControlProperty c : inheritedProperties) {
			if (c.getValue() == null) {
				continue;
			}
			definedProperties.add(c);
		}

		return definedProperties;
	}


	/**
	 Returns a list of all inherited properties (retrieved from list {@link #getOverriddenProperties()}) that have an override value (the extended's control property is unedited and a new one is
	 defined in this {@link ControlClass})
	 */
	@NotNull
	public List<ControlProperty> getOverriddenDefinedProperties() {
		List<ControlProperty> defined = new ArrayList<>(overrideProperties.size());
		for (ControlProperty property : overrideProperties) {
			if (property.getValue() != null) {
				defined.add(property);
			}
		}
		return defined;
	}

	@NotNull
	public final List<ControlProperty> getInheritedProperties() {
		if (extend == null) {
			return new ArrayList<>();
		}
		ArrayList<ControlProperty> list = new ArrayList<>();
		appendInheritedProperties(extend, list);
		return list;
	}

	private void appendInheritedProperties(@NotNull ControlClass extend, @NotNull ArrayList<ControlProperty> list) {
		for (ControlProperty c : extend.getDefinedProperties()) {
			if (!list.contains(c)) {
				list.add(c);
			}
		}
		if (extend.extend != null) {
			for (ControlProperty c : extend.extend.getInheritedProperties()) {
				if (!list.contains(c)) {
					list.add(c);
				}
			}
			appendInheritedProperties(extend.extend, list);
		}
	}

	public final ReadOnlyList<ControlProperty> getEventProperties() {
		final List<ControlProperty> eventProperties = new ArrayList<>();
		for (ControlProperty controlProperty : requiredProperties) {
			if (ControlPropertyEventLookup.getEventProperty(controlProperty.getPropertyLookup()) != null) {
				eventProperties.add(controlProperty);
			}
		}
		for (ControlProperty controlProperty : optionalProperties) {
			if (ControlPropertyEventLookup.getEventProperty(controlProperty.getPropertyLookup()) != null) {
				eventProperties.add(controlProperty);
			}
		}
		return new ReadOnlyList<>(eventProperties);
	}

	/**
	 Gets the update listener group that listens to all {@link ControlProperty} instances. Instead of adding listeners to all {@link ControlProperty}'s potentially hundreds of times scattered
	 across the program, the ControlClass listens to it's own ControlProperties. Any time any of the ControlProperty's receive an update, the value inside the listener will be the
	 {@link ControlProperty} that was updated as well as the property's old value and the updated/new value. If this ControlClass extends some ControlClass via
	 {@link #extendControlClass(ControlClass)}, the update groups will <b>not</b> be synced. You will have to listen to each ControlClass separately.
	 */
	public UpdateListenerGroup<ControlPropertyUpdate> getUpdateGroup() {
		return updateGroup;
	}

	/**
	 Checks if the given {@link ControlClass} matches the following criteria:<br>
	 <ul>
	 <li>{@code this.getClassName().equals(controlClass.getClassName())}</li>
	 <li>this class's required properties matches {@code controlClass}'s required properties</li>
	 <li>this class's required sub-classes matches {@code controlClass}'s required sub-classes (recursive check with this method)</li>
	 </ul>

	 @param controlClass class to check if equals
	 @return true if the criteria are met, false otherwise
	 */
	public boolean classEquals(@NotNull ControlClass controlClass) {
		boolean name = this.getClassName().equals(controlClass.getClassName());
		if (!name) {
			return false;
		}

		for (ControlPropertyLookup lookup : specProvider.getRequiredProperties()) {
			boolean found = false;
			for (ControlPropertyLookup lookup2 : controlClass.specProvider.getRequiredProperties()) {
				if (lookup == lookup2) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}

		for (ControlClass requiredClass : getRequiredSubClasses()) {
			boolean found = false;
			for (ControlClass requiredClass2 : controlClass.getRequiredSubClasses()) {
				if (requiredClass.classEquals(requiredClass2)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return getClassName();
	}
}
