package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.*;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/27/2017 */
public class ProgressControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ProgressControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
						   @NotNull ArmaDisplay display) {
		super(name, ArmaControlLookup.Progress, resolution, env, display);
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ConfigPropertyLookupConstant.class, defaultRequiredProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ConfigPropertyLookup[]{
											ConfigPropertyLookup.COLOR_BAR,
											ConfigPropertyLookup.COLOR_FRAME,
											ConfigPropertyLookup.TEXTURE
									}
							),
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
											ConfigPropertyLookup.TOOLTIP,
											ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
											ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
											ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
											ConfigPropertyLookup.BLINKING_PERIOD
									},
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithSliderScope()
							),
							ConfigPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ReadOnlyArray<ControlStyle> getAllowedStyles() {
			return new ReadOnlyArray<>(new ControlStyle[]{
					ControlStyle.HORIZONTAL,
					ControlStyle.VERTICAL
			});
		}
	}
}
