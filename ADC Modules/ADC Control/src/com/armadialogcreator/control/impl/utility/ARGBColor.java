package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.util.ColorUtil;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 3/7/19 */
public class ARGBColor {
	private int argb;
	private Color color;

	public ARGBColor(int argb) {
		this.argb = argb;
		this.color = ColorUtil.toColor(argb);
	}

	public ARGBColor(@NotNull Color color) {
		this.color = color;
		this.argb = ColorUtil.toARGB(color);
	}

	public int getArgb() {
		return argb;
	}

	@NotNull
	public Color getColor() {
		return color;
	}

	public void setArgb(int argb) {
		this.argb = argb;
		this.color = ColorUtil.toColor(argb);
	}

	public void setColor(@NotNull Color color) {
		this.color = color;
		this.argb = ColorUtil.toARGB(color);
	}
}
