package com.kaylerrenslow.armaDialogCreator.arma.control;

/**
 Created by Kayler on 05/22/2016.
 */
public enum ControlStyle {
	//The standard font in Arma 3 is "PuristaMedium"

	// Static styles\
	POS(0x0F, ""),
	HPOS(0x03, ""),
	VPOS(0x0C, ""),
	LEFT(0x00, ""),
	RIGHT(0x01, ""),
	CENTER(0x02, ""),
	DOWN(0x04, ""),
	UP(0x08, ""),
	VCENTER(0x0c, ""),

	TYPE(0xF0, ""),
	SINGLE(0, ""),
	MULTI(16, ""),
	TITLE_BAR(32, ""),
	PICTURE(48, ""),
	FRAME(64, ""),
	BACKGROUND(80, ""),
	GROUP_BOX(96, ""),
	GROUP_BOX2(112, ""),
	HUD_BACKGROUND(128, ""),
	TILE_PICTURE(144, ""), //tileH and tileW params required for tiled image
	WITH_RECT(160, ""),
	LINE(176, ""),

	SHADOW(0x100, ""),
	NO_RECT(0x200, ""), // this style works for CT_STATIC in conjunction with MULTI
	KEEP_ASPECT_RATIO(0x800, ""),

	TITLE(TITLE_BAR.styleId + CENTER.styleId, ""),

	// Slider styles
	SL_DIR(0x400, ""),
	SL_VERT(0, ""),
	SL_HORZ(1024, ""),

	SL_TEXTURES(0x10, ""),

	// progress bar
	VERTICAL(0x01, ""),
	HORIZONTAL(0, ""),

	// Listbox styles
	LB_TEXTURES(0x10, ""),
	LB_MULTI(0x20, ""),

	// Tree styles
	TR_SHOWROOT(1, ""),
	TR_AUTOCOLLAPSE(2, ""),

	// MessageBox styles
	MB_BUTTON_OK(1, ""),
	MB_BUTTON_CANCEL(2, ""),
	MB_BUTTON_USER(4, "");

	public final int styleId;
	public final String displayName;

	ControlStyle(int styleId, String displayName) {
		this.styleId = styleId;
		this.displayName = displayName;
	}

}

