package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/21/2016 */
public class ButtonControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new ArmaControlSpecRequirement() {

		private final ReadOnlyList<ControlPropertyLookupConstant> requiredProperties = new ReadOnlyList<>(ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
				new ControlPropertyLookup[]{
						ControlPropertyLookup.COLOR_BACKGROUND,
						ControlPropertyLookup.SOUND_ENTER,
						ControlPropertyLookup.SOUND_PUSH,
						ControlPropertyLookup.SOUND_CLICK,
						ControlPropertyLookup.SOUND_ESCAPE,
						ControlPropertyLookup.TEXT,
						ControlPropertyLookup.COLOR_TEXT,
						ControlPropertyLookup.BTN_COLOR_DISABLED,
						ControlPropertyLookup.FONT,
						ControlPropertyLookup.SIZE_EX,
						ControlPropertyLookup.BTN_COLOR_BACKGROUND_ACTIVE,
						ControlPropertyLookup.BTN_COLOR_BACKGROUND_DISABLED,
						ControlPropertyLookup.BTN_COLOR_FOCUSED,
						ControlPropertyLookup.BTN_OFFSET_X,
						ControlPropertyLookup.BTN_OFFSET_Y,
						ControlPropertyLookup.BTN_OFFSET_PRESSED_X,
						ControlPropertyLookup.BTN_OFFSET_PRESSED_Y,
						ControlPropertyLookup.BTN_COLOR_SHADOW,
						ControlPropertyLookup.BTN_COLOR_BORDER,
						ControlPropertyLookup.BTN_BORDER_SIZE
				},
				ControlPropertyLookupConstant.PRIORITY_SORT)
		);

		private final ReadOnlyList<ControlPropertyLookupConstant> optionalProperties = new ReadOnlyList<>(ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultOptionalProperties,
				new ControlPropertyLookup[]{
						ControlPropertyLookup.BTN_DEFAULT,
						ControlPropertyLookup.BTN_ACTION,
						ControlPropertyLookup.SHADOW,
						ControlPropertyLookup.TOOLTIP,
						ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
						ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
						ControlPropertyLookup.TOOLTIP_COLOR_BOX,
						ControlPropertyLookup.BLINKING_PERIOD
				},
				ControlPropertyLookupConstant.PRIORITY_SORT)
		);


		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return requiredProperties;
		}

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
			return optionalProperties;
		}

		private final ControlStyle[] allowedStyles = {
				ControlStyle.LEFT,
				ControlStyle.RIGHT,
				ControlStyle.CENTER
		};

		@Override
		public ControlStyle[] getAllowedStyles() {
			return allowedStyles;
		}
	};

	public ButtonControl(@NotNull String name, int idc, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(ControlType.BUTTON, name, SPEC_PROVIDER, idc, ControlStyle.CENTER.getStyleGroup(), resolution, RendererLookup.BUTTON, env, registry);
	}
}
