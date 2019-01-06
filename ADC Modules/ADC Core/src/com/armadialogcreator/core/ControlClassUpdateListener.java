package com.armadialogcreator.core;

import com.armadialogcreator.util.UpdateGroupListener;
import org.jetbrains.annotations.NotNull;

/**
 A {@link UpdateGroupListener} useful for preventing memory leaks while wanting to listen to {@link ControlClass#getControlClassUpdateGroup()}

 @author Kayler
 @see ControlPropertyUpdateListener
 @since 11/20/2016 */
public abstract class ControlClassUpdateListener implements UpdateGroupListener<ControlClassUpdate> {
	private final ControlClass controlClass;

	public ControlClassUpdateListener(@NotNull ControlClass controlClass) {
		this.controlClass = controlClass;
	}

	public void unlink() {
		controlClass.getControlClassUpdateGroup().removeListener(this);
	}

	public void relink() {
		controlClass.getControlClassUpdateGroup().addListener(this);
	}
}
