package com.armadialogcreator.gui.fxcontrol.treeView;


import com.armadialogcreator.util.NotNullValueObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreeItemData {

	private final Node graphic;
	private final ObservableList<String> styleClassList = FXCollections.observableArrayList();
	private final NotNullValueObserver<String> textObserver = new NotNullValueObserver<>("");


	public TreeItemData(@NotNull String text, @Nullable Node graphic) {
		this.graphic = graphic;
		textObserver.updateValue(text);
	}

	@Nullable
	public Node getGraphic() {
		return graphic;
	}

	public boolean canHaveChildren() {
		return true;
	}

	@Override
	public final String toString() {
		return textObserver.getValue();
	}

	public final String getText() {
		return textObserver.getValue();
	}

	public void setText(@NotNull String t) {
		textObserver.updateValue(t);
	}

	@NotNull
	public ObservableList<String> getStyleClass() {
		return styleClassList;
	}

	@NotNull
	public NotNullValueObserver<String> getTextObserver() {
		return textObserver;
	}
}
