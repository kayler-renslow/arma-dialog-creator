package com.armadialogcreator.control;

import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/06/2019 */
public class FolderUINode  extends StructureUINode {
	private final NotNullValueObserver<String> folderNameObserver;

	public FolderUINode(@NotNull UINode rootNode, @NotNull String folderName) {
		super(rootNode);
		folderNameObserver = new NotNullValueObserver<>(folderName);
	}

	@NotNull
	public NotNullValueObserver<String> getFolderNameObserver() {
		return folderNameObserver;
	}
}
