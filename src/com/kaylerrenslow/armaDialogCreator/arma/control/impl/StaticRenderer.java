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
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
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
		Text, Image, Error, ErrorImage
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

	public StaticRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this, ControlPropertyLookup.TEXT, ControlPropertyLookup.COLOR_TEXT, ControlPropertyLookup.STYLE, ControlPropertyLookup.SIZE_EX);

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
		if (renderTypeForStyle == RenderType.Image) {

			ImageHelper.getImageAsync(textValue, image -> {
				synchronized (StaticRenderer.this) {
					renderType = image != null ? RenderType.Image : RenderType.ErrorImage;
					imageToPaint = image;
					requestRender();
				}
				return null;
			});

			return;
		} else {
			imageToPaint = null;
			renderType = renderTypeForStyle;
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
				case Image: {
					//paint the background color
					super.paint(gc, dataContext);
					if (imageToPaint == null) {
						throw new IllegalStateException("imageToPaint is null");
					}
					gc.drawImage(imageToPaint, getX1(), getY1(), getWidth(), getHeight());
					break;
				}
				case ErrorImage: {
					//paint the background color
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


	public void setTextColor(@NotNull Color color) {
		this.textRenderer.setTextColor(color);
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
			for (ControlStyle style : group.getValues()) {
				if (style == pictureStyle()) {
					return RenderType.Image;
				}
			}
		}
		//		if (value instanceof SVExpression) {
		//			SVExpression expr = (SVExpression) value;
		//			//check if bits required for ControlStyle.Picture.styleValue are 1's
		//			int v = (int) expr.getNumVal();
		//			int styleV = pictureStyle().styleValue;
		//			if ((v & styleV) == styleV) {
		//				return RenderType.Image;
		//			}
		//		}
		//		String[] values = value.getAsStringArray();
		//		for (String s : values) {
		//			if (s.contains(pictureStyle().styleValue + "")) {
		//				return RenderType.Image;
		//			}
		//		}
		return RenderType.Text;
	}

	@NotNull
	private static ControlStyle pictureStyle() {
		return ControlStyle.PICTURE;
	}

}
