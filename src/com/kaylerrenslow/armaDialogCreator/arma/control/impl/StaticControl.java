package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 Used for a controls with type=0 (Static)

 @author Kayler
 @since 05/25/2016. */
public class StaticControl extends ArmaControl {

	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new ArmaControlSpecRequirement() {

		private final ReadOnlyList<ControlPropertyLookupConstant> requiredProperties = new ReadOnlyList<>(ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
				new ControlPropertyLookup[]{
						ControlPropertyLookup.COLOR_BACKGROUND,
						ControlPropertyLookup.COLOR_TEXT,
						ControlPropertyLookup.TEXT,
						ControlPropertyLookup.FONT,
						ControlPropertyLookup.SIZE_EX
				},
				ControlPropertyLookupConstant.PRIORITY_SORT)
		);

		private final ReadOnlyList<ControlPropertyLookupConstant> optionalProperties = new ReadOnlyList<>(ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultOptionalProperties,
				new ControlPropertyLookup[]{
						ControlPropertyLookup.MOVING,
						ControlPropertyLookup.SHADOW,
						ControlPropertyLookup.TOOLTIP,
						ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
						ControlPropertyLookup.TOOLTIP_COLOR_BOX,
						ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
						ControlPropertyLookup.STATIC_FIXED_WIDTH,
						ControlPropertyLookup.STATIC_LINE_SPACING,
						ControlPropertyLookup.BLINKING_PERIOD
				},
				ControlPropertyLookupConstant.PRIORITY_SORT)
		);

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return requiredProperties;
		}

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
			return optionalProperties;
		}

		private final ControlStyle[] allowedStyles = {
				ControlStyle.LEFT,
				ControlStyle.RIGHT,
				ControlStyle.CENTER,
				ControlStyle.MULTI,
				ControlStyle.TITLE_BAR,
				ControlStyle.PICTURE,
				ControlStyle.FRAME,
				ControlStyle.BACKGROUND,
				ControlStyle.GROUP_BOX,
				ControlStyle.GROUP_BOX2,
				ControlStyle.HUD_BACKGROUND,
				ControlStyle.WITH_RECT,
				ControlStyle.LINE,
				ControlStyle.SHADOW,
				ControlStyle.NO_RECT,
				ControlStyle.KEEP_ASPECT_RATIO
		};

		@Override
		public ControlStyle[] getAllowedStyles() {
			return allowedStyles;
		}
	};

	public StaticControl(@NotNull String name, int idc, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(ControlType.Static, name, SPEC_PROVIDER, idc, ControlStyle.CENTER.getStyleGroup(), resolution, RendererLookup.Static, env, registry);
	}

}
