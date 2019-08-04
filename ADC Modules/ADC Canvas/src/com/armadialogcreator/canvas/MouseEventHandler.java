package com.armadialogcreator.canvas;

import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 8/3/19. */
public interface MouseEventHandler {

	default void handleMouseMove(double mousex, double mousey) {

	}

	default void handleMouseEnter(double mousex, double mousey) {

	}

	default void handleMouseExit(double mousex, double mousey) {

	}

	default void handleMousePress(double mousex, double mousey, @NotNull MouseButton btn) {

	}

	default void handleMouseRelease(double mousex, double mousey, @NotNull MouseButton btn) {

	}

}
