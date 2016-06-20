package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.dataCreator.StaticDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.TreeItemEntry;

/**
 Created by Kayler on 06/19/2016.
 */
public enum ControlTreeItemDataCreatorLookup {
	STATIC(ControlType.STATIC, StaticDataCreator.INSTANCE),
	HTML(ControlType.HTML, StaticDataCreator.INSTANCE),
	EDIT(ControlType.EDIT, StaticDataCreator.INSTANCE),
	STRUCTURED_TEXT(ControlType.STRUCTURED_TEXT, StaticDataCreator.INSTANCE),
	ACTIVETEXT(ControlType.ACTIVETEXT, StaticDataCreator.INSTANCE),

	BUTTON(ControlType.BUTTON, StaticDataCreator.INSTANCE),
	SHORTCUTBUTTON(ControlType.SHORTCUTBUTTON, StaticDataCreator.INSTANCE),
	XBUTTON(ControlType.XBUTTON, StaticDataCreator.INSTANCE),

	PROGRESS(ControlType.PROGRESS, StaticDataCreator.INSTANCE),
	STATIC_SKEW(ControlType.STATIC_SKEW, StaticDataCreator.INSTANCE),
	LINEBREAK(ControlType.LINEBREAK, StaticDataCreator.INSTANCE),
	TREE(ControlType.TREE, StaticDataCreator.INSTANCE),
	CONTROLS_GROUP(ControlType.CONTROLS_GROUP, StaticDataCreator.INSTANCE, true),
	XKEYDESC(ControlType.XKEYDESC, StaticDataCreator.INSTANCE),
	ANIMATED_TEXTURE(ControlType.ANIMATED_TEXTURE, StaticDataCreator.INSTANCE),
	ANIMATED_USER(ControlType.ANIMATED_USER, StaticDataCreator.INSTANCE),
	ITEMSLOT(ControlType.ITEMSLOT, StaticDataCreator.INSTANCE),

	SLIDER(ControlType.SLIDER, StaticDataCreator.INSTANCE),
	XSLIDER(ControlType.XSLIDER, StaticDataCreator.INSTANCE),

	COMBO(ControlType.COMBO, StaticDataCreator.INSTANCE),
	XCOMBO(ControlType.XCOMBO, StaticDataCreator.INSTANCE),

	LISTBOX(ControlType.LISTBOX, StaticDataCreator.INSTANCE),
	XLISTBOX(ControlType.XLISTBOX, StaticDataCreator.INSTANCE),
	LISTNBOX(ControlType.LISTNBOX, StaticDataCreator.INSTANCE),

	TOOLBOX(ControlType.TOOLBOX, StaticDataCreator.INSTANCE),
	CHECKBOXES(ControlType.CHECKBOXES, StaticDataCreator.INSTANCE),
	CHECKBOX(ControlType.CHECKBOX, StaticDataCreator.INSTANCE),

	CONTEXT_MENU(ControlType.CONTEXT_MENU, StaticDataCreator.INSTANCE),
	MENU(ControlType.MENU, StaticDataCreator.INSTANCE),
	MENU_STRIP(ControlType.MENU_STRIP, StaticDataCreator.INSTANCE),

	OBJECT(ControlType.OBJECT, StaticDataCreator.INSTANCE),
	OBJECT_ZOOM(ControlType.OBJECT_ZOOM, StaticDataCreator.INSTANCE),
	OBJECT_CONTAINER(ControlType.OBJECT_CONTAINER, StaticDataCreator.INSTANCE),
	OBJECT_CONT_ANIM(ControlType.OBJECT_CONT_ANIM, StaticDataCreator.INSTANCE),

	MAP(ControlType.MAP, StaticDataCreator.INSTANCE),
	MAP_MAIN(ControlType.MAP_MAIN, StaticDataCreator.INSTANCE);

	public final ControlType controlType;
	public final TreeItemDataCreator<TreeItemEntry> creator;
	public final boolean allowsSubControls;

	ControlTreeItemDataCreatorLookup(ControlType controlType, TreeItemDataCreator<TreeItemEntry> creator) {
		this(controlType, creator, false);
	}

	ControlTreeItemDataCreatorLookup(ControlType controlType, TreeItemDataCreator<TreeItemEntry> creator, boolean allowsSubControls) {
		this.controlType = controlType;
		this.creator = creator;
		this.allowsSubControls = allowsSubControls;
	}
}
