package com.armadialogcreator.canvas;


import com.armadialogcreator.util.ColorUtil;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/18/2016.
 */
public class UICanvasEditorColors {
	public final static Color DEFAULT_SELECTION = ColorUtil.toColor(0, 147, 255);
	public final static Color DARK_THEME_GRID = ColorUtil.toColor(51, 51, 51);
	public final static Color DARK_THEME_EDITOR_BG = ColorUtil.toColor(26, 26, 26);
	public final static Color DEFAULT_ABS_REGION = Color.RED;
	public final static Color DEFAULT_EDITOR_BG = Color.WHITE;
	public final static Color DEFAULT_GRID = Color.GRAY;

	@NotNull
	public Color selection = DEFAULT_SELECTION;
	@NotNull
	public Color grid = DEFAULT_GRID;
	@NotNull
	public Color editorBg = DEFAULT_EDITOR_BG;
	@NotNull
	public Color absRegion = DEFAULT_ABS_REGION;
}
