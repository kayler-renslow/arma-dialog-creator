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
	public ControlClass getOwnerControlClass() {
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
