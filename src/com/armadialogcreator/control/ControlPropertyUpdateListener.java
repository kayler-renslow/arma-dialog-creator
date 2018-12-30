package com.armadialogcreator.control;

import com.armadialogcreator.util.UpdateGroupListener;
import org.jetbrains.annotations.NotNull;

/**
 A {@link UpdateGroupListener} implementation for preventing memory leaks when adding listeners to {@link ControlProperty#getControlPropertyUpdateGroup()}

 @author Kayler
 @since 11/20/2016 */
public abstract class ControlPropertyUpdateListener implements UpdateGroupListener<ControlPropertyUpdate> {
	private final ControlProperty property;

	public ControlPropertyUpdateListener(@NotNull ControlProperty property) {
		this.property = property;
	}

	public void linkListener() {
		property.getControlPropertyUpdateGroup().addListener(this);
	}

	public void unlinkListener() {
		property.getControlPropertyUpdateGroup().removeListener(this);
	}

}
