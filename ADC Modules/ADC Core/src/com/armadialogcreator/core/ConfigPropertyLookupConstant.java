package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;

/**
 A {@link ConfigPropertyLookupConstant} is a way to check if one {@link ConfigProperty} equals another.
 Each instance that implements this interface must be instantiated only <b>once</b>.

 @author Kayler
 @since 09/15/2016. */
public interface ConfigPropertyLookupConstant extends ConfigPropertyKey {
	ReadOnlyList<ConfigPropertyLookupConstant> EMPTY = new ReadOnlyList<>(new ArrayList<>());

	/** @return all values that the property can be, or null if not using options. */
	@Nullable
	ReadOnlyArray<ConfigPropertyValueOption> getOptions();

	/** @return the name associated with the property. Is not guaranteed to be unique. */
	@NotNull
	String getPropertyName();

	/**
	 @return the {@link PropertyType} associated with the property. This is the <b>initial</b> {@link PropertyType}.
	 */
	@NotNull
	PropertyType getPropertyType();

	/**
	 A unique id for the lookup item to guarantee a match by despite order change or property name change, or some other change.
	 <br>When the lookup item is written, the propertyId must <b>NEVER</b> change.
	 */
	int getPropertyId();

	/** @return the sort priority for {@link #PRIORITY_SORT}. By default, returns {@link Integer#MAX_VALUE} */
	default int priority() {
		return Integer.MAX_VALUE;
	}

	Comparator<ConfigPropertyLookupConstant> PRIORITY_SORT = new Comparator<ConfigPropertyLookupConstant>() {
		@Override
		public int compare(ConfigPropertyLookupConstant o1, ConfigPropertyLookupConstant o2) {
			if (o1.priority() == o2.priority()) {
				return o1.getPropertyName().compareTo(o2.getPropertyName());
			}
			if (o1.priority() < o2.priority()) {
				return -1;
			}
			return 1;
		}
	};

	@NotNull
	default String debugToString() {
		return getPropertyName() + "[id=" + getPropertyId() + "]";
	}
}
