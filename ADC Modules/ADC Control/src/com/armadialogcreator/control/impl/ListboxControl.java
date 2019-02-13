package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.*;
import com.armadialogcreator.core.old.ControlClassSpecification;
import com.armadialogcreator.core.old.ControlPropertySpecification;
import com.armadialogcreator.core.old.SpecificationRegistry;
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
		super(name, ArmaControlLookup.ListBox, resolution, env);

		//force these value so that if the default value provider doesn't provide a value, there's still one present
		findProperty(ConfigPropertyLookup.STYLE).setValue(ControlStyle.LB_TEXTURES.getStyleGroup());
		findProperty(ConfigPropertyLookup.MAX_HISTORY_DELAY).setValue(new SVDouble(0));
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ConfigPropertyLookupConstant.class, defaultRequiredProperties,
							new ConfigPropertyLookup[]{
									ConfigPropertyLookup.FONT,
									ConfigPropertyLookup.SIZE_EX,
									ConfigPropertyLookup.COLOR_DISABLED,
									ConfigPropertyLookup.COLOR_TEXT,
									ConfigPropertyLookup.COLOR_BACKGROUND,
									ConfigPropertyLookup.COLOR_SELECT,
									ConfigPropertyLookup.SOUND_SELECT,
									ConfigPropertyLookup.ROW_HEIGHT,
									ConfigPropertyLookup.MAX_HISTORY_DELAY
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
											ConfigPropertyLookup.COLOR_SELECT_BACKGROUND,
											ConfigPropertyLookup.COLOR_SELECT_BACKGROUND2,
											ConfigPropertyLookup.COLOR_SELECT2,
											ConfigPropertyLookup.PERIOD,
											ConfigPropertyLookup.SHADOW,
											ConfigPropertyLookup.TOOLTIP,
											ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
											ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
											ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
											ConfigPropertyLookup.BLINKING_PERIOD
									},
									//events
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithListboxScope()
							),
							ConfigPropertyLookupConstant.PRIORITY_SORT
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
									new ControlPropertySpecification(ConfigPropertyLookup.COLOR),
									new ControlPropertySpecification(ConfigPropertyLookup.THUMB),
									new ControlPropertySpecification(ConfigPropertyLookup.ARROW_FULL),
									new ControlPropertySpecification(ConfigPropertyLookup.ARROW_EMPTY),
									new ControlPropertySpecification(ConfigPropertyLookup.BORDER)
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
