package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.AllowedStyleProvider;
import com.armadialogcreator.core.ControlPropertyEventLookup;
import com.armadialogcreator.core.ControlPropertyLookup;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.old.ControlPropertyLookupConstant;
import com.armadialogcreator.core.old.SpecificationRegistry;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/08/2017 */
public class XSliderControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public XSliderControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
						  @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.XSlider, resolution, env, registry);
		findProperty(ControlPropertyLookup.STYLE).setValue(ControlStyle.SL_HORZ.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ControlPropertyLookup[]{
											ControlPropertyLookup.COLOR,
											ControlPropertyLookup.COLOR_ACTIVE,
											ControlPropertyLookup.ARROW_EMPTY,
											ControlPropertyLookup.ARROW_FULL,
											ControlPropertyLookup.BORDER,
											ControlPropertyLookup.THUMB,
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
					ControlStyle.SL_HORZ
			};
		}
	}
}
