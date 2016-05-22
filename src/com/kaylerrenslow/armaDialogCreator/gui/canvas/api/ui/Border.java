package com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui;

import javafx.scene.paint.Color;

/**
 * Created by Kayler on 05/11/2016.
 */
public class Border {
	private int thickness;
	private Color color;

	public Border(int thickness, Color color) {
		this.thickness = thickness;
		this.color = color;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getThickness() {
		return thickness;
	}

	public Color getColor() {
		return color;
	}
}
