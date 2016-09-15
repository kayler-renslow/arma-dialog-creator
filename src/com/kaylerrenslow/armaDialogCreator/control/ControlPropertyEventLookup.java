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

import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 A place to store all event control properties
 Created on 08/21/2016. */
public enum ControlPropertyEventLookup {
	EVENT_ON_LOAD(ControlPropertyLookup.EVENT_ON_LOAD, 1, "Display"),
	EVENT_ON_UNLOAD(ControlPropertyLookup.EVENT_ON_UNLOAD, 1, "Display"),
	EVENT_ON_CHILD_DESTROYED(ControlPropertyLookup.EVENT_ON_CHILD_DESTROYED, 1, "Display"),
	EVENT_ON_MOUSE_ENTER(ControlPropertyLookup.EVENT_ON_MOUSE_ENTER, 1, "Control"),
	EVENT_ON_MOUSE_EXIT(ControlPropertyLookup.EVENT_ON_MOUSE_EXIT, 1, "Control"),
	EVENT_ON_SET_FOCUS(ControlPropertyLookup.EVENT_ON_SET_FOCUS, 2, "Control"),
	EVENT_ON_KILL_FOCUS(ControlPropertyLookup.EVENT_ON_KILL_FOCUS, 2, "Control"),
	EVENT_ON_TIMER(ControlPropertyLookup.EVENT_ON_TIMER, 3, "Control"),
	EVENT_ON_KEY_DOWN(ControlPropertyLookup.EVENT_ON_KEY_DOWN, 2, "Display, Control"),
	EVENT_ON_KEY_UP(ControlPropertyLookup.EVENT_ON_KEY_UP, 2, "Display, Control"),
	EVENT_ON_CHAR(ControlPropertyLookup.EVENT_ON_CHAR, 2, "Control"),
	EVENT_ON_IME_CHAR(ControlPropertyLookup.EVENT_ON_IME_CHAR, 2, "Control"),
	EVENT_ON_IME_COMPOSITION(ControlPropertyLookup.EVENT_ON_IME_COMPOSITION, 2, "Control"),
	EVENT_ON_JOYSTICK_BUTTON(ControlPropertyLookup.EVENT_ON_JOYSTICK_BUTTON, 3, "Control"),
	EVENT_ON_MOUSE_BUTTON_DOWN(ControlPropertyLookup.EVENT_ON_MOUSE_BUTTON_DOWN, 2, "Control"),
	EVENT_ON_MOUSE_BUTTON_UP(ControlPropertyLookup.EVENT_ON_MOUSE_BUTTON_UP, 2, "Control"),
	EVENT_ON_MOUSE_BUTTON_CLICK(ControlPropertyLookup.EVENT_ON_MOUSE_BUTTON_CLICK, 2, "ListBox, ComboBox, TextBox, Button, ActiveText"),
	EVENT_ON_MOUSE_BUTTON_DBL_CLICK(ControlPropertyLookup.EVENT_ON_MOUSE_BUTTON_DBL_CLICK, 2, "Control"),
	EVENT_ON_MOUSE_MOVING(ControlPropertyLookup.EVENT_ON_MOUSE_MOVING, 2, "Control"),
	EVENT_ON_MOUSE_HOLDING(ControlPropertyLookup.EVENT_ON_MOUSE_HOLDING, 2, "Display"),
	EVENT_ON_MOUSE_ZCHANGED(ControlPropertyLookup.EVENT_ON_MOUSE_ZCHANGED, 2, "Control"),
	EVENT_ON_CAN_DESTROY(ControlPropertyLookup.EVENT_ON_CAN_DESTROY, 3, "Control only"),
	EVENT_ON_DESTROY(ControlPropertyLookup.EVENT_ON_DESTROY, 3, "Control"),
	EVENT_ON_BUTTON_CLICK(ControlPropertyLookup.EVENT_ON_BUTTON_CLICK, 1, "Control"),
	EVENT_ON_BUTTON_DBL_CLICK(ControlPropertyLookup.EVENT_ON_BUTTON_DBL_CLICK, -1, "Button"),
	EVENT_ON_BUTTON_DOWN(ControlPropertyLookup.EVENT_ON_BUTTON_DOWN, 1, "Button"),
	EVENT_ON_BUTTON_UP(ControlPropertyLookup.EVENT_ON_BUTTON_UP, 1, "Button"),
	EVENT_ON_LB_SEL_CHANGED(ControlPropertyLookup.EVENT_ON_LB_SEL_CHANGED, 2, "Button"),
	EVENT_ON_LB_LIST_SEL_CHANGED(ControlPropertyLookup.EVENT_ON_LB_LIST_SEL_CHANGED, 2, "ListBox"),
	EVENT_ON_LB_DBL_CLICK(ControlPropertyLookup.EVENT_ON_LB_DBL_CLICK, 2, "ListBox"),
	EVENT_ON_LB_DRAG(ControlPropertyLookup.EVENT_ON_LB_DRAG, 2, "ListBox"),
	EVENT_ON_LB_DRAGGING(ControlPropertyLookup.EVENT_ON_LB_DRAGGING, 2, "ListBox"),
	EVENT_ON_LB_DROP(ControlPropertyLookup.EVENT_ON_LB_DROP, 2, "ListBox"),
	EVENT_ON_TREE_SEL_CHANGED(ControlPropertyLookup.EVENT_ON_TREE_SEL_CHANGED, 2, "ListBox, ComboBox, TextBox, ActiveText, Button"),
	EVENT_ON_TREE_LBUTTON_DOWN(ControlPropertyLookup.EVENT_ON_TREE_LBUTTON_DOWN, 2, "Tree"),
	EVENT_ON_TREE_DBL_CLICK(ControlPropertyLookup.EVENT_ON_TREE_DBL_CLICK, 2, "Tree"),
	EVENT_ON_TREE_EXPANDED(ControlPropertyLookup.EVENT_ON_TREE_EXPANDED, 3, "Tree"),
	EVENT_ON_TREE_COLLAPSED(ControlPropertyLookup.EVENT_ON_TREE_COLLAPSED, 3, "Tree"),
	EVENT_ON_TREE_MOUSE_MOVE(ControlPropertyLookup.EVENT_ON_TREE_MOUSE_MOVE, 2, "Tree"),
	EVENT_ON_TREE_MOUSE_HOLD(ControlPropertyLookup.EVENT_ON_TREE_MOUSE_HOLD, 2, "Tree"),
	EVENT_ON_TREE_MOUSE_EXIT(ControlPropertyLookup.EVENT_ON_TREE_MOUSE_EXIT, 2, "Tree"),
	EVENT_ON_TOOL_BOX_SEL_CHANGED(ControlPropertyLookup.EVENT_ON_TOOL_BOX_SEL_CHANGED, 2, "Tree"),
	EVENT_ON_CHECKED(ControlPropertyLookup.EVENT_ON_CHECKED, -1, "Toolbox"),
	EVENT_ON_CHECKED_CHANGED(ControlPropertyLookup.EVENT_ON_CHECKED_CHANGED, -1, "Checkbox"),
	EVENT_ON_CHECK_BOXES_SEL_CHANGED(ControlPropertyLookup.EVENT_ON_CHECK_BOXES_SEL_CHANGED, 2, "Checkbox"),
	EVENT_ON_HTML_LINK(ControlPropertyLookup.EVENT_ON_HTML_LINK, 2, "Checkbox"),
	EVENT_ON_SLIDER_POS_CHANGED(ControlPropertyLookup.EVENT_ON_SLIDER_POS_CHANGED, 2, "HTML"),
	EVENT_ON_OBJECT_MOVED(ControlPropertyLookup.EVENT_ON_OBJECT_MOVED, 2, "Slider"),
	EVENT_ON_MENU_SELECTED(ControlPropertyLookup.EVENT_ON_MENU_SELECTED, 2, "Object"),
	EVENT_ON_DRAW(ControlPropertyLookup.EVENT_ON_DRAW, -1, "Context menu"),
	EVENT_ON_VIDEO_STOPPED(ControlPropertyLookup.EVENT_ON_VIDEO_STOPPED, 2, "Map");

	public final ControlPropertyLookup lookup;
	public final int priority;
	public final String scope;

	ControlPropertyEventLookup(ControlPropertyLookup lookup, int priority, String scope) {
		this.lookup = lookup;
		this.priority = priority;
		this.scope = scope;
	}

	public String getPriority() {
		return priority < 0 ? "unknown" : priority + "";
	}

	/**Return the {@link ControlPropertyEventLookup} instance that is associated with the given lookup. If no correlation exists, will return null. */
	@Nullable
	public static ControlPropertyEventLookup getEventProperty(ControlPropertyLookupConstant lookup) {
		for (ControlPropertyEventLookup eventLookup : values()) {
			if (eventLookup.lookup == lookup) {
				return eventLookup;
			}
		}
		return null;
	}
}
