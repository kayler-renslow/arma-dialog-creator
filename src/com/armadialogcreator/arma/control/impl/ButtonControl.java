package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlSpecRequirement;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.control.*;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/21/2016 */
public class ButtonControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ButtonControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.Button, resolution, env, registry);
		findProperty(ControlPropertyLookup.STYLE).setValueIfAbsent(true, ControlStyle.CENTER.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
							new ControlPropertyLookup[]{
									ControlPropertyLookup.COLOR_BACKGROUND,
									ControlPropertyLookup.SOUND_ENTER,
									ControlPropertyLookup.SOUND_PUSH,
									ControlPropertyLookup.SOUND_CLICK,
									ControlPropertyLookup.SOUND_ESCAPE,
									ControlPropertyLookup.TEXT,
									ControlPropertyLookup.COLOR_TEXT,
									ControlPropertyLookup.COLOR_DISABLED,
									ControlPropertyLookup.FONT,
									ControlPropertyLookup.SIZE_EX,
									ControlPropertyLookup.COLOR_BACKGROUND_ACTIVE,
									ControlPropertyLookup.COLOR_BACKGROUND_DISABLED,
									ControlPropertyLookup.COLOR_FOCUSED,
									ControlPropertyLookup.OFFSET_X,
									ControlPropertyLookup.OFFSET_Y,
									ControlPropertyLookup.OFFSET_PRESSED_X,
									ControlPropertyLookup.OFFSET_PRESSED_Y,
									ControlPropertyLookup.COLOR_SHADOW,
									ControlPropertyLookup.COLOR_BORDER,
									ControlPropertyLookup.BORDER_SIZE
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
											ControlPropertyLookup.DEFAULT,
											ControlPropertyLookup.ACTION,
											ControlPropertyLookup.SHADOW,
											ControlPropertyLookup.TOOLTIP,
											ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
											ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
											ControlPropertyLookup.TOOLTIP_COLOR_BOX,
											ControlPropertyLookup.BLINKING_PERIOD,
											ControlPropertyLookup.COLOR_FOCUSED2
									},
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithButtonScope()
							),
							ControlPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return new ControlStyle[]{
					ControlStyle.LEFT,
					ControlStyle.RIGHT,
					ControlStyle.CENTER
			};
		}
	}
}
