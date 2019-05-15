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
 @since 7/5/2017 */
public class ShortcutButtonControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ShortcutButtonControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
								 @NotNull ArmaDisplay display) {
		super(name, ArmaControlLookup.ShortcutButton, resolution, env, display);
		findProperty(ConfigPropertyLookup.STYLE).setValue(ControlStyle.NONE.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@Override
		public @NotNull ReadOnlyMap<String, ArmaControlSpecRequirement> getNestedConfigClasses() {
			HashMap<String, ArmaControlSpecRequirement> map = new HashMap<>();
			ReadOnlyMap<String, ArmaControlSpecRequirement> ret = new ReadOnlyMap<>(map);
			map.put(HitZoneControlSpec.CLASS_NAME, HitZoneControlSpec.instance);
			map.put(ShortcutPosControlSpec.CLASS_NAME, ShortcutPosControlSpec.instance);
			map.put(TextPosControlSpec.CLASS_NAME, TextPosControlSpec.instance);
			return ret;
		}

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
									ConfigPropertyEventLookup.allWithControlScope(),
									ConfigPropertyEventLookup.allWithButtonScope()
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
