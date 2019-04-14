package com.armadialogcreator.control;

import com.armadialogcreator.canvas.NamedUINode;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/06/2019 */
public class CollectionUINode extends StructureUINode implements NamedUINode {
	private final NotNullValueObserver<String> collectionNameObserver;

	public CollectionUINode(@NotNull String collectionName) {
		collectionNameObserver = new NotNullValueObserver<>(collectionName);
	}

	@Override
	public @NotNull UINode deepCopy() {
		return new CollectionUINode(collectionNameObserver.getValue());
	}

	@Override
	@NotNull
	public NotNullValueObserver<String> getUINodeName() {
		return collectionNameObserver;
	}
}
