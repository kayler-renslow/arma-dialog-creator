/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;

/**
 Update triggered by {@link ControlClass#overrideProperty(ControlPropertyLookup)}

 @author Kayler
 @since 11/16/2016 */
public class ControlClassOverridePropertyUpdate implements ControlClassUpdate {
	private final ControlClass controlClass;
	private final ControlProperty overidden;
	private final boolean wasAdded;

	/**
	 @param controlClass {@link ControlClass} that was updated
	 @param overridden the {@link ControlProperty} that was overridden
	 @param wasAdded true if {@link #getOveridden()} was added to {@link ControlClass#getOverriddenProperties()}, false if it was removed
	 */
	public ControlClassOverridePropertyUpdate(@NotNull ControlClass controlClass, @NotNull ControlProperty overridden, boolean wasAdded) {
		this.controlClass = controlClass;
		this.overidden = overridden;
		this.wasAdded = wasAdded;
	}

	@NotNull
	public ControlProperty getOveridden() {
		return overidden;
	}

	/** @return true if {@link #getOveridden()} was added to {@link ControlClass#getOverriddenProperties()}, false if it was removed */
	public boolean wasAdded() {
		return wasAdded;
	}

	@Override
	public @NotNull ControlClass getControlClass() {
		return controlClass;
	}
}
