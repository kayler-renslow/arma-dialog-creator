package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;


import com.kaylerrenslow.armaDialogCreator.gui.fx.control.IGraphicCreator;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreeItemData<E> {

	private static int lastId = 0;

	private final int ID = lastId++;

	private final E data;
	private final CellType cellType;
	private final ITreeNodeUpdateListener updateListener;

	private final boolean isPlaceholder;
	private final Node graphic;

	private String text;

	/**
	 Creates a place holder TreeItemData to be used in folders

	 @return a new place holder TreeItemData
	 */
	static TreeItemData getPlaceHolder() {
		return new TreeItemData();
	}

	private TreeItemData() {
		this.isPlaceholder = true;
		data = null;
		cellType = null;
		updateListener = null;
		javafx.scene.control.Label lbl = new javafx.scene.control.Label("empty");
		lbl.setFont(Font.font(Font.getDefault().getFamily(), FontPosture.ITALIC, Font.getDefault().getSize()));
		lbl.setOpacity(0.5);
		this.graphic = lbl;
	}

	public TreeItemData(@NotNull String text, @NotNull CellType cellType, @NotNull E data, @Nullable IGraphicCreator creator, @Nullable ITreeNodeUpdateListener updateListener) {
		this.isPlaceholder = false;
		this.graphic = (creator != null ? creator.createGraphic() : null);
		this.text = text;
		this.cellType = cellType;
		this.data = data;
		this.updateListener = updateListener;
	}


	public TreeItemData(@NotNull String text, @NotNull CellType cellType, @NotNull E data, @Nullable IGraphicCreator creator) {
		this(text, cellType, data, creator, null);
	}

	public ITreeNodeUpdateListener getUpdateListener() {
		return updateListener;
	}

	public Node getGraphic() {
		return graphic;
	}

	public E getData() {
		return data;
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

	boolean isPlaceholder() {
		return isPlaceholder;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TreeItemData) {
			return this.ID == ((TreeItemData) other).ID;
		}
		return false;
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
