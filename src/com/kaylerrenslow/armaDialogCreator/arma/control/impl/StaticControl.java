package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecProvider;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.AFont;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Used for a controls with type=0 (Static)
 Created on 05/25/2016. */
public class StaticControl extends ArmaControl {
	
	public final static ArmaControlSpecProvider SPEC_PROVIDER = new ArmaControlSpecProvider() {
		
		private final ControlPropertyLookup[] requiredProperties = ArrayUtil.mergeArrays(ControlPropertyLookup.class, DEFAULT_REQUIRED_PROPERTIES, new ControlPropertyLookup[]{
				ControlPropertyLookup.COLOR_BACKGROUND,
				ControlPropertyLookup.COLOR_TEXT,
				ControlPropertyLookup.TEXT,
				ControlPropertyLookup.FONT,
		});
		
		private final ControlPropertyLookup[] optionalProperties = ArrayUtil.mergeArrays(ControlPropertyLookup.class, DEFAULT_OPTIONAL_PROPERTIES, new ControlPropertyLookup[]{
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
			return requiredProperties;
		}
		
		@NotNull
		@Override
		public ControlPropertyLookup[] getOptionalProperties() {
			return optionalProperties;
		}
	};
	
	public StaticControl(@NotNull String name, int idc, @NotNull ControlStyle style, Expression x, Expression y, Expression width, Expression height, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(name, SPEC_PROVIDER, idc, ControlType.STATIC, style, x, y, width, height, resolution, RendererLookup.STATIC, env);
		findRequiredProperty(ControlPropertyLookup.COLOR_BACKGROUND).setDefaultValue(true, new AColor(renderer.getBackgroundColor()));
		findRequiredProperty(ControlPropertyLookup.COLOR_TEXT).setDefaultValue(true, new AColor(renderer.getTextColor()));
		findRequiredProperty(ControlPropertyLookup.TEXT).setDefaultValue(true, "");
		findRequiredProperty(ControlPropertyLookup.FONT).setDefaultValue(true, AFont.DEFAULT);
	}
	
}
