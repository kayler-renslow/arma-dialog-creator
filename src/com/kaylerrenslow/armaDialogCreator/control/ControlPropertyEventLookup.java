package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 A place to store all event control properties

 @author Kayler
 @since 08/21/2016. */
public enum ControlPropertyEventLookup {
	EventOnLoad(ControlPropertyLookup.EVENT_ON_LOAD, 1, "Display"),
	EventOnUnload(ControlPropertyLookup.EVENT_ON_UNLOAD, 1, "Display"),
	EventOnChildDestroyed(ControlPropertyLookup.EVENT_ON_CHILD_DESTROYED, 1, "Display"),
	EventOnMouseEnter(ControlPropertyLookup.EVENT_ON_MOUSE_ENTER, 1, "Control"),
	EventOnMouseExit(ControlPropertyLookup.EVENT_ON_MOUSE_EXIT, 1, "Control"),
	EventOnSetFocus(ControlPropertyLookup.EVENT_ON_SET_FOCUS, 2, "Control"),
	EventOnKillFocus(ControlPropertyLookup.EVENT_ON_KILL_FOCUS, 2, "Control"),
	EventOnTimer(ControlPropertyLookup.EVENT_ON_TIMER, 3, "Control"),
	EventOnKeyDown(ControlPropertyLookup.EVENT_ON_KEY_DOWN, 2, "Display, Control"),
	EventOnKeyUp(ControlPropertyLookup.EVENT_ON_KEY_UP, 2, "Display, Control"),
	EventOnChar(ControlPropertyLookup.EVENT_ON_CHAR, 2, "Control"),
	EventOnImeChar(ControlPropertyLookup.EVENT_ON_IME_CHAR, 2, "Control"),
	EventOnImeComposition(ControlPropertyLookup.EVENT_ON_IME_COMPOSITION, 2, "Control"),
	EventOnJoystickButton(ControlPropertyLookup.EVENT_ON_JOYSTICK_BUTTON, 3, "Control"),
	EventOnMouseButtonDown(ControlPropertyLookup.EVENT_ON_MOUSE_BUTTON_DOWN, 2, "Control"),
	EventOnMouseButtonUp(ControlPropertyLookup.EVENT_ON_MOUSE_BUTTON_UP, 2, "Control"),
	EventOnMouseButtonClick(ControlPropertyLookup.EVENT_ON_MOUSE_BUTTON_CLICK, 2, "ListBox, ComboBox, TextBox, Button, ActiveText"),
	EventOnMouseButtonDblClick(ControlPropertyLookup.EVENT_ON_MOUSE_BUTTON_DBL_CLICK, 2, "Control"),
	EventOnMouseMoving(ControlPropertyLookup.EVENT_ON_MOUSE_MOVING, 2, "Control"),
	EventOnMouseHolding(ControlPropertyLookup.EVENT_ON_MOUSE_HOLDING, 2, "Display"),
	EventOnMouseZChanged(ControlPropertyLookup.EVENT_ON_MOUSE_ZCHANGED, 2, "Control"),
	EventOnCanDestroy(ControlPropertyLookup.EVENT_ON_CAN_DESTROY, 3, "Control only"),
	EventOnDestroy(ControlPropertyLookup.EVENT_ON_DESTROY, 3, "Control"),
	EventOnButtonClick(ControlPropertyLookup.EVENT_ON_BUTTON_CLICK, 1, "Control"),
	EventOnButtonDblClick(ControlPropertyLookup.EVENT_ON_BUTTON_DBL_CLICK, -1, "Button"),
	EventOnButtonDown(ControlPropertyLookup.EVENT_ON_BUTTON_DOWN, 1, "Button"),
	EventOnButtonUp(ControlPropertyLookup.EVENT_ON_BUTTON_UP, 1, "Button"),
	EventOnLbSelChanged(ControlPropertyLookup.EVENT_ON_LB_SEL_CHANGED, 2, "Button"),
	EventOnLbListSelChanged(ControlPropertyLookup.EVENT_ON_LB_LIST_SEL_CHANGED, 2, "ListBox"),
	EventOnLbDblClick(ControlPropertyLookup.EVENT_ON_LB_DBL_CLICK, 2, "ListBox"),
	EventOnLbDrag(ControlPropertyLookup.EVENT_ON_LB_DRAG, 2, "ListBox"),
	EventOnLbDragging(ControlPropertyLookup.EVENT_ON_LB_DRAGGING, 2, "ListBox"),
	EventOnLbDrop(ControlPropertyLookup.EVENT_ON_LB_DROP, 2, "ListBox"),
	EventOnTreeSelChanged(ControlPropertyLookup.EVENT_ON_TREE_SEL_CHANGED, 2, "ListBox, ComboBox, TextBox, ActiveText, Button"),
	EventOnTreeLButtonDown(ControlPropertyLookup.EVENT_ON_TREE_LBUTTON_DOWN, 2, "Tree"),
	EventOnTreeDblClick(ControlPropertyLookup.EVENT_ON_TREE_DBL_CLICK, 2, "Tree"),
	EventOnTreeExpanded(ControlPropertyLookup.EVENT_ON_TREE_EXPANDED, 3, "Tree"),
	EventOnTreeCollapsed(ControlPropertyLookup.EVENT_ON_TREE_COLLAPSED, 3, "Tree"),
	EventOnTreeMouseMove(ControlPropertyLookup.EVENT_ON_TREE_MOUSE_MOVE, 2, "Tree"),
	EventOnTreeMouseHold(ControlPropertyLookup.EVENT_ON_TREE_MOUSE_HOLD, 2, "Tree"),
	EventOnTreeMouseExit(ControlPropertyLookup.EVENT_ON_TREE_MOUSE_EXIT, 2, "Tree"),
	EventOnToolBoxSelChanged(ControlPropertyLookup.EVENT_ON_TOOL_BOX_SEL_CHANGED, 2, "Tree"),
	EventOnChecked(ControlPropertyLookup.EVENT_ON_CHECKED, -1, "Toolbox"),
	EventOnCheckedChanged(ControlPropertyLookup.EVENT_ON_CHECKED_CHANGED, -1, "Checkbox"),
	EventOnCheckBoxesSelChanged(ControlPropertyLookup.EVENT_ON_CHECK_BOXES_SEL_CHANGED, 2, "Checkbox"),
	EventOnHtmlLink(ControlPropertyLookup.EVENT_ON_HTML_LINK, 2, "Checkbox"),
	EventOnSliderPosChanged(ControlPropertyLookup.EVENT_ON_SLIDER_POS_CHANGED, 2, "HTML"),
	EventOnObjectMoved(ControlPropertyLookup.EVENT_ON_OBJECT_MOVED, 2, "Slider"),
	EventOnMenuSelected(ControlPropertyLookup.EVENT_ON_MENU_SELECTED, 2, "Object"),
	EventOnDraw(ControlPropertyLookup.EVENT_ON_DRAW, -1, "Context menu"),
	EventOnVideoStopped(ControlPropertyLookup.EVENT_ON_VIDEO_STOPPED, 2, "Map");

	@NotNull
	public final ControlPropertyLookup lookup;
	public final int priority;
	@NotNull
	public final String scope;

	ControlPropertyEventLookup(@NotNull ControlPropertyLookup lookup, int priority, @NotNull String scope) {
		this.lookup = lookup;
		this.priority = priority;
		this.scope = scope;
	}

	public String getPriority() {
		return priority < 0 ? "unknown" : priority + "";
	}

	/** Return the {@link ControlPropertyEventLookup} instance that is associated with the given lookup. If no correlation exists, will return null. */
	@Nullable
	public static ControlPropertyEventLookup getEventProperty(ControlPropertyLookupConstant lookup) {
		for (ControlPropertyEventLookup eventLookup : values()) {
			if (eventLookup.lookup == lookup) {
				return eventLookup;
			}
		}
		return null;
	}

	private static ControlPropertyLookup[] allWithControlPriority;

	@NotNull
	public static ControlPropertyLookup[] allWithControlScope() {
		if (allWithControlPriority == null) {
			ArrayList<ControlPropertyLookup> lookups = new ArrayList<>(values().length);
			for (ControlPropertyEventLookup lookup : values()) {
				if (lookup.scope.toLowerCase().contains("control")) {
					lookups.add(lookup.lookup);
				}
			}
			allWithControlPriority = lookups.toArray(new ControlPropertyLookup[lookups.size()]);
		}

		return allWithControlPriority;
	}
}
