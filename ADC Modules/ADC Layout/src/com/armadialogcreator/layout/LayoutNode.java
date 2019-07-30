package com.armadialogcreator.layout;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 7/23/19. */
public abstract class LayoutNode {
	private final Bounds bounds = new Bounds(this);
	@NotNull Layout layout = ScreenPositionLayout.SHARED;

	void setLayout(@NotNull Layout layout){
		this.layout = layout;
	}

	@NotNull
	public Bounds getBounds() {
		return bounds;
	}

	public double getWidth() {
		return bounds.getWidth();
	}

	public double getHeight() {
		return bounds.getHeight();
	}

	public double getX() {
		return 0;
	}

	double getY() {
		return 0;
	}
}