package com.armadialogcreator.data;

/**
 @author K
 @since 02/15/2019 */
public class ProjectSettings extends Settings {

	public final BooleanSetting ShowCanvasGridSetting = new BooleanSetting(true);
	public final ColorSetting AbsRegionColorSetting = new ColorSetting(255, 0, 0, 1);
	public final ColorSetting EditorBackgroundSetting = new ColorSetting(255, 255, 255, 255);
	public final ColorSetting EditorSelectionColorSetting = new ColorSetting(0, 147, 255, 255);

	public ProjectSettings() {
		map.put("ShowCanvasGrid", ShowCanvasGridSetting);
		map.put("AbsRegionColor", AbsRegionColorSetting);
		map.put("EditorBackgroundColor", EditorBackgroundSetting);
		map.put("EditorSelectionColor", EditorSelectionColorSetting);
	}

}
