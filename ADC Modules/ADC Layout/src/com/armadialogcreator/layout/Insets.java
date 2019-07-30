package com.armadialogcreator.layout;

/**
 @author Kayler
 @since 7/30/19. */
public class Insets {
	public static final Insets NONE = new Insets(0);
	private double top, right, bottom, left;

	public Insets(double top, double right, double bottom, double left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public Insets(double all) {
		this(all, all, all, all);
	}

	public double getTop() {
		return top;
	}

	public double getRight() {
		return right;
	}

	public double getBottom() {
		return bottom;
	}

	public double getLeft() {
		return left;
	}
}
