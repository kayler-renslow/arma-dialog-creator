package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaControlSpecRequirement;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.core.old.*;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 @author Kayler
 @since 7/5/2017 */
public class ShortcutButtonControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public static final String NestedClassName_HitZone = "HitZone";
	public static final String NestedClassName_ShortcutPos = "ShortcutPos";
	public static final String NestedClassName_TextPos = "TextPos";

	public ShortcutButtonControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.ShortcutButton, resolution, env, registry);
		findProperty(ControlPropertyLookup.STYLE).setValueIfAbsent(true, ControlStyle.NONE.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {

		@Override
		@NotNull
		public ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
			return new ReadOnlyList<>(
					Arrays.asList(
							new ControlClassSpecification(
									NestedClassName_HitZone, Arrays.asList(
									new ControlPropertySpecification(ControlPropertyLookup.TOP),
									new ControlPropertySpecification(ControlPropertyLookup.RIGHT),
									new ControlPropertySpecification(ControlPropertyLookup.BOTTOM),
									new ControlPropertySpecification(ControlPropertyLookup.LEFT)
							), ControlPropertySpecification.EMPTY),
							new ControlClassSpecification(
									NestedClassName_ShortcutPos, Arrays.asList(
									new ControlPropertySpecification(ControlPropertyLookup.TOP),
									new ControlPropertySpecification(ControlPropertyLookup.LEFT),
									new ControlPropertySpecification(ControlPropertyLookup.W),
									new ControlPropertySpecification(ControlPropertyLookup.H)
							), ControlPropertySpecification.EMPTY),
							new ControlClassSpecification(
									NestedClassName_TextPos, Arrays.asList(
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
									ControlPropertyLookup.SOUND_ENTER,
									ControlPropertyLookup.FONT,
									ControlPropertyLookup.SIZE,
									ControlPropertyLookup.SIZE_EX
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
							ControlPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return new ControlStyle[]{
					ControlStyle.NONE,
					ControlStyle.UPPERCASE,
					ControlStyle.LOWERCASE
			};
		}
	}
}
