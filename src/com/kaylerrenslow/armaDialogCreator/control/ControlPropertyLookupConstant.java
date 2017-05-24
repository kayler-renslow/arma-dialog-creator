package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;

/**
 A {@link ControlPropertyLookupConstant} is a way to check if one {@link ControlProperty} equals another.
 Each instance that implements this interface must be instantiated only <b>once</b>.

 @author Kayler
 @since 09/15/2016. */
public interface ControlPropertyLookupConstant {
	ReadOnlyList<ControlPropertyLookupConstant> EMPTY = new ReadOnlyList<>(new ArrayList<>());

	/** All values that the property can be, or null if not using options. */
	@Nullable
	ControlPropertyOption[] getOptions();

	/** Get the name associated with the property. Is not guaranteed to be unique. */
	@NotNull
	String getPropertyName();

	/**
	 Get the {@link PropertyType} associated with the property. This is the <b>initial</b> {@link PropertyType} and for each {@link ControlProperty}.
	 */
	@NotNull
	PropertyType getPropertyType();

	/**
	 A unique id for the lookup item to guarantee a match by despite order change or property name change, or some other change.
	 <br>When the lookup item is written, the propertyId must <b>NEVER</b> change.
	 */
	int getPropertyId();

	/** Get documentation on the property. */
	String getAbout();


	/**
	 Construct a new {@link ControlProperty} instance with the given {@link DefaultValueProvider}.
	 If <code>provider==null</code>, will simply return {@link ControlProperty#ControlProperty(ControlPropertyLookupConstant)}
	 with this as the parameter.<p>
	 If <code>provider!=null</code>, will return {@link ControlProperty#ControlProperty(ControlPropertyLookupConstant, SerializableValue)}
	 with the value equal to what {@link DefaultValueProvider#getDefaultValue(ControlPropertyLookupConstant)} returns

	 @param provider provider to use, or null if not to use one
	 @return the new instance
	 */
	@NotNull
	default ControlProperty newEmptyProperty(@Nullable DefaultValueProvider provider) {
		if (provider == null) {
			return new ControlProperty(this);
		}
		SerializableValue value = provider.getDefaultValue(this);
		ControlProperty property = new ControlProperty(this, value);
		property.setDefaultValue(false, value);
		return property;
	}

	/** Return the sort priority for {@link #PRIORITY_SORT}. By default, returns {@link Integer#MAX_VALUE} */
	default int priority() {
		return Integer.MAX_VALUE;
	}

	Comparator<ControlPropertyLookupConstant> PRIORITY_SORT = new Comparator<ControlPropertyLookupConstant>() {
		@Override
		public int compare(ControlPropertyLookupConstant o1, ControlPropertyLookupConstant o2) {
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
