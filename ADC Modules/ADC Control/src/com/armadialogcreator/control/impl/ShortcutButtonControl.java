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
 @since 7/5/2017 */
public class ShortcutButtonControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public static final String NestedClassName_HitZone = "HitZone";
	public static final String NestedClassName_ShortcutPos = "ShortcutPos";
	public static final String NestedClassName_TextPos = "TextPos";

	public ShortcutButtonControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(name, ArmaControlLookup.ShortcutButton, resolution, env);
		findProperty(ConfigPropertyLookup.STYLE).setValue(ControlStyle.NONE.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		/*
				@NotNull
				public ReadOnlyMap<String, ReadOnlySet<ImmutableConfigProperty>> getRequiredNestedClasses() {
					return new ReadOnlyList<>(
							Arrays.asList(
									new ControlClassSpecification(
											NestedClassName_HitZone, Arrays.asList(
											new ControlPropertySpecification(ConfigPropertyLookup.TOP),
											new ControlPropertySpecification(ConfigPropertyLookup.RIGHT),
											new ControlPropertySpecification(ConfigPropertyLookup.BOTTOM),
											new ControlPropertySpecification(ConfigPropertyLookup.LEFT)
									), ControlPropertySpecification.EMPTY),
									new ControlClassSpecification(
											NestedClassName_ShortcutPos, Arrays.asList(
											new ControlPropertySpecification(ConfigPropertyLookup.TOP),
											new ControlPropertySpecification(ConfigPropertyLookup.LEFT),
											new ControlPropertySpecification(ConfigPropertyLookup.W),
											new ControlPropertySpecification(ConfigPropertyLookup.H)
									), ControlPropertySpecification.EMPTY),
									new ControlClassSpecification(
											NestedClassName_TextPos, Arrays.asList(
											new ControlPropertySpecification(ConfigPropertyLookup.TOP),
											new ControlPropertySpecification(ConfigPropertyLookup.RIGHT),
											new ControlPropertySpecification(ConfigPropertyLookup.BOTTOM),
											new ControlPropertySpecification(ConfigPropertyLookup.LEFT)
									), ControlPropertySpecification.EMPTY)
							)
					);
				}
		*/
		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ConfigPropertyLookupConstant.class, defaultRequiredProperties,
							new ConfigPropertyLookup[]{
									ConfigPropertyLookup.TEXT,
									ConfigPropertyLookup.ANIM_TEXTURE_NORMAL,
									ConfigPropertyLookup.ANIM_TEXTURE_DISABLED,
									ConfigPropertyLookup.ANIM_TEXTURE_OVER,
									ConfigPropertyLookup.ANIM_TEXTURE_PRESSED,
									ConfigPropertyLookup.ANIM_TEXTURE_FOCUSED,
									ConfigPropertyLookup.ANIM_TEXTURE_DEFAULT,
									ConfigPropertyLookup.TEXTURE_NO_SHORTCUT,
									ConfigPropertyLookup.COLOR,
									ConfigPropertyLookup.COLOR2,
									ConfigPropertyLookup.COLOR_FOCUSED,
									ConfigPropertyLookup.COLOR_DISABLED,
									ConfigPropertyLookup.COLOR_BACKGROUND_FOCUSED,
									ConfigPropertyLookup.COLOR_BACKGROUND,
									ConfigPropertyLookup.COLOR_BACKGROUND2,
									ConfigPropertyLookup.SOUND_PUSH,
									ConfigPropertyLookup.SOUND_CLICK,
									ConfigPropertyLookup.SOUND_ESCAPE,
									ConfigPropertyLookup.SOUND_ENTER,
									ConfigPropertyLookup.FONT,
									ConfigPropertyLookup.SIZE,
									ConfigPropertyLookup.SIZE_EX
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
											ConfigPropertyLookup.BLINKING_PERIOD,
											ConfigPropertyLookup.PERIOD_FOCUS,
											ConfigPropertyLookup.PERIOD_OVER,
											ConfigPropertyLookup.SHORTCUTS,
											ConfigPropertyLookup.TOOLTIP,
											ConfigPropertyLookup.TOOLTIP_COLOR_SHADE,
											ConfigPropertyLookup.TOOLTIP_COLOR_TEXT,
											ConfigPropertyLookup.TOOLTIP_COLOR_BOX,
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
					ControlStyle.NONE,
					ControlStyle.UPPERCASE,
					ControlStyle.LOWERCASE
			});
		}
	}
}
