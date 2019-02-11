package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.AllowedStyleProvider;
import com.armadialogcreator.core.ControlPropertyEventLookup;
import com.armadialogcreator.core.ControlPropertyLookup;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.old.ControlClassSpecification;
import com.armadialogcreator.core.old.ControlPropertyLookupConstant;
import com.armadialogcreator.core.old.ControlPropertySpecification;
import com.armadialogcreator.core.old.SpecificationRegistry;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 @author Kayler
 @since 07/28/2017 */
public class StructuredTextControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();
	public static String NestedClassName_Attributes = "Attributes";

	public StructuredTextControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
								 @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.Progress, resolution, env);
		findProperty(ControlPropertyLookup.STYLE).setValue(ControlStyle.NONE.getStyleGroup());
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {
		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeAndSort(ControlPropertyLookupConstant.class, defaultRequiredProperties,
							ArmaControlSpecRequirement.mergeArrays(
									new ControlPropertyLookup[]{
											ControlPropertyLookup.TEXT,
											ControlPropertyLookup.SIZE
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
											ControlPropertyLookup.COLOR_BACKGROUND,
											ControlPropertyLookup.TOOLTIP,
											ControlPropertyLookup.TOOLTIP_COLOR_SHADE,
											ControlPropertyLookup.TOOLTIP_COLOR_TEXT,
											ControlPropertyLookup.TOOLTIP_COLOR_BOX,
											ControlPropertyLookup.BLINKING_PERIOD
									},
									ControlPropertyEventLookup.allWithControlScope(),
									ControlPropertyEventLookup.allWithSliderScope()
							),
							ControlPropertyLookupConstant.PRIORITY_SORT
					)
			);
		}

		@Override
		@NotNull
		public ReadOnlyList<ControlClassSpecification> getOptionalNestedClasses() {
			return new ReadOnlyList<>(
					Arrays.asList(
							new ControlClassSpecification(
									NestedClassName_Attributes, ControlPropertySpecification.EMPTY,
									Arrays.asList(
											new ControlPropertySpecification(ControlPropertyLookup.FONT),
											new ControlPropertySpecification(ControlPropertyLookup.COLOR__HEX),
											new ControlPropertySpecification(ControlPropertyLookup.ALIGN),
											new ControlPropertySpecification(ControlPropertyLookup.SHADOW_COLOR),
											new ControlPropertySpecification(ControlPropertyLookup.SIZE)
									)
							)
					)
			);
		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return new ControlStyle[]{
					ControlStyle.NONE
			};
		}
	}
}
