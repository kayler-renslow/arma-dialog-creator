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
import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 A {@link ControlClass} is merely a header file (.h) class that is:<br>
 <ol>
 <li>Extend-able via {@link ControlClass#extendControlClass(ControlClass)}</li>
 <li>Required and optional {@link ControlProperty}'s (obtainable via {@link #getRequiredProperties()} and {@link #getOptionalProperties()} respectively).</li>
 <li>Required and optional nested classes (classes within this class) which can be accessed with {@link #getRequiredNestedClasses()} and {@link #getOptionalNestedClasses()} respectively.</li>
 </ol>
 <p>
 A required {@link ControlProperty} is a property that is necessary to fill a {@link ControlClassRequirementSpecification}. In other words, those properties' values
 ({@link ControlProperty#getValue()}) should <b>not</b> be null. An optional property is one that can be null.
 </p>
 <p>
 A required nested class is merely a class that should also have it's required properties filled in. An optional nested class doesn't need it's required properties to be filled in.
 </p>

 @author Kayler
 @since 05/23/2016. */
public class ControlClass {
	public static final ControlClass[] EMPTY = new ControlClass[0];

	private final ControlClassRequirementSpecification specProvider;

	private final List<ControlProperty> requiredProperties = new LinkedList<>();
	private final List<ControlProperty> optionalProperties = new LinkedList<>();
	private final List<ControlProperty> overrideProperties = new LinkedList<>();

	private final List<ControlClass> requiredNestedClasses = new LinkedList<>();
	private final List<ControlClass> optionalNestedClasses = new LinkedList<>();


	private final ReadOnlyList<ControlProperty> requiredPropertiesReadOnly = new ReadOnlyList<>(requiredProperties);
	private final ReadOnlyList<ControlProperty> optionalPropertiesReadOnly = new ReadOnlyList<>(optionalProperties);
	private final ReadOnlyList<ControlProperty> overridePropertiesReadOnly = new ReadOnlyList<>(overrideProperties);

	private final ReadOnlyList<ControlClass> requiredNestedClassesReadOnly = new ReadOnlyList<>(requiredNestedClasses);
	private final ReadOnlyList<ControlClass> optionalNestedClassesReadOnly = new ReadOnlyList<>(optionalNestedClasses);

	private final DataContext userData = new DataContext();

	private final ValueObserver<String> classNameObserver = new ValueObserver<>(null);
	private final ValueObserver<ControlClass> extendClassObserver = new ValueObserver<>(null);

	private final UpdateListenerGroup<ControlPropertyUpdate> propertyUpdateGroup = new UpdateListenerGroup<>();
	private final UpdateListenerGroup<ControlClassUpdate> controlClassUpdateGroup = new UpdateListenerGroup<>();
	private final UpdateListener<ControlClassUpdate> controlClassUpdateExtendListener = new UpdateListener<ControlClassUpdate>() {
		@Override
		public void update(ControlClassUpdate data) {
			if (data instanceof ControlClassPropertyUpdate) {
				updateControlProperty((ControlClassPropertyUpdate) data);
			}
		}

		private void updateControlProperty(ControlClassPropertyUpdate data) {
			ControlPropertyUpdate propertyUpdate = data.getPropertyUpdate();
			try {
				ControlProperty myProperty = findProperty(propertyUpdate.getControlProperty().getPropertyLookup());
				if (overrideProperties.contains(myProperty)) {
					return; //should not replace the value since it is overridden
				}
				System.out.println("ControlClass.updateControlProperty UPDATE FROM EXTEND" + propertyUpdate.getControlProperty().getPropertyLookup());
				//todo we should save original value before extend so when extend is undone, that before-extend value is used/back in place
				myProperty.update(propertyUpdate, false);
			} catch (IllegalArgumentException ignore) {
				//property simple doesn't exist, so nothing to do
			}
		}
	};

	public ControlClass(@NotNull String name, @NotNull ControlClassRequirementSpecification specification, @NotNull SpecificationRegistry registry) {
		classNameObserver.updateValue(name);
		this.specProvider = specification;

		addProperties(requiredProperties, specProvider.getRequiredProperties());
		addProperties(optionalProperties, specProvider.getOptionalProperties());
		addNestedClasses(requiredNestedClasses, registry, specProvider.getRequiredNestedClasses());
		addNestedClasses(optionalNestedClasses, registry, specProvider.getOptionalNestedClasses());

		afterConstructor();
	}

	/** Construct a {@link ControlClass} with the given specification and {@link SpecificationRegistry} */
	public ControlClass(@NotNull ControlClassSpecification specification, @NotNull SpecificationRegistry registry) {
		classNameObserver.updateValue(specification.getClassName());
		this.specProvider = specification;
		if (specification.getExtendClassName() != null) {
			extendControlClass(ApplicationDataManager.getInstance().getCurrentProject().findControlClassByName(specification.getExtendClassName()));
		}
		for (ControlPropertySpecification property : specification.getRequiredControlProperties()) {
			requiredProperties.add(property.constructNewControlProperty(registry));
		}
		for (ControlPropertySpecification property : specification.getOptionalControlProperties()) {
			optionalProperties.add(property.constructNewControlProperty(registry));
		}
		for (ControlClassSpecification s : specification.getRequiredNestedClasses()) {
			requiredNestedClasses.add(s.constructNewControlClass(registry));
		}
		for (ControlClassSpecification s : specification.getOptionalNestedClasses()) {
			optionalNestedClasses.add(s.constructNewControlClass(registry));
		}
		afterConstructor();
	}

	private void afterConstructor() {
		for (ControlProperty property : getAllChildProperties()) {
			overrideProperties.add(property);
		}

		final UpdateListener<ControlPropertyUpdate> controlPropertyListener = new UpdateListener<ControlPropertyUpdate>() {
			@Override
			public void update(ControlPropertyUpdate data) {
				propertyUpdateGroup.update(data);
				controlClassUpdateGroup.update(new ControlClassPropertyUpdate(ControlClass.this, data));
			}
		};
		for (ControlProperty controlProperty : requiredProperties) {
			controlProperty.getControlPropertyUpdateGroup().addListener(controlPropertyListener);
		}
		for (ControlProperty controlProperty : optionalProperties) {
			controlProperty.getControlPropertyUpdateGroup().addListener(controlPropertyListener);
		}
		classNameObserver.addValueListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
				controlClassUpdateGroup.update(new ControlClassRenameUpdate(ControlClass.this, oldValue, newValue));
			}
		});
		extendClassObserver.addValueListener(new ValueListener<ControlClass>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<ControlClass> observer, ControlClass oldValue, ControlClass newValue) {
				controlClassUpdateGroup.update(new ControlClassExtendUpdate(ControlClass.this, oldValue, newValue));
			}
		});
	}

	private void addProperties(List<ControlProperty> propertiesList, ControlPropertyLookupConstant[] props) {
		for (ControlPropertyLookupConstant lookup : props) {
			propertiesList.add(lookup.getPropertyWithNoData());
		}
	}

	private void addNestedClasses(@NotNull List<ControlClass> nestedClasses, @NotNull SpecificationRegistry registry, @NotNull ControlClassSpecification... nestedClassesSpecs) {
		for (ControlClassSpecification nestedClass : nestedClassesSpecs) {
			nestedClasses.add(new ControlClass(nestedClass, registry));
		}
	}

	public final void setClassName(@NotNull String className) {
		classNameObserver.updateValue(className);
	}

	@NotNull
	public final String getClassName() {
		return classNameObserver.getValue();
	}

	@NotNull
	public final ValueObserver<String> getClassNameObserver() {
		return classNameObserver;
	}

	/**
	 Extend the given {@link ControlClass}.

	 @param controlClass class to extend
	 @throws IllegalArgumentException if <code>controlClass</code>==this
	 */
	public final void extendControlClass(@Nullable ControlClass controlClass) {
		if (controlClass == this) {
			throw new IllegalArgumentException("Extend class can't extend itself!");
		}
		ControlClass oldExtendClass = getExtendClass();
		if (controlClass != null) {
			for (ControlProperty property : getAllChildProperties()) {
				if (propertyIsOverridden(property.getPropertyLookup())) {
					continue;
				}
				try {
					ControlProperty inherit = controlClass.findProperty(property.getPropertyLookup());
					property.setTo(inherit);
				} catch (IllegalArgumentException ignore) {

				}
			}
			controlClass.getControlClassUpdateGroup().addListener(controlClassUpdateExtendListener);
		} else {
			for (ControlProperty property : getAllChildProperties()) {
				overrideProperty(property.getPropertyLookup()); //override again
			}
			if (oldExtendClass == null) {
				return;
			}
			oldExtendClass.getControlClassUpdateGroup().removeListener(controlClassUpdateExtendListener);
		}

		extendClassObserver.updateValue(controlClass);

	}

	@Nullable
	public final ControlClass getExtendClass() {
		return extendClassObserver.getValue();
	}

	@NotNull
	public final ValueObserver<ControlClass> getExtendClassObserver() {
		return extendClassObserver;
	}

	/** Get the instance of this provider. It is best to not return a new instance each time and store the instance for later use. */
	public final ControlClassRequirementSpecification getSpecProvider() {
		return specProvider;
	}

	@NotNull
	public final ReadOnlyList<ControlClass> getRequiredNestedClasses() {
		return requiredNestedClassesReadOnly;
	}

	@NotNull
	public final ReadOnlyList<ControlClass> getOptionalNestedClasses() {
		return optionalNestedClassesReadOnly;
	}


	/** Return a concatenation of {@link #getRequiredNestedClasses()} and {@link #getOptionalNestedClasses()} in an iterator */
	@NotNull
	public final Iterable<ControlClass> getAllNestedClasses() {
		ArrayList<List<ControlClass>> merge = new ArrayList<>(2);
		merge.add(requiredNestedClasses);
		merge.add(optionalNestedClasses);
		return new ListMergeIterator<>(false, merge);
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

	/**
	 Get the control property instance for the given lookup item. The search will be done inside the {@link ControlClass#getRequiredProperties()} return value.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup wasn't in required properties
	 */
	@NotNull
	public final ControlProperty findRequiredProperty(@NotNull ControlPropertyLookupConstant lookup) {
		for (ControlProperty controlProperty : getRequiredProperties()) {
			if (controlProperty.getPropertyLookup() == lookup) {
				return controlProperty;
			}
		}
		noPropertyMatch(lookup, "required properties");
		return null;
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside the {@link ControlClass#getOptionalProperties()} return value.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup wasn't in optional properties
	 */
	@NotNull
	public final ControlProperty findOptionalProperty(@NotNull ControlPropertyLookupConstant lookup) {
		for (ControlProperty controlProperty : getOptionalProperties()) {
			if (controlProperty.getPropertyLookup() == lookup) {
				return controlProperty;
			}
		}
		noPropertyMatch(lookup, "optional properties");
		return null;
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside {@link #getOptionalProperties()} and {@link #getRequiredProperties()}.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup doesn't exist in the {@link ControlClass}
	 */
	@NotNull
	public final ControlProperty findProperty(@NotNull ControlPropertyLookupConstant lookup) {
		try {
			return findRequiredProperty(lookup);
		} catch (IllegalArgumentException ignored) {
		}
		try {
			return findOptionalProperty(lookup);
		} catch (IllegalArgumentException ignored) {
		}
		noPropertyMatch(lookup, "control class");
		return null;
	}

	private void noPropertyMatch(@NotNull ControlPropertyLookupConstant lookup, @NotNull String place) {
		throw new IllegalArgumentException("Lookup element '" + lookup.getPropertyName() + "[" + lookup.getPropertyId() + "]" + "' wasn't in " + place + ".");
	}

	@NotNull
	public final ControlClass findRequiredNestedClass(@NotNull String className) {
		for (ControlClass controlClass : requiredNestedClasses) {
			if (controlClass.getClassName().equals(className)) {
				return controlClass;
			}
		}
		noClassMatch(className, "required nested classes");
		return null;
	}

	@NotNull
	public final ControlClass findOptionalNestedClass(@NotNull String className) {
		for (ControlClass controlClass : optionalNestedClasses) {
			if (controlClass.getClassName().equals(className)) {
				return controlClass;
			}
		}
		noClassMatch(className, "optional nested classes");
		return null;
	}

	@NotNull
	public final ControlClass findNestedClass(@NotNull String className) {
		try {
			return findRequiredNestedClass(className);
		} catch (IllegalArgumentException ignore) {
		}
		try {
			return findOptionalNestedClass(className);
		} catch (IllegalArgumentException ignore) {
		}
		noClassMatch(className, " control class " + getClassName());
		return null;
	}

	private void noClassMatch(@NotNull String className, @NotNull String place) {
		throw new IllegalArgumentException("Could not find class '" + className + "' in " + place + ".");
	}

	/**
	 Override's a property that may exist inside {@link #getExtendClass()}. When a property is "overridden", the property's value will never be inherited until it is no longer overridden
	 (achievable with {@link #removeOverrideProperty(ControlPropertyLookupConstant)}). If the property is overridden and inherited from {@link #getExtendClass()}, a deep copy of the inherited value
	 will be created and the this class's overridden property will be inserted into the list {@link #getOverriddenProperties()}.<br>
	 By default, all properties (optional and required) are overridden.

	 @throws IllegalArgumentException when the property doesn't exist in this control class
	 */
	public final void overrideProperty(@NotNull ControlPropertyLookupConstant property) throws IllegalArgumentException, IllegalStateException {
		if (propertyIsOverridden(property)) {
			return;
		}
		SerializableValue inheritValue = null;
		if (getExtendClass() != null) {
			try {
				ControlProperty toOverride = getExtendClass().findProperty(property);
				inheritValue = toOverride.getValue();
			} catch (IllegalArgumentException ignore) {

			}
		}
		ControlProperty mine = findProperty(property);
		SerializableValue value = inheritValue;
		if (value != null) {
			value = value.deepCopy();
		}
		mine.setValue(value);
		overrideProperties.add(mine);
		controlClassUpdateGroup.update(new ControlClassOverridePropertyUpdate(this, mine, true));
	}

	/** Will remove the given property from {@link #getOverriddenProperties()}. If the lookup isn't found, nothing will happen */
	public final void removeOverrideProperty(@NotNull ControlPropertyLookupConstant property) {
		int i = 0;
		while (i < overrideProperties.size()) {
			if (overrideProperties.get(i).getPropertyLookup() == property) {
				ControlProperty removed = overrideProperties.remove(i);
				if (getExtendClass() != null) {
					try {
						ControlProperty inherit = getExtendClass().findProperty(removed.getPropertyLookup());
						removed.setTo(inherit);
					} catch (IllegalArgumentException ignore) {

					}
				}
				controlClassUpdateGroup.update(new ControlClassOverridePropertyUpdate(this, overrideProperties.get(i), false));
				return;
			}
			i++;
		}
	}

	/** Return true if the given property lookup is overridden, false if it isn't */
	public final boolean propertyIsOverridden(@NotNull ControlPropertyLookupConstant lookup) {
		for (ControlProperty property : getOverriddenProperties()) {
			if (property.getPropertyLookup() == lookup) {
				return true;
			}
		}
		return false;
	}

	@NotNull
	public final ReadOnlyList<ControlProperty> getRequiredProperties() {
		return requiredPropertiesReadOnly;
	}

	@NotNull
	public final ReadOnlyList<ControlProperty> getOptionalProperties() {
		return optionalPropertiesReadOnly;
	}

	/** @see #overrideProperty(ControlPropertyLookupConstant) */
	@NotNull
	public final ReadOnlyList<ControlProperty> getOverriddenProperties() {
		return overridePropertiesReadOnly;
	}


	/** Will return all properties that are defined (excluding inherited properties that are defined) */
	@NotNull
	public final List<ControlProperty> getDefinedProperties() {
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

	/** Returns all {@link ControlProperty} instances (concatenation of {@link #getRequiredProperties()}) and {@link #getOptionalProperties()} in an iterator */
	public final Iterable<ControlProperty> getAllChildProperties() {
		ArrayList<ReadOnlyList<ControlProperty>> merge = new ArrayList<>(2);
		merge.add(getRequiredProperties());
		merge.add(getOptionalProperties());
		return new ListMergeIterator<>(false, merge);
	}


	/** Will return all properties from {@link #getInheritedProperties()} that have defined values ({@link ControlProperty#getValue()} != null) */
	@NotNull
	public final List<ControlProperty> getDefinedInheritedProperties() {
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
	public final List<ControlProperty> getOverriddenDefinedProperties() {
		List<ControlProperty> defined = new ArrayList<>(overrideProperties.size());
		for (ControlProperty property : overrideProperties) {
			if (property.getValue() != null) {
				defined.add(property);
			}
		}
		return defined;
	}

	/** Return a list of {@link ControlProperty} that are <b>not</b> overridden via {@link #overrideProperty(ControlPropertyLookupConstant)} and are inherited from {@link #getExtendClass()} */
	@NotNull
	public final List<ControlProperty> getInheritedProperties() {
		if (getExtendClass() == null) {
			return new ArrayList<>();
		}
		ArrayList<ControlProperty> list = new ArrayList<>();
		appendInheritedProperties(getExtendClass(), list);
		return list;
	}

	private void appendInheritedProperties(@NotNull ControlClass extend, @NotNull ArrayList<ControlProperty> list) {
		for (ControlProperty c : extend.getDefinedProperties()) {
			if (!propertyIsOverridden(c.getPropertyLookup())) {
				if (!list.contains(c)) {
					list.add(c);
				}
			}
		}
		if (extend.getExtendClass() != null) {
			appendInheritedProperties(extend.getExtendClass(), list);
		}
	}

	public final ReadOnlyList<ControlProperty> getEventProperties() { //todo have getOptionalPropertiesNoEvents where it returns all optional properties without event properties
		final List<ControlProperty> eventProperties = new ArrayList<>();
		for (ControlProperty property : getAllChildProperties()) {
			if (ControlPropertyEventLookup.getEventProperty(property.getPropertyLookup()) != null) {
				eventProperties.add(property);
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
	public final UpdateListenerGroup<ControlPropertyUpdate> getPropertyUpdateGroup() {
		return propertyUpdateGroup;
	}

	/**
	 Gets the update listener group that listens to when an update happens to this {@link ControlClass}. Things that may trigger the update:<br>
	 <ul>
	 <li>update in conjunction with {@link #getPropertyUpdateGroup()}</li>
	 <li>{@link #setClassName(String)}</li>
	 <li>{@link #extendControlClass(ControlClass)}</li>
	 <li>{@link #overrideProperty(ControlPropertyLookupConstant)}</li>
	 <li>{@link #removeOverrideProperty(ControlPropertyLookupConstant)}</li>
	 </ul>
	 */
	@NotNull
	public final UpdateListenerGroup<ControlClassUpdate> getControlClassUpdateGroup() {
		return controlClassUpdateGroup;
	}

	/**
	 Checks if the given {@link ControlClass} matches the following criteria:<br>
	 <ul>
	 <li>{@code this.getClassName().equals(controlClass.getClassName())}</li>
	 <li>this class's required properties matches {@code controlClass}'s required properties</li>
	 <li>this class's required nested classes matches {@code controlClass}'s required nested classes (recursive check with this method)</li>
	 </ul>

	 @param controlClass class to check if equals
	 @return true if the criteria are met, false otherwise
	 */
	public boolean classEquals(@NotNull ControlClass controlClass) {
		boolean name = this.getClassName().equals(controlClass.getClassName());
		if (!name) {
			return false;
		}

		for (ControlPropertyLookupConstant lookup : specProvider.getRequiredProperties()) {
			boolean found = false;
			for (ControlPropertyLookupConstant lookup2 : controlClass.specProvider.getRequiredProperties()) {
				if (lookup == lookup2) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}

		for (ControlClass requiredClass : getRequiredNestedClasses()) {
			boolean found = false;
			for (ControlClass requiredClass2 : controlClass.getRequiredNestedClasses()) {
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

	/**
	 Sets this {@link ControlClass} fully equal to <code>controlClass</code> (disregards {@link #getUserData()}). NOTE: nothing will be deep copied!

	 @param controlClass class to use
	 */
	public final void setTo(@NotNull ControlClass controlClass) {
		setClassName(controlClass.getClassName());
		extendControlClass(controlClass.getExtendClass());
		for (ControlProperty property : controlClass.getAllChildProperties()) {
			try {
				findProperty(property.getPropertyLookup()).setTo(property);
			} catch (IllegalArgumentException ignore) {

			}
		}
		for (ControlClass nested : controlClass.getAllNestedClasses()) {
			try {
				findNestedClass(nested.getClassName()).setTo(nested);
			} catch (IllegalArgumentException ignore) {

			}
		}
		for (ControlProperty override : controlClass.getOverriddenProperties()) {
			try {
				overrideProperty(findProperty(override.getPropertyLookup()).getPropertyLookup());
			} catch (IllegalArgumentException | NullPointerException ignore) {

			}
		}
	}

	@Override
	public String toString() {
		return getClassName();
	}

	/** Get a {@link DataContext} instance that stores random things. */
	@NotNull
	public DataContext getUserData() {
		return userData;
	}

}
