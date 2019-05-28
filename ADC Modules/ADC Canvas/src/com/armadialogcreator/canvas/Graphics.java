package com.armadialogcreator.canvas;

import com.armadialogcreator.util.ColorUtil;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 @author K
 @since 3/6/19 */
public class Graphics {
	private final List<Consumer<Graphics>> paintLast = new ArrayList<>();
	private final GraphicsContext gc;

	public Graphics(@NotNull GraphicsContext gc) {
		this.gc = gc;
	}

	/**
	 Use this function to paint something after the initial {@link UICanvas#paint()} is invoked.

	 @param paint the function to use to paint. The returned value of the function is ignored
	 */
	public void paintLast(@NotNull Consumer<Graphics> paint) {
		paintLast.add(paint);
	}

	@NotNull
	protected List<Consumer<Graphics>> getPaintLast() {
		return paintLast;
	}

	public void save() {
		gc.save();
	}

	public void restore() {
		gc.restore();
	}

	public void setFill(int argb) {
		gc.setFill(ColorUtil.toColor(argb));
	}

	public void setStroke(int argb) {
		gc.setStroke(ColorUtil.toColor(argb));
	}

	public void fillRectangle(@NotNull Region r) {
		fillRectangle(r.getLeftX(), r.getTopY(), r.getWidth(), r.getHeight());
	}

	public void strokeRectangle(@NotNull Region r) {
		strokeRectangle(r.getLeftX(), r.getTopY(), r.getWidth(), r.getHeight());
	}

	public void fillRectangle(int x, int y, int w, int h) {
		gc_fillRectangleCrisp(x, y, w, h);
	}

	public void strokeRectangle(int x, int y, int w, int h) {
		final int x2 = x + w;
		final int y2 = y + h;
		gc_strokeRectangleCrisp(x, y, x2, y2);
	}

	public void strokeLine(int x1, int y1, int x2, int y2) {
		gc_strokeLineCrisp(x1, y1, x2, y2);
	}

	private void gc_strokeRectangleCrisp(int x1, int y1, int x2, int y2) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		double x1a = x1 + antiAlias;
		double y1a = y1 + antiAlias;
		double x2a = x2 - antiAlias;
		double y2a = y2 - antiAlias;

		gc.strokeLine(x1a, y1a, x2a, y1a); //top left to top right
		gc.strokeLine(x2a, y1a, x2a, y2a); //top right to bottom right
		gc.strokeLine(x2a, y2a, x1a, y2a); //bottom right to bottom left
		gc.strokeLine(x1a, y2a, x1a, y1a); //bottom left to top left
	}

	private void gc_strokeLineCrisp(int x1, int y1, int x2, int y2) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		double x1a = x1 + antiAlias;
		double y1a = y1 + antiAlias;
		double x2a = x2 - antiAlias;
		double y2a = y2 + antiAlias;

		gc.strokeLine(x1a, y1a, x2a, y2a);
	}

	private void gc_fillRectangleCrisp(int x1, int y1, int w, int h) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		// for some reason, using this instead creates random vertical lines when using drop shadow behind
		// gc.fillRect(x1 + antiAlias, y1 + antiAlias, w - antiAlias, h + antiAlias);
		final int x2 = x1 + w;
		final int y2 = y1 + h;
		Paint old = gc.getStroke();
		gc.setStroke(gc.getFill());
		for (int y = y1; y < y2; y++) {
			gc.strokeLine(x1 + antiAlias, y + antiAlias, x2 - antiAlias, y + antiAlias);
		}
		gc.setStroke(old);
	}

	/**
	 Paint a checkerboard with the given {@link GraphicsContext} and coordinates

	 @param x x pos
	 @param y y pos
	 @param w width
	 @param h height
	 @param color1 one of the colors to use
	 @param color2 other color to use
	 @param numBoxes number of boxes across horizontal and vertical
	 */
	public void paintCheckerboard(int x, int y, int w, int h,
								  @NotNull Color color1, @NotNull Color color2, int numBoxes) {
		final int boxWidth = w / numBoxes;
		final int boxHeight = h / numBoxes;
		final int remainderWidth = w - numBoxes * boxWidth;
		final int remainderHeight = h - numBoxes * boxHeight;

		save();
		for (int row = 0; row < numBoxes || (row == numBoxes && remainderHeight > 0); row++) {
			//doing <= to make sure that the full height and width is painted
			//since the aspect ratio of the checkerboard may not be equal to aspect ratio of the given width and height

			int yy = y + row * boxHeight;
			for (int box = 0; box < numBoxes || (box == numBoxes && remainderWidth > 0); box++) {
				setStroke((box + row) % 2 == 0 ? color1 : color2);
				int xx = x + box * boxWidth;
				fillRectangle(
						xx, yy,
						(box == numBoxes ? remainderWidth : boxWidth),
						(row == numBoxes ? remainderHeight : boxHeight)
				);
			}
		}

		restore();
	}

	/**
	 Paint a checkerboard with the given {@link GraphicsContext} and coordinates

	 @param x x pos
	 @param y y pos
	 @param w width
	 @param h height
	 @param color1 one of the colors to use
	 @param color2 other color to use
	 @param numBoxes number of boxes across horizontal and vertical
	 */
	public static void paintCheckerboard(@NotNull Graphics g, int x, int y, int w, int h,
										 @NotNull Color color1, @NotNull Color color2, int numBoxes) {
		g.paintCheckerboard(x, y, w, h, color1, color2, numBoxes);
	}

	public void setGlobalAlpha(double globalAlpha) {
		gc.setGlobalAlpha(globalAlpha);
	}

	public double getGlobalAlpha() {
		return gc.getGlobalAlpha();
	}

	public void clip() {
		gc.clip();
	}

	public void rect(int x, int y, int w, int h) {
		gc.rect(x, y, w, h);
	}

	public void beginPath() {
		gc.beginPath();
	}

	public void closePath() {
		gc.closePath();
	}

	public void setLineWidth(int thickness) {
		gc.setLineWidth(thickness);
	}

	public int getStrokeARGB() {
		return ColorUtil.toARGB((Color) gc.getStroke());
	}

	public int getFillARGB() {
		return ColorUtil.toARGB((Color) gc.getFill());
	}

	public void setStroke(@NotNull Paint p) {
		gc.setStroke(p);
	}

	public void setFill(@NotNull Paint p) {
		gc.setFill(p);
	}

	public void setEffect(@NotNull Effect e) {
		gc.setEffect(e);
	}

	public void setFont(@NotNull Font f) {
		gc.setFont(f);
	}

	public void fillText(@NotNull String text, double x, double y) {
		gc.fillText(text, x, y);
	}

	public void strokeText(@NotNull String text, double x, double y) {
		gc.strokeText(text, x, y);
	}

	public void translate(double x, double y) {
		gc.translate(x, y);
	}

	public void scale(double x, double y) {
		gc.scale(x, y);
	}

	public void rotate(double degrees) {
		gc.rotate(degrees);
	}

	@NotNull
	public Paint getStroke() {
		return gc.getStroke();
	}

	@NotNull
	public Paint getFill() {
		return gc.getFill();
	}

	public void drawImage(@NotNull Image image, int x, int y, int w, int h) {
		gc.drawImage(image, x, y, w, h);
	}

	public void setGlobalBlendMode(@NotNull BlendMode mode) {
		gc.setGlobalBlendMode(mode);
	}

	public void setTextBaseline(@NotNull VPos pos) {
		gc.setTextBaseline(pos);
	}

	/** Fills a rectangle with no anti aliasing */
	public void fillRectNoAA(int x, int y, double width, double height) {
		gc.fillRect(x, y, width, height);
	}

	public void setLineDashes(double... dash) {
		gc.setLineDashes(dash);
	}

	public void setLineDashOffset(int offset) {
		gc.setLineDashOffset(offset);
	}
}
