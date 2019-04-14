package com.armadialogcreator.img;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/17/2016.
 */
public abstract class ADCImagePaths {
	private static final String pathPrefix = "/com/armadialogcreator/img/";

	public static final String BG_1 = pathPrefix + "backgroundImages/image1.jpg";
	public static final String BG_2 = pathPrefix + "backgroundImages/image2.jpg";
	public static final String BG_3 = pathPrefix + "backgroundImages/image3.jpg";

	public static final String ICON_FOLDER = getIcon("folder.png");
	public static final String ICON_FOLDER_MINIPLUS = getIcon("folder_miniplus.png");
	public static final String ICON_APP = getIcon("app.png");
	public static final String ICON_UNDO = getIcon("undo.png");
	public static final String ICON_REDO = getIcon("redo.png");
	public static final String ICON_HEART = getIcon("heart.png");
	public static final String ICON_GEAR = getIcon("gear.png");
	public static final String ICON_REFRESH = getIcon("refresh.png");
	public static final String ICON_SAVE = getIcon("save.png");
	public static final String ICON_EXPORT = getIcon("export.png");
	public static final String ICON_PLUS = getIcon("plus.png");
	public static final String ICON_MINUS = getIcon("minus.png");
	public static final String ICON_DOWN_ARROW = getIcon("down_arrow.png");
	public static final String ICON_UP_ARROW = getIcon("up_arrow.png");
	public static final String ICON_DOWN_ARROW_INTO = getIcon("down_arrow_into.png");
	public static final String ICON_UP_ARROW_INTO = getIcon("up_arrow_into.png");
	public static final String ICON_DOWN_ARROW_OUT = getIcon("down_arrow_out.png");
	public static final String ICON_UP_ARROW_OUT = getIcon("up_arrow_out.png");
	public static final String ICON_AUTO_SIZE = getIcon("autosize.png");
	public static final String ICON_HASH = getIcon("hash.png");
	public static final String ICON_HASH_MINIPLUS = getIcon("hash_miniplus.png");
	public static final String ICON_MINIPLUS = getIcon("miniplus.png");

	public static final String ICON_BRACKETS = getIcon("brackets.png");
	public static final String PRELOAD_SCREEN = pathPrefix + "preload_screen.png";

	public static final String ABOUT_HEADER = pathPrefix + "adc_title.png";

	public static String getIcon(@NotNull String name) {
		return pathPrefix + "icons/" + name;
	}

	public static String getControlIcon(@NotNull String name) {
		return pathPrefix + "icons/controls/" + name;
	}
}
