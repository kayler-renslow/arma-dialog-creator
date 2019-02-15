package com.armadialogcreator.control;

import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/06/2019 */
public class CollectionUINode extends StructureUINode {
	private final NotNullValueObserver<String> collectionNameObserver;

	public CollectionUINode(@NotNull String folderName) {
		collectionNameObserver = new NotNullValueObserver<>(folderName);
	}

	@NotNull
	public NotNullValueObserver<String> getCollectionNameObserver() {
		return collectionNameObserver;
	}

}
