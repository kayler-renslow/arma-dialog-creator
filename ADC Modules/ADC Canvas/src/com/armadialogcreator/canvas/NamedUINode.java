package com.armadialogcreator.canvas;

import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 4/8/19 */
public interface NamedUINode extends UINode {
	/** @return the name of the node as an {@link NotNullValueObserver} */
	@NotNull NotNullValueObserver<String> getUINodeName();
}
