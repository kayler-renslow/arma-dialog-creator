package com.armadialogcreator.gui.preview;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.util.SGAS;
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

	/** @return the control that has focus, or null if no control has focus */
	@Nullable
	public ArmaControl getFocusedControl() {
		return focusedControl;
	}

	/**
	 Set the focused control. If the current focused control is reference equal to the provided control, this method will do nothing.

	 @param control the control to set as focused
	 */
	public void setFocusedControl(@Nullable ArmaControl control) {
		if (focusedControl == control) {
			return;
		}
		if (focusedControl != null) {
			setControlFocused(focusedControl, false);
		}
		focusedControl = control;
		if (control != null) {
			setControlFocused(control, true);
		}
	}

	/**
	 Set the focus to last control requesting focus.
	 If no control is requesting focus, the last control that can have focus will get the focus.
	 If no control can have focus, no control will get focus.
	 */
	public void autoFocusToControl() {
		SGAS<ArmaControl> focusToMe = new SGAS<>();
		SGAS<ArmaControl> lastControl = new SGAS<>();
		armaDisplay.getControlNodes().deepIterateChildren().forEach(node -> {
			if (!(node instanceof ArmaControl)) {
				return;
			}
			ArmaControl control = (ArmaControl) node;
			setControlFocused(control, false);
			if (control.getRenderer().requestingFocus()) {
				focusToMe.setValue(control);
			}
			if (control.getRenderer().canHaveFocus()) {
				lastControl.setValue(control);
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
