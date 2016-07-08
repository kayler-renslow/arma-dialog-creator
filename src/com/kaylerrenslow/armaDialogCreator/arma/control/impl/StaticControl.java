package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Used for a controls with type=0 (Static)
 Created on 05/25/2016. */
public class StaticControl extends ArmaControl {

	private ControlProperty backgroundColorProperty, colorTextProperty, textProperty;

	public final static ArmaControlSpecProvider SPEC_PROVIDER = new ArmaControlSpecProvider(){

		private final ControlPropertyLookup[] REQUIRED_PROPERTIES = ArrayUtil.mergeArrays(ControlPropertyLookup.class, DEFAULT_REQUIRED_PROPERTIES, new ControlPropertyLookup[]{
				ControlPropertyLookup.COLOR_BACKGROUND,
				ControlPropertyLookup.COLOR_TEXT,
				ControlPropertyLookup.TEXT,
				ControlPropertyLookup.FONT,
		});

		private final ControlPropertyLookup[] OPTIONAL_PROPERTIES = ArrayUtil.mergeArrays(ControlPropertyLookup.class, DEFAULT_OPTIONAL_PROPERTIES, new ControlPropertyLookup[]{
				ControlPropertyLookup.MOVING,
				ControlPropertyLookup.SHADOW,
				ControlPropertyLookup.TOOLTIP,
				ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
				ControlPropertyLookup.TOOLTIP_COLOR_BOX,
				ControlPropertyLookup.STATIC_FIXED_WIDTH,
				ControlPropertyLookup.STATIC_LINE_SPACING,
				ControlPropertyLookup.BLINKING_PERIOD
		});

		@NotNull
		@Override
		public ControlPropertyLookup[] getRequiredProperties() {
			return REQUIRED_PROPERTIES;
		}

		@NotNull
		@Override
		public ControlPropertyLookup[] getOptionalProperties() {
			return OPTIONAL_PROPERTIES;
		}
	};

	public StaticControl(@NotNull String name, int idc, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull ArmaResolution resolution) {
		super(name, SPEC_PROVIDER, idc, ControlType.STATIC, style, x, y, width, height, resolution, StaticRenderer.class);
		backgroundColorProperty = findRequiredProperty(ControlPropertyLookup.COLOR_BACKGROUND);
		backgroundColorProperty.setDefaultValue(true, new AColor(renderer.getBackgroundColor()));
		colorTextProperty = findRequiredProperty(ControlPropertyLookup.COLOR_TEXT);
		colorTextProperty.setDefaultValue(true, new AColor(renderer.getTextColor()));
		textProperty = findRequiredProperty(ControlPropertyLookup.TEXT);
		textProperty.setDefaultValues(true, "");

		findRequiredProperty(ControlPropertyLookup.FONT).setDefaultValue(true, AFont.DEFAULT);

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
