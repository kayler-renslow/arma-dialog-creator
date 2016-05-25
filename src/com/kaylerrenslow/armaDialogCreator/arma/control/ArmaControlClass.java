package com.kaylerrenslow.armaDialogCreator.arma.control;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 @author Kayler
 Base class for ArmaControl that may or may not be a control (could be missing properties like style or type, which are required for all controls)<br>
 This class is useful for creating base control classes and not having to type a bunch of redundant information.<br>
 Be sure to remember that classes inside Controls class of dialogs require types and things. These "macro" classes must stay out of the Controls class.
 Created on 05/23/2016. */
public class ArmaControlClass {
	private ArmaControlClass extend;

	private final ArrayList<ControlProperty> requiredProperties = new ArrayList<>();
	private final ArrayList<ControlProperty> optionalProperties = new ArrayList<>();
	private final ArrayList<ControlProperty> definedProperties = new ArrayList<>();

	private final ArrayList<ArmaControlClass> definedSubClasses = new ArrayList<>();
	private final ArrayList<ArmaControlClass> requiredSubClasses = new ArrayList<>();
	private final ArrayList<ArmaControlClass> optionalSubClasses = new ArrayList<>();

	protected String className;

	public ArmaControlClass(@NotNull String name) {
		this.className = name;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public final void extendControlClass(@Nullable ArmaControlClass armaControl) {
		if (armaControl == this) {
			throw new IllegalArgumentException("Extend class can't extend itself!");
		}
		this.extend = armaControl;
	}

	@Nullable
	public final ArmaControlClass getExtendControl() {
		return extend;
	}

	public final void defineSubClass(@NotNull ArmaControlClass subClass) {
		if (subClass == this) {
			throw new IllegalArgumentException("Can't define a class as a subclass of itself");
		}
		if (!definedSubClasses.contains(subClass)) {
			definedSubClasses.add(subClass);
		}
	}

	public final boolean undefineSubClass(@NotNull ArmaControlClass subClass) {
		return definedSubClasses.remove(subClass);
	}

	protected final void addRequiredSubClasses(@NotNull ArmaControlClass... subClasses) {
		for (ArmaControlClass subClass : subClasses) {
			if (subClass == this) {
				throw new IllegalArgumentException("Can't require a class as a subclass of itself");
			}
			requiredSubClasses.add(subClass);
		}
	}

	protected final void addOptionalSubClasses(@NotNull ArmaControlClass... subClasses) {
		for (ArmaControlClass subClass : subClasses) {
			if (subClass == this) {
				throw new IllegalArgumentException("Can't make a class as a subclass of itself");
			}
			optionalSubClasses.add(subClass);
		}
	}

	@NotNull
	public final ArmaControlClass[] getDefinedSubClasses() {
		return this.definedSubClasses.toArray(new ArmaControlClass[definedSubClasses.size()]);
	}

	@NotNull
	public ArmaControlClass[] getRequiredSubClasses() {
		return requiredSubClasses.toArray(new ArmaControlClass[requiredSubClasses.size()]);
	}

	@NotNull
	public ArmaControlClass[] getOptionalSubClasses() {
		return optionalSubClasses.toArray(new ArmaControlClass[optionalSubClasses.size()]);
	}

	@NotNull
	public final ControlProperty[] getMissingRequiredProperties() {
		ArrayList<ControlProperty> missing = new ArrayList<>();
		ControlProperty[] defined = getAllDefinedProperties();

		boolean found;
		for (ControlProperty req : requiredProperties) {
			found = false;
			for (ControlProperty d : defined) {
				if (req.equals(d)) {
					found = true;
					break;
				}
			}
			if (!found) {
				missing.add(req);
			}
		}
		return missing.toArray(new ControlProperty[missing.size()]);
	}

	public final void defineProperty(ControlProperty c) {
		if (!definedProperties.contains(c)) {
			definedProperties.add(c);
		}
	}

	public final boolean propertyIsDefined(ControlProperty c) {
		return definedProperties.contains(c);
	}

	public final boolean removeDefinedProperty(ControlProperty c) {
		return definedProperties.remove(c);
	}

	protected final void addRequiredProperties(ControlProperty... props) {
		for (ControlProperty p : props) {
			if (!requiredProperties.contains(p)) {
				requiredProperties.add(p);
			}
		}
	}

	protected final void addOptionalProperties(ControlProperty... props) {
		for (ControlProperty p : props) {
			if (!optionalProperties.contains(p)) {
				optionalProperties.add(p);
			}
		}
	}

	@NotNull
	public final ControlProperty[] getRequiredProperties() {
		return requiredProperties.toArray(new ControlProperty[definedProperties.size()]);
	}

	@NotNull
	public final ControlProperty[] getOptionalProperties() {
		return optionalProperties.toArray(new ControlProperty[definedProperties.size()]);
	}

	@NotNull
	public final ControlProperty[] getAllDefinedProperties() {
		ControlProperty[] arr = new ControlProperty[getInheritedProperties().length + definedProperties.size()];
		int i = 0;
		for (ControlProperty inherit : getInheritedProperties()) {
			arr[i++] = inherit;
		}
		for (ControlProperty defined : definedProperties) {
			arr[i++] = defined;
		}
		return arr;
	}

	@NotNull
	public final ControlProperty[] getInheritedProperties() {
		if (extend == null) {
			return ControlProperty.EMPTY;
		}
		ArrayList<ControlProperty> list = new ArrayList<>();
		appendInheritedProperties(extend, list);
		return list.toArray(new ControlProperty[list.size()]);
	}

	private void appendInheritedProperties(@NotNull ArmaControlClass extend, @NotNull ArrayList<ControlProperty> list) {
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

}
