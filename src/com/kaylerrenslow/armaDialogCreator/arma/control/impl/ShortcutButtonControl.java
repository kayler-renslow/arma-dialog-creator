package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 @author Kayler
 @since 7/5/2017 */
public class ShortcutButtonControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ShortcutButtonControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.ShortcutButton, resolution, env, registry);
		findProperty(ControlPropertyLookup.STYLE).setValueIfAbsent(true, ControlStyle.NA.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {

		@Override
		@NotNull
		public ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
			return new ReadOnlyList<>(
					Arrays.asList(
							new ControlClassSpecification(
									"HitZone", Arrays.asList(
									new ControlPropertySpecification(ControlPropertyLookup.TOP),
									new ControlPropertySpecification(ControlPropertyLookup.RIGHT),
									new ControlPropertySpecification(ControlPropertyLookup.BOTTOM),
									new ControlPropertySpecification(ControlPropertyLookup.LEFT)
							), ControlPropertySpecification.EMPTY),
							new ControlClassSpecification(
									"ShortcutPos", Arrays.asList(
									new ControlPropertySpecification(ControlPropertyLookup.TOP),
									new ControlPropertySpecification(ControlPropertyLookup.LEFT),
									new ControlPropertySpecification(ControlPropertyLookup.W),
									new ControlPropertySpecification(ControlPropertyLookup.H)
							), ControlPropertySpecification.EMPTY),
							new ControlClassSpecification(
									"TextPos", Arrays.asList(
									new ControlPropertySpecification(ControlPropertyLookup.TOP),
									new ControlPropertySpecification(ControlPropertyLookup.RIGHT),
									new ControlPropertySpecification(ControlPropertyLookup.BOTTOM),
									new ControlPropertySpecification(ControlPropertyLookup.LEFT)
							), ControlPropertySpecification.EMPTY)
					)
			);
		}

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
							new ControlPropertyLookup[]{
									ControlPropertyLookup.TEXT,
									ControlPropertyLookup.ANIM_TEXTURE_NORMAL,
									ControlPropertyLookup.ANIM_TEXTURE_DISABLED,
									ControlPropertyLookup.ANIM_TEXTURE_OVER,
									ControlPropertyLookup.ANIM_TEXTURE_PRESSED,
									ControlPropertyLookup.ANIM_TEXTURE_FOCUSED,
									ControlPropertyLookup.ANIM_TEXTURE_DEFAULT,
									ControlPropertyLookup.TEXTURE_NO_SHORTCUT,
									ControlPropertyLookup.COLOR,
									ControlPropertyLookup.COLOR2,
									ControlPropertyLookup.COLOR_FOCUSED,
									ControlPropertyLookup.COLOR_DISABLED,
									ControlPropertyLookup.COLOR_BACKGROUND_FOCUSED,
									ControlPropertyLookup.COLOR_BACKGROUND,
									ControlPropertyLookup.COLOR_BACKGROUND2,
									ControlPropertyLookup.SOUND_PUSH,
									ControlPropertyLookup.SOUND_CLICK,
									ControlPropertyLookup.SOUND_ESCAPE,
									ControlPropertyLookup.FONT,
									ControlPropertyLookup.SIZE,
									ControlPropertyLookup.SIZE_EX
							},
							ControlPropertyLookupConstant.PRIORITY_SORT)
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
											ControlPropertyLookup.BLINKING_PERIOD,
											ControlPropertyLookup.PERIOD_FOCUS,
											ControlPropertyLookup.PERIOD_OVER,
											ControlPropertyLookup.SHORTCUTS,
											ControlPropertyLookup.TOOLTIP,
											ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
											ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
											ControlPropertyLookup.TOOLTIP_COLOR_BOX,
									},
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithButtonScope()
							),
							ControlPropertyLookupConstant.PRIORITY_SORT)
			);
		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return new ControlStyle[]{
					ControlStyle.NA
			};
		}
	}
}
