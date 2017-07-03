package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BasicTextRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.ImageHelper;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Region;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A renderer for {@link StaticControl}

 @author Kayler
 @since 05/25/2016 */
public class StaticRenderer extends ArmaControlRenderer {

	private enum RenderType {
		Text, Image, Error, ErrorImage, Line, Frame
	}

	private final BlinkControlHandler blinkControlHandler;
	private final ControlProperty styleProperty;
	private final ControlProperty textProperty;

	private BasicTextRenderer textRenderer;

	/**
	 If false, the background color can't be determined so there will be a checkerboard rendered.
	 If true, the {@link #getBackgroundColor()} will be used to paint the control.
	 */
	private boolean useBackgroundColor = true;

	private volatile RenderType renderType = RenderType.Text;
	/** The image to paint, or null if not set */
	private volatile Image imageToPaint = null;
	private SerializableValue styleValue = null;
	private RenderType renderTypeForStyle = RenderType.Error;
	private boolean keepImageAspectRatio = false;

	public StaticRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this,
				ControlPropertyLookup.TEXT, ControlPropertyLookup.COLOR_TEXT,
				ControlPropertyLookup.STYLE, ControlPropertyLookup.SIZE_EX,
				ControlPropertyLookup.SHADOW
		);

		ControlProperty colorBackground = myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND);

		colorBackground.getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof SVColor) {
					getBackgroundColorObserver().updateValue((SVColor) newValue);
					useBackgroundColor = true;
				} else {
					useBackgroundColor = false;
				}
			}
		});
		colorBackground.setValueIfAbsent(true, new SVColorArray(getBackgroundColor()));

		if (colorBackground.getValue() instanceof SVColor) {
			setBackgroundColor(((SVColor) colorBackground.getValue()).toJavaFXColor());
		} else {
			useBackgroundColor = false;
		}

		myControl.findProperty(ControlPropertyLookup.COLOR_TEXT).setValueIfAbsent(true, new SVColorArray(getTextColor()));

		styleProperty = myControl.findProperty(ControlPropertyLookup.STYLE);
		styleProperty.getValueObserver().addListener((observer, oldValue, newValue) -> {
			if (newValue instanceof SVControlStyleGroup) {
				SVControlStyleGroup group = (SVControlStyleGroup) newValue;
				keepImageAspectRatio = group.hasStyle(ControlStyle.KEEP_ASPECT_RATIO);
			}
			renderTypeForStyle = getRenderTypeFromStyle();
			styleValue = newValue;
			checkAndSetRenderType();
		});

		textProperty = myControl.findProperty(ControlPropertyLookup.TEXT);

		textProperty.setValueIfAbsent(true, SVString.newEmptyString());
		textProperty.getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
				checkAndSetRenderType();
			}
		});

		myControl.findProperty(ControlPropertyLookup.FONT).setValueIfAbsent(true, SVFont.DEFAULT);
		blinkControlHandler = new BlinkControlHandler(myControl.findProperty(ControlPropertyLookup.BLINKING_PERIOD));

		renderTypeForStyle = getRenderTypeFromStyle();
		checkAndSetRenderType();
	}

	private void checkAndSetRenderType() {
		SerializableValue textValue = textProperty.getValue();
		switch (renderTypeForStyle) {
			case Image: {
				ImageHelper.getImageAsync(textValue, image -> {
					synchronized (StaticRenderer.this) {
						renderType = image != null ? RenderType.Image : RenderType.ErrorImage;
						imageToPaint = image;
						requestRender();
					}
					return null;
				});
				//nothing left to do since its async
				return;
			}
			default: {
				imageToPaint = null;
				renderType = renderTypeForStyle;
			}
		}
		requestRender();
	}

	public synchronized void paint(@NotNull GraphicsContext gc, @NotNull DataContext dataContext) {
		if (paintPreview(dataContext)) {
			blinkControlHandler.paint(gc, dataContext);
		}
		if (!useBackgroundColor) {
			paintBackgroundError(gc);
		} else {
			switch (renderType) {
				case Text: {
					super.paint(gc, dataContext);
					textRenderer.paint(gc);
					break;
				}
				case Line: {
					//draw line from top left of control to bottom right of control
					//the text color is the color of the line
					gc.setStroke(getTextColor());
					gc.strokeLine(getLeftX(), getTopY(), getRightX(), getBottomY());
					break;
				}
				case Frame: {
					gc.setStroke(getTextColor());

					int controlWidth = getWidth();

					int textWidth = 0;
					double padding = controlWidth * .02;
					int xLeftOfText = (int) Math.round(x1 + padding);

					//draw the text, if the length is > 0
					if (textRenderer.getText().length() > 0) {
						textWidth = textRenderer.getTextWidth();
						if (textWidth < controlWidth - (2 * padding)) {
							//text will paint within the bounds of the frame
							textRenderer.paint(gc, xLeftOfText, y1 + textRenderer.getTextHeight() / 2);
						} else {
							//don't paint any text if the text is longer than the frame's width
							textWidth = 0;
						}
					}


					//draw the frame itself
					//in Arma 3, the top line is crisp, while the other lines are blurred and 2 pixels in width

					//draw top line
					if (textWidth > 0) {
						Region.strokeLine(gc, x1, y1, xLeftOfText, y1);
						Region.strokeLine(gc, xLeftOfText + textWidth, y1, x2, y1);
					} else {
						Region.strokeLine(gc, x1, y1, x2, y1);
					}
					//+0.5 to make the line start crisp
					gc.strokeLine(x2, y1 + 0.5, x2, y2); //right line
					gc.strokeLine(x1, y2, x2, y2); //bottom line
					gc.strokeLine(x1, y1 + 0.5, x1, y2); //left line

					break;
				}
				case Image: {
					if (imageToPaint == null) {
						throw new IllegalStateException("imageToPaint is null");
					}
					if (keepImageAspectRatio) {
						int imgWidth = (int) imageToPaint.getWidth();
						int imgHeight = (int) imageToPaint.getHeight();
						double aspectRatio = imgWidth * 1.0 / imgHeight;

						//We want to make sure that the image doesn't surpass the bounds of the control
						//while also maintaining the aspect ratio. In arma 3, the height of the image will
						//never surpass the height of the control. The width is allowed to surpass the bounds though.

						int drawHeight = getHeight();
						int drawWidth = (int) Math.round(drawHeight * aspectRatio);

						//after the image as been resized to aspect ratio, center the image
						int centerX = getX1() + (getWidth() - drawWidth) / 2;

						gc.drawImage(imageToPaint, centerX, getY1(), drawWidth, drawHeight);

						//multiply the text color on the image
						gc.setStroke(getTextColor());
						gc.setGlobalBlendMode(BlendMode.MULTIPLY);
						Region.fillRectangle(gc, centerX, getY1(), centerX + drawWidth, getY1() + drawHeight);
					} else {
						gc.drawImage(imageToPaint, getX1(), getY1(), getWidth(), getHeight());
					}
					break;
				}
				case ErrorImage: {
					paintBadImageError(gc);
					break;
				}
				case Error: {
					paintBackgroundError(gc);
					break;
				}
				default: {
					throw new IllegalStateException("unhandled renderType:" + renderType);
				}
			}
		}
	}

	private void paintBackgroundError(@NotNull GraphicsContext gc) {
		paintCheckerboard(gc, getX1(), getY1(), getWidth(), getHeight(), Color.BLACK, getBackgroundColor());
	}

	private void paintBadImageError(@NotNull GraphicsContext gc) {
		paintCheckerboard(gc, getX1(), getY1(), getWidth(), getHeight(), Color.RED, getBackgroundColor());
	}

	@NotNull
	public Color getTextColor() {
		return textRenderer.getTextColor();
	}

	/**
	 @return the {@link RenderType} to use, or {@link #renderTypeForStyle}
	 if the style value didn't change
	 */
	@NotNull
	private RenderType getRenderTypeFromStyle() {
		SerializableValue value = styleProperty.getValue();
		if (value == null) {
			return RenderType.Error;
		}
		if (value == styleValue) {
			return renderTypeForStyle;
		}
		if (value instanceof SVControlStyleGroup) {
			SVControlStyleGroup group = (SVControlStyleGroup) value;
			for (ControlStyle style : group.getStyleArray()) {
				if (style == ControlStyle.PICTURE) {
					return RenderType.Image;
				}
				if (style == ControlStyle.LINE) {
					return RenderType.Line;
				}
				if (style == ControlStyle.FRAME) {
					return RenderType.Frame;
				}
			}
		}
		return RenderType.Text;
	}

}
