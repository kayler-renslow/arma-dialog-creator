package com.armadialogcreator.control;

import com.armadialogcreator.canvas.NamedUINode;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/06/2019 */
public class FolderUINode extends StructureUINode implements NamedUINode {
	private final NotNullValueObserver<String> folderNameObserver;

	public FolderUINode(@NotNull String folderName) {
		folderNameObserver = new NotNullValueObserver<>(folderName);
	}

	@NotNull
	public String getFolderName() {
		return folderNameObserver.getValue();
	}

	@Override
	@NotNull
	public UINode deepCopy() {
		FolderUINode node = new FolderUINode(folderNameObserver.getValue());
		node.setParentNode(getParentNode());
		return node;
	}

	@Override
	@NotNull
	public NotNullValueObserver<String> getUINodeName() {
		return folderNameObserver;
	}

	@Override
	public void invalidate() {
		folderNameObserver.invalidate();
	}
}
