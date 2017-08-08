package com.kaylerrenslow.armaDialogCreator.arma.control;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 08/07/2017 */
public class TintedImageHelperRenderer {

	private final Effect effect1, effect2;
	private boolean previewMode = false;
	private int x, y, w, h;
	private final PerspectiveTransform imgTrans1 = new PerspectiveTransform();
	private final ImageInput imgInput1 = new ImageInput();
	private final ColorInput colorInput1 = new ColorInput();
	private final PerspectiveTransform imgTrans2 = new PerspectiveTransform();
	private final ImageInput imgInput2 = new ImageInput();
	private final ColorInput colorInput2 = new ColorInput();
	private boolean flipped = false;
	private boolean rotated;
	private double rotateDeg;

	public TintedImageHelperRenderer() {
		imgTrans1.setInput(imgInput1);
		this.effect1 = new Blend(
				BlendMode.MULTIPLY,
				imgTrans1,
				new Blend(
						BlendMode.SRC_ATOP,
						imgTrans1,
						colorInput1
				)
		);
		imgTrans2.setInput(imgInput2);
		this.effect2 = new Blend(
				BlendMode.MULTIPLY,
				imgTrans2,
				new Blend(
						BlendMode.SRC_ATOP,
						imgTrans2,
						colorInput2
				)
		);
	}

	/**
	 For some reason, JavaFX has each of the Effect's properties change on different threads, making a single {@link Effect}
	 unreliable for use in preview and just editing mode of Arma controls. A workaround for this is to have 2 Effect instances
	 that are identical.
	 <p>
	 You can ignore 'preview mode" if the tint applies to both preview and non-preview.

	 @param previewMode use true so that methods will modify the preview effect, false so that methods will modify the non-preview effect
	 */
	public void setToPreviewMode(boolean previewMode) {
		this.previewMode = previewMode;
	}

	/** This method applies to {@link #setToPreviewMode(boolean)} */
	public void updateEffect(@NotNull Image img, @NotNull Paint tintColor, int x, int y, int w, int h) {
		updateEffect(img, tintColor, x, y, w, h, true);
	}

	/** @param modifyBoth set to true if both preview and non-preview effect should be modified, false otherwise */
	public void updateEffect(@NotNull Image img, @NotNull Paint tintColor, int x, int y, int w, int h, boolean modifyBoth) {
		updatePosition(x, y, w, h, modifyBoth);

		updateImage(img, modifyBoth);
		updateTint(tintColor, modifyBoth);
	}

	/** This method applies to {@link #setToPreviewMode(boolean)} */
	public void updateImage(@Nullable Image img) {
		updateImage(img, true);
	}

	/** This method applies to {@link #setToPreviewMode(boolean)} */
	public void updateTint(@NotNull Paint tintColor) {
		updateTint(tintColor, true);
	}

	/** @param modifyBoth set to true if both preview and non-preview effect should be modified, false otherwise */
	public void updateImage(@Nullable Image img, boolean modifyBoth) {
		if (modifyBoth || previewMode) {
			imgInput2.setSource(img);
		}
		if (modifyBoth || !previewMode) {
			imgInput1.setSource(img);
		}
	}

	/** @param modifyBoth set to true if both preview and non-preview effect should be modified, false otherwise */
	public void updateTint(@NotNull Paint tintColor, boolean modifyBoth) {
		if (modifyBoth || previewMode) {
			colorInput2.setPaint(tintColor);
		}
		if (modifyBoth || !previewMode) {
			colorInput1.setPaint(tintColor);
		}
	}

	/** This method applies to {@link #setToPreviewMode(boolean)} */
	public void updatePosition(int x, int y, int w, int h) {
		updatePosition(x, y, w, h, true);
	}

	/** @param modifyBoth set to true if both preview and non-preview effect should be modified, false otherwise */
	public void updatePosition(int x, int y, int w, int h, boolean modifyBoth) {
		int ulx, uly, urx, ury, lrx, lry, llx, lly, colorx, colory = y;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		if (flipped) {
			ulx = -x - w;
			uly = y;
			urx = -x;
			ury = y;
			lrx = -x;
			lry = y + h;
			llx = -x - w;
			lly = y + h;

			colorx = -x - w;
		} else if (rotated) {
			int centerx = -w / 2;
			int centery = -h / 2;

			colorx = centerx;
			colory = centery;

			ulx = centerx;
			uly = centery;
			urx = centerx + w;
			ury = centery;
			lrx = centerx + w;
			lry = centery + h;
			llx = centerx;
			lly = centery + h;
		} else {
			colorx = x;

			ulx = x;
			uly = y;
			urx = x + w;
			ury = y;
			lrx = x + w;
			lry = y + h;
			llx = x;
			lly = y + h;
		}

		if (modifyBoth || previewMode) {
			updateTransAndColor(w, h,
					ulx, uly, urx, ury, lrx, lry, llx, lly,
					colorx, colory, colorInput2, imgTrans2);
		}
		if (modifyBoth || !previewMode) {
			updateTransAndColor(w, h,
					ulx, uly, urx, ury, lrx, lry, llx, lly,
					colorx, colory, colorInput1, imgTrans1);
		}
	}

	private void updateTransAndColor(int w, int h,
									 int ulx, int uly, int urx, int ury, int lrx, int lry, int llx, int lly,
									 int colorx, int colory, ColorInput colorInput, PerspectiveTransform imgTrans) {
		colorInput.setX(colorx);
		colorInput.setY(colory);
		colorInput.setWidth(w);
		colorInput.setHeight(h);

		imgTrans.setUlx(ulx);
		imgTrans.setUly(uly);
		imgTrans.setUrx(urx);
		imgTrans.setUry(ury);
		imgTrans.setLrx(lrx);
		imgTrans.setLry(lry);
		imgTrans.setLlx(llx);
		imgTrans.setLly(lly);
	}

	/** This method applies to {@link #setToPreviewMode(boolean)} */
	public void paintTintedImage(@NotNull GraphicsContext gc) {
		gc.save();
		if (rotated) {
			gc.translate(x + w / 2, y + h / 2); //move to center
			gc.rotate(rotateDeg);
		}
		if (flipped) {
			gc.scale(-1, 1);
		}
		gc.setFill(Color.TRANSPARENT);
		gc.setEffect(previewMode ? effect2 : effect1);
		gc.fillRect(0, 0, 1, 1); //this is just to trigger drawing the effect. Won't draw anything itself
		gc.restore();
	}

	/**
	 This method <b>DOES NOT</b> apply to {@link #setToPreviewMode(boolean)}.
	 This will affect both the preview and non-preview {@link Effect}
	 */
	public void flipHorizontally() {
		rotated = false;
		this.flipped = !flipped;

		updatePosition(x, y, w, h, false);
	}

	/**
	 This method <b>DOES NOT</b> apply to {@link #setToPreviewMode(boolean)}.
	 This will affect both the preview and non-preview {@link Effect}
	 */
	public void rotate(double deg) {
		flipped = false;
		this.rotated = true;
		this.rotateDeg = deg;

		updatePosition(x, y, w, h, false);
	}

	@NotNull
	public Paint getTint() {
		return colorInput1.getPaint();
	}

	@NotNull
	public Paint getTintPreview() {
		return colorInput2.getPaint();
	}

}
