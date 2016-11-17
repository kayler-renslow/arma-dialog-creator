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
 Used when {@link ControlClass#setClassName(String)} is invoked, or when {@link ControlClass#getClassNameObserver()} is updated
 via {@link com.kaylerrenslow.armaDialogCreator.util.ValueObserver#updateValue(Object)}

 @author Kayler
 @since 11/16/16 */
public class ControlClassRenameUpdate implements ControlClassUpdate {
	private ControlClass updated;
	private final String oldName;
	private final String newName;

	/**
	 @param updated the updated {@link ControlClass}
	 @param oldName the old class name
	 @param newName the new class name
	 */
	public ControlClassRenameUpdate(@NotNull ControlClass updated, @NotNull String oldName, @NotNull String newName) {
		this.updated = updated;
		this.oldName = oldName;
		this.newName = newName;
	}

	@Override
	@NotNull
	public ControlClass getControlClass() {
		return updated;
	}

	/** Get the old class name */
	@NotNull
	public String getOldName() {
		return oldName;
	}

	/** Get the new class name */
	@NotNull
	public String getNewName() {
		return newName;
	}
}
