package com.armadialogcreator.gui.fxcontrol;

import javafx.scene.Node;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 10/22/2016.
 */
public interface ImageContainer {

	@NotNull
	Image getImage();

	@NotNull
	ImageContainer copy();

	@NotNull
	Node getNode();
}
