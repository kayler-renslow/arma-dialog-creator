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

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A custom control class has two things: a specification and a implementation. The specification, provided by a {@link ControlClassSpecification} instance, is a way to construct the implementation,
 which is a {@link ControlClass} instance. There will only be one {@link ControlClass} instance per {@link CustomControlClass} instance and the two will be synchronized (when an update happens via
 {@link ControlClass#getPropertyUpdateGroup()}, the {@link ControlClassSpecification} will update as well). This synchronization will only happen between this class instance and {@link #getControlClass()}.
 However, editing the {@link ControlClassSpecification} will not change the {@link ControlClass} implementation.

 @author Kayler
 @since 11/13/2016 */
public class CustomControlClass {
	private final ControlClassSpecification specification;
	private final SpecificationRegistry registry;
	private ControlClass controlClass;
	private final DataContext programData = new DataContext();
	private String comment;


	/**
	 Construct a new custom control class with the given {@link ControlClass} instance. The given instance will be the underlying instance for {@link #getControlClass()} and a new
	 {@link ControlClassSpecification} will be created with the instance via {@link ControlClassSpecification(ControlClass)}.

	 @param controlClass instance to use
	 */
	public CustomControlClass(@NotNull ControlClass controlClass, @NotNull SpecificationRegistry registry) {
		this(new ControlClassSpecification(controlClass), registry);
		this.controlClass = controlClass;
		loadControlClassListeners();
	}

	/**
	 Construct a new custom control class with the given specification

	 @param specification specification to use
	 */
	public CustomControlClass(@NotNull ControlClassSpecification specification, @NotNull SpecificationRegistry registry) {
		this.specification = specification;
		this.registry = registry;
	}

	/** Get the {@link ControlClass} instance. This instance will remain constant. */
	@NotNull
	public ControlClass getControlClass() {
		if (controlClass == null) {
			controlClass = specification.constructNewControlClass(registry);
			loadControlClassListeners();
		}
		return controlClass;
	}

	private void loadControlClassListeners() {
		controlClass.getControlClassUpdateGroup().addListener(new UpdateGroupListener<ControlClassUpdate>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
				if (data instanceof ControlClassRenameUpdate) {
					ControlClassRenameUpdate update = (ControlClassRenameUpdate) data;
					specification.setClassName(update.getNewName());
				} else if (data instanceof ControlClassPropertyUpdate) {
					update(((ControlClassPropertyUpdate) data).getPropertyUpdate());
				} else if (data instanceof ControlClassOverridePropertyUpdate) {
					ControlClassOverridePropertyUpdate update = (ControlClassOverridePropertyUpdate) data;
					if (update.wasOverridden()) {
						ControlPropertySpecification propertySpecification = specification.findInheritedProperty(update.getOveridden().getPropertyLookup());
						if (propertySpecification != null) {
							specification.getInheritedProperties().remove(propertySpecification);
						}
					} else {
						specification.getInheritedProperties().add(new ControlPropertySpecification(update.getOveridden()));

					}
				} else if (data instanceof ControlClassExtendUpdate) {
					ControlClassExtendUpdate update = (ControlClassExtendUpdate) data;
					if (update.getNewValue() == null) {
						specification.setExtendClass(null);
					} else {
						specification.setExtendClass(update.getNewValue().getClassName());
					}
				} else {
					System.err.println("WARNING: CustomControlClass.loadControlClassListeners(): unknown control class update:" + data);
				}
			}

			private void update(ControlPropertyUpdate data) {
				ControlPropertySpecification propertySpec = specification.findProperty(data.getControlProperty().getPropertyLookup());
				if (data instanceof ControlPropertyValueUpdate) {
					ControlPropertyValueUpdate update = (ControlPropertyValueUpdate) data;
					propertySpec.setValue(update.getNewValue());
				} else if (data instanceof ControlPropertyMacroUpdate) {
					ControlPropertyMacroUpdate update = (ControlPropertyMacroUpdate) data;
					propertySpec.setMacroKey(update.getNewMacro() != null ? update.getNewMacro().getKey() : null);
				} else if (data instanceof ControlPropertyCustomDataUpdate) {
					ControlPropertyCustomDataUpdate update = (ControlPropertyCustomDataUpdate) data;
					propertySpec.setCustomData(update.getNewCustomData());
					propertySpec.setUsingCustomData(update.isUsingCustomData());
				} else if (data instanceof ControlPropertyInheritUpdate) {
					//already handled in control class update
				} else {
					System.err.println("WARNING: CustomControlClass.loadControlClassListeners(): unknown control property update:" + data);
				}
			}
		});

	}

	/** Some information provided by the user about the custom control */
	@Nullable
	public String getComment() {
		return comment;
	}

	/**
	 @param comment information about the custom control
	 */
	public void setComment(@Nullable String comment) {
		this.comment = comment;
	}

	@NotNull
	public ControlClassSpecification getSpecification() {
		return specification;
	}

	@Override
	public int hashCode() {
		return specification.getClassName().hashCode();
	}

	@Override
	public String toString() {
		return specification.getClassName();
	}

	/** Get a {@link DataContext} instance that stores random things. */
	@NotNull
	public DataContext getUserData() {
		return programData;
	}
}
