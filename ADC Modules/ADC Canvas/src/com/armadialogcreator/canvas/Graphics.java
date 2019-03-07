package com.armadialogcreator.canvas;

import com.armadialogcreator.util.ColorUtil;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

/**
 @author K
 @since 3/6/19 */
public class Graphics {
	private final List<Consumer<Graphics>> paintLast = new ArrayList<>();
	private final GraphicsContext gc;
	private final Stack<Attributes> attributesStack = new Stack<>();
	private final PixelWriter writer;

	public Graphics(@NotNull GraphicsContext gc) {
		this.gc = gc;
		writer = gc.getPixelWriter();
		attributesStack.push(new Attributes());
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
		attributesStack.push(attributesStack.peek().duplicate());
	}

	public void restore() {
		if (attributesStack.size() == 1) {
			throw new IllegalStateException();
		}
		attributesStack.pop();
	}

	public void setFill(int argb) {
		Attributes a = attributesStack.peek();
		a.fillARGB = argb;
		a.fillARGBWithGlobalAlpha = ColorUtil.multiplyAlphaARGB(argb, a.globalAlpha);
	}

	public void setStroke(int argb) {
		Attributes a = attributesStack.peek();
		a.strokeARGB = argb;
		a.strokeARGBWithGlobalAlpha = ColorUtil.multiplyAlphaARGB(argb, a.globalAlpha);
	}

	public void fillRectangle(@NotNull Region r) {
		fillRectangle(r.getLeftX(), r.getTopY(), r.getWidth(), r.getHeight());
	}

	public void strokeRectangle(@NotNull Region r) {
		strokeRectangle(r.getLeftX(), r.getTopY(), r.getWidth(), r.getHeight());
	}

	public void fillRectangle(int x, int y, int w, int h) {
		Attributes a = attributesStack.peek();
		final int offset = Math.max(0, a.lineWidth - 1);
		final int x1 = x - offset;
		final int y1 = y - offset;
		final int x2 = x + w + offset;
		final int y2 = y + h + offset;
		final int color = a.fillARGBWithGlobalAlpha;
		System.out.println("Graphics.fillRectangle area=" + (y2 - y1) * (x2 - x1));
		for (int yy = y1; yy < y2; yy++) {
			for (int xx = x1; xx < x2; xx++) {
				writer.setArgb(xx, yy, color);
			}
		}
	}

	public void strokeRectangle(int x, int y, int w, int h) {
		final int x2 = x + w;
		final int y2 = y + h;
		Attributes a = attributesStack.peek();
		final int color = a.strokeARGBWithGlobalAlpha;

		for (int xx = x; xx <= x2; xx++) {
			//top
			writer.setArgb(xx, y, color);
			//bottom
			writer.setArgb(xx, y2, color);
		}

		for (int yy = y + 1; yy <= y2 - 1; yy++) {
			//left
			writer.setArgb(x, yy, color);
			//right
			writer.setArgb(x2, yy, color);
		}
	}

	public void strokeLine(int x1, int y1, int x2, int y2) {
		final int color = attributesStack.peek().strokeARGBWithGlobalAlpha;

		double slope = (y2 - y1) * 1.0 / (x2 - x1);
		int p = 0;
		for (int x = x1; x < x2; x++) {
			writer.setArgb(x, (int) (y1 + slope * (p++)), color);
		}

	}

	/** Draw the crisp border of a rectangle without filling it using {@link #getGC()} */
	public void gc_strokeRectangleCrisp(int x1, int y1, int x2, int y2) {
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

	/** Draw a crisp line with {@link #getGC()} */
	public void gc_strokeLineCrisp(int x1, int y1, int x2, int y2) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		double x1a = x1 + antiAlias;
		double y1a = y1 + antiAlias;
		double x2a = x2 - antiAlias;
		double y2a = y2 + antiAlias;

		gc.strokeLine(x1a, y1a, x2a, y2a);
	}

	/** Fills a crisp rectangle with {@link #getGC()} */
	public void gc_fillRectangleCrisp(int x1, int y1, int x2, int y2) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		for (int y = y1; y < y2; y++) {
			gc.strokeLine(x1 + antiAlias, y + antiAlias, x2 - antiAlias, y + antiAlias);
		}
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
				setStroke(ColorUtil.toARGB((box + row) % 2 == 0 ? color1 : color2));
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
		Attributes a = attributesStack.peek();
		a.globalAlpha = globalAlpha;
		a.strokeARGBWithGlobalAlpha = ColorUtil.multiplyAlphaARGB(a.strokeARGB, globalAlpha);
		a.fillARGBWithGlobalAlpha = ColorUtil.multiplyAlphaARGB(a.fillARGB, globalAlpha);
	}

	public double getGlobalAlpha() {
		return attributesStack.peek().globalAlpha;
	}

	@NotNull
	public GraphicsContext getGC() {
		return gc;
	}

	public void setLineWidth(int thickness) {
		attributesStack.peek().lineWidth = thickness;
	}

	public int getStroke() {
		return attributesStack.peek().strokeARGB;
	}

	public int getFill() {
		return attributesStack.peek().fillARGB;
	}

	public void drawImage(@NotNull Image image, int x, int y, int w, int h) {
		gc.drawImage(image, x, y, w, h);
	}

	private static class Attributes {
		public double globalAlpha = 1.0;
		public int fillARGB = ColorUtil.toARGB(0, 0, 0, 255);
		public int strokeARGB = ColorUtil.toARGB(0, 0, 0, 255);
		public int lineWidth = 1;
		public int fillARGBWithGlobalAlpha = fillARGB;
		public int strokeARGBWithGlobalAlpha = strokeARGB;

		@NotNull
		public Attributes duplicate() {
			Attributes clone = new Attributes();
			clone.globalAlpha = globalAlpha;
			clone.strokeARGB = strokeARGB;
			clone.fillARGB = fillARGB;
			clone.lineWidth = lineWidth;
			clone.fillARGBWithGlobalAlpha = fillARGBWithGlobalAlpha;
			clone.strokeARGBWithGlobalAlpha = strokeARGBWithGlobalAlpha;

			return clone;
		}
	}
}
