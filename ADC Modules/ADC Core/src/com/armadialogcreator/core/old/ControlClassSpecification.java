package com.armadialogcreator.core.old;

import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 @see CustomControlClass
 @since 11/12/2016. */
public class ControlClassSpecification implements ControlClassRequirementSpecification {
	public static final ReadOnlyList<ControlClassSpecification> EMPTY = new ReadOnlyList<>(Collections.emptyList());

	private String controlClassName;
	private final ReadOnlyList<ControlPropertySpecification> requiredProperties;
	private final ReadOnlyList<ControlPropertySpecification> optionalProperties;
	private final ReadOnlyList<ControlClassSpecification> requiredNestedClasses;
	private final ReadOnlyList<ControlClassSpecification> optionalNestedClasses;
	private final ReadOnlyList<ControlPropertyLookupConstant> requiredPropertiesLookup, optionalPropertiesLookup;
	private final List<ControlPropertyLookupConstant> inheritedProperties = new LinkedList<>();
	private @Nullable String extendClass;
	private String comment;

	/**
	 Construct a specification with the given lists. Note: the lists will not be deep copied.

	 @param controlClassName class name
	 @param requiredProperties required properties
	 @param optionalProperties optional properties
	 @param requiredNestedClasses required nested classes
	 @param optionalNestedClasses optional nested classes
	 */
	public ControlClassSpecification(@NotNull String controlClassName,
									 @NotNull List<ControlPropertySpecification> requiredProperties,
									 @NotNull List<ControlPropertySpecification> optionalProperties,
									 @NotNull List<ControlClassSpecification> requiredNestedClasses,
									 @NotNull List<ControlClassSpecification> optionalNestedClasses) {
		this.controlClassName = controlClassName;
		if (requiredProperties instanceof ReadOnlyList) {
			this.requiredProperties = (ReadOnlyList<ControlPropertySpecification>) requiredProperties;
		} else {
			this.requiredProperties = new ReadOnlyList<>(requiredProperties);
		}
		if (optionalProperties instanceof ReadOnlyList) {
			this.optionalProperties = (ReadOnlyList<ControlPropertySpecification>) optionalProperties;
		} else {
			this.optionalProperties = new ReadOnlyList<>(optionalProperties);
		}
		if (requiredNestedClasses instanceof ReadOnlyList) {
			this.requiredNestedClasses = (ReadOnlyList<ControlClassSpecification>) requiredNestedClasses;
		} else {
			this.requiredNestedClasses = new ReadOnlyList<>(requiredNestedClasses);
		}
		if (optionalNestedClasses instanceof ReadOnlyList) {
			this.optionalNestedClasses = (ReadOnlyList<ControlClassSpecification>) optionalNestedClasses;
		} else {
			this.optionalNestedClasses = new ReadOnlyList<>(optionalNestedClasses);
		}

		if (requiredProperties.size() == 0) {
			requiredPropertiesLookup = ControlPropertyLookupConstant.EMPTY;
		} else {
			List<ControlPropertyLookupConstant> required = new LinkedList<>();
			requiredPropertiesLookup = new ReadOnlyList<>(required);

			for (ControlPropertySpecification property : requiredProperties) {
				required.add(property.getPropertyLookup());
			}
		}
		if (optionalProperties.size() == 0) {
			optionalPropertiesLookup = ControlPropertyLookupConstant.EMPTY;
		} else {
			List<ControlPropertyLookupConstant> optional = new LinkedList<>();
			optionalPropertiesLookup = new ReadOnlyList<>(optional);
			for (ControlPropertySpecification property : optionalProperties) {
				optional.add(property.getPropertyLookup());
			}

		}

	}

	/**
	 Constructs a specification with the given properties and assumes there to be no nested classes (will use {@link ControlClassSpecification#EMPTY} as parameters)

	 @param controlClassName class name
	 @param requiredProperties required properties
	 @param optionalProperties optional properties
	 @see #ControlClassSpecification(String, List, List, List, List)
	 */
	public ControlClassSpecification(@NotNull String controlClassName,
									 @NotNull List<ControlPropertySpecification> requiredProperties,
									 @NotNull List<ControlPropertySpecification> optionalProperties) {
		this(controlClassName, requiredProperties, optionalProperties, ControlClassSpecification.EMPTY, ControlClassSpecification.EMPTY);
	}

	/** Create a specification from the given {@link ControlClassOld} and deep copy the class */
	public ControlClassSpecification(@NotNull ControlClassOld controlClass) {
		this(controlClass, true);
	}

	/**
	 Constructs a specification from the given {@link ControlClassOld}.
	 If <code>deepCopy</code> is true, the {@link ControlProperty}'s from {@link ControlClassOld#getRequiredProperties()} and
	 {@link ControlClassOld#getOptionalProperties()} will be deep copied via
	 {@link ControlPropertySpecification#ControlPropertySpecification(ControlProperty, boolean)}. Also,
	 any nested classes would be deep copied as well. If <code>deepCopy</code> is false , will shallow copy the values.

	 @param controlClass class to use
	 @param deepCopy true to deep copy, false otherwise
	 */
	public ControlClassSpecification(@NotNull ControlClassOld controlClass, boolean deepCopy) {
		this.controlClassName = controlClass.getClassName();

		List<ControlPropertySpecification> requiredProperties = new LinkedList<>();
		List<ControlPropertySpecification> optionalProperties = new LinkedList<>();
		this.requiredProperties = new ReadOnlyList<>(requiredProperties);
		this.optionalProperties = new ReadOnlyList<>(optionalProperties);

		if (deepCopy) {
			List<ControlClassSpecification> requiredNested = new LinkedList<>();
			List<ControlClassSpecification> optionalNested = new LinkedList<>();

			this.optionalNestedClasses = new ReadOnlyList<>(optionalNested);
			this.requiredNestedClasses = new ReadOnlyList<>(requiredNested);

			for (ControlClassOld nested : controlClass.getRequiredNestedClasses()) {
				if (controlClass.getTempNestedClassesReadOnly().contains(nested)) {
					continue;
				}
				requiredNested.add(new ControlClassSpecification(nested, true));
			}
			for (ControlClassOld nested : controlClass.getOptionalNestedClasses()) {
				if (controlClass.getTempNestedClassesReadOnly().contains(nested)) {
					continue;
				}
				optionalNested.add(new ControlClassSpecification(nested, true));
			}
		} else {
			this.requiredNestedClasses = controlClass.getSpecProvider().getRequiredNestedClasses();
			this.optionalNestedClasses = controlClass.getSpecProvider().getOptionalNestedClasses();
		}

		for (ControlProperty property : controlClass.getRequiredProperties()) {
			if (controlClass.getTempPropertiesReadOnly().contains(property)) {
				continue;
			}
			requiredProperties.add(new ControlPropertySpecification(property, deepCopy));
		}
		for (ControlProperty property : controlClass.getOptionalProperties()) {
			if (controlClass.getTempPropertiesReadOnly().contains(property)) {
				continue;
			}
			optionalProperties.add(new ControlPropertySpecification(property, deepCopy));
		}
		this.optionalPropertiesLookup = controlClass.getSpecProvider().getOptionalProperties();
		this.requiredPropertiesLookup = controlClass.getSpecProvider().getRequiredProperties();

		if (controlClass.getExtendClass() != null) {
			setExtendClass(controlClass.getExtendClass().getClassName());
		}

		for (ControlProperty inheritedProperty : controlClass.getInheritedProperties()) {
			getInheritedProperties().add(inheritedProperty.getPropertyLookup());
		}
	}

	public void setClassName(@NotNull String className) {
		this.controlClassName = className;
	}

	public void setExtendClass(@Nullable String extendClassName) {
		if (extendClassName != null && extendClassName.trim().length() == 0) {
			this.extendClass = null;
			return;
		}
		this.extendClass = extendClassName;
	}

	@Nullable
	public String getExtendClassName() {
		return extendClass;
	}

	@NotNull
	public String getClassName() {
		return controlClassName;
	}

	@NotNull
	@Override
	public ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
		return requiredNestedClasses;
	}

	@NotNull
	@Override
	public ReadOnlyList<ControlClassSpecification> getOptionalNestedClasses() {
		return optionalNestedClasses;
	}

	@NotNull
	public ReadOnlyList<ControlPropertySpecification> getRequiredControlProperties() {
		return requiredProperties;
	}

	@NotNull
	public ReadOnlyList<ControlPropertySpecification> getOptionalControlProperties() {
		return optionalProperties;
	}

	@NotNull
	@Override
	public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
		return requiredPropertiesLookup;
	}

	@NotNull
	@Override
	public ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
		return optionalPropertiesLookup;
	}

	@NotNull
	public List<ControlPropertyLookupConstant> getInheritedProperties() {
		return inheritedProperties;
	}


	/** Just invokes {@link ControlClassOld#ControlClassOld(ControlClassSpecification, SpecificationRegistry)} with this instance provided */
	@NotNull
	public ControlClassOld constructNewControlClass(@NotNull SpecificationRegistry registry) {
		return new ControlClassOld(this, registry);
	}

	/**
	 Just invokes {@link ControlClassOld#ControlClassOld(ControlClassSpecification, SpecificationRegistry, DefaultValueProvider.Context)}
	 with this instance provided
	 */
	@NotNull
	public ControlClassOld constructNewControlClass(@NotNull SpecificationRegistry registry,
													@Nullable DefaultValueProvider.Context context) {
		return new ControlClassOld(this, registry, context);
	}

	/**
	 Find a {@link ControlClassSpecification} instance between {@link #getRequiredControlProperties()} and
	 {@link #getOptionalControlProperties()}

	 @param lookup the lookup to fetch instance for
	 @return the instance
	 @throws IllegalArgumentException if nothing could be matched
	 @see #findPropertySpecificationNullable(ControlPropertyLookupConstant)
	 */
	@NotNull
	public ControlPropertySpecification findPropertySpecification(@NotNull ControlPropertyLookupConstant lookup) {
		ControlPropertySpecification spec = findPropertySpecificationNullable(lookup);
		if (spec != null) {
			return spec;
		}
		throw new IllegalArgumentException("couldn't find property " + lookup.getPropertyName() + "[" + lookup.getPropertyId() + "]");
	}

	/**
	 Find a {@link ControlClassSpecification} instance between {@link #getRequiredControlProperties()} and
	 {@link #getOptionalControlProperties()}

	 @param lookup the lookup to fetch instance for
	 @return the instance, or null if couldn't be found
	 */
	@Nullable
	public ControlPropertySpecification findPropertySpecificationNullable(@NotNull ControlPropertyLookupConstant lookup) {
		for (ControlPropertySpecification property : getRequiredControlProperties()) {
			if (property.getPropertyLookup() == lookup) {
				return property;
			}
		}
		for (ControlPropertySpecification property : getOptionalControlProperties()) {
			if (property.getPropertyLookup() == lookup) {
				return property;
			}
		}
		return null;
	}

	@Nullable
	public String getComment() {
		return comment;
	}

	public void setComment(@Nullable String comment) {
		this.comment = comment;
	}
}
