package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BasicTextRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility.BlinkControlHandler;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.AFont;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Region;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/21/2016 */
public class ButtonRenderer extends ArmaControlRenderer {
	private final BasicTextRenderer textRenderer;
	private final ControlProperty shadowProperty;
	private final ControlProperty offsetXProperty;
	private final ControlProperty offsetYProperty;
	private final BlinkControlHandler blinkControlHandler;

	public ButtonRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
		textRenderer = new BasicTextRenderer(control, this, ControlPropertyLookup.TEXT, ControlPropertyLookup.COLOR_TEXT, ControlPropertyLookup.STYLE, ControlPropertyLookup.SIZE_EX);

		myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				getBackgroundColorObserver().updateValue((AColor) newValue);
			}
		});
		shadowProperty = myControl.findProperty(ControlPropertyLookup.BTN_COLOR_SHADOW);
		shadowProperty.getValueObserver().addListener(renderValueUpdateListener);
		shadowProperty.setDefaultValue(true, new AColor(0d, 0d, 0d, 1d));

		offsetXProperty = myControl.findProperty(ControlPropertyLookup.BTN_OFFSET_X);
		offsetXProperty.getValueObserver().addListener(renderValueUpdateListener);
		offsetYProperty = myControl.findProperty(ControlPropertyLookup.BTN_OFFSET_Y);
		offsetYProperty.getValueObserver().addListener(renderValueUpdateListener);

		blinkControlHandler = new BlinkControlHandler(myControl.findProperty(ControlPropertyLookup.BLINKING_PERIOD));

		myControl.findProperty(ControlPropertyLookup.COLOR_BACKGROUND).setDefaultValue(true, new AColor(getBackgroundColor()));
		myControl.findProperty(ControlPropertyLookup.COLOR_TEXT).setDefaultValue(true, new AColor(getTextColor()));
		myControl.findProperty(ControlPropertyLookup.TEXT).setDefaultValue(true, "");
		myControl.findProperty(ControlPropertyLookup.FONT).setDefaultValue(true, AFont.DEFAULT);
		myControl.findProperty(ControlPropertyLookup.BTN_OFFSET_X).setDefaultValue(true, 0.01);
		myControl.findProperty(ControlPropertyLookup.BTN_OFFSET_Y).setDefaultValue(true, 0.01);

	}

	@Override
	public void paint(@NotNull GraphicsContext gc, @NotNull DataContext dataContext) {
		if (paintPreview(dataContext)) {
			blinkControlHandler.paint(gc, dataContext);
		}
		Paint old = gc.getStroke();
		gc.setStroke(getShadowColor());
		double offsetx = getOffsetX();
		double offsety = getOffsetY();
		int w = (int) (getWidth() * offsetx);
		int h = (int) (getHeight() * offsety);
		Region.fillRectangle(gc, getLeftX() + w, getTopY() + h, getRightX() + w, getBottomY() + h);
		gc.setStroke(old);
		super.paint(gc, dataContext);
		textRenderer.paint(gc);

	}

	private double getOffsetX() {
		if (offsetXProperty.getValue() == null) {
			return 0;
		}
		return offsetXProperty.getFloatValue();
	}

	private double getOffsetY() {
		if (offsetYProperty.getValue() == null) {
			return 0;
		}
		return offsetYProperty.getFloatValue();
	}


	@Override
	public void setTextColor(@NotNull Color color) {
		this.textRenderer.setTextColor(color);
	}

	@Override
	public Color getTextColor() {
		return textRenderer.getTextColor();
	}

	private Color getShadowColor() {
		if (shadowProperty.getValue() instanceof AColor) {
			return ((AColor) shadowProperty.getValue()).toJavaFXColor();
		}
		return Color.BLACK;
	}
}
