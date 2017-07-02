package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.MissingResourceException;

/**
 @author Kayler
 @since 05/22/2016
 */
public enum ControlStyle {
	NA(0, 0, getString("ControlStyle.na"), getString("ControlStyle.Doc.na")),
	//POS(1, 0x0F, getString("ControlStyle.pos"), getString("ControlStyle.Doc.pos")),
	//HPOS(2, 0x03, getString("ControlStyle.hpos"), getString("ControlStyle.Doc.hpos")),
	//POS(3, 0x0C, getString("ControlStyle.vpos"), getString("ControlStyle.Doc.vpos")),
	LEFT(4, 0x00, getString("ControlStyle.left"), getString("ControlStyle.Doc.left")),
	RIGHT(5, 0x01, getString("ControlStyle.right"), getString("ControlStyle.Doc.right")),
	CENTER(6, 0x02, getString("ControlStyle.center"), getString("ControlStyle.Doc.center")),
	//DOWN(7, 0x04, getString("ControlStyle.down"), getString("ControlStyle.Doc.down")),
	//UP(8, 0x08, getString("ControlStyle.up"), getString("ControlStyle.Doc.up")),
	//CENTER(9, 0x0c, getString("ControlStyle.vcenter"), getString("ControlStyle.Doc.vcenter")),

	//TYPE(10, 0xF0, getString("ControlStyle.type"), getString("ControlStyle.Doc.type")),
	SINGLE(11, 0, getString("ControlStyle.single"), getString("ControlStyle.Doc.single")),
	MULTI(12, 16, getString("ControlStyle.multi"), getString("ControlStyle.Doc.multi")),
	TITLE_BAR(13, 32, getString("ControlStyle.title_bar"), getString("ControlStyle.Doc.title_bar")),
	PICTURE(14, 48, getString("ControlStyle.picture"), getString("ControlStyle.Doc.picture")),
	FRAME(15, 64, getString("ControlStyle.frame"), getString("ControlStyle.Doc.frame")),
	BACKGROUND(16, 80, getString("ControlStyle.background"), getString("ControlStyle.Doc.background")),
	GROUP_BOX(17, 96, getString("ControlStyle.group_box"), getString("ControlStyle.Doc.group_box")),
	GROUP_BOX2(18, 112, getString("ControlStyle.group_box2"), getString("ControlStyle.Doc.group_box2")),
	HUD_BACKGROUND(19, 128, getString("ControlStyle.hud_background"), getString("ControlStyle.Doc.hud_background")),
	TILE_PICTURE(20, 144, getString("ControlStyle.tile_picture"), getString("ControlStyle.Doc.tile_picture")),
	WITH_RECT(21, 160, getString("ControlStyle.with_rect"), getString("ControlStyle.Doc.with_rect")),
	LINE(22, 176, getString("ControlStyle.line"), getString("ControlStyle.Doc.line")),

	//SHADOW(23, 0x100, getString("ControlStyle.shadow"), getString("ControlStyle.Doc.shadow")),
	NO_RECT(24, 0x200, getString("ControlStyle.no_rect"), getString("ControlStyle.Doc.no_rect")),
	KEEP_ASPECT_RATIO(25, 0x800, getString("ControlStyle.keep_aspect_ratio"), getString("ControlStyle.Doc.keep_aspect_ratio")),
	//TITLE(26, TITLE_BAR.styleValue + CENTER.styleValue, getString("ControlStyle.title"), getString("ControlStyle.Doc.title")),

	// Slider styles
	//SL_DIR(27, 0x400, getString("ControlStyle.sl_dir"), getString("ControlStyle.Doc.sl_dir")),
	SL_VERT(28, 0, getString("ControlStyle.sl_vert"), getString("ControlStyle.Doc.sl_vert")),
	SL_HORZ(29, 1024, getString("ControlStyle.sl_horz"), getString("ControlStyle.Doc.sl_horz")),
	SL_TEXTURES(30, 0x10, getString("ControlStyle.sl_textures"), getString("ControlStyle.Doc.sl_textures")),

	// progress bar
	VERTICAL(31, 0x01, getString("ControlStyle.vertical"), getString("ControlStyle.Doc.vertical")),
	HORIZONTAL(32, 0, getString("ControlStyle.horizontal"), getString("ControlStyle.Doc.horizontal")),

	// Listbox styles
	LB_TEXTURES(33, 0x10, getString("ControlStyle.lb_textures"), getString("ControlStyle.Doc.lb_textures")),
	LB_MULTI(34, 0x20, getString("ControlStyle.lb_multi"), getString("ControlStyle.Doc.lb_multi")),

	// Tree styles
	TR_SHOWROOT(35, 1, getString("ControlStyle.tr_showroot"), getString("ControlStyle.Doc.tr_showroot")),
	TR_AUTOCOLLAPSE(36, 2, getString("ControlStyle.tr_autocollapse"), getString("ControlStyle.Doc.tr_autocollapse")),

	// MessageBox styles
	//MB_BUTTON_OK(37, 1, getString("ControlStyle.mb_button_ok"), getString("ControlStyle.Doc.mb_button_ok")),
	//MB_BUTTON_CANCEL(38, 2, getString("ControlStyle.mb_button_cancel"), getString("ControlStyle.Doc.mb_button_cancel")),
	//MB_BUTTON_USER(39, 4, getString("ControlStyle.mb_button_user"), getString("ControlStyle.Doc.mb_button_user"))
	;

	public static final ControlStyle[] EMPTY = new ControlStyle[0];
	public final int styleValue;
	public final int styleId;
	public final String displayName;
	public final String documentation;

	private SVControlStyleGroup styleGroup;

	ControlStyle(int styleId, int styleValue, String displayName, String documentation) {
		if (StyleIdVerifier.usedIds.contains(styleId)) {
			throw new IllegalArgumentException("styleId '" + styleId + "' is already used");
		}
		StyleIdVerifier.usedIds.add(styleId);
		this.styleId = styleId;
		this.styleValue = styleValue;
		this.displayName = displayName;
		this.documentation = documentation;

	}

	/** A SVControlStyleGroup instance with only this instance's ControlStyle inside */
	public SVControlStyleGroup getStyleGroup() {
		if (styleGroup == null) {
			styleGroup = new SVControlStyleGroup(new ControlStyle[]{this});
		}
		return styleGroup;
	}

	/**
	 Match the {@link ControlStyle} by {@link ControlStyle#styleId}

	 @return the matched instance
	 @throws IllegalArgumentException when id couldn't be matched
	 @see #findByValue(int)
	 */
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

	private static String getString(String s) {
		try {
			return Lang.LookupBundle().getString(s);
		} catch (MissingResourceException e) {
		}
		return Lang.LookupBundle().getString("ControlStyle.Doc.no_doc");
	}

	/**
	 Match the {@link ControlStyle} by {@link ControlStyle#styleValue}

	 @return the matched instance
	 @throws IllegalArgumentException when nothing could be matched
	 @see #findById(int)
	 */
	@NotNull
	public static ControlStyle findByValue(int value) {
		for (ControlStyle style : values()) {
			if (style.styleValue == value) {
				return style;
			}
		}
		throw new IllegalArgumentException("value " + value + " couldn't be matched");
	}

	private static class StyleIdVerifier {
		static final LinkedList<Integer> usedIds = new LinkedList<>();
	}
}

