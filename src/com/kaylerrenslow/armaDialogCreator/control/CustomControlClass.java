package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A custom control class has two things: a specification and a implementation.
 The specification, provided by a {@link ControlClassSpecification} instance, is a way to construct the implementation,
 which is a {@link ControlClass} instance.
 There will only be one {@link ControlClass} instance per {@link CustomControlClass} instance and
 the {@link ControlClass} changes will be tracked (when an update happens via
 {@link ControlClass#getPropertyUpdateGroup()}, the {@link ControlClassSpecification} will update as well).
 This update tracking will only happen between this class instance and {@link #getControlClass()}.
 Editing the {@link ControlClassSpecification} will not change the {@link ControlClass} implementation.

 @author Kayler
 @since 11/13/2016 */
public class CustomControlClass {
	public enum Scope {
		Project, Workspace
	}

	private final ControlClass controlClass;
	private final DataContext programData = new DataContext();
	private String comment;
	private Scope scope;


	/**
	 Construct a new custom control class with the given {@link ControlClass} instance.
	 The given instance will be the underlying instance for {@link #getControlClass()} and a new
	 {@link ControlClassSpecification} will be created with the instance via {@link ControlClassSpecification(ControlClass)}.

	 @param controlClass instance to use
	 @param scope the scope of this {@link CustomControlClass}
	 */
	public CustomControlClass(@NotNull ControlClass controlClass, @NotNull Scope scope) {
		this.controlClass = controlClass;
		this.scope = scope;
	}

	/**
	 Construct a new custom control class with the given specification

	 @param specification specification to use
	 @param registry the {@link SpecificationRegistry} used to construct a new {@link ControlClass} for {@link #getControlClass()}
	 @param scope the scope of this {@link CustomControlClass}
	 */
	public CustomControlClass(@NotNull ControlClassSpecification specification,
							  @NotNull SpecificationRegistry registry,
							  @NotNull Scope scope) {
		controlClass = specification.constructNewControlClass(registry);
		setComment(specification.getComment());
		this.scope = scope;
	}

	/** @return the {@link ControlClass} instance. This instance will remain constant. */
	@NotNull
	public ControlClass getControlClass() {
		return controlClass;
	}

	/** Some information provided by the user about the custom control */
	@Nullable
	public String getComment() {
		return comment;
	}

	@NotNull
	public Scope getScope() {
		return scope;
	}

	public void setScope(@NotNull Scope scope) {
		this.scope = scope;
	}

	/**
	 @param comment information about the custom control
	 */
	public void setComment(@Nullable String comment) {
		this.comment = comment;
	}

	/** @return a new instance of {@link ControlClassSpecification} equal to {@link #getControlClass()} */
	@NotNull
	public ControlClassSpecification newSpecification() {
		return new ControlClassSpecification(this.controlClass);
	}

	@Override
	public int hashCode() {
		return controlClass.getClassName().hashCode();
	}

	@Override
	public String toString() {
		return controlClass.getClassName();
	}

	/** Get a {@link DataContext} instance that stores random things. */
	@NotNull
	public DataContext getUserData() {
		return programData;
	}
}
