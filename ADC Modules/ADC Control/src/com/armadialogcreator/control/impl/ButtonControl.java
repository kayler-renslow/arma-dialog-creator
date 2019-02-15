package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.*;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/21/2016 */
public class ButtonControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ButtonControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(name, ArmaControlLookup.Button, resolution, env);
		findProperty(ConfigPropertyLookup.STYLE).setValue(ControlStyle.CENTER.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ConfigPropertyLookupConstant.class, defaultRequiredProperties,
							new ConfigPropertyLookup[]{
									ConfigPropertyLookup.COLOR_BACKGROUND,
									ConfigPropertyLookup.SOUND_ENTER,
									ConfigPropertyLookup.SOUND_PUSH,
									ConfigPropertyLookup.SOUND_CLICK,
									ConfigPropertyLookup.SOUND_ESCAPE,
									ConfigPropertyLookup.TEXT,
									ConfigPropertyLookup.COLOR_TEXT,
									ConfigPropertyLookup.COLOR_DISABLED,
									ConfigPropertyLookup.FONT,
									ConfigPropertyLookup.SIZE_EX,
									ConfigPropertyLookup.COLOR_BACKGROUND_ACTIVE,
									ConfigPropertyLookup.COLOR_BACKGROUND_DISABLED,
									ConfigPropertyLookup.COLOR_FOCUSED,
									ConfigPropertyLookup.OFFSET_X,
									ConfigPropertyLookup.OFFSET_Y,
									ConfigPropertyLookup.OFFSET_PRESSED_X,
									ConfigPropertyLookup.OFFSET_PRESSED_Y,
									ConfigPropertyLookup.COLOR_SHADOW,
									ConfigPropertyLookup.COLOR_BORDER,
									ConfigPropertyLookup.BORDER_SIZE
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
											ConfigPropertyLookup.DEFAULT,
											ConfigPropertyLookup.ACTION,
											ConfigPropertyLookup.SHADOW,
											ConfigPropertyLookup.TOOLTIP,
											ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
											ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
											ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
											ConfigPropertyLookup.BLINKING_PERIOD,
											ConfigPropertyLookup.COLOR_FOCUSED2
									},
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithButtonScope()
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
					ControlStyle.CENTER
			});
		}
	}
}
