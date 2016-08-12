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
 This class is useful for creating base control classes and not having to type a bunch of redundant information.<br>
 Be sure to remember that classes inside Controls class of dialogs require types and things. These "macro" classes must stay out of the Controls class.
 Created on 05/23/2016. */
public class ControlClass {
	public static final ControlClass[] EMPTY = new ControlClass[0];
	
	private final ControlClassSpecificationProvider specProvider;
	private ControlClass extend;
	
	private final LinkedList<ControlProperty> requiredProperties = new LinkedList<>();
	private final LinkedList<ControlProperty> optionalProperties = new LinkedList<>();
	private final LinkedList<ControlProperty> eventProperties = new LinkedList<>();
	
	private final LinkedList<ControlClass> requiredSubClasses = new LinkedList<>();
	private final LinkedList<ControlClass> optionalSubClasses = new LinkedList<>();
	
	protected String className;
	
	private final UpdateListenerGroup<ControlPropertyUpdate> updateGroup = new UpdateListenerGroup<>();
	
	public ControlClass(@NotNull String name, @NotNull ControlClassSpecificationProvider provider) {
		this.className = name;
		this.specProvider = provider;
		
		addProperties(requiredProperties, specProvider.getRequiredProperties());
		addProperties(optionalProperties, specProvider.getOptionalProperties());
		addProperties(eventProperties, specProvider.getEventProperties());
		addRequiredSubClasses(specProvider.getRequiredSubClasses());
		addOptionalSubClasses(specProvider.getOptionalSubClasses());
		
		for (ControlProperty controlProperty : requiredProperties) {
			controlProperty.getValueObserver().addValueListener(getControlPropertyListener(controlProperty));
		}
		for (ControlProperty controlProperty : optionalProperties) {
			controlProperty.getValueObserver().addValueListener(getControlPropertyListener(controlProperty));
		}
		for (ControlProperty controlProperty : eventProperties) {
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
	
	public final @Nullable ControlClass getExtendControl() {
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
	public ControlClass[] getRequiredSubClasses() {
		return requiredSubClasses.toArray(new ControlClass[requiredSubClasses.size()]);
	}
	
	@NotNull
	public ControlClass[] getOptionalSubClasses() {
		return optionalSubClasses.toArray(new ControlClass[optionalSubClasses.size()]);
	}
	
	@NotNull
	public final List<ControlProperty> getMissingRequiredProperties() {
		List<ControlProperty> defined = getAllDefinedProperties();
		
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
	public ControlProperty findRequiredProperty(ControlPropertyLookup lookup) {
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
	public ControlProperty findOptionalProperty(ControlPropertyLookup lookup) {
		for (ControlProperty controlProperty : getOptionalProperties()) {
			if (controlProperty.getPropertyLookup() == lookup) {
				return controlProperty;
			}
		}
		throw new IllegalArgumentException("Lookup element '" + lookup.name() + "' wasn't in optional properties.");
	}
	
	@NotNull
	public final ControlProperty[] getRequiredProperties() {
		return requiredProperties.toArray(new ControlProperty[requiredProperties.size()]);
	}
	
	@NotNull
	public final ControlProperty[] getOptionalProperties() {
		return optionalProperties.toArray(new ControlProperty[optionalProperties.size()]);
	}
	
	public @NotNull List<ControlProperty> getAllDefinedProperties() {
		List<ControlProperty> properties = new ArrayList<>();
		for (ControlProperty property : getInheritedProperties()) {
			if (property.getValue() != null) {
				properties.add(property);
			}
		}
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
	
	@NotNull
	public final List<ControlProperty> getInheritedProperties() {
		if (extend == null) {
			return new ArrayList<>();
		}
		ArrayList<ControlProperty> list = new ArrayList<>();
		appendInheritedProperties(extend, list);
		return list;
	}
	
	public final ControlProperty[] getEventProperties() {
		return eventProperties.toArray(new ControlProperty[eventProperties.size()]);
	}
	
	private void appendInheritedProperties(@NotNull ControlClass extend, @NotNull ArrayList<ControlProperty> list) {
		for (ControlProperty c : extend.getAllDefinedProperties()) {
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
	
	/**
	 Gets the update listener group that listens to all {@link ControlProperty} instances. Instead of adding listeners to all {@link ControlProperty}'s potentially hundreds of times scattered
	 across the program, the ControlClass listens to it's own ControlProperties. Any time any of the ControlProperty's receive an update, the value inside the listener will be the
	 {@link ControlProperty} that was updated as well as the property's old value and the updated/new value. If this ControlClass extends some ControlClass via
	 {@link #extendControlClass(ControlClass)}, the update groups will <b>not</b> be synced. You will have to listen to each ControlClass separately.
	 */
	public UpdateListenerGroup<ControlPropertyUpdate> getUpdateGroup() {
		return updateGroup;
	}
	
}
