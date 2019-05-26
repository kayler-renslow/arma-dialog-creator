package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaConfigClassSpec;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 5/14/19 */
public class HitZoneControlSpec implements ArmaConfigClassSpec {
	public static final String CLASS_NAME = "HitZone";

	public static final HitZoneControlSpec instance = new HitZoneControlSpec();

	private final ReadOnlyList<ConfigPropertyLookupConstant> propertiesMergedCache = ArmaConfigClassSpec.super.getAllProperties();

	@Override
	@NotNull
	public ReadOnlyList<ConfigPropertyLookupConstant> getAllProperties() {
		return propertiesMergedCache;
	}

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
