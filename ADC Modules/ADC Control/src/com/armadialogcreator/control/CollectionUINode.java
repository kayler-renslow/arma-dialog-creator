package com.armadialogcreator.control;

import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/06/2019 */
public class CollectionUINode extends StructureUINode {
	private final NotNullValueObserver<String> collectionNameObserver;

	public CollectionUINode(@NotNull UINode rootNode, @NotNull String folderName) {
		super(rootNode);
		collectionNameObserver = new NotNullValueObserver<>(folderName);
	}

	@NotNull
	public NotNullValueObserver<String> getCollectionNameObserver() {
		return collectionNameObserver;
	}

}
