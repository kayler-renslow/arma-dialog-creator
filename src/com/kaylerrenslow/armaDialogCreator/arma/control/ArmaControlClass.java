package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
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
public class ArmaControlClass {
	private ArmaControlClass extend;

	private final ArrayList<ControlProperty> requiredProperties = new ArrayList<>();
	private final ArrayList<ControlProperty> optionalProperties = new ArrayList<>();

	private final ArrayList<ArmaControlClass> requiredSubClasses = new ArrayList<>();
	private final ArrayList<ArmaControlClass> optionalSubClasses = new ArrayList<>();

	protected String className;

	private final ValueObserver<ArmaControlClass> myselfListener = new ValueObserver<>(this);


	public ArmaControlClass(@NotNull String name) {
		this.className = name;
		myselfListener.addValueListener(new ValueListener<ArmaControlClass>() {
			@Override
			public void valueUpdated(ArmaControlClass oldValue, ArmaControlClass newValue) {
				if (newValue != null) {
					updateProperties();
				}
			}
		});
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
	public ArmaControlClass[] getRequiredSubClasses() {
		return requiredSubClasses.toArray(new ArmaControlClass[requiredSubClasses.size()]);
	}

	@NotNull
	public ArmaControlClass[] getOptionalSubClasses() {
		return optionalSubClasses.toArray(new ArmaControlClass[optionalSubClasses.size()]);
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

	/**
	 Get the listener that listens to this object. Instead of adding listeners to all properties, anytime a control property is changed inside this control this returned observer should be notified from where it was changed.<br>
	 Also, since it will not automatically change, it will cut down on the number of renders performed by the editor's canvas<br>
	 The value inside the listener <b>SHOULD NOT CHANGE</b> as that would be expensive to constantly recreate the control. It can be null, however, which means all listeners except this control class's inner listener will be notified
	 */
	public ValueObserver<ArmaControlClass> getControlListener() {
		return myselfListener;
	}

	/** Called when myselfObserver has been notified of an update. This is explicitly called and not called by a listener. Default implementation is nothing */
	protected void updateProperties() {

	}

}
