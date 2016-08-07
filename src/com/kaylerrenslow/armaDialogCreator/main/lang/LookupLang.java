/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.main.lang;

/**
 Created by Kayler on 08/05/2016.
 */
public interface LookupLang {
	/** {@link com.kaylerrenslow.armaDialogCreator.control.PropertyType} */
	interface PropertyType {
		String INT = "Integer";
		String FLOAT = "Floating Point Number";
		String EXP = "Expression";
		String BOOLEAN = "Boolean";
		String STRING = "String";
		String ARRAY = "Array";
		String COLOR = "Color";
		String SOUND = "Sound";
		String FONT = "Font";
		String FILE_NAME = "File Name";
		String IMAGE = "Image";
		String HEX_COLOR_STRING = "Hex Color String";
		String TEXTURE = "Texture";
		String EVENT = "Event SQF Code String";
		String SQF = "SQF Code String";
		String CONTROL_STYLE = "Control Style";
	}
	
	/** {@link com.kaylerrenslow.armaDialogCreator.control.ControlType} */
	interface ControlType {
		interface TypeGroup {
			String TEXT = "Text";
			String BUTTON = "Button";
			String COMBO = "Combo Box";
			String SLIDER = "Slider";
			String LIST_BOX = "List Box";
			String CHECK_BOX = "Check Box";
			String MENU = "Menu";
			String OBJECT = "Object";
			String MAP = "Map";
			String MISC = "Misc";
		}
		
		String STATIC = "Static";
		String HTML = "HTML";
		String EDIT = "Edit";
		String STRUCTURED_TEXT = "Structured Text";
		String ACTIVETEXT = "Active Text";
		String BUTTON = "Button";
		String SHORTCUTBUTTON = "Shortcut Button";
		String XBUTTON = "X Button";
		String PROGRESS = "Progress Bar";
		String STATIC_SKEW = "Static Skew";
		String LINEBREAK = "Line Break";
		String TREE = "Tree";
		String CONTROLS_GROUP = "Controls Group";
		String XKEYDESC = "XKEYDESC";
		String ANIMATED_TEXTURE = "Animated Texture";
		String ANIMATED_USER = "Animated User";
		String ITEMSLOT = "Item Slot";
		String SLIDER = "Slider";
		String XSLIDER = "X Slider";
		String COMBO = "Combo";
		String XCOMBO = "X Combo";
		String LISTBOX = "List Box";
		String XLISTBOX = "X List Box";
		String LISTNBOX = "List N Box";
		String TOOLBOX = "Tool Box";
		String CHECKBOXES = "Check Boxes";
		String CHECKBOX = "Check Box";
		String CONTEXT_MENU = "Context Menu";
		String MENU = "Menu";
		String MENU_STRIP = "Menu Strip";
		String OBJECT = "Object";
		String OBJECT_ZOOM = "Object Zoom";
		String OBJECT_CONTAINER = "Object Container";
		String OBJECT_CONT_ANIM = "Object Container Animation";
		String MAP = "Map";
		String MAP_MAIN = "Map Main";
	}
	
	interface ControlStyle{
		String POS = "Pos";
		String HPOS = "H Pos";
		String VPOS = "V Pos";
		String LEFT = "Left";
		String RIGHT = "Right";
		String CENTER = "Center";
		String DOWN = "Down";
		String UP = "Up";
		String VCENTER = "V Center";
		String TYPE = "Type";
		String SINGLE = "Single";
		String MULTI = "Multi-line";
		String TITLE_BAR = "Title Bar";
		String PICTURE = "Picture";
		String FRAME = "Frame";
		String BACKGROUND = "Background";
		String GROUP_BOX = "Group Box";
		String GROUP_BOX2 = "Group Box2";
		String HUD_BACKGROUND = "HUD Background";
		String TILE_PICTURE = "Title Picture";
		String WITH_RECT = "With Rectangle";
		String LINE = "Line";
		String SHADOW = "Shadow";
		String NO_RECT = "No Rectangle";
		String KEEP_ASPECT_RATIO = "Keep Aspect Ratio";
		String TITLE = "Title";
		String SL_DIR = "Slider Dir";
		String SL_VERT = "Slider Vertical";
		String SL_HORZ = "Slider Horizontal";
		String SL_TEXTURES = "Slider Textures";
		String VERTICAL = "Vertical";
		String HORIZONTAL = "Horizontal";
		String LB_TEXTURES = "Listbox Textures";
		String LB_MULTI = "Listbox Multi";
		String TR_SHOWROOT = "Show Tree Root";
		String TR_AUTOCOLLAPSE = "Auto Collapse Tree";
		String MB_BUTTON_OK = "Message Box OK";
		String MB_BUTTON_CANCEL = "Message Box Cancel";
		String MB_BUTTON_USER = "Message Box User";
		
		interface Doc {
			String NO_DOC = "No documentation.";
			String POS = null;
			String HPOS = null;
			String VPOS = null;
			String LEFT = "Text is left-aligned.";
			String RIGHT = "Text is right-aligned.";
			String CENTER = "Text is center-aligned.";
			String DOWN = null;
			String UP = null;
			String VCENTER = null;
			String TYPE = null;
			String SINGLE = null;
			String MULTI = "Text can be placed on more than one lines.";
			String TITLE_BAR = null;
			String PICTURE = "The 'text' property will read as a link to an image or video file.";
			String FRAME = "A border line is drawn around control.";
			String BACKGROUND = null;
			String GROUP_BOX = null;
			String GROUP_BOX2 = null;
			String HUD_BACKGROUND = null;
			String TILE_PICTURE = null;
			String WITH_RECT = "Border is drawn around control and background color becomes invisible.";
			String LINE = "Draws a line from [x,y] to [x+w, y+h].";
			String SHADOW = "Black drop shadow is placed behind text. (Deprecated-use control property shadow).";
			String NO_RECT = "If used with multi-line (16), this will suppress border drawn by multi-line controls.";
			String KEEP_ASPECT_RATIO = "Image/video will retrain it's original proportions.";
			String TITLE = null;
			String SL_DIR = null;
			String SL_VERT = "Orientation of slider is vertical.";
			String SL_HORZ = "Orientation of slider is horizontal.";
			String SL_TEXTURES = null;
			String VERTICAL = null;
			String HORIZONTAL = null;
			String LB_TEXTURES = "Solid scrollbar.";
			String LB_MULTI = "Multiple items can be selected.";
			String TR_SHOWROOT = "Show the root node.";
			String TR_AUTOCOLLAPSE = "Only one branch can be expanded.";
			String MB_BUTTON_OK = null;
			String MB_BUTTON_CANCEL = null;
			String MB_BUTTON_USER = null;
		}
	}
}
