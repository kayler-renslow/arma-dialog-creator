package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView;


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
