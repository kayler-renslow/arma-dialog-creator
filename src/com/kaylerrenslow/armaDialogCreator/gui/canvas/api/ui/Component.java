package com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui;


/**
 Created by Kayler on 05/12/2016.
 */
public class Component extends PaintedRegion {

	private boolean isEnabled = true;
	private boolean isVisible = true;

	public Component(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	/**
	 Returns true if the component is invisible and is disabled, false otherwise
	 */
	public boolean isGhost() {
		return !isVisible && !isEnabled();
	}

	/**
	 Sets the visibility and enable values. A ghost is not visible and is not enabled.
	 */
	public void setGhost(boolean ghost) {
		this.isVisible = !ghost;
		setEnabled(!ghost);
	}
}
