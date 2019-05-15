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
public class TextPosControlSpec implements ArmaControlSpecRequirement {
	public static final String CLASS_NAME = "TextPos";

	public static final TextPosControlSpec instance = new TextPosControlSpec();

	@Override
	@NotNull
	public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
		return new ReadOnlyList<>(
				ArrayUtil.sort(new ConfigPropertyLookup[]{
								ConfigPropertyLookup.TOP,
								ConfigPropertyLookup.RIGHT,
								ConfigPropertyLookup.BOTTOM,
								ConfigPropertyLookup.LEFT
						},
						ConfigPropertyLookupConstant.PRIORITY_SORT
				)
		);
	}

}
