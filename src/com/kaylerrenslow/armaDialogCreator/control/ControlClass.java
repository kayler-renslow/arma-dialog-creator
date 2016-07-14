package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

	private final ArrayList<ControlProperty> requiredProperties = new ArrayList<>();
	private final ArrayList<ControlProperty> optionalProperties = new ArrayList<>();
	private final ArrayList<ControlProperty> eventProperties = new ArrayList<>();

	private final ArrayList<ControlClass> requiredSubClasses = new ArrayList<>();
	private final ArrayList<ControlClass> optionalSubClasses = new ArrayList<>();

	protected String className;

	private UpdateListenerGroup<Object> updateGroup = new UpdateListenerGroup<>();

	public ControlClass(@NotNull String name, @NotNull ControlClassSpecificationProvider provider) {
		this.className = name;
		this.specProvider = provider;
		updateGroup.addListener(new UpdateListener<Object>() {
			@Override
			public void update(@Nullable Object data) {
				if (data != null) {
					updateProperties();
				}
			}
		});
		addProperties(requiredProperties, specProvider.getRequiredProperties());
		addProperties(optionalProperties, specProvider.getOptionalProperties());
		addProperties(eventProperties, specProvider.getEventProperties());
		addRequiredSubClasses(specProvider.getRequiredSubClasses());
		addOptionalSubClasses(specProvider.getOptionalSubClasses());

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
	public final ControlClassSpecificationProvider getSpecProvider(){
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

	private void addProperties(ArrayList<ControlProperty> propertiesList, ControlPropertyLookup... props) {
		main:
		for (ControlPropertyLookup lookup : props) {
			for(ControlProperty req : propertiesList){
				if(req.getPropertyLookup() == lookup){
					continue main;
				}
			}
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
		throw new IllegalArgumentException("Lookup element '"+lookup.name()+"' wasn't in required properties.");
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
		throw new IllegalArgumentException("Lookup element '"+lookup.name()+"' wasn't in optional properties.");
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
			if (property.valuesAreSet()) {
				properties.add(property);
			}
		}
		for (ControlProperty property : getRequiredProperties()) {
			if (property.valuesAreSet()) {
				properties.add(property);
			}
		}
		for (ControlProperty property : getOptionalProperties()) {
			if (property.valuesAreSet()) {
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
	 Gets the update listener group that listens to this object. Instead of adding listeners to all properties, anytime a control property is changed inside this control the listeners should be notified from where it was changed.<br>
	 Also, since it will not automatically change, it will cut down on the number of renders performed by the editor's canvas<br>
	 The value inside the listener can be null, which means all listeners except this control class's inner listener will be notified
	 */
	public UpdateListenerGroup<Object> getUpdateGroup() {
		return updateGroup;
	}


	/** Called when update listeners have been notified of an update and the new value is not null. Default implementation is nothing */
	protected void updateProperties() {

	}

}
