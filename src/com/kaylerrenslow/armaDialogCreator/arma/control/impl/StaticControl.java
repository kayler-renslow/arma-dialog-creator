package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Used for a controls with type=0 (Static)
 Created on 05/25/2016. */
public class StaticControl extends ArmaControl {

	public StaticControl(@NotNull String name, int idc, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull Resolution resolution) {
		super(name, idc, ControlType.STATIC, style, x, y, width, height, resolution, StaticRenderer.class, null, null);
		ControlProperty backgroundColorProperty = ControlPropertiesLookup.COLOR_BACKGROUND.getColorProperty(new AColor(1d, 1d, 1d, 1d));

		addRequiredProperties(backgroundColorProperty);
		addRequiredProperties(ControlPropertiesLookup.COLOR_TEXT.getColorProperty(new AColor(0d, 0d, 0d, 1d)));
		addRequiredProperties(ControlPropertiesLookup.FONT.getFontProperty(AFont.PuristaMedium));
		addRequiredProperties(ControlPropertiesLookup.TEXT.getStringProperty(""));

		addOptionalProperties(ControlPropertiesLookup.MOVING.getBooleanProperty(false));
		addOptionalProperties(ControlPropertiesLookup.SHADOW.getPropertyFromOption(0));
		addOptionalProperties(ControlPropertiesLookup.TOOLTIP.getPropertyWithNoData(1));
		addOptionalProperties(ControlPropertiesLookup.TOOLTIP_COLOR_SHADE.getPropertyWithNoData(AColor.ARRAY_SIZE)); //todo set universal default values?
		addOptionalProperties(ControlPropertiesLookup.TOOLTIP_COLOR_BOX.getPropertyWithNoData(AColor.ARRAY_SIZE));
		addOptionalProperties(ControlPropertiesLookup.STATIC_FIXED_WIDTH.getFloatProperty(0));
		addOptionalProperties(ControlPropertiesLookup.STATIC_LINE_SPACING.getFloatProperty(0));
		addOptionalProperties(ControlPropertiesLookup.BLINKING_PERIOD.getFloatProperty(0));

		backgroundColorProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
			@Override
			public void valueUpdated(String[] oldValue, String[] newValue) {
				getRenderer().getBackgroundColorObserver().updateValue(new AColor(newValue));
			}
		});
	}

}
