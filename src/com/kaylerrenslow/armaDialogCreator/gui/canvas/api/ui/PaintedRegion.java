package com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 Created by Kayler on 05/11/2016.
 */
public class PaintedRegion extends Region {
	protected Color backgroundColor = Color.ORANGE;
	protected Color textColor = Color.WHITE;
	private Border border;

	private Text textObj = new Text();

	protected PaintedRegion(int x, int y, int width, int height) {
		super(x, y, x + width, y + height);
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
		if (border != null) {
			gc.save();
			gc.setStroke(border.getColor());
			gc.setLineWidth(border.getThickness());
			drawRectangle(gc);
			gc.restore();
		}
		gc.setFill(backgroundColor);
		gc.setStroke(backgroundColor);
		fillRectangle(gc);
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

	public void setBackgroundColor(@NotNull Color paint) {
		this.backgroundColor = paint;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Border getBorder() {
		return border;
	}

	public void setBorder(@Nullable Border border) {
		this.border = border;
	}

	public void setTextColor(@NotNull Color color) {
		this.textColor = color;
	}

	public int getRenderPriority() {
		return 0;
	}

	public static final Comparator<PaintedRegion> RENDER_PRIORITY_COMPARATOR = new Comparator<PaintedRegion>() {
		@Override
		public int compare(PaintedRegion o1, PaintedRegion o2) {
			if (o1.getRenderPriority() < o2.getRenderPriority()) {
				return -1;
			}
			if (o1.getRenderPriority() > o2.getRenderPriority()) {
				return 1;
			}
			return 0;
		}

	};

}
