package com.armadialogcreator.util;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 3/5/19 */
public class AColorConstant implements AColor {
	public static final AColorConstant BLACK = new AColorConstant(0, 0, 0, 255);
	public static final AColorConstant RED = new AColorConstant(255, 0, 0, 255);
	public static final AColorConstant GREEN = new AColorConstant(0, 255, 0, 255);
	public static final AColorConstant BLUE = new AColorConstant(0, 0, 255, 255);
	public static final AColorConstant WHITE = new AColorConstant(255, 255, 255, 255);
	public static final AColorConstant TRANSPARENT = new AColorConstant(0, 0, 0, 0);

	/** ARGB color */
	public final int argb;

	/**
	 Creates a color
	 */
	public AColorConstant(int argb) {
		this.argb = argb;
	}

	/**
	 Creates a color

	 @param r red (range 0-1.0)
	 @param g green (range 0-1.0)
	 @param b blue (range 0-1.0)
	 @param a alpha (range 0-1.0)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1
	 */
	public AColorConstant(double r, double g, double b, double a) {
		this.argb = ColorUtil.toARGB(r, g, b, a);
	}

	/**
	 Creates a color

	 @param r red (range 0-255)
	 @param g green (range 0-255)
	 @param b blue (range 0-255)
	 @param a alpha (range 0-255)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 255
	 */
	public AColorConstant(int r, int g, int b, int a) {
		this.argb = ColorUtil.toARGB(r, g, b, a);
	}

	public AColorConstant(@NotNull Color color) {
		this.argb = ColorUtil.toARGB(color);
	}

	@Override
	public int getRedI() {
		return ColorUtil.ri(argb);
	}

	@Override
	public void setRedI(int r) {
	}

	@Override
	public int getGreenI() {
		return ColorUtil.gi(argb);
	}

	@Override
	public void setGreenI(int g) {
	}

	@Override
	public int getBlueI() {
		return ColorUtil.bi(argb);
	}

	@Override
	public void setBlueI(int b) {
	}

	@Override
	public int getAlphaI() {
		return ColorUtil.ai(argb);
	}

	@Override
	public void setAlphaI(int a) {
	}

	@Override
	public double getRedF() {
		return ColorUtil.rf(argb);
	}

	@Override
	public void setRedF(double r) {
	}

	@Override
	public double getGreenF() {
		return ColorUtil.gf(argb);
	}

	@Override
	public void setGreenF(double g) {
	}

	@Override
	public double getBlueF() {
		return ColorUtil.bf(argb);
	}

	@Override
	public void setBlueF(double b) {
	}

	@Override
	public double getAlphaF() {
		return ColorUtil.af(argb);
	}

	@Override
	public void setAlphaF(double a) {
	}

	@Override
	public int toARGB() {
		return argb;
	}

	/**
	 @return the color ARGB int
	 */
	@NotNull
	public String toString() {
		return argb + "";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AColor) {
			return AColor.isEqualTo(this, (AColor) o);
		}
		return false;
	}
}
