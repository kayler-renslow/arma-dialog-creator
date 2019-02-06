package com.armadialogcreator.core.old;

import com.armadialogcreator.core.Macro;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 11/14/2016.
 */
public class ControlPropertyMacroUpdate implements ControlPropertyUpdate {
	private ControlProperty controlProperty;

	private final Macro oldMacro;
	private final Macro newMacro;

	public ControlPropertyMacroUpdate(@NotNull ControlProperty controlProperty, @Nullable Macro oldMacro, @Nullable Macro newMacro) {
		this.controlProperty = controlProperty;
		this.oldMacro = oldMacro;
		this.newMacro = newMacro;
	}

	@Override
	public @NotNull ControlProperty getControlProperty() {
		return controlProperty;
	}

	@Nullable
	public Macro getNewMacro() {
		return newMacro;
	}

	@Nullable
	public Macro getOldMacro() {
		return oldMacro;
	}

}
