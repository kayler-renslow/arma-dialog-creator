package com.kaylerrenslow.armaDialogCreator.gui.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.util.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 07/07/2017 */
public class ControlFocusHandler {
	private final ArmaDisplay armaDisplay;
	private ArmaControl focusedControl = null;

	public ControlFocusHandler(@NotNull ArmaDisplay display) {
		this.armaDisplay = display;
	}

	@Nullable
	public ArmaControl getFocusedControl() {
		return focusedControl;
	}

	public void setFocusedControl(@Nullable ArmaControl control) {
		if (focusedControl != null) {
			setControlFocused(focusedControl, false);
		}
		focusedControl = control;
		if (control != null) {
			setControlFocused(control, true);
		}
	}

	public void autoFocusToControl() {
		Reference<ArmaControl> focusToMe = new Reference<>();
		Reference<ArmaControl> lastControl = new Reference<>();
		armaDisplay.getControls().deepIterator().forEach(armaControl -> {
			setControlFocused(armaControl, false);
			if (armaControl.getRenderer().requestingFocus()) {
				focusToMe.setValue(armaControl);
			}
			if (armaControl.getRenderer().canHaveFocus()) {
				lastControl.setValue(armaControl);
			}
		});
		if (focusToMe.getValue() == null) {
			focusedControl = lastControl.getValue();
		} else {
			focusedControl = focusToMe.getValue();
		}
		if (focusedControl != null) {
			setControlFocused(focusedControl, true);
		}
	}

	private void setControlFocused(@NotNull ArmaControl armaControl, boolean focused) {
		armaControl.getRenderer().setFocused(focused);
	}
}
