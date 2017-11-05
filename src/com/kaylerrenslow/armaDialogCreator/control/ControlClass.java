package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 A {@link ControlClass} is merely a header file (.h) class that is:<br>
 <ol>
 <li>Extend-able via {@link ControlClass#extendControlClass(ControlClass)}</li>
 <li>Required and optional {@link ControlProperty}'s (obtainable via {@link #getRequiredProperties()}
 and {@link #getOptionalProperties()} respectively).</li>
 <li>Required and optional nested classes (classes within this class) which can be accessed with
 {@link #getRequiredNestedClasses()} and {@link #getOptionalNestedClasses()} respectively.</li>
 <li>There should be no duplicate names across all {@link ControlProperty} instances.</li>
 </ol>
 <p>
 A required {@link ControlProperty} is a property that is necessary to fill a {@link ControlClassRequirementSpecification}.
 In other words, those properties' values
 ({@link ControlProperty#getValue()}) should <b>not</b> be null. An optional property is one that can be null.
 </p>
 <p>
 A required nested class is merely a class that should also have it's required properties filled in.
 An optional nested class doesn't need it's required properties to be filled in.
 </p>

 @author Kayler
 @since 05/23/2016. */
public class ControlClass {
	private final ControlClassRequirementSpecification specProvider;

	private final List<ControlProperty> requiredProperties = new LinkedList<>();
	private final List<ControlProperty> optionalProperties = new LinkedList<>();
	private final List<ControlProperty> tempProperties = new LinkedList<>();
	private final ReadOnlyList<ControlProperty> requiredPropertiesReadOnly = new ReadOnlyList<>(requiredProperties);
	private final ReadOnlyList<ControlProperty> optionalPropertiesReadOnly = new ReadOnlyList<>(optionalProperties);
	private final ReadOnlyList<ControlProperty> tempPropertiesReadOnly = new ReadOnlyList<>(tempProperties);

	private final List<ControlClass> requiredNestedClasses = new LinkedList<>();
	private final List<ControlClass> optionalNestedClasses = new LinkedList<>();
	private final List<ControlClass> inheritedNestedClasses = new LinkedList<>();
	private final ReadOnlyList<ControlClass> requiredNestedClassesReadOnly = new ReadOnlyList<>(requiredNestedClasses);
	private final ReadOnlyList<ControlClass> optionalNestedClassesReadOnly = new ReadOnlyList<>(optionalNestedClasses);
	private final ReadOnlyList<ControlClass> inheritedNestedClassesReadOnly = new ReadOnlyList<>(inheritedNestedClasses);
	private final List<ControlClass> tempNestedClasses = new LinkedList<>();
	private final ReadOnlyList<ControlClass> tempNestedClassesReadOnly = new ReadOnlyList<>(tempNestedClasses);

	private final DataContext userData = new DataContext();

	private final ValueObserver<String> classNameObserver = new ValueObserver<>(null);
	private final ValueObserver<ControlClass> extendClassObserver = new ValueObserver<>(null);
	private final Set<ControlClass> mySubClasses = new HashSet<>();

	/** set to true when {@link #extendControlClass(ControlClass)} is invoked, false after the method has completed */
	private boolean updatingExtendClass = false;

	private final UpdateListenerGroup<ControlPropertyUpdate> propertyUpdateGroup = new UpdateListenerGroup<>();
	private final UpdateListenerGroup<ControlClassUpdate> controlClassUpdateGroup = new UpdateListenerGroup<>();
	/** This listener is for handling any inheritance updates */
	private final UpdateGroupListener<ControlClassUpdate> controlClassUpdateExtendListener = new UpdateGroupListener<ControlClassUpdate>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
			if (data instanceof ControlClassTemporaryPropertyUpdate) {
				handleTempPropertyUpdate((ControlClassTemporaryPropertyUpdate) data);
			} else if (data instanceof ControlClassTemporaryNestedClassUpdate) {
				handleTempNestedClassUpdate((ControlClassTemporaryNestedClassUpdate) data);
			}
		}

		private void handleTempNestedClassUpdate(ControlClassTemporaryNestedClassUpdate update) {
			ControlClass mine = findNestedClassNullable(update.getNestedClass().getClassName());
			ControlClass updateNested = update.getNestedClass();
			if (update.isAdded()) {
				if (mine == null) {
					optionalNestedClasses.add(updateNested);
					tempNestedClasses.add(updateNested);
				}
			} else {
				if (mine != null) {
					optionalNestedClasses.remove(updateNested);
					tempNestedClasses.remove(updateNested);
				}
			}
			controlClassUpdateGroup.update(
					new ControlClassTemporaryNestedClassUpdate(ControlClass.this, updateNested, update.isAdded())
			);
		}

		private void handleTempPropertyUpdate(ControlClassTemporaryPropertyUpdate tempPropertyUpdate) {
			ControlProperty mine = findPropertyByNameNullable(tempPropertyUpdate.getProperty().getName());
			ControlPropertyLookupConstant constant = tempPropertyUpdate.getProperty().getPropertyLookup();
			if (tempPropertyUpdate.isAdded()) {
				if (mine == null || !propertyIsOverridden(mine)) {
					inheritProperty(constant);
				}
			} else {
				if (mine == null || !mine.isInherited()) {
					overrideProperty(constant);
				}
			}
		}
	};
	private final UpdateGroupListener<ControlPropertyUpdate> controlPropertyListener = new UpdateGroupListener<ControlPropertyUpdate>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ControlPropertyUpdate> group, ControlPropertyUpdate data) {
			if (data instanceof ControlPropertyInheritUpdate) {
				ControlPropertyInheritUpdate update = (ControlPropertyInheritUpdate) data;
				controlClassUpdateGroup.update(
						new ControlClassInheritPropertyUpdate(ControlClass.this,
								update.getControlProperty(), update.wasInherited(), updatingExtendClass
						)
				);
			}
			propertyUpdateGroup.update(data);
			controlClassUpdateGroup.update(new ControlClassPropertyUpdate(ControlClass.this, data));
		}
	};
	private final IdentityHashMap<String, UpdateGroupListener<ControlClassUpdate>> inheritNestedClassListenerMap = new IdentityHashMap<>();

	/**
	 Construct a new instance with the given class name, specification, and registry.
	 This constructor will create new {@link ControlProperty} instances for
	 {@link #getOptionalProperties()} and {@link #getRequiredProperties()} via {@link ControlPropertyLookupConstant#newEmptyProperty(DefaultValueProvider)}
	 (using parameter <code>registry</code>).
	 <p>
	 This constructor will invoke {@link #ControlClass(String, ControlClassRequirementSpecification, SpecificationRegistry, DefaultValueProvider.Context)},
	 with a {@link DefaultValueProvider.ControlClassNameContext} passed as the context parameter.

	 @param name the class name
	 @param specification specification to use
	 @param registry registry to use
	 @see ControlProperty
	 */
	public ControlClass(@NotNull String name, @NotNull ControlClassRequirementSpecification specification, @NotNull SpecificationRegistry registry) {
		this(name, specification, registry, new DefaultValueProvider.ControlClassNameContext(name));
	}


	/**
	 This is equivalent to {@link #ControlClass(String, ControlClassRequirementSpecification, SpecificationRegistry)},
	 but this constructor allows for a predefined {@link DefaultValueProvider.Context}.
	 <p>
	 All nested classes for this {@link ControlClass} will then get a new {@link DefaultValueProvider.ControlClassNameContext}
	 with the provided {@link DefaultValueProvider.Context} as a parent context
	 (e.g. {@link DefaultValueProvider.Context#getParentContext()} )
	 */
	public ControlClass(@NotNull String name, @NotNull ControlClassRequirementSpecification specification,
						@NotNull SpecificationRegistry registry, @Nullable DefaultValueProvider.Context context) {
		classNameObserver.updateValue(name);
		this.specProvider = specification;

		List<ControlPropertyLookupConstant> prefetch = new LinkedList<>();
		prefetch.addAll(specProvider.getRequiredProperties());
		prefetch.addAll(specProvider.getOptionalProperties());
		registry.prefetchValues(prefetch, context);

		addProperties(requiredProperties, specProvider.getRequiredProperties(), registry);
		addProperties(optionalProperties, specProvider.getOptionalProperties(), registry);

		registry.cleanup();

		addNestedClasses(requiredNestedClasses, registry, specProvider.getRequiredNestedClasses(), context);
		addNestedClasses(optionalNestedClasses, registry, specProvider.getOptionalNestedClasses(), context);

		afterPropertyAndNestedClassConstruction();
	}

	/**
	 Construct a {@link ControlClass} with the given specification and {@link SpecificationRegistry}.
	 This constructor will not use the {@link DefaultValueProvider} since the {@link ControlClassSpecification} should specify
	 the values for each of its {@link ControlPropertySpecification}
	 */
	public ControlClass(@NotNull ControlClassSpecification specification, @NotNull SpecificationRegistry registry) {
		this(specification, registry, null);
	}

	/**
	 Construct a {@link ControlClass} with the given specification and {@link SpecificationRegistry}.
	 This constructor will use the {@link DefaultValueProvider} if the provided {@link DefaultValueProvider.Context} is not
	 null.
	 <p>
	 For each {@link ControlPropertySpecification} in {@link ControlClassSpecification},
	 if a {@link DefaultValueProvider#getDefaultValue(ControlPropertyLookupConstant)} call returns a null value for the property spec,
	 the {@link SerializableValue} inside the {@link ControlPropertySpecification#getValue()} will be used.
	 <p>
	 All nested classes for the {@link ControlClassSpecification} will then get a new {@link DefaultValueProvider.ControlClassNameContext}
	 with the provided {@link DefaultValueProvider.Context} as a parent context
	 (e.g. {@link DefaultValueProvider.Context#getParentContext()} )

	 @param specification spec to use
	 @param registry registry to use
	 @param context context to use for getting default values, or null to just use the values inside the {@link ControlClassSpecification}
	 */
	public ControlClass(@NotNull ControlClassSpecification specification, @NotNull SpecificationRegistry registry,
						@Nullable DefaultValueProvider.Context context) {
		setClassName(specification.getClassName());
		this.specProvider = specification;

		LinkedList<ControlPropertyLookupConstant> prefetch = new LinkedList<>();

		addProperties(specification.getRequiredControlProperties(), requiredProperties, registry, prefetch);
		addProperties(specification.getOptionalControlProperties(), optionalProperties, registry, prefetch);

		if (context != null) {
			registry.prefetchValues(prefetch, context);
			for (ControlPropertyLookupConstant lookup : prefetch) {
				SerializableValue def = registry.getDefaultValue(lookup);
				if (def == null) {
					continue;
				}
				findProperty(lookup).setValue(def);
			}
			registry.cleanup();
		}

		for (ControlClassSpecification s : specification.getRequiredNestedClasses()) {
			requiredNestedClasses.add(s.constructNewControlClass(registry, new DefaultValueProvider.ControlClassNameContext(context, s.getClassName())));
		}
		for (ControlClassSpecification s : specification.getOptionalNestedClasses()) {
			optionalNestedClasses.add(s.constructNewControlClass(registry, new DefaultValueProvider.ControlClassNameContext(context, s.getClassName())));
		}

		//needs to come before extendControlClass, otherwise we will be attaching listeners to temporary inherited properties
		afterPropertyAndNestedClassConstruction();

		if (specification.getExtendClassName() != null) {
			ControlClass extendMe = registry.findControlClassByName(specification.getExtendClassName());
			if (extendMe == null) {
				throw new IllegalArgumentException("couldn't find ControlClass " + specification.getExtendClassName() + " to extend for " + this.getClassName());
			}
			extendControlClass(extendMe);
		}
	}

	private void afterPropertyAndNestedClassConstruction() {
		for (ControlProperty controlProperty : requiredProperties) {
			controlProperty.getControlPropertyUpdateGroup().addListener(controlPropertyListener);
		}
		for (ControlProperty controlProperty : optionalProperties) {
			controlProperty.getControlPropertyUpdateGroup().addListener(controlPropertyListener);
		}
		for (ControlClass nestedClass : getAllNestedClasses()) {
			nestedClass.controlClassUpdateGroup.addListener((group, data) -> {
				controlClassUpdateGroup.update(new ControlClassNestedClassUpdate(this, nestedClass, data));
			});
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
	}

	private void addProperties(@NotNull Iterable<ControlPropertySpecification> props, @NotNull List<ControlProperty> addTo,
							   @NotNull SpecificationRegistry registry, @NotNull LinkedList<ControlPropertyLookupConstant> prefetch) {
		for (ControlPropertySpecification property : props) {
			ControlProperty p = property.constructNewControlProperty(registry);
			addTo.add(p);
			prefetch.add(p.getPropertyLookup());
		}
	}

	private void addProperties(@NotNull List<ControlProperty> propertiesList,
							   @NotNull List<ControlPropertyLookupConstant> props,
							   @NotNull SpecificationRegistry registry) {
		for (ControlPropertyLookupConstant lookup : props) {
			propertiesList.add(lookup.newEmptyProperty(registry));
		}
	}

	private void addNestedClasses(@NotNull List<ControlClass> nestedClasses, @NotNull SpecificationRegistry registry,
								  @NotNull List<ControlClassSpecification> nestedClassesSpecs,
								  @Nullable DefaultValueProvider.Context context) {
		for (ControlClassSpecification nestedClass : nestedClassesSpecs) {
			nestedClasses.add(new ControlClass(nestedClass, registry, new DefaultValueProvider.ControlClassNameContext(context, nestedClass.getClassName())));
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
	 Check if the given class will create an inheritance loop if this ControlClass were to extend it.
	 The given class may already be in the current inheritance tree,
	 but as long as there is not a cyclic dependence/inheritance, this method will return false.
	 If <code>other=={@link #getExtendClass()}</code>, then will return false.

	 @param other other class
	 @return true if there would be a loop if this extended other, false if there wouldn't be a loop
	 @see #extendControlClass(ControlClass)
	 */
	public boolean hasInheritanceLoop(@NotNull ControlClass other) {
		if (other == getExtendClass()) {
			return false;
		}
		if (this.getClassName().equals(other.getClassName())) {
			return true;
		}
		if (getExtendClass() == null) {
			return false;
		}
		if (other.getExtendClass() == null) {
			return false;
		}
		//Check for loops, like its a graph.

		//If we can get from this to other, there would be a loop if we extend other.
		//Therefore, we should return true (because there would be a loop)
		LinkedList<ControlClass> toVisit = new LinkedList<>();
		HashSet<String> visited = new HashSet<>();
		toVisit.add(this.getExtendClass());
		while (!toVisit.isEmpty()) {
			ControlClass visit = toVisit.pop();
			if (visit.getClassName().equals(other.getClassName())) {
				return true;
			}
			visited.add(visit.getClassName());

			ControlClass extendClass = visit.getExtendClass();
			if (extendClass == null || visited.contains(extendClass.getClassName())) {
				continue;
			}
			toVisit.add(extendClass);
		}
		return false;
	}

	/**
	 Extend the given {@link ControlClass}, or clear the extend class with null.
	 <p>
	 For each property in this {@link ControlClass}: if it initially has a value in it
	 ({@link #propertyIsDefined(ControlProperty)}), it will not be inherited, otherwise it will be by this method.
	 You can explicitly invoke {@link #inheritProperty(ControlPropertyLookupConstant)} and
	 {@link #overrideProperty(ControlPropertyLookupConstant)} at a later time.
	 <p>
	 Any of {@link ControlClass#getAllNestedClasses()} that don't exist in this class will be inherited.
	 Use the method {@link #getTempNestedClassesReadOnly()} to get such nested classes. All inherited nested classes
	 will be appended to {@link #getOptionalNestedClasses()} as well. The inherited nested classes will <b>not</b> be
	 deep copied.

	 @param extendMe class to extend
	 @throws IllegalArgumentException if there is an inheritance loop ({@link #hasInheritanceLoop(ControlClass)})
	 @see ControlProperty#inherit(ControlProperty)
	 */
	public final void extendControlClass(@Nullable ControlClass extendMe) {
		ControlClass oldExtendClass = getExtendClass();
		if (oldExtendClass == extendMe) {
			return;
		}

		Iterable<ControlProperty> oldInherits = getInheritedProperties();

		if (extendMe != null) {
			if (hasInheritanceLoop(extendMe)) {
				throw new IllegalArgumentException("Extend class can't extend itself (inheritance loop)!");
			}
		}
		updatingExtendClass = true;

		if (extendMe != null) {

			//attempt to inherit all properties, if they aren't defined in this ControlClass
			for (ControlProperty checkToInherit : extendMe.getAllChildProperties()) {
				if (this.propertyIsOverridden(checkToInherit.getPropertyLookup())) {
					continue;
				}
				this.inheritProperty(checkToInherit.getPropertyLookup(), extendMe);
			}

			for (ControlClass nested : extendMe.getAllNestedClasses()) {
				ControlClass nestedMatch = findNestedClassNullable(nested.getClassName());
				if (nestedMatch == null) {
					optionalNestedClasses.add(nested);
					tempNestedClasses.add(nested);
					getControlClassUpdateGroup().update(
							new ControlClassTemporaryNestedClassUpdate(this, nested, true)
					);
				} else {
					//do nothing if the nested class already exists (tested this Aug 5, 2017)
				}
			}

			extendMe.getControlClassUpdateGroup().addListener(controlClassUpdateExtendListener);
			extendMe.mySubClasses.add(this);
		} else {
			//remove all temp properties
			for (ControlProperty tempProperty : tempProperties) {
				controlClassUpdateGroup.update(new ControlClassTemporaryPropertyUpdate(this, tempProperty, false));
			}
			tempProperties.clear();

			//remove all temp nested classes
			for (ControlClass tempNested : tempNestedClasses) {
				optionalNestedClasses.remove(tempNested);
				controlClassUpdateGroup.update(new ControlClassTemporaryNestedClassUpdate(this, tempNested, false));
			}
			tempNestedClasses.clear();


			//tell all sub classes that this class isn't extending anything
			for (ControlClass subClass : mySubClasses) {
				for (ControlProperty inheritedProperty : subClass.getInheritedProperties()) {
					boolean matched = false;
					for (ControlProperty myProperty : getAllChildProperties()) {
						if (inheritedProperty.getInherited() == myProperty) {
							//If the property exists in this control class, nothing needs to be done.
							//If the property was inherited by this control class's extend class, it needs to be removed.
							matched = true;
							break;
						}
					}
					if (!matched) {
						subClass.overrideProperty(inheritedProperty.getPropertyLookup());
					}
				}
				for (ControlClass tempNested : subClass.tempNestedClasses) {
					boolean matched = false;
					for (ControlClass myNestedClass : getAllNestedClasses()) {
						if (tempNested == myNestedClass) {
							//was inherited from this control class
							matched = true;
							break;
						}
					}
					if (matched) {
						continue;
					}
					//If reached this point, the temp nested class was the result of this control class's extend class
					//so it needs to be removed.
					subClass.tempNestedClasses.remove(tempNested);
					subClass.optionalNestedClasses.remove(tempNested);
					subClass.controlClassUpdateGroup.update(new ControlClassTemporaryNestedClassUpdate(subClass, tempNested, false));
				}

			}

			//clear all inheritance
			for (ControlProperty property : this.getAllChildProperties()) {
				this.overrideProperty(property.getPropertyLookup());
			}

			for (ControlClass nested : getAllNestedClasses()) {
				nested.extendControlClass(null);
			}

			oldExtendClass.getControlClassUpdateGroup().removeListener(controlClassUpdateExtendListener);
			oldExtendClass.mySubClasses.remove(this);

		}

		extendClassObserver.updateValue(extendMe);
		controlClassUpdateGroup.update(
				new ControlClassExtendUpdate(this, oldExtendClass, extendMe, oldInherits)
		);

		updatingExtendClass = false;

	}

	@Deprecated
	public final boolean inheritNestedClass(@NotNull String nestedClassName) {
		//todo finish this method
		if (getExtendClass() == null) {
			return false;
		}

		ControlClass parentNestedClass = getExtendClass().findNestedClassNullable(nestedClassName);
		if (parentNestedClass == null) {
			return false;
		}

		ControlClass mine = null;
		for (ControlClass inherited : getAllNestedClasses()) {
			if (inherited.getClassName().equals(nestedClassName)) {
				mine = inherited;
				break;
			}
		}

		if (mine != null && inheritedNestedClasses.contains(mine)) {
			//already inherited
			return true;
		}

		if (mine == null) {
			//should have already been inherited with extendControlClass()
			noClassMatch(nestedClassName, "nested classes");
		}

		//todo copy properties and nested classes from parentNestedClass

		final ControlClass finalMine = mine;
		UpdateGroupListener<ControlClassUpdate> inheritNestedClassListener = (group, data) -> {
			finalMine.update(data, true);
		};

		inheritNestedClassListenerMap.put(nestedClassName, inheritNestedClassListener);
		parentNestedClass.controlClassUpdateGroup.addListener(inheritNestedClassListener);
		//todo: for the class update, make sure there is an undo/redo action for it

		inheritedNestedClasses.add(mine);
		controlClassUpdateGroup.update(new ControlClassInheritNestedClassUpdate(this, mine, true));

		return true;
	}

	@Deprecated
	public final void overrideNestedClass(@NotNull String nestedClassName) {
		//todo finish this method
		if (getExtendClass() == null) {
			return;
		}

		ControlClass mine = null;
		for (ControlClass inherited : inheritedNestedClasses) {
			if (inherited.getClassName().equals(nestedClassName)) {
				mine = inherited;
				break;
			}
		}
		if (mine == null) {
			noClassMatch(nestedClassName, "inherited nested classes");
			return;
		}

		ControlClass parentNestedClass = getExtendClass().findNestedClassNullable(nestedClassName);
		if (parentNestedClass == null) {
			return;
		}

		if (tempNestedClasses.contains(mine)) {
			// no overriding inherited temp nested classes
			return;
		} else {
			UpdateGroupListener<ControlClassUpdate> listener = inheritNestedClassListenerMap.get(nestedClassName);
			if (listener == null) {
				throw new IllegalStateException("listener was null");
			}
			parentNestedClass.controlClassUpdateGroup.removeListener(listener);
		}

		inheritedNestedClasses.remove(mine);
		controlClassUpdateGroup.update(new ControlClassInheritNestedClassUpdate(this, mine, false));
	}

	/**
	 @return the {@link ControlClass} that this instance is extending, or null if extending nothing
	 @see #extendControlClass(ControlClass)
	 */
	@Nullable
	public final ControlClass getExtendClass() {
		return extendClassObserver.getValue();
	}

	/** @return a {@link ReadOnlyValueObserver} for {@link #getExtendClass()} */
	public final ReadOnlyValueObserver<ControlClass> getExtendClassReadOnlyObserver() {
		return extendClassObserver.getReadOnlyValueObserver();
	}

	/**
	 Get the instance of this provider.
	 It is best to not return a new instance each time and store the instance for later use.

	 @return the spec provider
	 */
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

	/**
	 Return a concatenation of {@link #getRequiredNestedClasses()} and
	 {@link #getOptionalNestedClasses()} in an iterator
	 */
	@NotNull
	public final Iterable<ControlClass> getAllNestedClasses() {
		ArrayList<List<ControlClass>> merge = new ArrayList<>(2);
		merge.add(requiredNestedClassesReadOnly);
		merge.add(optionalNestedClassesReadOnly);
		return new ListMergeIterator<>(false, merge);
	}

	@NotNull
	public final Iterable<ControlClass> getAllInheritedNestedClasses() {
		return inheritedNestedClassesReadOnly;
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
	 Get the control property instance for the given lookup item.
	 The search will be done inside the {@link ControlClass#getRequiredProperties()} return value.

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
	 Get the control property instance for the given lookup item.
	 The search will be done inside the {@link ControlClass#getRequiredProperties()} return value.

	 @return the ControlProperty instance, or null if couldn't be matched
	 */
	@Nullable
	public final ControlProperty findRequiredPropertyNullable(@NotNull ControlPropertyLookupConstant lookup) {
		return findPropertyFromList(lookup, getRequiredProperties());
	}

	/**
	 Get the control property instance for the given lookup item.
	 The search will be done inside the {@link ControlClass#getOptionalProperties()} return value.

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
	 Get the control property instance for the given lookup item.
	 The search will be done inside the {@link ControlClass#getOptionalProperties()} return value.

	 @return the ControlProperty instance, or null if couldn't be matched
	 @see #findOptionalProperty(ControlPropertyLookupConstant)
	 */
	@Nullable
	public final ControlProperty findOptionalPropertyNullable(@NotNull ControlPropertyLookupConstant lookup) {
		return findPropertyFromList(lookup, getOptionalProperties());
	}

	/**
	 Get the control property instance for the given lookup item.
	 The search will be done inside {@link #getOptionalProperties()} and {@link #getRequiredProperties()}.

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
	 Get the control property instance for the given lookup item.
	 The search will be done inside {@link #getOptionalProperties()} and {@link #getRequiredProperties()}.

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

	/**
	 Get the control property instance by name ({@link ControlProperty#getName()}).
	 The search will be done inside {@link #getOptionalProperties()}
	 There should be no duplicate names across all {@link ControlProperty} instances.

	 @return the ControlProperty instance, or null if couldn't be found
	 @see #findProperty(ControlPropertyLookupConstant)
	 */
	@Nullable
	public final ControlProperty findOptionalPropertyByNameNullable(@NotNull String propertyName) {
		return findPropertyByNameFromList(propertyName, optionalProperties);
	}

	/**
	 Get the control property instance by name ({@link ControlProperty#getName()}).
	 The search will be done inside {@link #getRequiredProperties()}.

	 @return the ControlProperty instance, or null if couldn't be found
	 @see #findProperty(ControlPropertyLookupConstant)
	 */
	@Nullable
	public final ControlProperty findRequiredPropertyByNameNullable(@NotNull String propertyName) {
		return findPropertyByNameFromList(propertyName, requiredProperties);
	}

	/**
	 Get the control property instance by name ({@link ControlProperty#getName()}).
	 The search will be done inside {@link #getOptionalProperties()} and {@link #getRequiredProperties()}.<br>
	 There should be no duplicate names across all {@link ControlProperty} instances.

	 @return the ControlProperty instance, or null if couldn't be found
	 @see #findProperty(ControlPropertyLookupConstant)
	 */
	@Nullable
	public final ControlProperty findPropertyByNameNullable(@NotNull String propertyName) {
		ControlProperty c = findPropertyByNameFromList(propertyName, requiredProperties);
		if (c != null) {
			return c;
		}
		return findPropertyByNameFromList(propertyName, optionalProperties);
	}

	@Nullable
	private ControlProperty findPropertyByNameFromList(@NotNull String propertyName, @NotNull Iterable<ControlProperty> iterable) {
		for (ControlProperty controlProperty : iterable) {
			if (controlProperty.getName().equals(propertyName)) {
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
		noClassMatch(className, "control class" + getClassName());
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
	 Override's a property that may exist inside {@link #getExtendClass()}. When a property is "overridden",
	 the property's value will never be inherited until it is no longer overridden
	 (achievable with {@link #inheritProperty(ControlPropertyLookupConstant)}). If the property is overridden but
	 doesn't have a value, it will be automatically inherited by {@link #extendControlClass(ControlClass)} invocations
	 or any other inheritance related updates.
	 <p>
	 If the given property is a temporary property (doesn't actually exist in the {@link ControlClass},
	 but does in {@link #extendControlClass(ControlClass)} and was inherited), the temporary property
	 will be deleted and a new {@link ControlClassTemporaryPropertyUpdate} will be created. The property will
	 also remain in {@link #getOptionalProperties()}.

	 @throws IllegalArgumentException when the property doesn't exist in this {@link ControlClass}
	 @see #propertyIsOverridden(ControlPropertyLookupConstant)
	 @see ControlProperty#inherit(ControlProperty)
	 @see #inheritProperty(ControlPropertyLookupConstant)
	 */
	public final void overrideProperty(@NotNull ControlPropertyLookupConstant property) {
		ControlProperty mine = findPropertyByNameNullable(property.getPropertyName());
		if (mine == null) {
			noPropertyMatch(property, "all properties");
		}
		mine.inherit(null);

		//check if was temporary. if was, remove it and broadcast update
		int index = tempProperties.indexOf(mine);
		if (index < 0) {
			return;
		}
		tempProperties.remove(index);
		controlClassUpdateGroup.update(new ControlClassTemporaryPropertyUpdate(this, mine, false));
	}

	/**
	 <ul>
	 <li>
	 If {@link #getExtendClass()}!=null, the matched {@link ControlProperty} will be set to
	 a matched inherited property by using the {@link ControlProperty#inherit(ControlProperty)} method.
	 </li>
	 <li>If {@link #getExtendClass()} is null, nothing will happen.</li>
	 <li>If the lookup isn't found in this {@link ControlClass} and {@link #getExtendClass()} is null, nothing will happen.</li>
	 <li>
	 If the lookup isn't found in this {@link ControlClass} but is inside {@link #getExtendClass()},
	 the {@link ControlProperty} will be inserted into {@link #getOptionalProperties()} and an
	 {@link ControlClassTemporaryPropertyUpdate} will be created for {@link #getControlClassUpdateGroup()}.
	 </li>
	 <li>
	 If the lookup is found in this {@link ControlClass} but isn't inside {@link #getExtendClass()},
	 nothing will happen.
	 </li>
	 </ul>
	 If at any point, one of the above conditions changes because {@link #extendControlClass(ControlClass)} is
	 invoked, the properties should react accordingly due to {@link ControlClass#getControlClassUpdateGroup()}
	 listeners.

	 @return true if the property was inherited, false if nothing happened because of a reason described above
	 @see #propertyIsOverridden(ControlPropertyLookupConstant)
	 @see #overrideProperty(ControlPropertyLookupConstant)
	 */
	public final boolean inheritProperty(@NotNull ControlPropertyLookupConstant lookup) {
		if (getExtendClass() == null) {
			return false;
		}
		return inheritProperty(lookup, getExtendClass());
	}

	private boolean inheritProperty(@NotNull ControlPropertyLookupConstant lookup, @NotNull ControlClass extendClass) {
		ControlProperty inherit = extendClass.findPropertyByNameNullable(lookup.getPropertyName());
		if (inherit == null) {
			return false;
		}

		ControlProperty mine = findPropertyByNameNullable(lookup.getPropertyName());

		if (mine == null) {
			//create a temporary property for this class
			mine = lookup.newEmptyProperty(null);
			controlClassUpdateGroup.update(new ControlClassTemporaryPropertyUpdate(this, mine, true));
			tempProperties.add(mine);
			optionalProperties.add(mine);
		}
		mine.inherit(inherit);

		return true;
	}

	/**
	 In order for a property to be overridden, the property exists in this {@link ControlClass},
	 the property is defined ({@link #propertyIsDefined(ControlProperty)}), and the property isn't
	 inherited ({@link ControlProperty#isInherited()})

	 @return true if the given property lookup is overridden, false if it isn't.
	 */
	public final boolean propertyIsOverridden(@NotNull ControlPropertyLookupConstant lookup) {
		ControlProperty found = findPropertyByNameNullable(lookup.getPropertyName());
		return found != null && propertyIsOverridden(found);
	}

	/**
	 In order for a property to be overridden,
	 the property is not inherited ({@link ControlProperty#isInherited()}==false)
	 and the property is defined ({@link #propertyIsDefined(ControlProperty)})

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
	 @return all properties that are defined (excluding inherited properties that are defined).
	 To check if property is defined, the method {@link #propertyIsDefined(ControlProperty)} will
	 be used.
	 */
	@NotNull
	public final Iterable<ControlProperty> getDefinedProperties() {
		List<ControlProperty> properties = new LinkedList<>();
		for (ControlProperty property : getAllChildProperties()) {
			if (propertyIsDefined(property)) {
				properties.add(property);
			}
		}
		return properties;
	}

	/**
	 @return all {@link ControlProperty} instances (concatenation of {@link #getRequiredProperties()})
	 and {@link #getOptionalProperties()} in an iterator
	 */
	public final Iterable<ControlProperty> getAllChildProperties() {
		ArrayList<ReadOnlyList<ControlProperty>> merge = new ArrayList<>(2);
		merge.add(getRequiredProperties());
		merge.add(getOptionalProperties());
		return new ListMergeIterator<>(false, merge);
	}


	/**
	 Will return all properties from {@link #getInheritedProperties()} that
	 have defined values ({@link #propertyIsDefined(ControlProperty)})
	 */
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


	/**
	 @return an iterable of {@link ControlProperty} that are inherited from {@link #getExtendClass()}
	 */
	@NotNull
	public final Iterable<ControlProperty> getInheritedProperties() {
		if (getExtendClass() == null) {
			return Collections.emptyList();
		}
		List<ControlProperty> list = new LinkedList<>();
		for (ControlProperty c : getAllChildProperties()) {
			if (c.isInherited()) {
				list.add(c);
			}
		}
		return list;
	}

	/**
	 @return an iterable of all optional {@link ControlProperty} instances inside
	 {@link #getOptionalProperties()} excluding ones that are event {@link ControlProperty}
	 @see #getEventProperties()
	 */
	@NotNull
	public final Iterable<ControlProperty> getOptionalPropertiesWithoutEvents() {
		LinkedList<ControlProperty> noEvents = new LinkedList<>();
		for (ControlProperty property : getOptionalProperties()) {
			if (ControlPropertyEventLookup.getEventProperty(property.getPropertyLookup()) != null) {
				continue;
			}
			noEvents.add(property);
		}
		return noEvents;
	}

	/**
	 To determine if a {@link ControlProperty} is an event or not, you check its
	 {@link ControlProperty#getPropertyLookup()}. If that lookup results in a non null match from
	 {@link ControlPropertyEventLookup#getEventProperty(ControlPropertyLookupConstant)}, then it is an event property.

	 @return all event properties
	 */
	@NotNull
	public final Iterable<ControlProperty> getEventProperties() {
		final List<ControlProperty> eventProperties = new LinkedList<>();
		for (ControlProperty property : getAllChildProperties()) {
			if (ControlPropertyEventLookup.getEventProperty(property.getPropertyLookup()) != null) {
				eventProperties.add(property);
			}
		}

		return eventProperties;
	}

	/**
	 To listen for when temporary properties are added/removed, check for {@link ControlClassTemporaryPropertyUpdate}
	 in {@link #getControlClassUpdateGroup()}

	 @return all {@link ControlProperty} instances that exist only because of inheritance.
	 @see #inheritProperty(ControlPropertyLookupConstant)
	 */
	@NotNull
	public final ReadOnlyList<ControlProperty> getTempPropertiesReadOnly() {
		return tempPropertiesReadOnly;
	}

	/**
	 To listen for when temporary nested classes are added/removed, check for
	 {@link ControlClassTemporaryNestedClassUpdate}
	 in {@link #getControlClassUpdateGroup()}

	 @return all {@link ControlClass} instances that exist only because of inheritance.
	 */
	@NotNull
	public final ReadOnlyList<ControlClass> getTempNestedClassesReadOnly() {
		return tempNestedClassesReadOnly;
	}


	/**
	 Gets the update listener group that listens to all {@link ControlProperty} instances.
	 Instead of adding listeners to all {@link ControlProperty}'s potentially hundreds of times scattered
	 across the program, the ControlClass listens to it's own ControlProperties.
	 Any time any of the ControlProperty's receive an update, the value inside the listener will be the
	 {@link ControlProperty} that was updated as well as the property's old value and the updated/new value.
	 If this ControlClass extends some ControlClass via
	 {@link #extendControlClass(ControlClass)}, the update groups will <b>not</b> be synced.
	 You will have to listen to each ControlClass separately.
	 */
	@NotNull
	public final UpdateListenerGroup<ControlPropertyUpdate> getPropertyUpdateGroup() {
		return propertyUpdateGroup;
	}

	/**
	 Gets the update listener group that listens to when an update happens to this {@link ControlClass}.
	 Things that may trigger the update:<br>
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
	 <li>each of this class's properties has a match with each of {@code controlClass}'s properties
	 ({@link #getAllChildProperties()}) and the classes have the same count of properties</li>
	 <li>each of this class's nested classes has a match with each of {@code controlClass}'s nested classes
	 (recursive check with this method) and the nested class counts are equal</li>
	 <li>The required/optional nested classes/properties do not need to be required/optional to match. The properties
	 and nested classes just need to exist. </li>
	 <li>Only the {@link ControlProperty#getName()}s are used to check equivalence</li>
	 </ul>

	 @param controlClass class to check if equals
	 @return true if the criteria are met, false otherwise
	 */
	public boolean classEquals(@NotNull ControlClass controlClass) {
		boolean name = this.getClassName().equals(controlClass.getClassName());
		if (!name) {
			return false;
		}

		{
			int mySize = this.optionalProperties.size() + this.requiredProperties.size();
			int otherSize = controlClass.optionalProperties.size() + controlClass.requiredProperties.size();
			if (mySize != otherSize) {
				return false;
			}
		}

		for (ControlProperty p : getAllChildProperties()) {
			if (controlClass.findPropertyByNameNullable(p.getName()) == null) {
				return false;
			}
		}

		{
			int mySize = this.optionalNestedClasses.size() + this.requiredNestedClasses.size();
			int otherSize = controlClass.optionalNestedClasses.size() + controlClass.requiredNestedClasses.size();
			if (mySize != otherSize) {
				return false;
			}
		}

		for (ControlClass myNested : getAllNestedClasses()) {
			ControlClass other = controlClass.findNestedClassNullable(myNested.getClassName());
			if (other == null) {
				return false;
			}
			if (!myNested.classEquals(other)) {
				return false;
			}
		}

		return true;
	}

	/**
	 Sets this {@link ControlClass} fully equal to <code>controlClass</code> (disregards {@link #getUserData()}).
	 NOTE: nothing will be deep copied!

	 @param controlClass class to use
	 */
	public final void setTo(@NotNull ControlClass controlClass) {
		setClassName(controlClass.getClassName());

		for (ControlProperty property : controlClass.getAllChildProperties()) {
			ControlProperty m = findPropertyNullable(property.getPropertyLookup());
			if (m == null) {
				optionalProperties.add(property);
			} else {
				m.setTo(property);
			}
		}
		for (ControlClass nested : controlClass.getAllNestedClasses()) {
			ControlClass m = findNestedClassNullable(nested.getClassName());
			if (m == null) {
				optionalNestedClasses.add(nested);
			} else {
				m.setTo(nested);
			}
		}

		extendControlClass(controlClass.getExtendClass());

		for (ControlProperty override : controlClass.getOverriddenProperties()) {
			overrideProperty(override.getPropertyLookup());
		}

		for (ControlProperty inherit : controlClass.getInheritedProperties()) {
			inheritProperty(inherit.getPropertyLookup());
		}
	}

	/**
	 Check if the given property is defined. A property is defined if {@link ControlProperty#getValue()} != null

	 @param property property to check
	 @return true if the property is defined, false otherwise
	 */
	public static boolean propertyIsDefined(@NotNull ControlProperty property) {
		return property.getValue() != null;
	}

	@Override
	public String toString() {
		return getClassName();
	}

	/** @return a {@link DataContext} instance that stores random things. */
	@NotNull
	public DataContext getUserData() {
		return userData;
	}

	/** Will set all {@link ControlClass} instances that have this as their {@link #getExtendClass()} and set it to null */
	public void clearSubClasses() {
		List<ControlClass> unextend = new ArrayList<>(mySubClasses.size());
		unextend.addAll(mySubClasses);
		for (ControlClass cc : unextend) {
			cc.extendControlClass(null); //todo this doesn't work
		}
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
			return;
		} else if (data instanceof ControlClassPropertyUpdate) {
			ControlPropertyUpdate propertyUpdate = ((ControlClassPropertyUpdate) data).getPropertyUpdate();
			try {
				findProperty(propertyUpdate.getControlProperty().getPropertyLookup()).update(propertyUpdate, deepCopy);
			} catch (IllegalArgumentException ignore) {

			}
			return;
		} else if (data instanceof ControlClassInheritPropertyUpdate) {
			ControlClassInheritPropertyUpdate update = (ControlClassInheritPropertyUpdate) data;
			if (update.wasOverridden()) {
				overrideProperty(update.getControlProperty().getPropertyLookup());
			} else {
				inheritProperty(update.getControlProperty().getPropertyLookup());
			}
			return;
		} else if (data instanceof ControlClassExtendUpdate) {
			ControlClassExtendUpdate update = (ControlClassExtendUpdate) data;
			extendControlClass(update.getNewExtendClass());
			return;
		} else if (data instanceof ControlClassTemporaryPropertyUpdate) {
			//This does not need to have anything handled since it happens inside overrideProperty() or inheritProperty()
			//which will create their respective updates.
			return;
		} else if (data instanceof ControlClassNestedClassUpdate) {
			ControlClassNestedClassUpdate update = (ControlClassNestedClassUpdate) data;
			ControlClass nestedClassToUpdate;
			ControlClass myNestedClass;
			ControlClass parentClass = this;
			do {
				nestedClassToUpdate = update.getNested();
				myNestedClass = parentClass.findNestedClassNullable(nestedClassToUpdate.getClassName());
				if (myNestedClass == null) {
					return;
				}
				parentClass = nestedClassToUpdate;
				if (update.getNestedClassUpdate() instanceof ControlClassNestedClassUpdate) {
					update = (ControlClassNestedClassUpdate) update.getNestedClassUpdate();
				} else {
					break;
				}
			} while (true);
			myNestedClass.update(data, deepCopy);
		} else if (data instanceof ControlClassInheritNestedClassUpdate) {
			ControlClassInheritNestedClassUpdate update = (ControlClassInheritNestedClassUpdate) data;
			if (update.isInherited()) {
				inheritNestedClass(update.getNested().getClassName());
			} else {
				overrideNestedClass(update.getNested().getClassName());
			}
		}
	}

	@Override
	public int hashCode() {
		return getClassName().hashCode();
	}
}
