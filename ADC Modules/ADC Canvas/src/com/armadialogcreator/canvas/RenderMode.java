package com.armadialogcreator.canvas;

/**
 @author K
 @since 3/6/19 */
public enum RenderMode {
	/** Paints only solid blocks with no detail (fastest) */
	Solid,
	/** Paints what the {@link CanvasComponent} would look like in {@link #Preview}, but has no user interaction */
	Basic,
	/** Paints all details and the mouse interacts with the {@link CanvasComponent} */
	Preview,
	/** Does same thing as {@link #Preview}, but button presses and event scripts are simulated */
	Simulation
}
