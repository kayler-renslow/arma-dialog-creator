package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 @author K
 @since 01/03/2019 */
public class ConfigClass {

	private final NotNullValueObserver<String> classNameObserver;
	private final ValueObserver<ConfigClass> extendClassObserver = new ValueObserver<>();
	private final ConfigPropertySet properties = new ConfigPropertySet();
	private final ConfigPropertySet propertiesInheritedOwnedByParent = new ConfigPropertySet();
	private final MapObserverListener<String, ConfigProperty> extendPropertiesSetListener = new MapObserverListener<>() {
		@Override
		public void onChanged(@NotNull MapObserver<String, ConfigProperty> list, @NotNull MapObserverChange<String, ConfigProperty> change) {
			if (properties.isImmutable()) {
				return;
			}
			switch (change.getChangeType()) {
				case Put: {
					ConfigProperty added = change.getPut().getValue();
					ConfigProperty mine = findPropertyNullable(added.getName());
					if (mine == null) {
						propertiesInheritedOwnedByParent.addProperty(change.getPut().getValue());
					}
					break;
				}
				case Move: {
					//shouldn't be able to move a property
					throw new IllegalStateException();
				}
				case Remove: {
					ConfigProperty removed = change.getRemoved().getValue();
					ConfigProperty inherited = propertiesInheritedOwnedByParent.findPropertyNullable(removed.getName());
					if (inherited != null) {
						propertiesInheritedOwnedByParent.removeProperty(inherited.getName());
						return;
					}
					ConfigProperty mine = properties.findPropertyNullable(removed.getName());
					if (mine != null) {
						mine.inherit(null);
					}
					break;
				}
				case Clear: {
					propertiesInheritedOwnedByParent.clearProperties();
					break;
				}
				case Replace: {
					//shouldn't be able to replace
					throw new IllegalStateException();
				}
				default: {
					throw new IllegalStateException();
				}
			}
		}
	};
	private final MapObserverListener<String, ConfigProperty> extendStrictlyPropertiesSetListener = new MapObserverListener<>() {
		@Override
		public void onChanged(@NotNull MapObserver<String, ConfigProperty> list, @NotNull MapObserverChange<String, ConfigProperty> change) {
			switch (change.getChangeType()) {
				case Put: {
					propertiesInheritedOwnedByParent.addProperty(change.getPut().getValue());
					break;
				}
				case Move: {
					//shouldn't be able to move a property
					throw new IllegalStateException();
				}
				case Remove: {
					propertiesInheritedOwnedByParent.removeProperty(change.getRemoved().getKey());
					break;
				}
				case Replace: {
					//shouldn't be able to replace
					throw new IllegalStateException();
				}
				case Clear: {
					propertiesInheritedOwnedByParent.clearProperties();
					break;
				}
				default: {
					throw new IllegalStateException();
				}
			}
		}
	};

	public ConfigClass(@NotNull String className) {
		classNameObserver = new NotNullValueObserver<>(className);
	}

	@NotNull
	public NotNullValueObserver<String> getClassNameObserver() {
		return classNameObserver;
	}

	@NotNull
	public String getClassName() {
		return classNameObserver.getValue();
	}

	public boolean isExtending() {
		return extendClassObserver.getValue() != null;
	}

	/**
	 Check if the given class will create an inheritance loop if this {@link ConfigClass} were to extend it.
	 The given class may already be in the current inheritance tree,
	 but as long as there is not a cyclic dependence/inheritance, this method will return false.
	 If <code>other=={@link #getExtendClass()}</code>, then will return false.

	 @param other other class
	 @return true if there would be a loop if this extended other, false if there wouldn't be a loop
	 @see #extendConfigClass(ConfigClass)
	 */
	public boolean hasInheritanceLoop(@NotNull ConfigClass other) {
		if (other == extendClassObserver.getValue()) {
			return false;
		}
		if (this.getClassName().equals(other.getClassName())) {
			return true;
		}
		if (getExtendClass() == null) {
			return false;
		}
		if (other.getExtendClass() == null) {
			return false;
		}
		//Check for loops, like its a graph.

		//If we can get from this to other, there would be a loop if we extend other.
		//Therefore, we should return true (because there would be a loop)
		LinkedList<ConfigClass> toVisit = new LinkedList<>();
		HashSet<String> visited = new HashSet<>();
		toVisit.add(other);
		final String myClassName = getClassName();
		while (!toVisit.isEmpty()) {
			ConfigClass visit = toVisit.pop();
			if (myClassName.equals(other.getClassName())) {
				return true;
			}
			visited.add(visit.getClassName());

			ConfigClass otherExtendClass = visit.getExtendClass();
			if (otherExtendClass == null || visited.contains(otherExtendClass.getClassName())) {
				continue;
			}
			toVisit.add(otherExtendClass);
		}
		return false;
	}

	public void extendConfigClass(@NotNull ConfigClass configClass) {
		if (configClass == this) {
			throw new IllegalArgumentException();
		}
		if (extendClassObserver.getValue() == configClass) {
			return;
		}
		if (hasInheritanceLoop(configClass)) {
			throw new ConfigClassInheritanceException();
		}
		propertiesInheritedOwnedByParent.clearProperties();
		propertiesInheritedOwnedByParent.addAllProperties(configClass.propertiesInheritedOwnedByParent);
		configClass.propertiesInheritedOwnedByParent.addPropertiesSetListener(extendStrictlyPropertiesSetListener);
		configClass.properties.addPropertiesSetListener(extendPropertiesSetListener);

		extendClassObserver.updateValue(configClass);
	}

	public void clearExtendConfigClass() {
		ConfigClass extendClass = extendClassObserver.getValue();
		if (extendClass == null) {
			return;
		}
		propertiesInheritedOwnedByParent.clearProperties();
		extendClass.propertiesInheritedOwnedByParent.removePropertiesSetListener(extendStrictlyPropertiesSetListener);
		extendClass.properties.removePropertiesSetListener(extendPropertiesSetListener);
		for (Map.Entry<String, ConfigProperty> entry : properties) {
			ConfigProperty property = entry.getValue();
			property.inherit(null);
		}
		extendClassObserver.updateValue(null);
	}

	@Nullable
	public ConfigClass getExtendClass() {
		return extendClassObserver.getValue();
	}

	public void inheritConfigProperty(@NotNull String propertyName) {
		ConfigClass extending = extendClassObserver.getValue();
		if (extending == null) {
			throw new ConfigClassInheritanceException();
		}
		ConfigProperty property = extending.properties.findPropertyNullable(propertyName);
		if (property == null) {
			throw new MissingConfigPropertyKeyException(propertyName);
		}
		ConfigProperty myProperty = properties.findPropertyNullable(propertyName);
		if (myProperty == null) {
			propertiesInheritedOwnedByParent.addProperty(property);
			return;
		}
		myProperty.inherit(property);
	}

	public void overrideConfigProperty(@NotNull String propertyName, boolean createIfAbsent) {
		ConfigClass extending = extendClassObserver.getValue();
		if (extending == null) {
			return;
		}
		ConfigProperty myProperty = properties.findPropertyNullable(propertyName);
		if (myProperty == null) {
			ConfigProperty removed = propertiesInheritedOwnedByParent.removeProperty(propertyName);
			if (removed == null) {
				throw new MissingConfigPropertyKeyException(propertyName);
			}
			if (createIfAbsent) {
				if (!properties.isImmutable()) {
					return;
				}
				properties.addProperty(new ConfigProperty(propertyName, removed.getValue().deepCopy()));
			}
			return;
		}
		myProperty.inherit(null);
	}

	@NotNull
	public ConfigProperty findProperty(@NotNull String propertyName) {
		ConfigProperty property = findPropertyNullable(propertyName);
		if (property == null) {
			throw new MissingConfigPropertyKeyException(propertyName);
		}
		return property;
	}

	@Nullable
	public ConfigProperty findPropertyNullable(@NotNull String propertyName) {
		ConfigProperty property = properties.findPropertyNullable(propertyName);
		if (property != null) {
			return property;
		}
		return propertiesInheritedOwnedByParent.findPropertyNullable(propertyName);
	}

	protected void makePropertySetImmutable() {
		properties.makeImmutable();
	}

	public boolean propertiesIsImmutable() {
		return properties.isImmutable();
	}

	public void addProperty(@NotNull String propertyName, @NotNull SerializableValue value) {
		if (properties.findPropertyNullable(propertyName) != null) {
			throw new IllegalArgumentException();
		}
		propertiesInheritedOwnedByParent.removeProperty(propertyName); //do optimistic remove (if it's there, success. if not, who cares)
		properties.addProperty(new ConfigProperty(propertyName, value));
	}

	public void removeProperty(@NotNull String propertyName) {
		properties.removeProperty(propertyName);
		ConfigClass extendClass = getExtendClass();
		if (extendClass == null) {
			return;
		}
		ConfigProperty parentProperty = extendClass.findPropertyNullable(propertyName);
		if (parentProperty != null) {
			propertiesInheritedOwnedByParent.addProperty(parentProperty);
		}
	}

	@NotNull
	public ReadOnlyIterable<ConfigProperty> iterateOwnedProperties() {
		return new ReadOnlyIterable<>(properties.iterable());
	}

	@NotNull
	public ReadOnlyIterable<ConfigProperty> iterateProperties() {
		return new ReadOnlyIterable<>(new PropertiesIterable());
	}

	private class PropertiesIterable implements Iterable<ConfigProperty>, Iterator<ConfigProperty> {
		Iterator<ConfigProperty> iter1 = properties.iterable().iterator();
		Iterator<ConfigProperty> iter2 = propertiesInheritedOwnedByParent.iterable().iterator();

		@NotNull
		@Override
		public Iterator<ConfigProperty> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return iter1.hasNext() || iter2.hasNext();
		}

		@Override
		public ConfigProperty next() {
			return iter1.hasNext() ? iter1.next() : iter2.next();
		}
	}

}
