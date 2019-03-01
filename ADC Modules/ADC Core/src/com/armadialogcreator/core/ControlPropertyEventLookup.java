package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 A place to store all event control properties

 @author Kayler
 @since 08/21/2016. */
public enum ControlPropertyEventLookup {
	EventOnLoad(ConfigPropertyLookup.EVENT_ON_LOAD, 1, "Display, Control"),
	EventOnUnload(ConfigPropertyLookup.EVENT_ON_UNLOAD, 1, "Display"),
	EventOnChildDestroyed(ConfigPropertyLookup.EVENT_ON_CHILD_DESTROYED, 1, "Display"),
	EventOnMouseEnter(ConfigPropertyLookup.EVENT_ON_MOUSE_ENTER, 1, "Control"),
	EventOnMouseExit(ConfigPropertyLookup.EVENT_ON_MOUSE_EXIT, 1, "Control"),
	EventOnSetFocus(ConfigPropertyLookup.EVENT_ON_SET_FOCUS, 2, "Control"),
	EventOnKillFocus(ConfigPropertyLookup.EVENT_ON_KILL_FOCUS, 2, "Control"),
	EventOnTimer(ConfigPropertyLookup.EVENT_ON_TIMER, 3, "Control"),
	EventOnKeyDown(ConfigPropertyLookup.EVENT_ON_KEY_DOWN, 2, "Display, Control"),
	EventOnKeyUp(ConfigPropertyLookup.EVENT_ON_KEY_UP, 2, "Display, Control"),
	EventOnChar(ConfigPropertyLookup.EVENT_ON_CHAR, 2, "Control"),
	EventOnImeChar(ConfigPropertyLookup.EVENT_ON_IME_CHAR, 2, "Control"),
	EventOnImeComposition(ConfigPropertyLookup.EVENT_ON_IME_COMPOSITION, 2, "Control"),
	EventOnJoystickButton(ConfigPropertyLookup.EVENT_ON_JOYSTICK_BUTTON, 3, "Control"),
	EventOnMouseButtonDown(ConfigPropertyLookup.EVENT_ON_MOUSE_BUTTON_DOWN, 2, "Control"),
	EventOnMouseButtonUp(ConfigPropertyLookup.EVENT_ON_MOUSE_BUTTON_UP, 2, "Control"),
	EventOnMouseButtonClick(ConfigPropertyLookup.EVENT_ON_MOUSE_BUTTON_CLICK, 2, "ListBox, ComboBox, TextBox, Button, ActiveText"),
	EventOnMouseButtonDblClick(ConfigPropertyLookup.EVENT_ON_MOUSE_BUTTON_DBL_CLICK, 2, "Control"),
	EventOnMouseMoving(ConfigPropertyLookup.EVENT_ON_MOUSE_MOVING, 2, "Control"),
	EventOnMouseHolding(ConfigPropertyLookup.EVENT_ON_MOUSE_HOLDING, 2, "Display"),
	EventOnMouseZChanged(ConfigPropertyLookup.EVENT_ON_MOUSE_ZCHANGED, 2, "Display, Control"),
	EventOnCanDestroy(ConfigPropertyLookup.EVENT_ON_CAN_DESTROY, 3, "Control"),
	EventOnDestroy(ConfigPropertyLookup.EVENT_ON_DESTROY, 3, "Control"),
	EventOnButtonClick(ConfigPropertyLookup.EVENT_ON_BUTTON_CLICK, 1, "Button"),
	EventOnButtonDblClick(ConfigPropertyLookup.EVENT_ON_BUTTON_DBL_CLICK, -1, "Button"),
	EventOnButtonDown(ConfigPropertyLookup.EVENT_ON_BUTTON_DOWN, 1, "Button"),
	EventOnButtonUp(ConfigPropertyLookup.EVENT_ON_BUTTON_UP, 1, "Button"),
	EventOnLbSelChanged(ConfigPropertyLookup.EVENT_ON_LB_SEL_CHANGED, 2, "ListBox, ComboBox"),
	EventOnLbListSelChanged(ConfigPropertyLookup.EVENT_ON_LB_LIST_SEL_CHANGED, 2, "ListBox"),
	EventOnLbDblClick(ConfigPropertyLookup.EVENT_ON_LB_DBL_CLICK, 2, "ListBox"),
	EventOnLbDrag(ConfigPropertyLookup.EVENT_ON_LB_DRAG, 2, "ListBox"),
	EventOnLbDragging(ConfigPropertyLookup.EVENT_ON_LB_DRAGGING, 2, "ListBox"),
	EventOnLbDrop(ConfigPropertyLookup.EVENT_ON_LB_DROP, 2, "Listbox, Combobox, Textbox, ActiveText, Button"),
	EventOnTreeSelChanged(ConfigPropertyLookup.EVENT_ON_TREE_SEL_CHANGED, 2, "Tree"),
	EventOnTreeLButtonDown(ConfigPropertyLookup.EVENT_ON_TREE_LBUTTON_DOWN, 2, "Tree"),
	EventOnTreeDblClick(ConfigPropertyLookup.EVENT_ON_TREE_DBL_CLICK, 2, "Tree"),
	EventOnTreeExpanded(ConfigPropertyLookup.EVENT_ON_TREE_EXPANDED, 3, "Tree"),
	EventOnTreeCollapsed(ConfigPropertyLookup.EVENT_ON_TREE_COLLAPSED, 3, "Tree"),
	EventOnTreeMouseMove(ConfigPropertyLookup.EVENT_ON_TREE_MOUSE_MOVE, 2, "Tree"),
	EventOnTreeMouseHold(ConfigPropertyLookup.EVENT_ON_TREE_MOUSE_HOLD, 2, "Tree"),
	EventOnTreeMouseExit(ConfigPropertyLookup.EVENT_ON_TREE_MOUSE_EXIT, 2, "Tree"),
	EventOnToolBoxSelChanged(ConfigPropertyLookup.EVENT_ON_TOOL_BOX_SEL_CHANGED, 2, "Toolbox"),
	EventOnChecked(ConfigPropertyLookup.EVENT_ON_CHECKED, -1, "Checkbox"),
	EventOnCheckedChanged(ConfigPropertyLookup.EVENT_ON_CHECKED_CHANGED, -1, "Checkbox"),
	EventOnCheckBoxesSelChanged(ConfigPropertyLookup.EVENT_ON_CHECK_BOXES_SEL_CHANGED, 2, "Checkbox"),
	EventOnHtmlLink(ConfigPropertyLookup.EVENT_ON_HTML_LINK, 2, "HTML"),
	EventOnSliderPosChanged(ConfigPropertyLookup.EVENT_ON_SLIDER_POS_CHANGED, 2, "Slider"),
	EventOnObjectMoved(ConfigPropertyLookup.EVENT_ON_OBJECT_MOVED, 2, "Object"),
	EventOnMenuSelected(ConfigPropertyLookup.EVENT_ON_MENU_SELECTED, 2, "Context menu"),
	EventOnDraw(ConfigPropertyLookup.EVENT_ON_DRAW, -1, "Map"),
	EventOnVideoStopped(ConfigPropertyLookup.EVENT_ON_VIDEO_STOPPED, 2, "Control");

	@NotNull
	public final ConfigPropertyLookup lookup;
	public final int priority;
	@NotNull
	public final String scope;

	ControlPropertyEventLookup(@NotNull ConfigPropertyLookup lookup, int priority, @NotNull String scope) {
		this.lookup = lookup;
		this.priority = priority;
		this.scope = scope;
	}

	public String getPriority() {
		return priority < 0 ? "unknown" : priority + "";
	}

	/** Return the {@link ControlPropertyEventLookup} instance that is associated with the given lookup. If no correlation exists, will return null. */
	@Nullable
	public static ControlPropertyEventLookup getEventProperty(ConfigPropertyLookupConstant lookup) {
		for (ControlPropertyEventLookup eventLookup : values()) {
			if (eventLookup.lookup == lookup) {
				return eventLookup;
			}
		}
		return null;
	}

	@NotNull
	public static ConfigPropertyLookup[] allWithControlScope() {
		return allWith("control");
	}

	@NotNull
	public static ConfigPropertyLookup[] allWithButtonScope() {
		return allWith("button");
	}

	@NotNull
	private static ConfigPropertyLookup[] allWith(@NotNull String s) {
		ArrayList<ConfigPropertyLookup> lookups = new ArrayList<>(values().length);
		for (ControlPropertyEventLookup lookup : values()) {
			if (lookup.scope.toLowerCase().contains(s)) {
				lookups.add(lookup.lookup);
			}
		}
		return lookups.toArray(new ConfigPropertyLookup[lookups.size()]);
	}

	@NotNull
	public static ConfigPropertyLookup[] allWithSliderScope() {
		return allWith("slider");
	}

	@NotNull
	public static ConfigPropertyLookup[] allWithComboScope() {
		return allWith("combo");
	}

	@NotNull
	public static ConfigPropertyLookup[] allWithListboxScope() {
		return allWith("listbox");
	}
}
