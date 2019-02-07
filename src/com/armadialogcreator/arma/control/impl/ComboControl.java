package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlSpecRequirement;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.old.*;
import com.armadialogcreator.core.sv.SVDouble;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 @author Kayler
 @since 07/22/2017 */
public class ComboControl extends ArmaControl {

	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public static final String NestedClassName_ComboScrollBar = "ComboScrollBar";

	public ComboControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.Combo, resolution, env, registry);

		//force these value so that if the default value provider doesn't provide a value, there's still one present
		findProperty(ControlPropertyLookup.STYLE).setValue(ControlStyle.LB_TEXTURES.getStyleGroup());
		findProperty(ControlPropertyLookup.MAX_HISTORY_DELAY).setValue(new SVDouble(0));
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
							new ControlPropertyLookup[]{
									ControlPropertyLookup.ARROW_EMPTY,
									ControlPropertyLookup.ARROW_FULL,
									ControlPropertyLookup.COLOR_SELECT,
									ControlPropertyLookup.COLOR_TEXT,
									ControlPropertyLookup.COLOR_BACKGROUND,
									ControlPropertyLookup.SOUND_SELECT,
									ControlPropertyLookup.SOUND_EXPAND,
									ControlPropertyLookup.SOUND_COLLAPSE,
									ControlPropertyLookup.COLOR_SELECT_BACKGROUND,
									ControlPropertyLookup.WHOLE_HEIGHT,
									ControlPropertyLookup.COLOR_DISABLED,
									ControlPropertyLookup.FONT,
									ControlPropertyLookup.SIZE_EX,
									ControlPropertyLookup.MAX_HISTORY_DELAY
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
											ControlPropertyLookup.SHADOW,
											ControlPropertyLookup.TOOLTIP,
											ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
											ControlPropertyLookup.TOOLTIP_COLOR_BOX,
											ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
											ControlPropertyLookup.BLINKING_PERIOD
									},
									//events
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithComboScope()
							),
							ControlPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@Override
		@NotNull
		public ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
			return new ReadOnlyList<>(
					Arrays.asList(
							new ControlClassSpecification(
									NestedClassName_ComboScrollBar, Arrays.asList(
									new ControlPropertySpecification(ControlPropertyLookup.COLOR),
									new ControlPropertySpecification(ControlPropertyLookup.THUMB),
									new ControlPropertySpecification(ControlPropertyLookup.ARROW_FULL),
									new ControlPropertySpecification(ControlPropertyLookup.ARROW_EMPTY),
									new ControlPropertySpecification(ControlPropertyLookup.BORDER)
							), ControlPropertySpecification.EMPTY)
					)
			);
		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return new ControlStyle[]{
					ControlStyle.NONE,
					ControlStyle.LB_TEXTURES
			};
		}
	}

}
