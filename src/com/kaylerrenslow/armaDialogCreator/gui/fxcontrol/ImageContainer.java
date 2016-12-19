package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol;

import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 Created by Kayler on 10/22/2016.
 */
public interface ImageContainer {
	Image getImage();

	ImageContainer copy();

	Node getNode();
}
