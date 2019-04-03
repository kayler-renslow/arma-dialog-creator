package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 @author K
 @since 01/03/2019 */
public class ConfigClass implements ConfigClassSpecification, AllowedStyleProvider {

	private final NotNullValueObserver<String> classNameObserver;
	private final ValueObserver<ConfigClass> extendClassObserver = new ValueObserver<>();
	private final ConfigPropertySet properties = new ConfigPropertySet();
	private final Set<ConfigProperty> propertiesInheritedOwnedByParent = new HashSet<>();
	private final Map<String, ConfigPropertyProxy> propertyProxies = new HashMap<>();
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
						propertiesInheritedOwnedByParent.add(change.getPut().getValue());
						properties.putProperty(change.getPut().getValue());
					}
					break;
				}
				case Move: {
					//shouldn't be able to move a property
					throw new IllegalStateException();
				}
				case Remove: {
					if (!propertiesInheritedOwnedByParent.contains(change.getRemoved().getValue())) {
						//can't remove a non inherited property
						return;
					}
					ConfigProperty removed = change.getRemoved().getValue();
					propertiesInheritedOwnedByParent.remove(removed);
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
								iterator.remove();
							}
						}
						return;
					}
					propertiesInheritedOwnedByParent.clear();
					break;
				}
				case Replace: {
					MapObserverChangeReplace<String, ConfigProperty> replace = change.getReplace();
					boolean removed = propertiesInheritedOwnedByParent.remove(replace.getOldValue());
					if (removed) {
						propertiesInheritedOwnedByParent.add(replace.getNewValue());
					}
					properties.replaceProperty(replace.getNewValue());
					break;
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
						nestedClassesInheritedOwnedByParent.add(added);
						nestedClasses.put(added.getClassName(), added);
						classUpdateGroup.update(new ConfigClassUpdate.AddNestedClassUpdate(added));
					}
					break;
				}
				case Move: {
					//shouldn't be able to move a property
					throw new IllegalStateException();
				}
				case Remove: {
					ConfigClass removed = change.getRemoved().getValue();
					if (!nestedClassesInheritedOwnedByParent.contains(removed)) {
						//don't remove owned classes
						return;
					}
					ConfigClass mine = nestedClasses.remove(removed.getClassName());
					nestedClassesInheritedOwnedByParent.remove(mine);
					classUpdateGroup.update(new ConfigClassUpdate.RemoveNestedClassUpdate(mine));
					break;
				}
				case Clear: {
					nestedClasses.entrySet().removeIf(entry -> nestedClassesInheritedOwnedByParent.contains(entry.getValue()));
					for (ConfigClass c : nestedClassesInheritedOwnedByParent) {
						classUpdateGroup.update(new ConfigClassUpdate.RemoveNestedClassUpdate(c));
					}
					nestedClassesInheritedOwnedByParent.clear();
					break;
				}
				case Replace: {
					MapObserverChangeReplace<String, ConfigClass> replace = change.getReplace();
					boolean removed = replace.getOldValue() != null && nestedClassesInheritedOwnedByParent.remove(replace.getOldValue());
					if (removed) {
						classUpdateGroup.update(new ConfigClassUpdate.RemoveNestedClassUpdate(replace.getOldValue()));
						nestedClassesInheritedOwnedByParent.add(replace.getNewValue());
					}
					nestedClasses.replace(replace.getKey(), replace.getNewValue());
					classUpdateGroup.update(new ConfigClassUpdate.AddNestedClassUpdate(replace.getNewValue()));
					break;
				}
				default: {
					throw new IllegalStateException();
				}
			}
		}
	};
	private final MapObserver<String, ConfigClass> nestedClasses = new MapObserver<>(new HashMap<>());
	private final Set<ConfigClass> nestedClassesInheritedOwnedByParent = new HashSet<>();
	private final UpdateListenerGroup<ConfigClassUpdate> classUpdateGroup = new UpdateListenerGroup<>();
	private final DataContext userData = new DataContext();
	private @Nullable String userComment;
	/** {@link #getOwnerClass()} */
	private ConfigClass ownerClass = null;

	public ConfigClass(@NotNull String className) {
		classNameObserver = new NotNullValueObserver<>(className);
	}

	@Override
	@NotNull
	public String getClassName() {
		return classNameObserver.getValue();
	}

	public void setClassName(@NotNull String name) {
		classNameObserver.updateValue(name);
	}

	@NotNull
	public NotNullValueObserver<String> getClassNameObserver() {
		return classNameObserver;
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
		for (Map.Entry<String, ConfigProperty> entry : configClass.properties) {
			ConfigProperty property = entry.getValue();
			boolean wasAbsent = properties.putPropertyIfAbsent(property);
			if (wasAbsent) {
				propertiesInheritedOwnedByParent.add(property);
			}
		}
		for (Map.Entry<String, ConfigClass> entry : configClass.nestedClasses.entrySet()) {
			ConfigClass c = entry.getValue();
			ConfigClass newVal = nestedClasses.putIfAbsent(c.getClassName(), c);
			if (newVal == c) { //was absent
				nestedClassesInheritedOwnedByParent.add(c);
				classUpdateGroup.update(new ConfigClassUpdate.AddNestedClassUpdate(c));
			}
		}

		configClass.properties.addPropertiesSetListener(extendPropertiesSetListener);
		configClass.nestedClasses.addListener(extendNestedClassesSetListener);

		extendClassObserver.updateValue(configClass);
		classUpdateGroup.update(new ConfigClassUpdate.ExtendClassUpdate(null, configClass));
	}

	public void clearExtendConfigClass() {
		ConfigClass oldExtendClass = extendClassObserver.getValue();
		if (oldExtendClass == null) {
			return;
		}
		oldExtendClass.properties.removePropertiesSetListener(extendPropertiesSetListener);
		oldExtendClass.nestedClasses.removeListener(extendNestedClassesSetListener);
		for (ConfigProperty property : propertiesInheritedOwnedByParent) {
			properties.removeProperty(property.getName());
		}
		for (ConfigClass configClass : nestedClassesInheritedOwnedByParent) {
			nestedClasses.remove(configClass.getClassName());
		}
		propertiesInheritedOwnedByParent.clear();
		nestedClassesInheritedOwnedByParent.clear();
		extendClassObserver.updateValue(null);
		classUpdateGroup.update(new ConfigClassUpdate.ExtendClassUpdate(oldExtendClass, null));
	}

	@Nullable
	public ConfigClass getExtendClass() {
		return extendClassObserver.getValue();
	}

	public boolean inheritProperty(@NotNull String propertyName) {
		ConfigClass extending = extendClassObserver.getValue();
		if (extending == null) {
			throw new ConfigClassInheritanceException();
		}
		ConfigProperty myProperty = properties.findPropertyNullable(propertyName);
		if (myProperty != null) {
			ConfigProperty parentProperty = extending.properties.findPropertyNullable(propertyName);
			if (parentProperty == null) {
				return false;
			}
			propertiesInheritedOwnedByParent.add(parentProperty);
			properties.replaceProperty(parentProperty);
			classUpdateGroup.update(new ConfigClassUpdate.InheritPropertyUpdate(propertyName));
			ConfigPropertyProxy proxy = getPropertyProxy(propertyName);
			if (proxy != null) {
				proxy.setConfigProperty(parentProperty);
			}
		}
		return true;
	}

	public void overrideProperty(@NotNull String propertyName, @NotNull SerializableValue initialValue) {
		if (properties.isImmutable()) {
			return;
		}
		ConfigProperty myProperty = properties.findPropertyNullable(propertyName);
		if (myProperty != null) {
			boolean removed = propertiesInheritedOwnedByParent.remove(myProperty);
			if (removed) { //was inherited
				//replace old inherited property with new one
				ConfigProperty property = new ConfigProperty(propertyName, initialValue);
				properties.replaceProperty(property);
				classUpdateGroup.update(new ConfigClassUpdate.AddPropertyUpdate(property));
				ConfigPropertyProxy proxy = getPropertyProxy(propertyName);
				if (proxy != null) {
					proxy.setConfigProperty(property);
				}
			}
			// update the value
			myProperty.setValue(initialValue);
			return;
		}
		ConfigProperty property = new ConfigProperty(propertyName, initialValue);
		properties.putProperty(property);
		classUpdateGroup.update(new ConfigClassUpdate.AddPropertyUpdate(property));
		ConfigPropertyProxy proxy = getPropertyProxy(propertyName);
		if (proxy != null) {
			proxy.setConfigProperty(property);
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
		overrideProperty(propertyName, value);
	}

	public void removeProperty(@NotNull String propertyName) {
		ConfigProperty removed = properties.removeProperty(propertyName);
		ConfigClass extendClass = getExtendClass();
		if (extendClass == null) {
			if (removed != null) {
				classUpdateGroup.update(new ConfigClassUpdate.RemovePropertyUpdate(removed));
			}
			ConfigPropertyProxy proxy = getPropertyProxy(propertyName);
			if (proxy != null) {
				proxy.setConfigProperty(null);
			}
			return;
		}
		inheritProperty(propertyName);
	}

	@Nullable
	public ConfigPropertyProxy getPropertyProxy(@NotNull String propertyName) {
		return propertyProxies.get(propertyName);
	}

	@NotNull
	public ConfigPropertyProxy createPropertyProxy(@NotNull ConfigPropertyKey key, @NotNull SerializableValue valueWhenPropertyAbsent) {
		return createPropertyProxy(key.getPropertyName(), valueWhenPropertyAbsent);
	}

	@NotNull
	public ConfigPropertyProxy createPropertyProxy(@NotNull String propertyName, @NotNull SerializableValue valueWhenPropertyAbsent) {
		ConfigPropertyProxy proxy = propertyProxies.computeIfAbsent(propertyName, s -> {
			return new ConfigPropertyProxy(s, valueWhenPropertyAbsent);
		});
		ConfigProperty property = properties.findPropertyNullable(propertyName);
		proxy.setConfigProperty(property);
		return proxy;
	}

	@Override
	public ConfigPropertyCategory getPropertyCategory(@NotNull ConfigPropertyKey property) {
		return ConfigPropertyCategory.Basic;
	}

	@NotNull
	public ReadOnlyIterable<ConfigProperty> iterateProperties() {
		return new ReadOnlyIterable<>(properties.iterable());
	}

	@Override
	public boolean propertyIsInherited(@NotNull String propertyName) {
		for (ConfigProperty p : propertiesInheritedOwnedByParent) {
			if (p.nameEquals(propertyName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@NotNull
	public String getExtendClassName() {
		ConfigClass extendClass = getExtendClass();
		if (extendClass == null) {
			throw new IllegalStateException();
		}
		return extendClass.getClassName();
	}

	@Override
	@Nullable
	public ReadOnlyIterable<ConfigPropertyLookupConstant> iterateLookupProperties() {
		return null;
	}

	@Nullable
	public ConfigPropertyLookupConstant getLookup(@NotNull String name) {
		ReadOnlyIterable<ConfigPropertyLookupConstant> iterable = iterateLookupProperties();
		if (iterable == null) {
			return null;
		}
		for (ConfigPropertyLookupConstant key : iterable) {
			if (key.nameEquals(name)) {
				return key;
			}
		}
		return null;
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
		return nestedClasses.get(className);
	}

	@Override
	public int hashCode() {
		return getClassName().hashCode();
	}

	@Override
	@NotNull
	public UpdateListenerGroup<ConfigClassUpdate> getClassUpdateGroup() {
		return classUpdateGroup;
	}

	@Override
	@Nullable
	public ConfigClassSpecification getOwnerClass() {
		return ownerClass;
	}

	@Nullable
	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(@Nullable String userComment) {
		this.userComment = userComment;
	}

	@NotNull
	public DataContext getUserData() {
		return userData;
	}
}
