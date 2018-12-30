package com.armadialogcreator.control;

import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 Provides the specification for a control class that says what sub-classes are required/optional and what properties are required/optional.
 Since the specification shouldn't change, it is best to store the returned values for later use
 @author Kayler
 @since 07/07/2016. */
public interface ControlClassRequirementSpecification {

	/** Trivial implementation of the interface */
	ControlClassRequirementSpecification TRIVIAL_IMPL = new ControlClassRequirementSpecification() {
	};

	/** Get all required nested classes for the control class. Default implementation returns {@link ControlClassSpecification#EMPTY} */
	@NotNull
	default ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
		return ControlClassSpecification.EMPTY;
	}

	/** Get all <b>optional</b> nested  classes for the control class. Default implementation returns {@link ControlClassSpecification#EMPTY} */
	@NotNull
	default ReadOnlyList<ControlClassSpecification> getOptionalNestedClasses() {
		return ControlClassSpecification.EMPTY;
	}

	/** Get all required properties for the control class. Default implementation returns {@link ControlPropertyLookupConstant#EMPTY} */
	@NotNull
	default ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
		return ControlPropertyLookupConstant.EMPTY;
	}

	/** Get all <b>optional</b> properties for the control class. Default implementation returns {@link ControlPropertyLookupConstant#EMPTY} */
	@NotNull
	default ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
		return ControlPropertyLookupConstant.EMPTY;
	}
}
