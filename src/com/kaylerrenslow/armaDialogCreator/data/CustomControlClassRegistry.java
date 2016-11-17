/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassSpecification;
import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 Created by Kayler on 10/23/2016.
 */
public class CustomControlClassRegistry {
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
		controlClassList.add(new CustomControlClass(controlClass));
	}

	public void addControlClass(@NotNull CustomControlClass controlClass) {
		controlClassList.add(controlClass);
	}

	/** Will get the custom control class by the given name, or null if nothing could be matched */
	public ControlClass findControlClassByName(@NotNull String className) {
		for (CustomControlClass controlClass : controlClassList) {
			if (controlClass.getSpecification().getClassName().equals(className)) {
				return controlClass.getControlClass();
			}
		}
		return null;
	}

	public void addControlClass(@NotNull ControlClass controlClass) {
		addControlClass(new CustomControlClass(controlClass));
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
