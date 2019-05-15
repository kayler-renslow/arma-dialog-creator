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
import com.armadialogcreator.util.ReadOnlyMap;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 @author Kayler
 @since 07/28/2017 */
public class StructuredTextControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public StructuredTextControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
								 @NotNull ArmaDisplay display) {
		super(name, ArmaControlLookup.Progress, resolution, env, display);
		findProperty(ConfigPropertyLookup.STYLE).setValue(ControlStyle.NONE.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ConfigPropertyLookupConstant.class, defaultRequiredProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ConfigPropertyLookup[]{
											ConfigPropertyLookup.TEXT,
											ConfigPropertyLookup.SIZE
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
											ConfigPropertyLookup.COLOR_BACKGROUND,
											ConfigPropertyLookup.TOOLTIP,
											ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
											ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
											ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
											ConfigPropertyLookup.BLINKING_PERIOD
									},
									ConfigPropertyEventLookup.allWithControlScope(),
									ConfigPropertyEventLookup.allWithSliderScope()
							),
							ConfigPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@Override
		public @NotNull ReadOnlyMap<String, ArmaControlSpecRequirement> getNestedConfigClasses() {
			HashMap<String, ArmaControlSpecRequirement> map = new HashMap<>();
			ReadOnlyMap<String, ArmaControlSpecRequirement> ret = new ReadOnlyMap<>(map);
			map.put(StructuredTextAttributesControlSpec.CLASS_NAME, StructuredTextAttributesControlSpec.instance);
			return ret;
		}

		@NotNull
		@Override
		public ReadOnlyArray<ControlStyle> getAllowedStyles() {
			return new ReadOnlyArray<>(new ControlStyle[]{
					ControlStyle.NONE
			});
		}
	}
}
