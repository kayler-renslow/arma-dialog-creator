/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.main.lang.LookupLang;

import java.util.LinkedList;

/**
 Created by Kayler on 05/22/2016.
 */
public enum ControlStyle {
	// Static styles
	NA(0, 0, LookupLang.ControlStyle.NA, LookupLang.ControlStyle.Doc.NA),
	POS(1, 0x0F, LookupLang.ControlStyle.POS, LookupLang.ControlStyle.Doc.POS),
	HPOS(2, 0x03, LookupLang.ControlStyle.HPOS, LookupLang.ControlStyle.Doc.HPOS),
	VPOS(3, 0x0C, LookupLang.ControlStyle.VPOS, LookupLang.ControlStyle.Doc.VPOS),
	LEFT(4, 0x00, LookupLang.ControlStyle.LEFT, LookupLang.ControlStyle.Doc.LEFT),
	RIGHT(5, 0x01, LookupLang.ControlStyle.RIGHT, LookupLang.ControlStyle.Doc.RIGHT),
	CENTER(6, 0x02, LookupLang.ControlStyle.CENTER, LookupLang.ControlStyle.Doc.CENTER),
	DOWN(7, 0x04, LookupLang.ControlStyle.DOWN, LookupLang.ControlStyle.Doc.DOWN),
	UP(8, 0x08, LookupLang.ControlStyle.UP, LookupLang.ControlStyle.Doc.UP),
	VCENTER(9, 0x0c, LookupLang.ControlStyle.VCENTER, LookupLang.ControlStyle.Doc.VCENTER),

	TYPE(10, 0xF0, LookupLang.ControlStyle.TYPE, LookupLang.ControlStyle.Doc.TYPE),
	SINGLE(11, 0, LookupLang.ControlStyle.SINGLE, LookupLang.ControlStyle.Doc.SINGLE),
	MULTI(12, 16, LookupLang.ControlStyle.MULTI, LookupLang.ControlStyle.Doc.MULTI),
	TITLE_BAR(13, 32, LookupLang.ControlStyle.TITLE_BAR, LookupLang.ControlStyle.Doc.TITLE_BAR),
	PICTURE(14, 48, LookupLang.ControlStyle.PICTURE, LookupLang.ControlStyle.Doc.PICTURE),
	FRAME(15, 64, LookupLang.ControlStyle.FRAME, LookupLang.ControlStyle.Doc.FRAME),
	BACKGROUND(16, 80, LookupLang.ControlStyle.BACKGROUND, LookupLang.ControlStyle.Doc.BACKGROUND),
	GROUP_BOX(17, 96, LookupLang.ControlStyle.GROUP_BOX, LookupLang.ControlStyle.Doc.GROUP_BOX),
	GROUP_BOX2(18, 112, LookupLang.ControlStyle.GROUP_BOX2, LookupLang.ControlStyle.Doc.GROUP_BOX2),
	HUD_BACKGROUND(19, 128, LookupLang.ControlStyle.HUD_BACKGROUND, LookupLang.ControlStyle.Doc.HUD_BACKGROUND),
	TILE_PICTURE(20, 144, LookupLang.ControlStyle.TILE_PICTURE, LookupLang.ControlStyle.Doc.TILE_PICTURE),  //tileH and tileW params required for tiled image
	WITH_RECT(21, 160, LookupLang.ControlStyle.WITH_RECT, LookupLang.ControlStyle.Doc.WITH_RECT),
	LINE(22, 176, LookupLang.ControlStyle.LINE, LookupLang.ControlStyle.Doc.LINE),

	SHADOW(23, 0x100, LookupLang.ControlStyle.SHADOW, LookupLang.ControlStyle.Doc.SHADOW),
	NO_RECT(24, 0x200, LookupLang.ControlStyle.NO_RECT, LookupLang.ControlStyle.Doc.NO_RECT), // this style works for CT_STATIC in conjunction with MULTI
	KEEP_ASPECT_RATIO(25, 0x800, LookupLang.ControlStyle.KEEP_ASPECT_RATIO, LookupLang.ControlStyle.Doc.KEEP_ASPECT_RATIO),
	TITLE(26, TITLE_BAR.styleValue + CENTER.styleValue, LookupLang.ControlStyle.TITLE, LookupLang.ControlStyle.Doc.TITLE),

	// Slider styles
	SL_DIR(27, 0x400, LookupLang.ControlStyle.SL_DIR, LookupLang.ControlStyle.Doc.SL_DIR),
	SL_VERT(28, 0, LookupLang.ControlStyle.SL_VERT, LookupLang.ControlStyle.Doc.SL_VERT),
	SL_HORZ(29, 1024, LookupLang.ControlStyle.SL_HORZ, LookupLang.ControlStyle.Doc.SL_HORZ),
	SL_TEXTURES(30, 0x10, LookupLang.ControlStyle.SL_TEXTURES, LookupLang.ControlStyle.Doc.SL_TEXTURES),

	// progress bar
	VERTICAL(31, 0x01, LookupLang.ControlStyle.VERTICAL, LookupLang.ControlStyle.Doc.VERTICAL),
	HORIZONTAL(32, 0, LookupLang.ControlStyle.HORIZONTAL, LookupLang.ControlStyle.Doc.HORIZONTAL),

	// Listbox styles
	LB_TEXTURES(33, 0x10, LookupLang.ControlStyle.LB_TEXTURES, LookupLang.ControlStyle.Doc.LB_TEXTURES),
	LB_MULTI(34, 0x20, LookupLang.ControlStyle.LB_MULTI, LookupLang.ControlStyle.Doc.LB_MULTI),

	// Tree styles
	TR_SHOWROOT(35, 1, LookupLang.ControlStyle.TR_SHOWROOT, LookupLang.ControlStyle.Doc.TR_SHOWROOT),
	TR_AUTOCOLLAPSE(36, 2, LookupLang.ControlStyle.TR_AUTOCOLLAPSE, LookupLang.ControlStyle.Doc.TR_AUTOCOLLAPSE),

	// MessageBox styles
	MB_BUTTON_OK(37, 1, LookupLang.ControlStyle.MB_BUTTON_OK, LookupLang.ControlStyle.Doc.MB_BUTTON_OK),
	MB_BUTTON_CANCEL(38, 2, LookupLang.ControlStyle.MB_BUTTON_CANCEL, LookupLang.ControlStyle.Doc.MB_BUTTON_CANCEL),
	MB_BUTTON_USER(39, 4, LookupLang.ControlStyle.MB_BUTTON_USER, LookupLang.ControlStyle.Doc.MB_BUTTON_USER);

	public static final ControlStyle[] EMPTY = new ControlStyle[0];
	public final int styleValue;
	public final int styleId;
	public final String displayName;
	public final String documentation;

	private ControlStyleGroup styleGroup;

	ControlStyle(int styleId, int styleValue, String displayName, String documentation) {
		if (StyleIdVerifier.usedIds.contains(styleId)) {
			throw new IllegalArgumentException("styleId '" + styleId + "' is already used");
		}
		StyleIdVerifier.usedIds.add(styleId);
		this.styleId = styleId;
		this.styleValue = styleValue;
		this.displayName = displayName;
		this.documentation = documentation != null ? documentation : LookupLang.ControlStyle.Doc.NO_DOC;

	}

	/** A ControlStyleGroup instance with only this instance's ControlStyle inside */
	public ControlStyleGroup getStyleGroup() {
		if (styleGroup == null) {
			styleGroup = new ControlStyleGroup(new ControlStyle[]{this});
		}
		return styleGroup;
	}

	/** @throws IllegalArgumentException when id couldn't be matched */
	public static ControlStyle findById(int id) {
		for (ControlStyle style : values()) {
			if (style.styleId == id) {
				return style;
			}
		}
		throw new IllegalArgumentException("id " + id + " couldn't be matched");
	}

	@Override
	public String toString() {
		return displayName + " (" + styleValue + ")";
	}

	private static class StyleIdVerifier {
		static final LinkedList<Integer> usedIds = new LinkedList<>();
	}
}

