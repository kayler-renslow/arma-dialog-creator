package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlSpecRequirement;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.core.old.*;
import com.armadialogcreator.core.sv.SVDouble;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 @author Kayler
 @since 07/27/2017 */
public class ListboxControl extends ArmaControl {

	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public static final String NestedClassName_ListScrollBar = "ListScrollBar";

	public ListboxControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.ListBox, resolution, env, registry);

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
									ControlPropertyLookup.FONT,
									ControlPropertyLookup.SIZE_EX,
									ControlPropertyLookup.COLOR_DISABLED,
									ControlPropertyLookup.COLOR_TEXT,
									ControlPropertyLookup.COLOR_BACKGROUND,
									ControlPropertyLookup.COLOR_SELECT,
									ControlPropertyLookup.SOUND_SELECT,
									ControlPropertyLookup.ROW_HEIGHT,
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
											ControlPropertyLookup.COLOR_SELECT_BACKGROUND,
											ControlPropertyLookup.COLOR_SELECT_BACKGROUND2,
											ControlPropertyLookup.COLOR_SELECT2,
											ControlPropertyLookup.PERIOD,
											ControlPropertyLookup.SHADOW,
											ControlPropertyLookup.TOOLTIP,
											ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
											ControlPropertyLookup.TOOLTIP_COLOR_BOX,
											ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
											ControlPropertyLookup.BLINKING_PERIOD
									},
									//events
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithListboxScope()
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
									NestedClassName_ListScrollBar, Arrays.asList(
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
					ControlStyle.LB_TEXTURES,
					ControlStyle.LB_MULTI
			};
		}
	}

}
