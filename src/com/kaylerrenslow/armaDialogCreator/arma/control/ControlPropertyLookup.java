package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty.PropertyType;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.AHexColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 Created by Kayler on 05/22/2016.
 */
public enum ControlPropertyLookup {
	IDC(0, "idc", PropertyType.INT, "Control id, or -1 if doesn't matter."),
	X(1, "x", PropertyType.FLOAT, "X position."),
	Y(2, "y", PropertyType.FLOAT, "Y position."),
	W(3, "w", PropertyType.FLOAT, "Width of control."),
	H(4, "h", PropertyType.FLOAT, "Height of control."),
	TYPE(5, "type", PropertyType.INT, "Type of the control."),
	STYLE(6, "style", PropertyType.INT, "Style of the control."),
	ACCESS(7, "access", PropertyType.INT, "Read and write setting.", new Option("Read and Write", "0", "Default case where properties can still be added or overridden."), new Option("Read and Create", "1", "Only allows creating new properties."), new Option("Read Only", "2", "Does not allow to do anything in deriving classes."), new Option("Read Only Verified", "3", "Does not allow to do anything either in deriving classes, and a CRC check will be performed.")),

	/*Common*/
	/** moving: boolean. Set whether control can be dragged */
	MOVING(8, "moving", PropertyType.BOOLEAN, "Whether or not this control can be dragged."),
	/** sizeEx: float. Set font size */
	SIZE_EX(9, "sizeEx", PropertyType.FLOAT, "Font size of text."),
	FONT(10, "font", PropertyType.FONT, "Font for text."),
	COLOR_TEXT(11, "colorText", PropertyType.COLOR, "Color of text."),
	COLOR_BACKGROUND(12, "colorBackground", PropertyType.COLOR, "Background color of control."),
	TEXT(13, "text", PropertyType.STRING, "Text to show."),
	SHADOW(14, "shadow", PropertyType.INT, "Shadow for control.", new Option("No", "0", "No shadow."), new Option("Yes", "1", "Drop shadow with soft edges."), new Option("Stroke", "2", "Stroke")), //does absolutely nothing inside the Attributes class for structured text
	TOOLTIP(15, "tooltip", PropertyType.STRING, "Text to display when mouse hovers over this control."),
	TOOLTIP_COLOR_SHADE(16, "tooltipColorShade", PropertyType.COLOR, "Tooltip background color."),
	TOOLTIP_COLOR_TEXT(17, "tooltipColorText", PropertyType.COLOR, "Tooltip text color."),
	TOOLTIP_COLOR_BOX(18, "tooltipColorBox", PropertyType.COLOR, "Tooltip border color."),
	ALIGN(19, "align", PropertyType.STRING, "Horizontal align of text.", new Option("Left", "left", "Left align."), new Option("Center", "center", "Center align."), new Option("Right", "right", "Right align.")),
	VALIGN(20, "valign", PropertyType.STRING, "Vertical align of text.", new Option("Top", "top", "Top align."), new Option("Middle", "middle", "Middle align."), new Option("Bottom", "bottom", "Bottom align.")),
	COLOR_HEX(21, "color", PropertyType.HEX_COLOR_STRING, "Text color."),
	SHADOW_COLOR(22, "shadowColor", PropertyType.HEX_COLOR_STRING, "Shadow color."), //default shadow color
	BLINKING_PERIOD(23, "blinkingPeriod", PropertyType.FLOAT, "Makes the text start transparent, go to full opacity and back to full transparent in the amount of seconds specified."),
	SOUND_CLICK(54, "soundClick", PropertyType.SOUND, "Sound to play when mouse button is released."),
	SOUND_ENTER(55, "soundEnter", PropertyType.SOUND, "Sound to play when mouse cursor is moved over the control."),
	SOUND_ESCAPE(56, "soundEscape", PropertyType.SOUND, "Sound to play when the control was clicked via the mouse, and then released outside the control area."),
	SOUND_PUSH(57, "soundPush", PropertyType.SOUND, "Sound to play when mouse is clicked on control."),

	/*Static*/
	STATIC_AUTO_PLAY(24, "autoPlay", PropertyType.BOOLEAN, "Autoplay the video (video only)."),
	STATIC_KEY(25, "key", PropertyType.STRING, "From the wiki:\"a possibly quite useless xbox value\"."),
	STATIC_LOOPS(26, "loops", PropertyType.INT, "Number of times the video loops."),
	STATIC_LINE_SPACING(27, "lineSpacing", PropertyType.FLOAT, "Line spacing of text and is required if style is MULTI (16)"),
	STATIC_FIXED_WIDTH(28, "fixedWidth", PropertyType.BOOLEAN, null),

	/*Structured Text*/
	STRUCT_TEXT_SIZE(29, "size", PropertyType.FLOAT, "Size of text. If 1, size will be value of parent class."),

	/*HTML*/
	HTML_CYCLE_LINKS(30, "cyclelinks", PropertyType.BOOLEAN, null),
	HTML_FILE_NAME(31, "filename", PropertyType.FILE_NAME, "HTML file to load into control at startup."),
	HTML_COLOR_BOLD(32, "colorBold", PropertyType.COLOR, "Text color of inside <b></b> (bold text)."),
	HTML_COLOR_LINK(33, "colorLink", PropertyType.COLOR, "Text color of inside <a href='#section'></a> (link text)."),
	HTML_COLOR_LINK_ACTIVE(34, "colorLinkActive", PropertyType.COLOR, "Text color of an active link."),
	HTML_COLOR_PICTURE(35, "colorPicture", PropertyType.COLOR, "Color of transparent part of image."),
	HTML_COLOR_PICTURE_BORDER(36, "colorPictureBorder", PropertyType.COLOR, "Color of border around image."),
	HTML_COLOR_PICTURE_LINK(37, "colorPictureLink", PropertyType.COLOR, "Color of transparent part of image that is inside link."),
	HTML_COLOR_PICTURE_SELECTED(38, "colorPictureSelected", PropertyType.COLOR, "Color of transparent part of image that is within an active link."),
	HTML_PREV_PAGE(39, "prevPage", PropertyType.IMAGE, "File name of image which is used for left arrow."),
	HTML_NEXT_PAGE(40, "nextPage", PropertyType.IMAGE, "File name of image which is used for right arrow."),

	/*Button*/
	BTN_ACTION(41, "action", PropertyType.SQF, "Script command(s) to execute when button is pressed. Variable 'this' contains unit that pressed button."),
	BTN_BORDER_SIZE(42, "borderSize", PropertyType.FLOAT, "If > 0 then a background (in the color defined in 'colorBorder') is drawn behind the button. It extends to the left by the distance defined here, its height is slightly less than that of the button, and it is vertically centered. The width extends to the right, to where the drop shadow starts. Stays static when button is pressed."),
	BTN_COLOR_BACKGROUND_ACTIVE(43, "colorBackgroundActive", PropertyType.COLOR, "Button's background color if 'active' (mouse pointer is over it)."),
	BTN_COLOR_BACKGROUND_DISABLED(44, "colorBackgroundDisabled", PropertyType.COLOR, "Button's background color if disabled."),
	BTN_COLOR_BORDER(45, "colorBorder", PropertyType.COLOR, "Color of left border that is defined in 'borderSize'."),
	BTN_COLOR_DISABLED(46, "colorDisabled", PropertyType.COLOR, "Text color if button is disabled."),
	BTN_COLOR_FOCUSED(47, "colorFocused", PropertyType.COLOR, "Alternating background color. While the control has focus (but without the mouse pointer being over it) the background will cycle between 'colorFocused' and 'colorBackground'. If both are the same, then the color will be steady."),
	BTN_COLOR_SHADOW(48, "colorShadow", PropertyType.COLOR, "Color of drop shadow behind button. This color is not visible when button is disabled."),
	BTN_DEFAULT(49, "default", PropertyType.BOOLEAN, "Whether or not the button will have focus upon loading the dialog."),
	BTN_OFFSET_PRESSED_X(50, "offsetPressedX", PropertyType.FLOAT, "The button's text & background will move by this distance when pressed (ff a shadow is defined, it will stay in place)."),
	BTN_OFFSET_PRESSED_Y(51, "offsetPressedY", PropertyType.FLOAT, "The button's text & background will move by this distance when pressed (ff a shadow is defined, it will stay in place)."),
	BTN_OFFSET_X(52, "offsetX", PropertyType.FLOAT, "Horizontal and vertical offset of drop shadow. if 0, then shadow will be placed directly behind button."),
	BTN_OFFSET_Y(53, "offsetY", PropertyType.FLOAT, "Horizontal and vertical offset of drop shadow. if 0, then shadow will be placed directly behind button."),
	// a note on toolTip: can be tooltip
	/* ..Shortcut Button */
	BTN_ANIM_TEXTURE_NORMAL(58, "animTextureNormal", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_DISABLED(59, "animTextureDisabled", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_OVER(60, "animTextureOver", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_FOCUSED(61, "animTextureFocused", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_PRESSED(62, "animTexturePressed", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_DEFAULT(63, "animTextureDefault", PropertyType.TEXTURE, null),
	BTN_TEXTURE_NO_SHORTCUT(64, "textureNoShortcut", PropertyType.TEXTURE, null),
	BTN_COLOR2(65, "color2", PropertyType.COLOR, null),
	BTN_COLOR_BACKGROUND2(66, "colorBackground2", PropertyType.COLOR, null),
	BTN_PERIOD(67, "period", PropertyType.FLOAT, null),
	BTN_PERIOD_FOCUS(68, "periodFocus", PropertyType.FLOAT, null),
	BTN_PERIOD_OVER(69, "periodOver", PropertyType.FLOAT, null),

	/*Active Text*/
	AT_ACTION(70, "action", PropertyType.SQF, "Script command(s) to execute when text is clicked."),
	AT_CAN_DRAG(71, "canDrag", PropertyType.BOOLEAN, null),
	AT_COLOR(72, "color", PropertyType.COLOR, "Text color and underline color."),
	AT_COLOR_ACTIVE(73, "colorActive", PropertyType.COLOR, "Text and underline color when mouse is over the active text."),
	AT_COLOR_SHADE(74, "colorShade", PropertyType.COLOR, null),
	AT_COLOR_FOCUSED(75, "colorFocused", PropertyType.COLOR, null),
	AT_COLOR_DISABLED(76, "colorDisabled", PropertyType.COLOR, null),
	AT_COLOR_BACKGROUND2(77, "colorBackground2", PropertyType.COLOR, null),
	AT_DEFAULT(78, "default", PropertyType.BOOLEAN, "Whether or not the active text will have focus upon loading the dialog."),
	AT_PICTURE_WIDTH(79, "pictureWidth", PropertyType.FLOAT, null),
	AT_PICTURE_HEIGHT(80, "pictureHeight", PropertyType.FLOAT, null),
	AT_SIDE_DISABLED(81, "sideDisabled", PropertyType.COLOR, null),
	AT_PICTURE(82, "picture", PropertyType.TEXTURE, null),
	AT_SIDE_TOGGLE(83, "sideToggle", PropertyType.COLOR, null),
	AT_TEXT_HEIGHT(84, "textHeight", PropertyType.FLOAT, null),

	/*Edit*/
	EDIT_AUTO_COMPLETE(85, "autocomplete", PropertyType.STRING, "Auto-completion option.", new Option("None", "", "No auto-completion."), new Option("Script", "scripting", "Auto-completion set for scripting."), new Option("General", "general", "Auto-completion on normal words.")),
	EDIT_HTML_CONTROL(86, "htmlControl", PropertyType.BOOLEAN, "If used together with style=ST_MULTI, allows multi-line editable text fields."),
	EDIT_LINE_SPACING(87, "lineSpacing", PropertyType.FLOAT, "Line spacing and this is required if style is MULTI (16)"),
	EDIT_COLOR_SELECTION(88, "colorSelection", PropertyType.COLOR, null),
	EDIT_SIZE(89, "size", PropertyType.FLOAT, "From the wiki: \"possibly a typo, perhaps irrelevant xbox property\"."),

	/*Sliders*/
	SLIDE_ARROW_EMPTY(90, "arrowEmpty", PropertyType.TEXTURE, null),
	SLIDE_ARROW_FULL(91, "arrowFull", PropertyType.TEXTURE, null),
	SLIDE_BORDER(92, "border", PropertyType.TEXTURE, null),
	SLIDE_ACTIVE(93, "colorActive", PropertyType.COLOR, "Color of the arrows."),
	SLIDE_DISABLED(94, "colorDisabled", PropertyType.COLOR, null),
	SLIDE_THUMB(95, "thumb", PropertyType.TEXTURE, null),
	SLIDE_VSPACING(96, "vspacing", PropertyType.FLOAT, null),

	/*Combo*/
	COMBO_ARROW_EMPTY(97, "arrowEmpty", PropertyType.TEXTURE, null),
	COMBO_ARROW_FULL(98, "arrowFull", PropertyType.TEXTURE, null),
	COMBO_COLOR(99, "color", PropertyType.COLOR, "Color of the control surrounding lines."),
	COMBO_ACTIVE(100, "colorActive", PropertyType.COLOR, null),
	COMBO_DISABLED(101, "colorDisabled", PropertyType.COLOR, null),
	COMBO_COLOR_SCROLL_BAR(102, "colorScrollbar", PropertyType.COLOR, null),
	COMBO_COLOR_SELECT(103, "colorSelect", PropertyType.COLOR, "Color of selected text."),
	COMBO_COLOR_SELECT_BACKGROUND(104, "colorSelectBackground", PropertyType.COLOR, "Background color of selected text."),
	COMBO_FROM(105, "from", PropertyType.FLOAT, null),
	COMBO_TO(106, "to", PropertyType.FLOAT, null),
	COMBO_MAX_HISTORY_DELAY(107, "maxHistoryDelay", PropertyType.FLOAT, null),
	COMBO_ROW_HEIGHT(108, "rowHeight", PropertyType.FLOAT, "Height of a single row in the elapsed box."),
	COMBO_SOUND_SELECT(109, "soundSelect", PropertyType.SOUND, null),
	COMBO_SOUND_COLLAPSE(110, "soundCollapse", PropertyType.SOUND, null),

	/*Listboxes*/
	LB_ACTIVE(111, "active", PropertyType.BOOLEAN, null),
	LB_AUTO_SCROLL(112, "autoScroll", PropertyType.INT, null),
	LB_ARROW_EMPTY(113, "arrowEmpty", PropertyType.TEXTURE, null),
	LB_ARROW_FULL(114, "arrowFull", PropertyType.TEXTURE, null),
	LB_BORDER(115, "border", PropertyType.TEXTURE, null),
	LB_CAN_DRAG(116, "canDrag", PropertyType.BOOLEAN, null),
	LB_COLLISION_COLOR(117, "collisionColor", PropertyType.COLOR, null),
	LB_COLOR_SCROLL_BAR(118, "colorScrollbar", PropertyType.COLOR, null),
	LB_COLOR(119, "color", PropertyType.COLOR, null),
	LB_COLOR_PLAYER_ITEM(120, "colorPlayerItem", PropertyType.COLOR, null),
	LB_COLOR_ACTIVE(121, "colorActive", PropertyType.COLOR, null),
	LB_COLOR_DISABLED(122, "colorDisabled", PropertyType.COLOR, null),
	LB_COLOR_SELECT(123, "colorSelect", PropertyType.COLOR, null),
	LB_COLOR_SELECT2(124, "colorSelect2", PropertyType.COLOR, null),
	LB_COLOR_SELECT_BACKGROUND(125, "colorSelectBackground", PropertyType.COLOR, null),
	LB_COLOR_SELECT_BACKGROUND2(126, "colorSelectBackground2", PropertyType.COLOR, null),
	LB_COLUMNS(127, "columns", PropertyType.ARRAY, "Float array and defines the left starting position of each column. The values are offsets ratios (not spacing ratios). Tip: Use {-0.01} in first column to fix unwanted offset, if desired."),
	LB_DISABLED(128, "disabled", PropertyType.BOOLEAN, null),
	LB_ENABLED(129, "enabled", PropertyType.BOOLEAN, null),
	LB_DISABLED_CTRL_COLOR(130, "disabledCtrlColor", PropertyType.COLOR, null),
	LB_DISABLED_KEY_COLOR(131, "disabledKeyColor", PropertyType.COLOR, null),
	LB_DRAW_SIDE_ARROWS(132, "drawSideArrows", PropertyType.BOOLEAN, "Each row can be linked to 2 arrow buttons which are shown on the left and right of the row."),
	LB_IDC_LEFT(133, "idcLeft", PropertyType.INT, "The IDC of the control to be used for the left button."),
	LB_IDC_RIGHT(134, "idcRight", PropertyType.INT, "The IDC of the control to be used for the right button."),
	LB_MAIN_COLUMNW(135, "mainCollumW", PropertyType.FLOAT, null),
	LB_SECOND_COLUMNW(136, "secndCollumW", PropertyType.FLOAT, null),
	LB_LINE_SPACING(138, "lineSpacing", PropertyType.FLOAT, null),
	LB_MAX_HISTORY_DELAY(139, "maxHistoryDelay", PropertyType.FLOAT, null),
	LB_ROW_HEIGHT(140, "rowHeight", PropertyType.FLOAT, "The height of a single row in the elapsed box."),
	LB_ROWS(141, "rows", PropertyType.INT, null),

	/*event handlers*/
	EVENT_ON_LOAD(1000, "onLoad", PropertyType.EVENT, strArr("Display and all controls are created, but no action on any is taken.", "Returns the display.", priority(1), "Display")),
	EVENT_ON_UNLOAD(1001, "onUnload", PropertyType.EVENT, strArr("Display is closed, but no controls are destroyed yet.", "Returns the display and exit code.", priority(1), "Display")),
	EVENT_ON_CHILD_DESTROYED(1002, "onChildDestroyed", PropertyType.EVENT, strArr("Child display is closed.", "Returns the display, which child display was closed and exit code.", priority(1), "Display")),
	EVENT_ON_MOUSE_ENTER(1003, "onMouseEnter", PropertyType.EVENT, strArr("The mouse pointer enters the control area.", "Returns control.", priority(1), "Control")),
	EVENT_ON_MOUSE_EXIT(1004, "onMouseExit", PropertyType.EVENT, strArr("The mouse pointer exits the control area.", "Returns control.", priority(1), "Control")),
	EVENT_ON_SET_FOCUS(1005, "onSetFocus", PropertyType.EVENT, strArr("Input focus is on control. It now begins to accept keyboard input.", "Returns control.", priority(2), "Control")),
	EVENT_ON_KILL_FOCUS(1006, "onKillFocus", PropertyType.EVENT, strArr("Input focus is no longer on control. It no longer accepts keyboard input.", "Returns control.", priority(2), "Control")),
	EVENT_ON_TIMER(1007, "onTimer", PropertyType.EVENT, strArr("After amount of time given by setTimer function.", "Returns control.", priority(3), "Control")),
	EVENT_ON_KEY_DOWN(1008, "onKeyDown", PropertyType.EVENT, strArr("Pressing any keyboard key. Fired before the onKeyUp event.", "Returns the control, the keyboard code and the state of Shift, Ctrl and Alt.", priority(2), "Display, Control")),
	EVENT_ON_KEY_UP(1009, "onKeyUp", PropertyType.EVENT, strArr("Releasing any keyboard key. Fired after the onKeyDown event.", "Returns the control, the keyboard code and the state of Shift, Ctrl and Alt.", priority(2), "Display, Control")),
	EVENT_ON_CHAR(1010, "onChar", PropertyType.EVENT, strArr("When some readable characters is recognised.", "Returns the control and the char code.", priority(2), "Control")),
	EVENT_ON_IME_CHAR(1011, "onIMEChar", PropertyType.EVENT, strArr("When IME character is recognized (used in Korean and other eastern languages).", "Returns the control and the char code.", priority(2), "Control")),
	EVENT_ON_IME_COMPOSITION(1012, "onIMEComposition", PropertyType.EVENT, strArr("When partial IME character is recognized (used in Korean and other eastern languages).", "Returns the control and the char code.", priority(2), "Control")),
	EVENT_ON_JOYSTICK_BUTTON(1013, "onJoystickButton", PropertyType.EVENT, strArr("Pressing and releasing any joystick button.", "Not in Arma 2 or Arma 3 Returns the control and the the pressed button.", priority(3), "Control")),
	EVENT_ON_MOUSE_BUTTON_DOWN(1014, "onMouseButtonDown", PropertyType.EVENT, strArr("Pressing a mouse button. Followed by the onMouseButtonUp event.", "Returns the control, the pressed button, the x and y coordinates and the state of Shift, Ctrl and Alt.", priority(2), "Control")),
	EVENT_ON_MOUSE_BUTTON_UP(1015, "onMouseButtonUp", PropertyType.EVENT, strArr("Releasing a mouse button. Follows the onMouseButtonDown event.", "Returns the control, the pressed button, the x and y coordinates and the state of Shift, Ctrl and Alt.", priority(2), "Control")),
	EVENT_ON_MOUSE_BUTTON_CLICK(1016, "onMouseButtonClick", PropertyType.EVENT, strArr("Pressing and releasing a mouse button.", "Returns the control, the pressed button, the x and y coordinates and the state of Shift, Ctrl and Alt.", priority(2), "ListBox, ComboBox, TextBox, Button, ActiveText")),
	EVENT_ON_MOUSE_BUTTON_DBL_CLICK(1017, "onMouseButtonDblClick", PropertyType.EVENT, strArr("Pressing and releasing a mouse button twice within very short time.", "Returns the control, the pressed button, the x and y coordinates and the state of Shift, Ctrl and Alt.", priority(2), "Control")),
	EVENT_ON_MOUSE_MOVING(1018, "onMouseMoving", PropertyType.EVENT, strArr("Fires continuously while moving the mouse with a certain interval.", "Returns the control, the x and y coordinates relative to control and mouseOver.", priority(2), "Control")),
	EVENT_ON_MOUSE_HOLDING(1019, "onMouseHolding", PropertyType.EVENT, strArr("Fires continuously while mouse is not moving with a certain interval.", "Returns the display, the some kind of x and y delta position.", priority(2), "Display")),
	EVENT_ON_MOUSE_ZCHANGED(1020, "onMouseZChanged", PropertyType.EVENT, strArr("Fires when mouse wheel position is changed. Does not fire on disabled control. Checked with CT_EDIT type in v1.50.", "Returns the control, the x and y coordinates and mouseOver. If used with a display, the mouseOver parameter is excluded.", priority(2), "Control")),
	EVENT_ON_CAN_DESTROY(1021, "onCanDestroy", PropertyType.EVENT, strArr("Ask this control if dialog can be closed (used for validation of contained entry).", "Returns the control and the change of the scrollbar.", priority(3), "Control only")),
	EVENT_ON_DESTROY(1022, "onDestroy", PropertyType.EVENT, strArr("Destroying control", "Returns the control and exit code.", priority(3), "Control")),
	EVENT_ON_BUTTON_CLICK(1023, "onButtonClick", PropertyType.EVENT, strArr("The attached button action is performed. When returned value is true, button's display remains opened.", "Returns the control and exit code.", priority(1), "Control")),
	EVENT_ON_BUTTON_DBL_CLICK(1024, "onButtonDblClick", PropertyType.EVENT, strArr("?", "Returns control.", priority(-1), "Button")),
	EVENT_ON_BUTTON_DOWN(1025, "onButtonDown", PropertyType.EVENT, strArr("The left mouse button is pressed over the button area or a key on the keyboard is pressed.", "Arma 3", priority(1), "Button")),
	EVENT_ON_BUTTON_UP(1026, "onButtonUp", PropertyType.EVENT, strArr("The left mouse buttons is released outside the button area and the attached button action is not performed.", "Returns control.", priority(1), "Button")),
	EVENT_ON_LB_SEL_CHANGED(1027, "onLBSelChanged", PropertyType.EVENT, strArr("The selection in a listbox is changed. The left mouse button has been released and the new selection is fully made.", "Returns control.", priority(2), "Button")),
	EVENT_ON_LB_LIST_SEL_CHANGED(1028, "onLBListSelChanged", PropertyType.EVENT, strArr("Selection in XCombo box changed (but value is not stored yet).", "Returns the control and the selected element index.", priority(2), "Listbox")),
	EVENT_ON_LB_DBL_CLICK(1029, "onLBDblClick", PropertyType.EVENT, strArr("Double click on some row in listbox.", "Returns the control and the selected element index.", priority(2), "Listbox")),
	EVENT_ON_LB_DRAG(1030, "onLBDrag", PropertyType.EVENT, strArr("Drag & drop operation started.", "Returns the control and the selected element index.", priority(2), "Listbox")),
	EVENT_ON_LB_DRAGGING(1031, "onLBDragging", PropertyType.EVENT, strArr("Drag & drop operation is in progress.", "Returns the control and the selected element index.", priority(2), "Listbox")),
	EVENT_ON_LB_DROP(1032, "onLBDrop", PropertyType.EVENT, strArr("Drag & drop operation finished.", "Returns the control and the x and y coordinates.", priority(2), "Listbox")),
	EVENT_ON_TREE_SEL_CHANGED(1033, "onTreeSelChanged", PropertyType.EVENT, strArr("Changing the selection in a tree.", "Returns the control and the x and y coordinates.", priority(2), "Listbox, Combobox, Textbox, ActiveText, Button")),
	EVENT_ON_TREE_LBUTTON_DOWN(1034, "onTreeLButtonDown", PropertyType.EVENT, strArr("Pressing and releasing left mouse button on a tree.", "Returns the control and the new selection path.", priority(2), "Tree")),
	EVENT_ON_TREE_DBL_CLICK(1035, "onTreeDblClick", PropertyType.EVENT, strArr("Pressing and releasing twice on a tree entry.", "Returns the control.", priority(2), "Tree")),
	EVENT_ON_TREE_EXPANDED(1036, "onTreeExpanded", PropertyType.EVENT, strArr("The tree folder structure has been expanded.", "Returns the control and the current selection path.", priority(3), "Tree")),
	EVENT_ON_TREE_COLLAPSED(1037, "onTreeCollapsed", PropertyType.EVENT, strArr("The tree folder structure has been collapsed.", "Returns the control.", priority(3), "Tree")),
	EVENT_ON_TREE_MOUSE_MOVE(1038, "onTreeMouseMove", PropertyType.EVENT, strArr("Fires continuously while moving the mouse with a certain interval.", "Returns the control.", priority(2), "Tree")),
	EVENT_ON_TREE_MOUSE_HOLD(1039, "onTreeMouseHold", PropertyType.EVENT, strArr("Fires continuously while mouse is not moving with a certain interval.", "Returns the control.", priority(2), "Tree")),
	EVENT_ON_TREE_MOUSE_EXIT(1040, "onTreeMouseExit", PropertyType.EVENT, strArr("The mouse pointer exits the tree control area", "Returns the control.", priority(2), "Tree")),
	EVENT_ON_TOOL_BOX_SEL_CHANGED(1041, "onToolBoxSelChanged", PropertyType.EVENT, strArr("Changed the selection in a toolbox.", "Returns the control.", priority(2), "Tree")),
	EVENT_ON_CHECKED(1042, "onChecked", PropertyType.EVENT, strArr("?", "Returns the control and the selected element index.", priority(-1), "Toolbox")),
	EVENT_ON_CHECKED_CHANGED(1043, "onCheckedChanged", PropertyType.EVENT, strArr("Checked state of CheckBox changed.", "Arma 3", priority(-1), "Checkbox")),
	EVENT_ON_CHECK_BOXES_SEL_CHANGED(1044, "onCheckBoxesSelChanged", PropertyType.EVENT, strArr("Changed the selection in a checkbox.", "Arma 3. Returns control and the checked state.", priority(2), "Checkbox")),
	EVENT_ON_HTML_LINK(1045, "onHTMLLink", PropertyType.EVENT, strArr("Pressing and releasing a HTML link.", "Returns the control, the selected element index and the current state.", priority(2), "Checkbox")),
	EVENT_ON_SLIDER_POS_CHANGED(1046, "onSliderPosChanged", PropertyType.EVENT, strArr("Changing the position of a slider.", "Returns the control and href.", priority(2), "HTML")),
	EVENT_ON_OBJECT_MOVED(1047, "onObjectMoved", PropertyType.EVENT, strArr("Moving an object.", "Returns the control and the change.", priority(2), "Slider")),
	EVENT_ON_MENU_SELECTED(1048, "onMenuSelected", PropertyType.EVENT, strArr("Some item in context menu (used now only in new mission editor) was selected.", "Returns the control and the offset on the x, y and z axes.", priority(2), "Object")),
	EVENT_ON_DRAW(1049, "onDraw", PropertyType.EVENT, strArr("Fires when the map is drawn (can occur more than once per second).", "Returns the control and the command id.", priority(-1), "Context menu")),
	EVENT_ON_VIDEO_STOPPED(1050, "onVideoStopped", PropertyType.EVENT, strArr("1.56 Activated every time video ends (when looped, handler is executed after every finished loop).", "Returns the map control.", priority(2), "Map")),


	/*HitZone and TextPos classes*/
	/*TOP(0, "top", PropertyType.FLOAT, null),
	RIGHT(0, "right", PropertyType.FLOAT, null),
	BOTTOM(0, "bottom", PropertyType.FLOAT, null),
	LEFT(0, "left", PropertyType.FLOAT, null),

	/*ShortcutPos class*/
	/*CLASS_SHORTCUT_POS__W(0, "w", PropertyType.FLOAT, null),
	CLASS_SHORTCUT_POS__H(0, "h", PropertyType.FLOAT, null)*/;


	public static final ControlPropertyLookup[] EMPTY = new ControlPropertyLookup[0];
	/** All values that the property can be, or null if user defined. */
	public final @Nullable Option[] options;
	public final String propertyName;
	public final PropertyType propertyType;
	public final String[] about;
	/**
	 A unique id for the lookup item to guarantee a match by despite order change or property name change, or some other change.
	 <br>When the loopup item is written, the propertyId must <b>NEVER</b> change.
	 */
	public final int propertyId;

	ControlPropertyLookup(int propertyId, @NotNull String propertyName, @NotNull PropertyType propertyType, @NotNull String[] about, @Nullable Option... options) {
		if (PropertiesLookupDataVerifier.usedIds.contains(propertyId)) {
			int canUse;
			for (int i = 0; true; i++) {
				if (!PropertiesLookupDataVerifier.usedIds.contains(i)) {
					canUse = i;
					break;
				}
			}
			throw new IllegalStateException("id '" + propertyId + "' is already taken for property enum name:" + name() + ". Here is an unused id for your convenience: " + canUse);
		}
		if (propertyId == -1) {
			throw new IllegalStateException("-1 propertyId is reserved for user-defined properties");
		}
		PropertiesLookupDataVerifier.usedIds.add(propertyId);
		this.propertyId = propertyId;
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.about = about;
		this.options = options;
	}

	ControlPropertyLookup(int propertyId, @NotNull String propertyName, @NotNull PropertyType propertyType, @Nullable String about) {
		this(propertyId, propertyName, propertyType, about, (Option[]) null);
	}

	ControlPropertyLookup(int propertyId, @NotNull String propertyName, @NotNull PropertyType propertyType, @Nullable String about, @Nullable Option... options) {
		this(propertyId, propertyName, propertyType, about == null ? strArr("No documentation.") : strArr(about), options);
	}

	@Override
	public String toString() {
		return propertyName;
	}

	public ControlProperty getEventProperty(String defaultEventValue) {
		return new ControlProperty(this, propertyName, propertyType, defaultEventValue);
	}

	public ControlProperty getIntProperty(int defaultValue) {
		return new ControlProperty(this, propertyName, defaultValue);
	}

	public ControlProperty getFloatProperty(double defaultValue) {
		return new ControlProperty(this, propertyName, defaultValue);
	}

	public ControlProperty getBooleanProperty(boolean defaultValue) {
		return new ControlProperty(this, propertyName, defaultValue);
	}

	public ControlProperty getStringProperty(String defaultValue) {
		return new ControlProperty(this, propertyName, PropertyType.STRING, defaultValue);
	}

	public ControlProperty getArrayProperty(String[] defaultValue) {
		return new ControlProperty(this, propertyName, PropertyType.ARRAY, defaultValue);
	}

	public ControlProperty getColorProperty(AColor defaultValue) {
		return new ControlProperty(this, propertyName, defaultValue);
	}

	public ControlProperty getFontProperty(AFont defaultValue) {
		return new ControlProperty(this, propertyName, defaultValue);
	}

	public ControlProperty getHexColorProperty(AHexColor defaultValue) {
		return new ControlProperty(this, propertyName, defaultValue);
	}

	public ControlProperty getProperty(Object defaultValue) {
		if (defaultValue instanceof String[]) {
			throw new IllegalArgumentException("Use getProperty(String[] defaultValues) instead");
		}
		if (defaultValue instanceof Option[]) {
			throw new IllegalArgumentException("Use getProperty(Option[] defaultValues) instead");
		}
		return new ControlProperty(this, propertyName, propertyType, defaultValue);
	}

	public ControlProperty getProperty(String[] defaultValues) {
		return new ControlProperty(this, propertyName, propertyType, defaultValues);
	}

	public ControlProperty getPropertyFromOption(int optionNum) {
		if(options == null || optionNum < 0 || optionNum > options.length){
			throw new IllegalStateException("options and optionNum are bad. options=" + (options != null ? Arrays.toString(options) : "null") + " optionNum=" + optionNum);
		}
		return new ControlProperty(this, propertyName, propertyType, options[optionNum].value);
	}

	public ControlProperty getPropertyWithNoData() {
		return new ControlProperty(this, propertyName, propertyType);
	}

	/** Get all lookup enums where their property type is equal to find */
	public static ControlPropertyLookup[] getAllOfTypeControlProperties(PropertyType find) {
		ArrayList<ControlPropertyLookup> props = new ArrayList<>(values().length);
		for (ControlPropertyLookup controlProperty : values()) {
			if (controlProperty.propertyType == find) {
				props.add(controlProperty);
			}
		}
		return props.toArray(new ControlPropertyLookup[props.size()]);
	}

	private static String[] strArr(String... vals) {
		return vals;
	}

	private static String priority(int i) {
		if (i < 0) {
			return "unknown";
		}
		return i + "";
	}

	private static class PropertiesLookupDataVerifier {
		static ArrayList<Integer> usedIds = new ArrayList<>();
	}

}
