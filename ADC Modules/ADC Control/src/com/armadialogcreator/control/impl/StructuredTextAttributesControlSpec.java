package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 5/14/19 */
public class StructuredTextAttributesControlSpec implements ArmaControlSpecRequirement {
	public static final String CLASS_NAME = "Attributes";

	public static final StructuredTextAttributesControlSpec instance = new StructuredTextAttributesControlSpec();

	@Override
	@NotNull
	public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
		return new ReadOnlyList<>(
				ArrayUtil.sort(new ConfigPropertyLookup[]{
								ConfigPropertyLookup.COLOR__HEX,
								ConfigPropertyLookup.ALIGN,
								ConfigPropertyLookup.SHADOW_COLOR,
								ConfigPropertyLookup.SIZE,
						},
						ConfigPropertyLookupConstant.PRIORITY_SORT
				)
		);
	}

}