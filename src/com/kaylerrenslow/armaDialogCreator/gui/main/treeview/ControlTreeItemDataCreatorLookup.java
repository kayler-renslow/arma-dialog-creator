package com.kaylerrenslow.armaDialogCreator.gui.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.dataCreator.*;

/**
 Created by Kayler on 06/19/2016.
 */
public enum ControlTreeItemDataCreatorLookup {
	STATIC(ControlType.Static, StaticDataCreator.INSTANCE),
	HTML(ControlType.HTML, StaticDataCreator.INSTANCE),
	EDIT(ControlType.Edit, EditDataCreator.INSTANCE),
	STRUCTURED_TEXT(ControlType.StructuredText, StaticDataCreator.INSTANCE),
	ACTIVETEXT(ControlType.ActiveText, StaticDataCreator.INSTANCE),

	BUTTON(ControlType.Button, ButtonDataCreator.INSTANCE),
	SHORTCUTBUTTON(ControlType.ShortcutButton, ShortcutButtonDataCreator.INSTANCE),
	XBUTTON(ControlType.XButton, StaticDataCreator.INSTANCE),

	PROGRESS(ControlType.Progress, ProgressDataCreator.INSTANCE),
	STATIC_SKEW(ControlType.StaticSkew, StaticDataCreator.INSTANCE),
	LINEBREAK(ControlType.Linebreak, StaticDataCreator.INSTANCE),
	TREE(ControlType.Tree, StaticDataCreator.INSTANCE),
	CONTROLS_GROUP(ControlType.ControlsGroup, ControlGroupDataCreator.INSTANCE, true),
	XKEYDESC(ControlType.XKeyDesc, StaticDataCreator.INSTANCE),
	ANIMATED_TEXTURE(ControlType.AnimatedTexture, StaticDataCreator.INSTANCE),
	ANIMATED_USER(ControlType.AnimatedUser, StaticDataCreator.INSTANCE),
	ITEMSLOT(ControlType.ItemSlot, StaticDataCreator.INSTANCE),

	SLIDER(ControlType.Slider, StaticDataCreator.INSTANCE),
	XSLIDER(ControlType.XSlider, XSliderDataCreator.INSTANCE),

	COMBO(ControlType.Combo, ComboDataCreator.INSTANCE),
	XCOMBO(ControlType.XCombo, StaticDataCreator.INSTANCE),

	LISTBOX(ControlType.ListBox, ListboxDataCreator.INSTANCE),
	XLISTBOX(ControlType.XListBox, StaticDataCreator.INSTANCE),
	LISTNBOX(ControlType.ListNBox, StaticDataCreator.INSTANCE),

	TOOLBOX(ControlType.ToolBox, StaticDataCreator.INSTANCE),
	CHECKBOXES(ControlType.CheckBoxes, StaticDataCreator.INSTANCE),
	CHECKBOX(ControlType.CheckBox, StaticDataCreator.INSTANCE),

	CONTEXT_MENU(ControlType.ContextMenu, StaticDataCreator.INSTANCE),
	MENU(ControlType.Menu, StaticDataCreator.INSTANCE),
	MENU_STRIP(ControlType.MenuStrip, StaticDataCreator.INSTANCE),

	OBJECT(ControlType.Object, StaticDataCreator.INSTANCE),
	OBJECT_ZOOM(ControlType.ObjectZoom, StaticDataCreator.INSTANCE),
	OBJECT_CONTAINER(ControlType.ObjectContainer, StaticDataCreator.INSTANCE),
	OBJECT_CONT_ANIM(ControlType.ObjectContAnim, StaticDataCreator.INSTANCE),

	MAP(ControlType.Map, StaticDataCreator.INSTANCE),
	MAP_MAIN(ControlType.MapMain, StaticDataCreator.INSTANCE);

	public final ControlType controlType;
	public final TreeItemDataCreator<ArmaControl, TreeItemEntry> creator;
	public final boolean allowsSubControls;

	ControlTreeItemDataCreatorLookup(ControlType controlType, TreeItemDataCreator<ArmaControl, TreeItemEntry> creator) {
		this(controlType, creator, false);
	}

	ControlTreeItemDataCreatorLookup(ControlType controlType, TreeItemDataCreator<ArmaControl, TreeItemEntry> creator, boolean allowsSubControls) {
		this.controlType = controlType;
		this.creator = creator;
		this.allowsSubControls = allowsSubControls;
	}
}
