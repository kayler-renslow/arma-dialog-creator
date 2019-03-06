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
 @since 07/08/2017 */
public class EditControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public EditControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
					   @NotNull ArmaDisplay display) {
		super(name, ArmaControlLookup.Edit, resolution, env, display);
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ConfigPropertyLookupConstant.class, defaultRequiredProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ConfigPropertyLookup[]{
											ConfigPropertyLookup.COLOR_BACKGROUND,
											ConfigPropertyLookup.TEXT,
											ConfigPropertyLookup.COLOR_TEXT,
											ConfigPropertyLookup.COLOR_DISABLED,
											ConfigPropertyLookup.COLOR_SELECTION,
											ConfigPropertyLookup.AUTO_COMPLETE,
											ConfigPropertyLookup.FONT,
											ConfigPropertyLookup.SIZE_EX
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
											ConfigPropertyLookup.SHADOW,
											ConfigPropertyLookup.CAN_MODIFY,
											ConfigPropertyLookup.MAX_CHARS,
											ConfigPropertyLookup.FORCE_DRAW_CARET,
											ConfigPropertyLookup.LINE_SPACING,
											ConfigPropertyLookup.TOOLTIP,
											ConfigPropertyLookup.HTML_CONTROL,
											ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
											ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
											ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
											ConfigPropertyLookup.BLINKING_PERIOD
									},
									ControlPropertyEventLookup.allWithControlScope()
							),
							ConfigPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ReadOnlyArray<ControlStyle> getAllowedStyles() {
			return new ReadOnlyArray<>(new ControlStyle[]{
					ControlStyle.NONE,
					ControlStyle.MULTI
			});
		}
	}
}
