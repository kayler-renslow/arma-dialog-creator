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
 @author Kayler
 @since 07/27/2017 */
public class ProgressControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ProgressControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
						   @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.Progress, resolution, env, registry);
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ControlPropertyLookup[]{
											ControlPropertyLookup.COLOR_BAR,
											ControlPropertyLookup.COLOR_FRAME,
											ControlPropertyLookup.TEXTURE
									}
							),
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
											ControlPropertyLookup.TOOLTIP,
											ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
											ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
											ControlPropertyLookup.TOOLTIP_COLOR_BOX,
											ControlPropertyLookup.BLINKING_PERIOD
									},
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithSliderScope()
							),
							ControlPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return new ControlStyle[]{
					ControlStyle.HORIZONTAL,
					ControlStyle.VERTICAL
			};
		}
	}
}
