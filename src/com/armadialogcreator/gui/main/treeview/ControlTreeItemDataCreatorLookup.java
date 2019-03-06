package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/19/2016.
 */
public enum ControlTreeItemDataCreatorLookup {
	STATIC(ControlType.Static),
	HTML(ControlType.HTML),
	EDIT(ControlType.Edit),
	STRUCTURED_TEXT(ControlType.StructuredText),
	ACTIVETEXT(ControlType.ActiveText),

	BUTTON(ControlType.Button),
	SHORTCUTBUTTON(ControlType.ShortcutButton),
	XBUTTON(ControlType.XButton),

	PROGRESS(ControlType.Progress),
	STATIC_SKEW(ControlType.StaticSkew),
	LINEBREAK(ControlType.Linebreak),
	TREE(ControlType.Tree),
	CONTROLS_GROUP(ControlType.ControlsGroup, true),
	XKEYDESC(ControlType.XKeyDesc),
	ANIMATED_TEXTURE(ControlType.AnimatedTexture),
	ANIMATED_USER(ControlType.AnimatedUser),
	ITEMSLOT(ControlType.ItemSlot),

	SLIDER(ControlType.Slider),
	XSLIDER(ControlType.XSlider),

	COMBO(ControlType.Combo),
	XCOMBO(ControlType.XCombo),

	LISTBOX(ControlType.ListBox),
	XLISTBOX(ControlType.XListBox),
	LISTNBOX(ControlType.ListNBox),

	TOOLBOX(ControlType.ToolBox),
	CHECKBOXES(ControlType.CheckBoxes),
	CHECKBOX(ControlType.CheckBox),

	CONTEXT_MENU(ControlType.ContextMenu),
	MENU(ControlType.Menu),
	MENU_STRIP(ControlType.MenuStrip),

	OBJECT(ControlType.Object),
	OBJECT_ZOOM(ControlType.ObjectZoom),
	OBJECT_CONTAINER(ControlType.ObjectContainer),
	OBJECT_CONT_ANIM(ControlType.ObjectContAnim),

	MAP(ControlType.Map),
	MAP_MAIN(ControlType.MapMain);

	public final ControlType controlType;
	public final TreeItemDataCreator<ArmaControl, UINodeTreeItemData> creator;
	public final boolean allowsSubControls;

	ControlTreeItemDataCreatorLookup(@NotNull ControlType controlType) {
		this(controlType, new GenericControlTreeItemCreator(controlType), false);
	}

	ControlTreeItemDataCreatorLookup(@NotNull ControlType controlType, boolean allowsSubControls) {
		this(controlType, new GenericControlTreeItemCreator(controlType), allowsSubControls);
	}

	ControlTreeItemDataCreatorLookup(@NotNull ControlType controlType, @NotNull TreeItemDataCreator<ArmaControl, UINodeTreeItemData> creator, boolean allowsSubControls) {
		this.controlType = controlType;
		this.creator = creator;
		this.allowsSubControls = allowsSubControls;
	}
}
