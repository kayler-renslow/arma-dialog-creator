package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
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

	private final ArrayList<ArmaControlSubClass> definedSubClasses = new ArrayList<>();
	private final ArrayList<ArmaControlSubClass> requiredSubClasses = new ArrayList<>();
	private final ArrayList<ArmaControlSubClass> optionalSubClasses = new ArrayList<>();


	public final void extendControlClass(@Nullable ArmaControlClass armaControl) {
		this.extend = armaControl;
	}

	@Nullable
	public final ArmaControlClass getExtendControl() {
		return extend;
	}

	public final void defineSubClass(@NotNull ArmaControlSubClass subClass){
		if(!definedSubClasses.contains(subClass)){
			definedSubClasses.add(subClass);
		}
	}

	public final boolean undefineSubClass(@NotNull ArmaControlSubClass subClass){
		return definedSubClasses.remove(subClass);
	}

	protected final void addRequiredSubClasses(@NotNull ArmaControlSubClass...subClasses){
		for(ArmaControlSubClass subClass : subClasses){
			requiredSubClasses.add(subClass);
		}
	}

	protected final void addOptionalSubClasses(@NotNull ArmaControlSubClass...subClasses){
		for(ArmaControlSubClass subClass : subClasses){
			optionalSubClasses.add(subClass);
		}
	}

	@NotNull
	public final ArmaControlSubClass[] getDefinedSubClasses(){
		return this.definedSubClasses.toArray(new ArmaControlSubClass[definedSubClasses.size()]);
	}

	@NotNull
	public ArmaControlSubClass[] getRequiredSubClasses() {
		return requiredSubClasses.toArray(new ArmaControlSubClass[requiredSubClasses.size()]);
	}

	@NotNull
	public ArmaControlSubClass[] getOptionalSubClasses() {
		return optionalSubClasses.toArray(new ArmaControlSubClass[optionalSubClasses.size()]);
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

	public final boolean propertyIsDefined(ControlProperty c){
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
