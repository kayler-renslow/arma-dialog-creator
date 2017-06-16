package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 A place to find ALL known control properties for all controls. This is where the name of the property, property type, description, and options (if allowed) are listed.

 @author Kayler
 @since 05/22/2016. */
public enum ControlPropertyLookup implements ControlPropertyLookupConstant {
	IDC(0, 0, "idc", PropertyType.Int),
	X(1, 1, "x", PropertyType.Float),
	Y(2, 2, "y", PropertyType.Float),
	W(3, 3, "w", PropertyType.Float),
	H(4, 4, "h", PropertyType.Float),
	TYPE(5, -1, "type", PropertyType.Int),
	STYLE(6, 5, "style", PropertyType.ControlStyle),
	ACCESS(7, "access", PropertyType.Int, new ControlPropertyOption("Read and Write", "0", "Default case where properties can still be added or overridden."), new ControlPropertyOption("Read and Create", "1", "Only allows creating new properties."), new ControlPropertyOption("Read Only", "2", "Does not allow to do anything in deriving classes."), new ControlPropertyOption("Read Only Verified", "3", "Does not allow to do anything either in deriving classes, and a CRC check will be performed.")),

	/*Common*/
	MOVING(8, "moving", PropertyType.Boolean),
	SIZE_EX(9, "sizeEx", PropertyType.Float),
	FONT(10, "font", PropertyType.Font),
	COLOR_TEXT(11, "colorText", PropertyType.Color),
	COLOR_BACKGROUND(12, "colorBackground", PropertyType.Color),
	TEXT(13, 6, "text", PropertyType.String),
	SHADOW(14, "shadow", PropertyType.Int, new ControlPropertyOption("No", "0", "No shadow."), new ControlPropertyOption("Yes", "1", "Drop shadow with soft edges."), new ControlPropertyOption("Stroke", "2", "Stroke")), //does absolutely nothing inside the Attributes class for structured text
	TOOLTIP(15, "tooltip", PropertyType.String),
	TOOLTIP_COLOR_SHADE(16, "tooltipColorShade", PropertyType.Color),
	TOOLTIP_COLOR_TEXT(17, "tooltipColorText", PropertyType.Color),
	TOOLTIP_COLOR_BOX(18, "tooltipColorBox", PropertyType.Color),
	ALIGN(19, "align", PropertyType.String, new ControlPropertyOption("Left", "left", "Left align."), new ControlPropertyOption("Center", "center", "Center align."), new ControlPropertyOption("Right", "right", "Right align.")),
	VALIGN(20, "valign", PropertyType.String, new ControlPropertyOption("Top", "top", "Top align."), new ControlPropertyOption("Middle", "middle", "Middle align."), new ControlPropertyOption("Bottom", "bottom", "Bottom align.")),
	COLOR_HEX(21, "color", PropertyType.HexColorString),
	SHADOW_COLOR(22, "shadowColor", PropertyType.HexColorString), //default shadow color
	BLINKING_PERIOD(23, "blinkingPeriod", PropertyType.Float),

	/*Static*/
	STATIC_AUTO_PLAY(24, "autoPlay", PropertyType.Boolean),
	STATIC_KEY(25, "key", PropertyType.String),
	STATIC_LOOPS(26, "loops", PropertyType.Int),
	STATIC_LINE_SPACING(27, "lineSpacing", PropertyType.Float),
	STATIC_FIXED_WIDTH(28, "fixedWidth", PropertyType.Boolean),

	/*Structured Text*/
	STRUCT_TEXT_SIZE(29, "size", PropertyType.Float),

	/*HTML*/
	HTML_CYCLE_LINKS(30, "cyclelinks", PropertyType.Boolean),
	HTML_FILE_NAME(31, "filename", PropertyType.FileName),
	HTML_COLOR_BOLD(32, "colorBold", PropertyType.Color),
	HTML_COLOR_LINK(33, "colorLink", PropertyType.Color),
	HTML_COLOR_LINK_ACTIVE(34, "colorLinkActive", PropertyType.Color),
	HTML_COLOR_PICTURE(35, "colorPicture", PropertyType.Color),
	HTML_COLOR_PICTURE_BORDER(36, "colorPictureBorder", PropertyType.Color),
	HTML_COLOR_PICTURE_LINK(37, "colorPictureLink", PropertyType.Color),
	HTML_COLOR_PICTURE_SELECTED(38, "colorPictureSelected", PropertyType.Color),
	HTML_PREV_PAGE(39, "prevPage", PropertyType.Image),
	HTML_NEXT_PAGE(40, "nextPage", PropertyType.Image),

	/*Button*/
	BTN_ACTION(41, "action", PropertyType.SQF),
	BTN_BORDER_SIZE(42, "borderSize", PropertyType.Float),
	BTN_COLOR_BACKGROUND_ACTIVE(43, "colorBackgroundActive", PropertyType.Color),
	BTN_COLOR_BACKGROUND_DISABLED(44, "colorBackgroundDisabled", PropertyType.Color),
	BTN_COLOR_BORDER(45, "colorBorder", PropertyType.Color),
	BTN_COLOR_DISABLED(46, "colorDisabled", PropertyType.Color),
	BTN_COLOR_FOCUSED(47, "colorFocused", PropertyType.Color),
	BTN_COLOR_SHADOW(48, "colorShadow", PropertyType.Color),
	BTN_DEFAULT(49, "default", PropertyType.Boolean),
	BTN_OFFSET_PRESSED_X(50, "offsetPressedX", PropertyType.Float),
	BTN_OFFSET_PRESSED_Y(51, "offsetPressedY", PropertyType.Float),
	BTN_OFFSET_X(52, "offsetX", PropertyType.Float),
	BTN_OFFSET_Y(53, "offsetY", PropertyType.Float),
	SOUND_CLICK(54, "soundClick", PropertyType.Sound),
	SOUND_ENTER(55, "soundEnter", PropertyType.Sound),
	SOUND_ESCAPE(56, "soundEscape", PropertyType.Sound),
	SOUND_PUSH(57, "soundPush", PropertyType.Sound),

	/* ..Shortcut Button */
	BTN_ANIM_TEXTURE_NORMAL(58, "animTextureNormal", PropertyType.Texture),
	BTN_ANIM_TEXTURE_DISABLED(59, "animTextureDisabled", PropertyType.Texture),
	BTN_ANIM_TEXTURE_OVER(60, "animTextureOver", PropertyType.Texture),
	BTN_ANIM_TEXTURE_FOCUSED(61, "animTextureFocused", PropertyType.Texture),
	BTN_ANIM_TEXTURE_PRESSED(62, "animTexturePressed", PropertyType.Texture),
	BTN_ANIM_TEXTURE_DEFAULT(63, "animTextureDefault", PropertyType.Texture),
	BTN_TEXTURE_NO_SHORTCUT(64, "textureNoShortcut", PropertyType.Texture),
	BTN_COLOR2(65, "color2", PropertyType.Color),
	BTN_COLOR_BACKGROUND2(66, "colorBackground2", PropertyType.Color),
	BTN_PERIOD(67, "period", PropertyType.Float),
	BTN_PERIOD_FOCUS(68, "periodFocus", PropertyType.Float),
	BTN_PERIOD_OVER(69, "periodOver", PropertyType.Float),

	/*Active Text*/
	AT_ACTION(70, "action", PropertyType.SQF),
	AT_CAN_DRAG(71, "canDrag", PropertyType.Boolean),
	AT_COLOR(72, "color", PropertyType.Color),
	AT_COLOR_ACTIVE(73, "colorActive", PropertyType.Color),
	AT_COLOR_SHADE(74, "colorShade", PropertyType.Color),
	AT_COLOR_FOCUSED(75, "colorFocused", PropertyType.Color),
	AT_COLOR_DISABLED(76, "colorDisabled", PropertyType.Color),
	AT_COLOR_BACKGROUND2(77, "colorBackground2", PropertyType.Color),
	AT_DEFAULT(78, "default", PropertyType.Boolean),
	AT_PICTURE_WIDTH(79, "pictureWidth", PropertyType.Float),
	AT_PICTURE_HEIGHT(80, "pictureHeight", PropertyType.Float),
	AT_SIDE_DISABLED(81, "sideDisabled", PropertyType.Color),
	AT_PICTURE(82, "picture", PropertyType.Texture),
	AT_SIDE_TOGGLE(83, "sideToggle", PropertyType.Color),
	AT_TEXT_HEIGHT(84, "textHeight", PropertyType.Float),

	/*Edit*/
	EDIT_AUTO_COMPLETE(85, "autocomplete", PropertyType.String, new ControlPropertyOption("None", "", "No auto-completion."), new ControlPropertyOption("Script", "scripting", "Auto-completion set for scripting."), new ControlPropertyOption("General", "general", "Auto-completion on normal words.")),
	EDIT_HTML_CONTROL(86, "htmlControl", PropertyType.Boolean),
	EDIT_LINE_SPACING(87, "lineSpacing", PropertyType.Float),
	EDIT_COLOR_SELECTION(88, "colorSelection", PropertyType.Color),
	EDIT_SIZE(89, "size", PropertyType.Float),

	/*Sliders*/
	SLIDE_ARROW_EMPTY(90, "arrowEmpty", PropertyType.Texture),
	SLIDE_ARROW_FULL(91, "arrowFull", PropertyType.Texture),
	SLIDE_BORDER(92, "border", PropertyType.Texture),
	SLIDE_ACTIVE(93, "colorActive", PropertyType.Color),
	SLIDE_DISABLED(94, "colorDisabled", PropertyType.Color),
	SLIDE_THUMB(95, "thumb", PropertyType.Texture),
	SLIDE_VSPACING(96, "vspacing", PropertyType.Float),

	/*Combo*/
	COMBO_ARROW_EMPTY(97, "arrowEmpty", PropertyType.Texture),
	COMBO_ARROW_FULL(98, "arrowFull", PropertyType.Texture),
	COMBO_COLOR(99, "color", PropertyType.Color),
	COMBO_ACTIVE(100, "colorActive", PropertyType.Color),
	COMBO_DISABLED(101, "colorDisabled", PropertyType.Color),
	COMBO_COLOR_SCROLL_BAR(102, "colorScrollbar", PropertyType.Color),
	COMBO_COLOR_SELECT(103, "colorSelect", PropertyType.Color),
	COMBO_COLOR_SELECT_BACKGROUND(104, "colorSelectBackground", PropertyType.Color),
	COMBO_FROM(105, "from", PropertyType.Float),
	COMBO_TO(106, "to", PropertyType.Float),
	COMBO_MAX_HISTORY_DELAY(107, "maxHistoryDelay", PropertyType.Float),
	COMBO_ROW_HEIGHT(108, "rowHeight", PropertyType.Float),
	COMBO_SOUND_SELECT(109, "soundSelect", PropertyType.Sound),
	COMBO_SOUND_COLLAPSE(110, "soundCollapse", PropertyType.Sound),

	/*Listboxes*/
	LB_ACTIVE(111, "active", PropertyType.Boolean),
	LB_AUTO_SCROLL(112, "autoScroll", PropertyType.Int),
	LB_ARROW_EMPTY(113, "arrowEmpty", PropertyType.Texture),
	LB_ARROW_FULL(114, "arrowFull", PropertyType.Texture),
	LB_BORDER(115, "border", PropertyType.Texture),
	LB_CAN_DRAG(116, "canDrag", PropertyType.Boolean),
	LB_COLLISION_COLOR(117, "collisionColor", PropertyType.Color),
	LB_COLOR_SCROLL_BAR(118, "colorScrollbar", PropertyType.Color),
	LB_COLOR(119, "color", PropertyType.Color),
	LB_COLOR_PLAYER_ITEM(120, "colorPlayerItem", PropertyType.Color),
	LB_COLOR_ACTIVE(121, "colorActive", PropertyType.Color),
	LB_COLOR_DISABLED(122, "colorDisabled", PropertyType.Color),
	LB_COLOR_SELECT(123, "colorSelect", PropertyType.Color),
	LB_COLOR_SELECT2(124, "colorSelect2", PropertyType.Color),
	LB_COLOR_SELECT_BACKGROUND(125, "colorSelectBackground", PropertyType.Color),
	LB_COLOR_SELECT_BACKGROUND2(126, "colorSelectBackground2", PropertyType.Color),
	LB_COLUMNS(127, "columns", PropertyType.Array),
	LB_DISABLED(128, "disabled", PropertyType.Boolean),
	LB_ENABLED(129, "enabled", PropertyType.Boolean),
	LB_DISABLED_CTRL_COLOR(130, "disabledCtrlColor", PropertyType.Color),
	LB_DISABLED_KEY_COLOR(131, "disabledKeyColor", PropertyType.Color),
	LB_DRAW_SIDE_ARROWS(132, "drawSideArrows", PropertyType.Boolean),
	LB_IDC_LEFT(133, "idcLeft", PropertyType.Int),
	LB_IDC_RIGHT(134, "idcRight", PropertyType.Int),
	LB_MAIN_COLUMNW(135, "mainCollumW", PropertyType.Float),
	LB_SECOND_COLUMNW(136, "secndCollumW", PropertyType.Float),
	LB_LINE_SPACING(138, "lineSpacing", PropertyType.Float),
	LB_MAX_HISTORY_DELAY(139, "maxHistoryDelay", PropertyType.Float),
	LB_ROW_HEIGHT(140, "rowHeight", PropertyType.Float),
	LB_ROWS(141, "rows", PropertyType.Int),

	/*event handlers*/
	EVENT_ON_LOAD(1000, "onLoad", PropertyType.SQF),
	EVENT_ON_UNLOAD(1001, "onUnload", PropertyType.SQF),
	EVENT_ON_CHILD_DESTROYED(1002, "onChildDestroyed", PropertyType.SQF),
	EVENT_ON_MOUSE_ENTER(1003, "onMouseEnter", PropertyType.SQF),
	EVENT_ON_MOUSE_EXIT(1004, "onMouseExit", PropertyType.SQF),
	EVENT_ON_SET_FOCUS(1005, "onSetFocus", PropertyType.SQF),
	EVENT_ON_KILL_FOCUS(1006, "onKillFocus", PropertyType.SQF),
	EVENT_ON_TIMER(1007, "onTimer", PropertyType.SQF),
	EVENT_ON_KEY_DOWN(1008, "onKeyDown", PropertyType.SQF),
	EVENT_ON_KEY_UP(1009, "onKeyUp", PropertyType.SQF),
	EVENT_ON_CHAR(1010, "onChar", PropertyType.SQF),
	EVENT_ON_IME_CHAR(1011, "onIMEChar", PropertyType.SQF),
	EVENT_ON_IME_COMPOSITION(1012, "onIMEComposition", PropertyType.SQF),
	EVENT_ON_JOYSTICK_BUTTON(1013, "onJoystickButton", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_DOWN(1014, "onMouseButtonDown", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_UP(1015, "onMouseButtonUp", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_CLICK(1016, "onMouseButtonClick", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_DBL_CLICK(1017, "onMouseButtonDblClick", PropertyType.SQF),
	EVENT_ON_MOUSE_MOVING(1018, "onMouseMoving", PropertyType.SQF),
	EVENT_ON_MOUSE_HOLDING(1019, "onMouseHolding", PropertyType.SQF),
	EVENT_ON_MOUSE_ZCHANGED(1020, "onMouseZChanged", PropertyType.SQF),
	EVENT_ON_CAN_DESTROY(1021, "onCanDestroy", PropertyType.SQF),
	EVENT_ON_DESTROY(1022, "onDestroy", PropertyType.SQF),
	EVENT_ON_BUTTON_CLICK(1023, "onButtonClick", PropertyType.SQF),
	EVENT_ON_BUTTON_DBL_CLICK(1024, "onButtonDblClick", PropertyType.SQF),
	EVENT_ON_BUTTON_DOWN(1025, "onButtonDown", PropertyType.SQF),
	EVENT_ON_BUTTON_UP(1026, "onButtonUp", PropertyType.SQF),
	EVENT_ON_LB_SEL_CHANGED(1027, "onLBSelChanged", PropertyType.SQF),
	EVENT_ON_LB_LIST_SEL_CHANGED(1028, "onLBListSelChanged", PropertyType.SQF),
	EVENT_ON_LB_DBL_CLICK(1029, "onLBDblClick", PropertyType.SQF),
	EVENT_ON_LB_DRAG(1030, "onLBDrag", PropertyType.SQF),
	EVENT_ON_LB_DRAGGING(1031, "onLBDragging", PropertyType.SQF),
	EVENT_ON_LB_DROP(1032, "onLBDrop", PropertyType.SQF),
	EVENT_ON_TREE_SEL_CHANGED(1033, "onTreeSelChanged", PropertyType.SQF),
	EVENT_ON_TREE_LBUTTON_DOWN(1034, "onTreeLButtonDown", PropertyType.SQF),
	EVENT_ON_TREE_DBL_CLICK(1035, "onTreeDblClick", PropertyType.SQF),
	EVENT_ON_TREE_EXPANDED(1036, "onTreeExpanded", PropertyType.SQF),
	EVENT_ON_TREE_COLLAPSED(1037, "onTreeCollapsed", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_MOVE(1038, "onTreeMouseMove", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_HOLD(1039, "onTreeMouseHold", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_EXIT(1040, "onTreeMouseExit", PropertyType.SQF),
	EVENT_ON_TOOL_BOX_SEL_CHANGED(1041, "onToolBoxSelChanged", PropertyType.SQF),
	EVENT_ON_CHECKED(1042, "onChecked", PropertyType.SQF),
	EVENT_ON_CHECKED_CHANGED(1043, "onCheckedChanged", PropertyType.SQF),
	EVENT_ON_CHECK_BOXES_SEL_CHANGED(1044, "onCheckBoxesSelChanged", PropertyType.SQF),
	EVENT_ON_HTML_LINK(1045, "onHTMLLink", PropertyType.SQF),
	EVENT_ON_SLIDER_POS_CHANGED(1046, "onSliderPosChanged", PropertyType.SQF),
	EVENT_ON_OBJECT_MOVED(1047, "onObjectMoved", PropertyType.SQF),
	EVENT_ON_MENU_SELECTED(1048, "onMenuSelected", PropertyType.SQF),
	EVENT_ON_DRAW(1049, "onDraw", PropertyType.SQF),
	EVENT_ON_VIDEO_STOPPED(1050, "onVideoStopped", PropertyType.SQF),


	/*HitZone and TextPos classes*/
	/*TOP(0, "top", PropertyType.Float),
	RIGHT(0, "right", PropertyType.Float),
	BOTTOM(0, "bottom", PropertyType.Float),
	LEFT(0, "left", PropertyType.Float),

	/*ShortcutPos class*/
	/*CLASS_SHORTCUT_POS__W(0, "w", PropertyType.Float),
	CLASS_SHORTCUT_POS__H(0, "h", PropertyType.Float)*/;


	public static final ReadOnlyList<ControlPropertyLookup> EMPTY = new ReadOnlyList<>(new ArrayList<>());

	private final @Nullable ControlPropertyOption[] options;
	private final String propertyName;
	private final PropertyType propertyType;
	private final int propertyId;
	private final int priority;

	ControlPropertyLookup(int propertyId, int priority, @NotNull String propertyName, @NotNull PropertyType propertyType, @Nullable ControlPropertyOption... options) {
		if (PropertiesLookupData.usedIds.contains(propertyId)) {
			int canUse;
			for (int i = 0; true; i++) {
				if (!PropertiesLookupData.usedIds.contains(i)) {
					canUse = i;
					break;
				}
			}
			throw new IllegalStateException("id '" + propertyId + "' is already taken for property enum name:" + name() + ". Here is an unused id for your convenience: " + canUse);
		}
		if (propertyId == -1) {
			throw new IllegalStateException("-1 propertyId is reserved for user-defined properties");
		}
		PropertiesLookupData.usedIds.add(propertyId);
		this.propertyId = propertyId;
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.options = options;
		this.priority = priority;
	}

	ControlPropertyLookup(int propertyId, @NotNull String propertyName, @NotNull PropertyType propertyType, @Nullable ControlPropertyOption... options) {
		this(propertyId, Integer.MAX_VALUE, propertyName, propertyType, options);
	}


	@Override
	public String toString() {
		return propertyName;
	}

	@Nullable
	@Override
	public ControlPropertyOption[] getOptions() {
		return options;
	}

	@NotNull
	@Override
	public String getPropertyName() {
		return propertyName;
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return propertyType;
	}

	@Override
	public int getPropertyId() {
		return propertyId;
	}

	@Override
	public int priority() {
		return priority;
	}

	/**
	 Get all {@link ControlPropertyLookup} instances where {@link ControlPropertyLookup#getPropertyType()}
	 is equal to find
	 @param find type to find

	 @return list of matched
	 */
	@NotNull
	public static List<ControlPropertyLookup> getAllOfByType(@NotNull PropertyType find) {
		ArrayList<ControlPropertyLookup> props = new ArrayList<>(values().length);
		for (ControlPropertyLookup lookup : values()) {
			if (lookup.propertyType == find) {
				props.add(lookup);
			}
		}
		return props;
	}

	/**
	 Get all {@link ControlPropertyLookup} instances where {@link ControlPropertyLookup#getPropertyName()}
	 is equal to find

	 @param find name to find
	 @param caseSensitive true if case sensitivity matters, false if it doesn't
	 @return list of matched
	 */
	@NotNull
	public static List<ControlPropertyLookup> getAllOfByName(@NotNull String find, boolean caseSensitive) {
		ArrayList<ControlPropertyLookup> props = new ArrayList<>(values().length);
		for (ControlPropertyLookup lookup : values()) {
			String propertyName = lookup.getPropertyName();
			if (caseSensitive ? propertyName.equals(find) : propertyName.equalsIgnoreCase(find)) {
				props.add(lookup);
			}
		}
		return props;
	}

	/** @throws IllegalArgumentException when id couldn't be matched */
	@NotNull
	public static ControlPropertyLookup findById(int id) {
		for (ControlPropertyLookup lookup : values()) {
			if (lookup.propertyId == id) {
				return lookup;
			}
		}
		throw new IllegalArgumentException("id " + id + " couldn't be matched");
	}

	private static class PropertiesLookupData {
		static ArrayList<Integer> usedIds = new ArrayList<>();
	}

}
