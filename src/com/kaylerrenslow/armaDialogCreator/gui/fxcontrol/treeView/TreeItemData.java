package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreeItemData {

	private final CellType cellType;
	private TreeNodeUpdateListener updateListener;

	private final Node graphic;

	private String text;
	private final ObservableList<String> styleClassList = FXCollections.observableArrayList();
	private SimpleStringProperty textProperty = new SimpleStringProperty();


	public TreeItemData(@NotNull String text, @NotNull CellType cellType, @Nullable Node graphic) {
		this.graphic = graphic;
		this.text = text;
		this.cellType = cellType;
		textProperty.set(text);
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
	public final String toString() {
		return textProperty.get();
	}

	public final String getText() {
		return textProperty.get();
	}

	public void setText(@NotNull String t) {
		textProperty.set(t);
		if (this.updateListener != null) {
			this.updateListener.renamed(t);
		}
	}

	void delete() {
		if (this.updateListener != null) {
			this.updateListener.delete();
		}
	}

	@NotNull
	public ObservableList<String> getStyleClass() {
		return styleClassList;
	}

	@NotNull
	public ObservableValue<String> getTextObservable() {
		return textProperty;
	}
}
