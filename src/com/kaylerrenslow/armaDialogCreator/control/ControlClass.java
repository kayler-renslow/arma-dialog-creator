package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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

	private final List<ControlClass> requiredNestedClasses = new LinkedList<>();
	private final List<ControlClass> optionalNestedClasses = new LinkedList<>();


	private final ReadOnlyList<ControlProperty> requiredPropertiesReadOnly = new ReadOnlyList<>(requiredProperties);
	private final ReadOnlyList<ControlProperty> optionalPropertiesReadOnly = new ReadOnlyList<>(optionalProperties);

	private final ReadOnlyList<ControlClass> requiredNestedClassesReadOnly = new ReadOnlyList<>(requiredNestedClasses);
	private final ReadOnlyList<ControlClass> optionalNestedClassesReadOnly = new ReadOnlyList<>(optionalNestedClasses);

	private final DataContext userData = new DataContext();

	private final ValueObserver<String> classNameObserver = new ValueObserver<>(null);
	private final ValueObserver<ControlClass> extendClassObserver = new ValueObserver<>(null);

	private final UpdateListenerGroup<ControlPropertyUpdate> propertyUpdateGroup = new UpdateListenerGroup<>();
	private final UpdateListenerGroup<ControlClassUpdate> controlClassUpdateGroup = new UpdateListenerGroup<>();
	private final UpdateGroupListener<ControlClassUpdate> controlClassUpdateExtendListener = new UpdateGroupListener<ControlClassUpdate>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
			if (data instanceof ControlClassPropertyUpdate) {
				updateControlProperty((ControlClassPropertyUpdate) data);
			}
		}

		private void updateControlProperty(ControlClassPropertyUpdate data) {
			ControlPropertyUpdate propertyUpdate = data.getPropertyUpdate();
			try {
				ControlProperty myProperty = findProperty(propertyUpdate.getControlProperty().getPropertyLookup());

				if (propertyIsOverridden(myProperty)) {
					return; //should not replace the value since it is overridden
				}
				if (propertyIsDefined(propertyUpdate.getControlProperty())) {
					myProperty.inherit(propertyUpdate.getControlProperty());
				} else {
					myProperty.inherit(null);
				}
			} catch (IllegalArgumentException ignore) {
				//property simple doesn't exist, so nothing to do
			}
		}
	};
	private final UpdateGroupListener<ControlPropertyUpdate> controlPropertyListener = new UpdateGroupListener<ControlPropertyUpdate>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ControlPropertyUpdate> group, ControlPropertyUpdate data) {
			if (data instanceof ControlPropertyInheritUpdate) {
				ControlPropertyInheritUpdate update = (ControlPropertyInheritUpdate) data;
				controlClassUpdateGroup.update(new ControlClassOverridePropertyUpdate(ControlClass.this, update.getControlProperty(), !update.wasInherited()));
			}
			propertyUpdateGroup.update(data);
			controlClassUpdateGroup.update(new ControlClassPropertyUpdate(ControlClass.this, data));
		}
	};


	public ControlClass(@NotNull String name, @NotNull ControlClassRequirementSpecification specification, @NotNull SpecificationRegistry registry) {
		classNameObserver.updateValue(name);
		this.specProvider = specification;

		List<ControlPropertyLookupConstant> prefetch = new LinkedList<>();
		prefetch.addAll(specProvider.getRequiredProperties());
		prefetch.addAll(specProvider.getOptionalProperties());
		registry.prefetchValues(prefetch);

		addProperties(requiredProperties, specProvider.getRequiredProperties(), registry, true);
		addProperties(optionalProperties, specProvider.getOptionalProperties(), registry, false);
		addNestedClasses(requiredNestedClasses, registry, specProvider.getRequiredNestedClasses());
		addNestedClasses(optionalNestedClasses, registry, specProvider.getOptionalNestedClasses());

		afterConstructor();
	}

	/** Construct a {@link ControlClass} with the given specification and {@link SpecificationRegistry} */
	public ControlClass(@NotNull ControlClassSpecification specification, @NotNull SpecificationRegistry registry) {
		setClassName(specification.getClassName());
		this.specProvider = specification;
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
		if (specification.getExtendClassName() != null) {
			extendControlClass(registry.findControlClassByName(specification.getExtendClassName()));
		}
		afterConstructor();
	}

	private void afterConstructor() {

		for (ControlProperty controlProperty : requiredProperties) {
			controlProperty.getControlPropertyUpdateGroup().addListener(controlPropertyListener);
		}
		for (ControlProperty controlProperty : optionalProperties) {
			controlProperty.getControlPropertyUpdateGroup().addListener(controlPropertyListener);
		}
		classNameObserver.addListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
				if (newValue == null) {
					throw new IllegalArgumentException("class name can't be null");
				}
				controlClassUpdateGroup.update(new ControlClassRenameUpdate(ControlClass.this, oldValue, newValue));
			}
		});
		extendClassObserver.addListener(new ValueListener<ControlClass>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<ControlClass> observer, ControlClass oldValue, ControlClass newValue) {
				controlClassUpdateGroup.update(new ControlClassExtendUpdate(ControlClass.this, oldValue, newValue));
			}
		});
	}

	private void addProperties(List<ControlProperty> propertiesList, List<ControlPropertyLookupConstant> props, @NotNull SpecificationRegistry registry, boolean required) {
		for (ControlPropertyLookupConstant lookup : props) {
			if (required) {
				propertiesList.add(lookup.newEmptyProperty(registry));
			} else {
				propertiesList.add(lookup.newEmptyProperty(null));
			}
		}
	}

	private void addNestedClasses(@NotNull List<ControlClass> nestedClasses, @NotNull SpecificationRegistry registry, @NotNull List<ControlClassSpecification> nestedClassesSpecs) {
		for (ControlClassSpecification nestedClass : nestedClassesSpecs) {
			nestedClasses.add(new ControlClass(nestedClass, registry));
		}
	}

	@NotNull
	public final String getClassName() {
		if (classNameObserver.getValue() == null) {
			throw new IllegalArgumentException("class name can't be null");
		}
		return classNameObserver.getValue();
	}

	public final void setClassName(@NotNull String className) {
		classNameObserver.updateValue(className);
	}

	@NotNull
	public final ValueObserver<String> getClassNameObserver() {
		return classNameObserver;
	}

	/**
	 Extend the given {@link ControlClass}, or clear the extend class with null.

	 @param controlClass class to extend
	 @throws IllegalArgumentException if <code>controlClass</code>==this
	 @see ControlProperty#inherit(ControlProperty)
	 */
	public final void extendControlClass(@Nullable ControlClass controlClass) {
		if (controlClass == this) {
			throw new IllegalArgumentException("Extend class can't extend itself!");
		}
		ControlClass oldExtendClass = getExtendClass();
		if (oldExtendClass == controlClass) {
			return;
		}
		if (controlClass != null) {
			for (ControlProperty property : getAllChildProperties()) {
				if (propertyIsOverridden(property)) {
					continue;
				}

				ControlProperty inherit = controlClass.findPropertyNullable(property.getPropertyLookup());
				if (inherit == null) {
					continue;
				}
				if (propertyIsDefined(inherit)) {
					property.getControlPropertyUpdateGroup().removeListener(this.controlPropertyListener);
					property.inherit(inherit);
					property.getControlPropertyUpdateGroup().addListener(this.controlPropertyListener);
				}
			}
			controlClass.getControlClassUpdateGroup().addListener(controlClassUpdateExtendListener);
		} else {
			for (ControlProperty property : getAllChildProperties()) {
				property.getControlPropertyUpdateGroup().removeListener(this.controlPropertyListener);
				property.inherit(null);
				property.getControlPropertyUpdateGroup().addListener(this.controlPropertyListener);
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

	/**
	 Get all missing properties. A missing property is one that is required and is undefined.

	 @return all missing properties
	 @see #getRequiredProperties()
	 @see #propertyIsDefined(ControlProperty)
	 */
	@NotNull
	public final Iterable<ControlProperty> getMissingRequiredProperties() {
		List<ControlProperty> missing = new LinkedList<>();

		for (ControlProperty req : requiredProperties) {
			if (!propertyIsDefined(req)) {
				missing.add(req);
			}

		}
		return missing;
	}

	/**
	 Return the first missing property, or null if not missing an properties

	 @return property, or null if nothing is missing.
	 @see #getMissingRequiredProperties()
	 */
	public final ControlProperty getFirstMissingProperty() {
		for (ControlProperty req : requiredProperties) {
			if (!propertyIsDefined(req)) {
				return req;
			}
		}
		return null;
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside the {@link ControlClass#getRequiredProperties()} return value.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup wasn't in required properties
	 @see #findRequiredPropertyNullable(ControlPropertyLookupConstant)
	 */
	@NotNull
	public final ControlProperty findRequiredProperty(@NotNull ControlPropertyLookupConstant lookup) {
		ControlProperty controlProperty = findRequiredPropertyNullable(lookup);
		if (controlProperty != null) {
			return controlProperty;
		}
		noPropertyMatch(lookup, "required properties");
		return null;
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside the {@link ControlClass#getRequiredProperties()} return value.

	 @return the ControlProperty instance, or null if couldn't be matched
	 */
	@Nullable
	public final ControlProperty findRequiredPropertyNullable(@NotNull ControlPropertyLookupConstant lookup) {
		return findPropertyFromList(lookup, getRequiredProperties());
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside the {@link ControlClass#getOptionalProperties()} return value.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup wasn't in optional properties
	 @see #findOptionalPropertyNullable(ControlPropertyLookupConstant)
	 */
	@NotNull
	public final ControlProperty findOptionalProperty(@NotNull ControlPropertyLookupConstant lookup) {
		ControlProperty controlProperty = findOptionalPropertyNullable(lookup);
		if (controlProperty != null) {
			return controlProperty;
		}
		noPropertyMatch(lookup, "optional properties");
		return null;
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside the {@link ControlClass#getOptionalProperties()} return value.

	 @return the ControlProperty instance, or null if couldn't be matched
	 @see #findOptionalProperty(ControlPropertyLookupConstant)
	 */
	@Nullable
	public final ControlProperty findOptionalPropertyNullable(@NotNull ControlPropertyLookupConstant lookup) {
		return findPropertyFromList(lookup, getOptionalProperties());
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside {@link #getOptionalProperties()} and {@link #getRequiredProperties()}.

	 @return the ControlProperty instance
	 @throws IllegalArgumentException when the lookup doesn't exist in the {@link ControlClass}
	 @see #findPropertyNullable(ControlPropertyLookupConstant)
	 */
	@NotNull
	public final ControlProperty findProperty(@NotNull ControlPropertyLookupConstant lookup) {
		ControlProperty c = findOptionalPropertyNullable(lookup);
		if (c != null) {
			return c;
		}
		c = findRequiredPropertyNullable(lookup);
		if (c != null) {
			return c;
		}
		noPropertyMatch(lookup, "control class");
		return null;
	}

	/**
	 Get the control property instance for the given lookup item. The search will be done inside {@link #getOptionalProperties()} and {@link #getRequiredProperties()}.

	 @return the ControlProperty instance, or null if couldn't be found
	 @see #findProperty(ControlPropertyLookupConstant)
	 */
	@Nullable
	public final ControlProperty findPropertyNullable(@NotNull ControlPropertyLookupConstant lookup) {
		ControlProperty c = findRequiredPropertyNullable(lookup);
		if (c != null) {
			return c;
		}
		c = findOptionalPropertyNullable(lookup);
		if (c != null) {
			return c;
		}
		return null;
	}

	@Nullable
	private ControlProperty findPropertyFromList(@NotNull ControlPropertyLookupConstant lookup, @NotNull Iterable<ControlProperty> iterable) {
		for (ControlProperty controlProperty : iterable) {
			if (controlProperty.getPropertyLookup() == lookup) {
				return controlProperty;
			}
		}
		return null;
	}

	private void noPropertyMatch(@NotNull ControlPropertyLookupConstant lookup, @NotNull String place) {
		throw new IllegalArgumentException("Lookup element '" + lookup.getPropertyName() + "[id=" + lookup.getPropertyId() + "]" + "' wasn't in " + place + ".");
	}

	/**
	 Find a required nested {@link ControlClass} by name. If it can't be found, will throw an exception.

	 @return matched class
	 @see #findRequiredNestedClassNullable(String)
	 */
	@NotNull
	public final ControlClass findRequiredNestedClass(@NotNull String className) {
		ControlClass c = findRequiredNestedClassNullable(className);
		if (c != null) {
			return c;
		}
		noClassMatch(className, "required nested classes");
		return null;
	}

	/**
	 Find a required nested {@link ControlClass} by name. If it can't be found, will return null

	 @return matched class, or null
	 @see #findRequiredNestedClass(String)
	 */
	@Nullable
	public final ControlClass findRequiredNestedClassNullable(@NotNull String className) {
		return findNestedClassFromList(className, requiredNestedClasses);
	}

	/**
	 Find an optional nested {@link ControlClass} by name. If it can't be found, will throw an exception.

	 @return matched class
	 @see #findOptionalNestedClassNullable(String)
	 */
	@NotNull
	public final ControlClass findOptionalNestedClass(@NotNull String className) {
		ControlClass c = findOptionalNestedClassNullable(className);
		if (c != null) {
			return c;
		}
		noClassMatch(className, "optional nested classes");
		return null;
	}

	/**
	 Find an optional nested {@link ControlClass} by name. If it can't be found, will return null

	 @return matched class, or null
	 @see #findOptionalNestedClass(String)
	 */
	@Nullable
	public final ControlClass findOptionalNestedClassNullable(@NotNull String className) {
		return findNestedClassFromList(className, optionalNestedClasses);
	}

	/**
	 Find an optional or required nested {@link ControlClass} by name. If it can't be found, will throw an exception

	 @return matched class
	 @see #findNestedClassNullable(String)
	 */
	@NotNull
	public final ControlClass findNestedClass(@NotNull String className) {
		ControlClass c = findNestedClassNullable(className);
		if (c != null) {
			return c;
		}
		noClassMatch(className, " control class " + getClassName());
		return null;
	}

	/**
	 Find an optional or required nested {@link ControlClass} by name. If it can't be found, will return null

	 @return matched class, or null
	 @see #findNestedClass(String)
	 */
	@Nullable
	public final ControlClass findNestedClassNullable(@NotNull String className) {
		ControlClass c = findRequiredNestedClassNullable(className);
		if (c != null) {
			return c;
		}
		return findOptionalNestedClassNullable(className);
	}

	@Nullable
	private ControlClass findNestedClassFromList(@NotNull String className, @NotNull Iterable<ControlClass> list) {
		for (ControlClass controlClass : list) {
			if (controlClass.getClassName().equals(className)) {
				return controlClass;
			}
		}
		return null;
	}

	private void noClassMatch(@NotNull String className, @NotNull String place) {
		throw new IllegalArgumentException("Could not find class '" + className + "' in " + place + ".");
	}

	/**
	 Override's a property that may exist inside {@link #getExtendClass()}. When a property is "overridden", the property's value will never be inherited until it is no longer overridden
	 (achievable with {@link #inheritProperty(ControlPropertyLookupConstant)}).

	 @throws IllegalArgumentException when the property doesn't exist in this {@link ControlClass}
	 @see #propertyIsOverridden(ControlPropertyLookupConstant)
	 */
	public final void overrideProperty(@NotNull ControlPropertyLookupConstant property) throws IllegalArgumentException, IllegalStateException {
		ControlProperty mine = findProperty(property);
		mine.inherit(null);
	}

	/**
	 <ul>
	 <li>
	 If {@link #getExtendClass()}!=null, the matched {@link ControlProperty} will be set to
	 a matched inherited property by using the {@link ControlProperty#setTo(ControlProperty)} method.
	 </li>
	 <li>If {@link #getExtendClass()} is null, nothing will happen.</li>
	 <li>If the lookup isn't found in this {@link ControlClass} and {@link #getExtendClass()} is null, nothing will happen.</li>
	 <li>
	 If the lookup isn't found in this {@link ControlClass} but is inside {@link #getExtendClass()},
	 the {@link ControlProperty} will be inserted into {@link #getOptionalProperties()}
	 </li>
	 <li>
	 If the lookup is found in this {@link ControlClass} but isn't inside {@link #getExtendClass()},
	 nothing will happen.
	 </li>
	 </ul>
	 If at any point, one of the above conditions changes because {@link #extendControlClass(ControlClass)} is
	 invoked, the properties should react accordingly due to {@link ControlClass#getControlClassUpdateGroup()}
	 listeners.

	 @throws IllegalArgumentException when the property isn't in this control class
	 @see #propertyIsOverridden(ControlPropertyLookupConstant)
	 @see #overrideProperty(ControlPropertyLookupConstant)
	 */
	public final void inheritProperty(@NotNull ControlPropertyLookupConstant lookup) {
		if (getExtendClass() == null) {
			return;
		}
		ControlProperty mine = findPropertyNullable(lookup);
		if (mine != null) {
			ControlProperty inherit = getExtendClass().findPropertyNullable(mine.getPropertyLookup());
			if (inherit == null) {
				//todo make sure to handle the case when the property is inserted into getExtendClass at a later time!
				return; //don't want to clear mine.inherit()
			}
			mine.inherit(inherit);
			return;
		} else {
			//todo
		}

	}

	/**
	 In order for a property to be overridden, the property exists in this {@link ControlClass}, the property is defined ({@link #propertyIsDefined(ControlProperty)}), and the property isn't
	 inherited ({@link ControlProperty#isInherited()})

	 @return true if the given property lookup is overridden, false if it isn't.
	 */
	public final boolean propertyIsOverridden(@NotNull ControlPropertyLookupConstant lookup) {
		try {
			return propertyIsOverridden(findProperty(lookup));
		} catch (IllegalArgumentException e) {
			return false;
		}

	}

	/**
	 In order for a property to be overridden, the property is not inherited ({@link ControlProperty#isInherited()}==false) and the property is defined ({@link #propertyIsDefined(ControlProperty)})

	 @return true if the given property lookup is overridden, false if it isn't.
	 */
	public final boolean propertyIsOverridden(@NotNull ControlProperty property) {
		return propertyIsDefined(property) && !property.isInherited();
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
	public final Iterable<ControlProperty> getOverriddenProperties() {
		List<ControlProperty> list = new LinkedList<>();
		getAllChildProperties().forEach(new Consumer<ControlProperty>() {
			@Override
			public void accept(ControlProperty property) {
				if (propertyIsOverridden(property)) {
					list.add(property);
				}
			}
		});
		return list;
	}


	/**
	 Will return all properties that are defined (excluding inherited properties that are defined). To check if property is defined, the method {@link #propertyIsDefined(ControlProperty)} will
	 be used.
	 */
	@NotNull
	public final Iterable<ControlProperty> getDefinedProperties() {
		List<ControlProperty> properties = new LinkedList<>();
		for (ControlProperty property : getRequiredProperties()) {
			if (propertyIsDefined(property)) {
				properties.add(property);
			}
		}
		for (ControlProperty property : getOptionalProperties()) {
			if (propertyIsDefined(property)) {
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


	/** Will return all properties from {@link #getInheritedProperties()} that have defined values ({@link #propertyIsDefined(ControlProperty)}) */
	@NotNull
	public final Iterable<ControlProperty> getDefinedInheritedProperties() {
		Iterable<ControlProperty> inheritedProperties = getInheritedProperties();
		List<ControlProperty> definedProperties = new LinkedList<>();
		for (ControlProperty c : inheritedProperties) {
			if (propertyIsDefined(c)) {
				definedProperties.add(c);
			}
		}

		return definedProperties;
	}


	/** Return a list of {@link ControlProperty} that are <b>not</b> overridden via {@link #overrideProperty(ControlPropertyLookupConstant)} and are inherited from {@link #getExtendClass()} */
	@NotNull
	public final Iterable<ControlProperty> getInheritedProperties() {
		if (getExtendClass() == null) {
			return Collections.emptyList();
		}
		List<ControlProperty> list = new LinkedList<>();
		appendInheritedProperties(getExtendClass(), list);
		return list;
	}

	private void appendInheritedProperties(@NotNull ControlClass extend, @NotNull List<ControlProperty> list) {
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

	public final Iterable<ControlProperty> getEventProperties() { //todo have getOptionalPropertiesNoEvents where it returns all optional properties without event properties
		final List<ControlProperty> eventProperties = new LinkedList<>();
		for (ControlProperty property : getAllChildProperties()) {
			if (ControlPropertyEventLookup.getEventProperty(property.getPropertyLookup()) != null) {
				eventProperties.add(property);
			}
		}

		return eventProperties;
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
	 <li>{@link #inheritProperty(ControlPropertyLookupConstant)}</li>
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

		for (ControlProperty property : controlClass.getAllChildProperties()) {
			ControlProperty m = findPropertyNullable(property.getPropertyLookup());
			if (m == null) {
				continue;
			}
			m.setTo(property);
		}
		for (ControlClass nested : controlClass.getAllNestedClasses()) {
			ControlClass m = findNestedClassNullable(nested.getClassName());
			if (m == null) {
				continue;
			}
			m.setTo(nested);
		}

		for (ControlProperty override : controlClass.getOverriddenProperties()) {
			try {
				overrideProperty(override.getPropertyLookup());
			} catch (IllegalArgumentException ignore) {

			}
		}

		extendControlClass(controlClass.getExtendClass());
	}

	/**
	 Check if the given property is defined. A property is defined if {@link ControlProperty#getValue()} != null or if {@link ControlProperty#isUsingCustomData()} and
	 {@link ControlProperty#getCustomData()} != null;

	 @param property property to check
	 @return true if the property is defined, false otherwise
	 */
	public static boolean propertyIsDefined(@NotNull ControlProperty property) {
		return property.getValue() != null || (property.isUsingCustomData() && property.getCustomData() != null);
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


	/**
	 Updates this {@link ControlClass} to the given update

	 @param data update to give
	 @param deepCopy true if any values passed should be deep copied, false otherwise
	 */
	public final void update(@NotNull ControlClassUpdate data, boolean deepCopy) {
		if (data instanceof ControlClassRenameUpdate) {
			ControlClassRenameUpdate update = (ControlClassRenameUpdate) data;
			setClassName(update.getNewName());
		} else if (data instanceof ControlClassPropertyUpdate) {
			update(((ControlClassPropertyUpdate) data).getPropertyUpdate(), deepCopy);
		} else if (data instanceof ControlClassOverridePropertyUpdate) {
			ControlClassOverridePropertyUpdate update = (ControlClassOverridePropertyUpdate) data;
			if (update.wasOverridden()) {
				try {
					overrideProperty(update.getOveridden().getPropertyLookup());
				} catch (IllegalArgumentException ignore) {

				}

			} else {
				try {
					inheritProperty(update.getOveridden().getPropertyLookup());
				} catch (IllegalArgumentException ignore) {

				}

			}
		} else if (data instanceof ControlClassExtendUpdate) {
			ControlClassExtendUpdate update = (ControlClassExtendUpdate) data;
			extendControlClass(update.getNewValue());
		} else {
			System.err.println("WARNING: ControlClass.update(): unknown control class update:" + data);
		}
	}

	private void update(@NotNull ControlPropertyUpdate propertyUpdate, boolean deepCopy) {
		try {
			findProperty(propertyUpdate.getControlProperty().getPropertyLookup()).update(propertyUpdate, deepCopy);
		} catch (IllegalArgumentException ignore) {

		}
	}
}
