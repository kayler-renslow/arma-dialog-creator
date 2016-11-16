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
import org.jetbrains.annotations.Nullable;

/**
 Used when {@link ControlClass#extendControlClass(ControlClass)} is invoked
 @author Kayler
 @since 11/16/16 */
public class ControlClassExtendUpdate implements ControlClassUpdate {
	private final ControlClass controlClass;
	private final ControlClass oldValue;
	private final ControlClass newValue;

	public ControlClassExtendUpdate(@NotNull ControlClass controlClass, @Nullable ControlClass oldValue, @Nullable ControlClass newValue) {
		this.controlClass = controlClass;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public @NotNull ControlClass getControlClass() {
		return controlClass;
	}

	@NotNull
	public ControlClass getOldValue() {
		return oldValue;
	}

	@NotNull
	public ControlClass getNewValue() {
		return newValue;
	}
}
