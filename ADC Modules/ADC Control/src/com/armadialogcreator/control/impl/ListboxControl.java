package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.*;
import com.armadialogcreator.core.sv.SVDouble;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.ReadOnlyList;
import com.armadialogcreator.util.ReadOnlyMap;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 @author Kayler
 @since 07/27/2017 */
public class ListboxControl extends ArmaControl {

	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ListboxControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
						  @NotNull ArmaDisplay display) {
		super(name, ArmaControlLookup.ListBox, resolution, env, display);

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
									ConfigPropertyEventLookup.allWithControlScope(),
									ConfigPropertyEventLookup.allWithListboxScope()
							),
							ConfigPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@Override
		public @NotNull ReadOnlyMap<String, ArmaControlSpecRequirement> getNestedConfigClasses() {
			HashMap<String, ArmaControlSpecRequirement> map = new HashMap<>();
			ReadOnlyMap<String, ArmaControlSpecRequirement> ret = new ReadOnlyMap<>(map);
			map.put(ListScrollbarControlSpec.CLASS_NAME, ListScrollbarControlSpec.instance);
			return ret;
		}

		@NotNull
		@Override
		public ReadOnlyArray<ControlStyle> getAllowedStyles() {
			return new ReadOnlyArray<>(new ControlStyle[]{
					ControlStyle.NONE,
					ControlStyle.LB_TEXTURES,
					ControlStyle.LB_MULTI
			});
		}
	}

}
