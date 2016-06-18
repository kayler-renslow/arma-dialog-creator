package com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 A canvas component that can render text in the middle of the component
 Created on 05/11/2016. */
public class TextCanvasComponent extends SimpleCanvasComponent {

	protected Color textColor = backgroundColor.invert();

	private Text textObj = new Text();

	protected TextCanvasComponent(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	private int getTextX() {
		int textWidth = (int) textObj.getLayoutBounds().getWidth();
		return getLeftX() + (getWidth() - textWidth) / 2;
	}

	private int getTextY() {
		int textHeight = (int) (textObj.getLayoutBounds().getHeight() * 0.25);
		return getTopY() + (getHeight() - textHeight) / 2;
	}

	public void paint(GraphicsContext gc) {
		super.paint(gc);
		gc.setFont(getFont());
		gc.setFill(textColor);
		gc.fillText(getText(), getTextX(), getTextY());
	}

	public Font getFont() {
		return textObj.getFont();
	}

	public void setFont(@NotNull Font font) {
		this.textObj.setFont(font);
	}

	public void setText(String text) {
		this.textObj.setText(text);
	}

	public String getText() {
		return this.textObj.getText();
	}

	public void setTextColor(@NotNull Color color) {
		this.textColor = color;
	}

	public Color getTextColor() {
		return textColor;
	}

}
