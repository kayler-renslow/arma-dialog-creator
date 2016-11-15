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
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 A custom control class has two things: a specification and a implementation. The specification, provided by a {@link ControlClassSpecification} instance, is a way to construct the implementation,
 which is a {@link ControlClass} instance. There will only be one {@link ControlClass} instance per {@link CustomControlClass} instance and the two will be synchronized (when an update happens via
 {@link ControlClass#getUpdateGroup()}, the {@link ControlClassSpecification} will update as well). This synchronization will only happen between this class instance and {@link #getControlClass()}.

 @author Kayler
 @since 11/13/2016 */
public class CustomControlClass {
	private ControlClassSpecification specification;
	private ControlClass controlClass;
	private final DataContext programData = new DataContext();


	/**
	 Construct a new custom control class with the given {@link ControlClass} instance. The given instance will be the underlying instance for {@link #getControlClass()} and a new
	 {@link ControlClassSpecification} will be created with the instance via {@link ControlClassSpecification(ControlClass}.

	@param controlClass instance to use
	 */
	public CustomControlClass(@NotNull ControlClass controlClass) {
		this.specification = new ControlClassSpecification(controlClass);
		this.controlClass = controlClass;
		loadControlClassListeners();
	}

	/**
	 Construct a new custom control class with the given specification

	 @param specification specification to use
	 */
	public CustomControlClass(@NotNull ControlClassSpecification specification) {
		this.specification = specification;
	}

	/** Get the {@link ControlClass} instance. This instance will remain constant. */
	@NotNull
	public ControlClass getControlClass() {
		if (controlClass == null) {
			controlClass = specification.constructNewControlClass();
			loadControlClassListeners();
		}
		return controlClass;
	}

	private void loadControlClassListeners() {
		controlClass.getClassNameObserver().addValueListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
				specification.setClassName(newValue);
			}
		});
		controlClass.getUpdateGroup().addListener(new UpdateListener<ControlPropertyUpdate>() {
			@Override
			public void update(ControlPropertyUpdate data) {
				ControlPropertySpecification propertySpec = specification.findProperty(data.getControlProperty().getPropertyLookup());
				if (data instanceof ControlPropertyValueUpdate) {
					ControlPropertyValueUpdate update = (ControlPropertyValueUpdate) data;
					propertySpec.setValue(update.getNewValue());
				} else if (data instanceof ControlPropertyMacroUpdate) {
					ControlPropertyMacroUpdate update = (ControlPropertyMacroUpdate) data;
					propertySpec.setMacroKey(update.getMacro() != null ? update.getMacro().getKey() : null);
				} else if (data instanceof ControlPropertyCustomDataUpdate) {
					ControlPropertyCustomDataUpdate update = (ControlPropertyCustomDataUpdate) data;
					propertySpec.setCustomData(update.getCustomData());
					propertySpec.setUsingCustomData(update.isSetTo());
				} else {
					System.err.println("WARNING: CustomControlClass.loadControlClassListeners(): unknown control property update:" + data);
				}
			}
		});
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
