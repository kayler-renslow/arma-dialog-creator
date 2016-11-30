package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassRegistry;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassSpecification;
import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 Created by Kayler on 10/23/2016.
 */
public class CustomControlClassRegistry implements ControlClassRegistry {
	private final List<CustomControlClass> controlClassList = new LinkedList<>();
	private final ReadOnlyList<CustomControlClass> controlClassReadOnlyList = new ReadOnlyList<>(controlClassList);

	CustomControlClassRegistry() {
	}

	public ReadOnlyList<CustomControlClass> getControlClassList() {
		return controlClassReadOnlyList;
	}

	public Iterator<ControlClass> customControlsIterator() {
		return new CustomControlClassIterator(this);
	}

	public void addControlClass(@NotNull ControlClassSpecification controlClass) {
		controlClassList.add(new CustomControlClass(controlClass, Project.getCurrentProject()));
	}

	public void addControlClass(@NotNull CustomControlClass controlClass) {
		controlClassList.add(controlClass);
	}

	/** Will get the {@link CustomControlClass#getControlClass()} by the given name, or null if nothing could be matched */
	@Override
	public ControlClass findControlClassByName(@NotNull String className) {
		for (CustomControlClass controlClass : controlClassList) {
			if (controlClass.getSpecification().getClassName().equals(className)) {
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
			if (controlClass.getSpecification().getClassName().equals(className)) {
				return controlClass;
			}
		}
		return null;
	}

	public void addControlClass(@NotNull ControlClass controlClass) {
		addControlClass(new CustomControlClass(controlClass, Project.getCurrentProject()));
	}

	public void removeControlClass(@NotNull CustomControlClass controlClass) {
		controlClassList.remove(controlClass);
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
