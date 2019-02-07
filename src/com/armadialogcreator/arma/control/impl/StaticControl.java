package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlSpecRequirement;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.old.*;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 Used for a controls with type=0 (Static)

 @author Kayler
 @since 05/25/2016. */
public class StaticControl extends ArmaControl {

	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public StaticControl(@NotNull String name, int idc, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.Static, resolution, env, registry);
		findProperty(ControlPropertyLookup.STYLE).setValueIfAbsent(true, ControlStyle.CENTER.getStyleGroup());
		findProperty(ControlPropertyLookup.IDC).setDefaultValue(true, idc);
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
							new ControlPropertyLookup[]{
									ControlPropertyLookup.COLOR_BACKGROUND,
									ControlPropertyLookup.COLOR_TEXT,
									ControlPropertyLookup.TEXT,
									ControlPropertyLookup.FONT,
									ControlPropertyLookup.SIZE_EX
							},
							ControlPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultOptionalProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ControlPropertyLookup[]{
											ControlPropertyLookup.MOVING,
											ControlPropertyLookup.SHADOW,
											ControlPropertyLookup.TOOLTIP,
											ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
											ControlPropertyLookup.TOOLTIP_COLOR_BOX,
											ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
											ControlPropertyLookup.FIXED_WIDTH,
											ControlPropertyLookup.LINE_SPACING,
											ControlPropertyLookup.BLINKING_PERIOD,
											ControlPropertyLookup.TILE_W,
											ControlPropertyLookup.TILE_H
									},
									//events
									ControlPropertyEventLookup.allWithControlScope()
							),
							ControlPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return new ControlStyle[]{
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
					ControlStyle.NO_RECT,
					ControlStyle.KEEP_ASPECT_RATIO,
					ControlStyle.TILE_PICTURE,
					ControlStyle.UPPERCASE,
					ControlStyle.LOWERCASE
			};
		}
	}

}
