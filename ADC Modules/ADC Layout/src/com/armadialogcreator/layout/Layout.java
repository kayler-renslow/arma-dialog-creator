package com.armadialogcreator.layout;

import com.armadialogcreator.util.DataInvalidator;
import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 7/23/19. */
public interface Layout extends DataInvalidator {
	void recomputePositions();

	@NotNull ListObserver<LayoutNode> getChildren();

	@NotNull String getName();

	@NotNull
	Bounds getLayoutBounds();

	@NotNull NotNullValueObserver<Alignment> getAlignment();

}
