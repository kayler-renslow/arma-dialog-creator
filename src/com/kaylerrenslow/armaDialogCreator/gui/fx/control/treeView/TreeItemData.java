/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;


import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreeItemData {

	private static int lastId = 0;

	private final int ID = lastId++;

	private final CellType cellType;
	private TreeNodeUpdateListener updateListener;

	private final Node graphic;

	private String text;


	public TreeItemData(@NotNull String text, @NotNull CellType cellType, @Nullable Node graphic) {
		this.graphic = graphic;
		this.text = text;
		this.cellType = cellType;
	}

	public void setUpdateListener(TreeNodeUpdateListener updateListener) {
		this.updateListener = updateListener;
	}

	public TreeNodeUpdateListener getUpdateListener() {
		return updateListener;
	}

	public Node getGraphic() {
		return graphic;
	}

	public final CellType getCellType() {
		return cellType;
	}

	public final boolean canHaveChildren() {
		return cellType == CellType.FOLDER || cellType == CellType.COMPOSITE;
	}

	public final boolean isFolder() {
		return cellType == CellType.FOLDER;
	}

	public final boolean isComposite() {
		return cellType == CellType.COMPOSITE;
	}


	@Override
	public boolean equals(Object other) {
		return other instanceof TreeItemData && this.ID == ((TreeItemData) other).ID;
	}

	@Override
	public final String toString() {
		return text;
	}

	public final String getText() {
		return text;
	}

	void setText(String t) {
		this.text = t;
		if (this.updateListener != null) {
			this.updateListener.renamed(t);
		}
	}

	void delete() {
		if (this.updateListener != null) {
			this.updateListener.delete();
		}
	}
}
