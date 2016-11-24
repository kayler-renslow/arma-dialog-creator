/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/20/2016.
 */
public interface CanvasView {

	@NotNull
	UICanvasConfiguration getConfiguration();

	/**
	 Set the background image of the canvas

	 @param imgPath the path to the image
	 */
	void setCanvasBackgroundToImage(@Nullable String imgPath);

	/** Fetches the new ui colors and repaints the canvas */
	void updateCanvas();

	/**
	 Update the Absolute region box. For each parameter: -1 to leave unchanged, 0 for false, 1 for true

	 @param alwaysFront true if the region should always be rendered last, false if it should be rendered first
	 @param showing true the region is showing, false if not
	 */
	void updateAbsRegion(int alwaysFront, int showing);
	
	void setTreeStructure(boolean backgroundTree, TreeStructure<TreeItemEntry> treeStructure);

	@NotNull
	TreeStructure<? extends TreeItemEntry> getMainControlsTreeStructure();

	@NotNull
	TreeStructure<? extends TreeItemEntry> getBackgroundControlsTreeStructure();

	@NotNull
	EditableTreeView<? extends TreeItemEntry> getMainControlTreeView();

	@NotNull
	EditableTreeView<? extends TreeItemEntry> getBackgroundControlTreeView();
}
