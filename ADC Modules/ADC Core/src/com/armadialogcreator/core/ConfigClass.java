package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 @author K
 @since 01/03/2019 */
public class ConfigClass {

	private final NotNullValueObserver<String> classNameObserver;
	private final ValueObserver<ConfigClass> extendClassObserver = new ValueObserver<>();
	private final ConfigPropertySet properties = new ConfigPropertySet();
	private final Set<ConfigProperty> propertiesInheritedOwnedByParent = new HashSet<>();
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
						propertiesInheritedOwnedByParent.remove(change.getPut().getValue());
						properties.addProperty(change.getPut().getValue());
					}
					break;
				}
				case Move: {
					//shouldn't be able to move a property
					throw new IllegalStateException();
				}
				case Remove: {
					if (propertiesInheritedOwnedByParent.contains(change.getRemoved().getValue())) {
						//can't remove an inherited property
						return;
					}
					ConfigProperty removed = change.getRemoved().getValue();
					properties.removeProperty(removed.getName());
					break;
				}
				case Clear: {
					if (!propertiesInheritedOwnedByParent.isEmpty()) {
						//can't remove inherited properties
						Iterator<Map.Entry<String, ConfigProperty>> iterator = properties.iterator();
						while (iterator.hasNext()) {
							Map.Entry<String, ConfigProperty> entry = iterator.next();
							if (propertiesInheritedOwnedByParent.contains(entry.getValue())) {
								continue;
							}
							iterator.remove();
						}
						return;
					}
					properties.clearProperties();
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

	private final MapObserverListener<String, ConfigClass> extendNestedClassesSetListener = new MapObserverListener<>() {
		@Override
		public void onChanged(@NotNull MapObserver<String, ConfigClass> list, @NotNull MapObserverChange<String, ConfigClass> change) {
			if (properties.isImmutable()) {
				return;
			}
			switch (change.getChangeType()) {
				case Put: {
					ConfigClass added = change.getPut().getValue();
					ConfigClass mine = findNestedClassNullable(added.getClassName());
					if (mine == null) {
						nestedClassesInheritedOwnedByParent.put(change.getPut().getKey(), change.getPut().getValue());
					}
					break;
				}
				case Move: {
					//shouldn't be able to move a property
					throw new IllegalStateException();
				}
				case Remove: {
					ConfigClass removed = change.getRemoved().getValue();
					if (nestedClassesInheritedOwnedByParent.remove(removed.getClassName()) != null) {
						return;
					}
					ConfigClass mine = nestedClasses.get(removed.getClassName());
					if (mine != null) {
						//todo?
					}
					break;
				}
				case Clear: {
					nestedClassesInheritedOwnedByParent.clear();
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
	private final MapObserverListener<String, ConfigClass> extendStrictlyNestedClassesSetListener = new MapObserverListener<>() {
		@Override
		public void onChanged(@NotNull MapObserver<String, ConfigClass> list, @NotNull MapObserverChange<String, ConfigClass> change) {
			switch (change.getChangeType()) {
				case Put: {
					MapObserverChangePut<String, ConfigClass> put = change.getPut();
					nestedClassesInheritedOwnedByParent.put(put.getKey(), put.getValue());
					break;
				}
				case Move: {
					//shouldn't be able to move a property
					throw new IllegalStateException();
				}
				case Remove: {
					nestedClassesInheritedOwnedByParent.remove(change.getRemoved().getKey());
					break;
				}
				case Replace: {
					//shouldn't be able to replace
					throw new IllegalStateException();
				}
				case Clear: {
					nestedClassesInheritedOwnedByParent.clear();
					break;
				}
				default: {
					throw new IllegalStateException();
				}
			}
		}
	};
	private final MapObserver<String, ConfigClass> nestedClasses = new MapObserver<>(new HashMap<>());
	private final MapObserver<String, ConfigClass> nestedClassesInheritedOwnedByParent = new MapObserver<>(new HashMap<>());

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

	public void setClassName(@NotNull String name) {
		classNameObserver.updateValue(name);
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
		propertiesInheritedOwnedByParent.clear();
		nestedClassesInheritedOwnedByParent.clear();
		propertiesInheritedOwnedByParent.addAll(configClass.propertiesInheritedOwnedByParent);
		nestedClassesInheritedOwnedByParent.putAll(configClass.nestedClassesInheritedOwnedByParent);

		configClass.properties.addPropertiesSetListener(extendPropertiesSetListener);
		configClass.nestedClassesInheritedOwnedByParent.addListener(extendStrictlyNestedClassesSetListener);
		configClass.nestedClasses.addListener(extendNestedClassesSetListener);

		extendClassObserver.updateValue(configClass);
	}

	public void clearExtendConfigClass() {
		ConfigClass extendClass = extendClassObserver.getValue();
		if (extendClass == null) {
			return;
		}
		propertiesInheritedOwnedByParent.clear();
		nestedClassesInheritedOwnedByParent.clear();
		extendClass.nestedClassesInheritedOwnedByParent.removeListener(extendStrictlyNestedClassesSetListener);
		extendClass.properties.removePropertiesSetListener(extendPropertiesSetListener);
		extendClass.nestedClasses.removeListener(extendNestedClassesSetListener);
		for (ConfigProperty property : propertiesInheritedOwnedByParent) {
			properties.removeProperty(property.getName());
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
		ConfigProperty myProperty = properties.findPropertyNullable(propertyName);
		if (myProperty == null) {
			ConfigProperty parentProperty = extending.properties.findPropertyNullable(propertyName);
			if (parentProperty == null) {
				throw new MissingConfigPropertyKeyException(propertyName);
			}
			propertiesInheritedOwnedByParent.add(parentProperty);
			properties.addProperty(parentProperty);
			return;
		}
	}

	public void overrideConfigProperty(@NotNull String propertyName, @NotNull SerializableValue initialValue) {
		ConfigClass extending = extendClassObserver.getValue();
		if (extending == null) {
			return;
		}
		ConfigProperty myProperty = properties.findPropertyNullable(propertyName);
		if (myProperty == null) {
			propertiesInheritedOwnedByParent.remove(myProperty);
			if (!properties.isImmutable()) {
				return;
			}
			properties.addProperty(new ConfigProperty(propertyName, initialValue));
			return;
		}
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
		return properties.findPropertyNullable(propertyName);
	}

	@NotNull
	public ConfigProperty findProperty(@NotNull ConfigPropertyKey key) {
		ConfigProperty property = findPropertyNullable(key);
		if (property == null) {
			throw new MissingConfigPropertyKeyException(key.getPropertyName());
		}
		return property;
	}

	@Nullable
	public ConfigProperty findPropertyNullable(@NotNull ConfigPropertyKey key) {
		return properties.findPropertyNullable(key);
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
		properties.addProperty(new ConfigProperty(propertyName, value));
	}

	public void removeProperty(@NotNull String propertyName) {
		properties.removeProperty(propertyName);
		ConfigClass extendClass = getExtendClass();
		if (extendClass == null) {
			return;
		}
	}

	@NotNull
	public ReadOnlyIterable<ConfigProperty> iterateOwnedProperties() {
		return new ReadOnlyIterable<>(properties.iterable());
	}

	@NotNull
	public ReadOnlyIterable<ConfigProperty> iterateProperties() {
		return new ReadOnlyIterable<>(new DoubleIterable<>(properties.iterable(), propertiesInheritedOwnedByParent));
	}

	@NotNull
	public ConfigClass findNestedClass(@NotNull String className) {
		ConfigClass c = findNestedClassNullable(className);
		if (c == null) {
			throw new IllegalArgumentException();
		}
		return c;
	}

	@Nullable
	public ConfigClass findNestedClassNullable(@NotNull String className) {
		ConfigClass configClass = nestedClasses.get(className);
		if (configClass != null) {
			return configClass;
		}
		return nestedClassesInheritedOwnedByParent.get(className);
	}
}
