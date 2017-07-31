package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 A {@link ControlClassRegistry} implementation for storing {@link CustomControlClass} instances

 @author Kayler
 @since 10/23/2016. */
public class CustomControlClassRegistry implements ControlClassRegistry, Iterable<CustomControlClass> {
	private final List<CustomControlClass> controlClassList = new LinkedList<>();
	private final ReadOnlyList<CustomControlClass> controlClassReadOnlyList = new ReadOnlyList<>(controlClassList);
	private final SpecificationRegistry specReg;
	private final CustomControlClass.Scope scope;

	public CustomControlClassRegistry(@NotNull SpecificationRegistry specReg, @NotNull CustomControlClass.Scope scope) {
		this.specReg = specReg;
		this.scope = scope;
	}

	/**
	 Returns a read-only list of all {@link CustomControlClass} instances in this registry.
	 The reason this is a list is so order-of-insertion is maintained.

	 @return list
	 */
	@NotNull
	public ReadOnlyList<CustomControlClass> getControlClassList() {
		return controlClassReadOnlyList;
	}

	@NotNull
	@Override
	public Iterator<CustomControlClass> iterator() {
		return controlClassList.iterator();
	}

	/** @return the scope of this registries {@link CustomControlClass} instances. */
	@NotNull
	public CustomControlClass.Scope getScope() {
		return scope;
	}

	/**
	 Add a new {@link CustomControlClass} instance by creating it from the given {@link ControlClassSpecification}.
	 <p>
	 {@link CustomControlClass#getScope()} will automatically be set to {@link #getScope()} on insertion.

	 @return the {@link CustomControlClass} that was created and added to {@link #getControlClassList()}
	 */
	public CustomControlClass addControlClass(@NotNull ControlClassSpecification controlClass) {
		CustomControlClass ccc = new CustomControlClass(controlClass, specReg, scope);
		controlClassList.add(ccc);
		return ccc;
	}

	/**
	 Add the given {@link CustomControlClass} instance to {@link #getControlClassList()}.
	 <p>
	 {@link CustomControlClass#getScope()} will automatically be set to {@link #getScope()} on insertion.
	 */
	public void addControlClass(@NotNull CustomControlClass controlClass) {
		controlClassList.add(controlClass);
		controlClass.setScope(this.scope);
	}

	/**
	 Add a new {@link CustomControlClass} instance by creating it from the given {@link ControlClass}.
	 <p>
	 {@link CustomControlClass#getScope()} will automatically be set to {@link #getScope()} on insertion.

	 @return the {@link CustomControlClass} that was created and added to {@link #getControlClassList()}
	 @see #addControlClass(ControlClassSpecification)
	 @see #addControlClass(CustomControlClass)
	 */
	@NotNull
	public CustomControlClass addControlClass(@NotNull ControlClass controlClass) {
		CustomControlClass ccc = new CustomControlClass(controlClass, scope);
		addControlClass(ccc);
		return ccc;
	}

	/**
	 Remove the given {@link CustomControlClass} from {@link #getControlClassList()}.
	 If the {@link CustomControlClass} wasn't in the list, nothing will happen.
	 */
	public void removeControlClass(@NotNull CustomControlClass controlClass) {
		controlClassList.remove(controlClass);
	}

	/**
	 Will get the {@link CustomControlClass#getControlClass()} by the given name, or null if nothing could be matched

	 @return the matched {@link ControlClass}, or null if couldn't be found
	 */
	@Override
	@Nullable
	public ControlClass findControlClassByName(@NotNull String className) {
		for (CustomControlClass controlClass : controlClassList) {
			if (controlClass.getControlClass().getClassName().equals(className)) {
				return controlClass.getControlClass();
			}
		}
		return null;
	}

	/**
	 Will get the {@link CustomControlClass} by the given name, or null if nothing could be matched

	 @return matched class, null if couldn't be matched
	 */
	@Nullable
	public CustomControlClass findCustomControlClassByName(@NotNull String className) {
		for (CustomControlClass controlClass : controlClassList) {
			if (controlClass.getControlClass().getClassName().equals(className)) {
				return controlClass;
			}
		}
		return null;
	}

	public Iterable<ControlClass> controlClassIterator() {
		return new Iterable<ControlClass>() {
			@NotNull
			@Override
			public Iterator<ControlClass> iterator() {
				return new CustomControlClassIterator(CustomControlClassRegistry.this);
			}
		};
	}

	private static class CustomControlClassIterator implements Iterator<ControlClass> {
		private final Iterator<CustomControlClass> iterator;

		public CustomControlClassIterator(CustomControlClassRegistry registry) {
			iterator = registry.getControlClassList().iterator();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public ControlClass next() {
			return iterator.next().getControlClass();
		}
	}
}
