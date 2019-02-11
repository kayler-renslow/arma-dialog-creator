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
public class EditControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public EditControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
					   @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.Edit, resolution, env);
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ControlPropertyLookup[]{
											ControlPropertyLookup.COLOR_BACKGROUND,
											ControlPropertyLookup.TEXT,
											ControlPropertyLookup.COLOR_TEXT,
											ControlPropertyLookup.COLOR_DISABLED,
											ControlPropertyLookup.COLOR_SELECTION,
											ControlPropertyLookup.AUTO_COMPLETE,
											ControlPropertyLookup.FONT,
											ControlPropertyLookup.SIZE_EX
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
											ControlPropertyLookup.SHADOW,
											ControlPropertyLookup.CAN_MODIFY,
											ControlPropertyLookup.MAX_CHARS,
											ControlPropertyLookup.FORCE_DRAW_CARET,
											ControlPropertyLookup.LINE_SPACING,
											ControlPropertyLookup.TOOLTIP,
											ControlPropertyLookup.HTML_CONTROL,
											ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
											ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
											ControlPropertyLookup.TOOLTIP_COLOR_BOX,
											ControlPropertyLookup.BLINKING_PERIOD
									},
									ControlPropertyEventLookup.allWithControlScope()
							),
							ControlPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return new ControlStyle[]{
					ControlStyle.NONE,
					ControlStyle.MULTI
			};
		}
	}
}