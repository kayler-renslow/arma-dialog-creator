package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ArmaResolution;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Used for a controls with type=0 (Static)
 Created on 05/25/2016. */
public class StaticControl extends ArmaControl {

	private ControlProperty backgroundColorProperty, colorTextProperty, textProperty;

	public StaticControl(@NotNull String name, int idc, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull ArmaResolution resolution) {
		super(name, idc, ControlType.STATIC, style, x, y, width, height, resolution, StaticRenderer.class, null, null);
		backgroundColorProperty = ControlPropertyLookup.COLOR_BACKGROUND.getColorProperty(new AColor(renderer.getBackgroundColor()));
		colorTextProperty = ControlPropertyLookup.COLOR_TEXT.getColorProperty(new AColor(renderer.getTextColor()));
		textProperty = ControlPropertyLookup.TEXT.getStringProperty("");

		addRequiredProperties(backgroundColorProperty);
		addRequiredProperties(colorTextProperty);
		addRequiredProperties(ControlPropertyLookup.FONT.getFontProperty(AFont.PuristaMedium));
		addRequiredProperties(textProperty);

		addOptionalProperties(ControlPropertyLookup.MOVING.getPropertyWithNoData());
		addOptionalProperties(ControlPropertyLookup.SHADOW.getPropertyWithNoData());
		addOptionalProperties(ControlPropertyLookup.TOOLTIP.getPropertyWithNoData());
		addOptionalProperties(ControlPropertyLookup.TOOLTIP_COLOR_SHADE.getPropertyWithNoData()); //todo set universal default values?
		addOptionalProperties(ControlPropertyLookup.TOOLTIP_COLOR_BOX.getPropertyWithNoData());
		addOptionalProperties(ControlPropertyLookup.STATIC_FIXED_WIDTH.getPropertyWithNoData());
		addOptionalProperties(ControlPropertyLookup.STATIC_LINE_SPACING.getPropertyWithNoData());
		addOptionalProperties(ControlPropertyLookup.BLINKING_PERIOD.getPropertyWithNoData());
	}

	@Override
	protected void updateProperties() {
		super.updateProperties();
		StaticRenderer renderer = (StaticRenderer) getRenderer();
		renderer.getBackgroundColorObserver().updateValue(new AColor(backgroundColorProperty.getValues()));
		renderer.setText(textProperty.getStringValue());
		renderer.setTextColor(AColor.toJavaFXColor(colorTextProperty.getValues()));
	}
}
