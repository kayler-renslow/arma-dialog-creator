package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.ConfigPropertyEventLookup;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 Used for a controls with type=0 (Static)

 @author Kayler
 @since 05/25/2016. */
public class StaticControl extends ArmaControl {

	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public StaticControl(@NotNull String name, int idc, @NotNull ArmaResolution resolution, @NotNull Env env,
						 @NotNull ArmaDisplay display) {
		super(name, ArmaControlLookup.Static, resolution, env, display);
		findProperty(ConfigPropertyLookup.STYLE).setValue(ControlStyle.CENTER.getStyleGroup());
		findProperty(ConfigPropertyLookup.IDC).setValue(idc);
	}

	private static class SpecReq implements ArmaControlSpecRequirement {
		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ConfigPropertyLookupConstant.class, defaultRequiredProperties,
							new ConfigPropertyLookup[]{
									ConfigPropertyLookup.COLOR_BACKGROUND,
									ConfigPropertyLookup.COLOR_TEXT,
									ConfigPropertyLookup.TEXT,
									ConfigPropertyLookup.FONT,
									ConfigPropertyLookup.SIZE_EX
							},
							ConfigPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getOptionalProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ConfigPropertyLookupConstant.class, defaultOptionalProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ConfigPropertyLookup[]{
											ConfigPropertyLookup.MOVING,
											ConfigPropertyLookup.SHADOW,
											ConfigPropertyLookup.TOOLTIP,
											ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
											ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
											ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
											ConfigPropertyLookup.FIXED_WIDTH,
											ConfigPropertyLookup.LINE_SPACING,
											ConfigPropertyLookup.BLINKING_PERIOD,
											ConfigPropertyLookup.TILE_W,
											ConfigPropertyLookup.TILE_H
									},
									//events
									ConfigPropertyEventLookup.allWithControlScope()
							),
							ConfigPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ReadOnlyArray<ControlStyle> getAllowedStyles() {
			return new ReadOnlyArray<>(new ControlStyle[]{
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
			});
		}
	}

}
