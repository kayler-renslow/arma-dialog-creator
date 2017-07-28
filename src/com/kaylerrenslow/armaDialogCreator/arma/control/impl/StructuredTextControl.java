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
 @since 07/28/2017 */
public class StructuredTextControl extends ArmaControl {
	public final static ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();
	public static String NestedClassName_Attributes = "Attributes";

	public StructuredTextControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env,
								 @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.Progress, resolution, env, registry);
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
									NestedClassName_Attributes, Arrays.asList(
									new ControlPropertySpecification(ControlPropertyLookup.FONT),
									new ControlPropertySpecification(ControlPropertyLookup.COLOR__HEX),
									new ControlPropertySpecification(ControlPropertyLookup.ALIGN),
									new ControlPropertySpecification(ControlPropertyLookup.SHADOW_COLOR),
									new ControlPropertySpecification(ControlPropertyLookup.SIZE)
							), ControlPropertySpecification.EMPTY)
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
