package com.kaylerrenslow.armaDialogCreator.gui.fx.control;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 Created by Kayler on 10/22/2016.
 */
public class SimpleImageContainer extends ImageView implements ImageContainer {
	public SimpleImageContainer() {
	}

	public SimpleImageContainer(String url) {
		super(url);
	}

	public SimpleImageContainer(Image image) {
		super(image);
	}

	@Override
	public ImageContainer copy() {
		return new SimpleImageContainer(getImage());
	}

	@Override
	public Node getNode() {
		return this;
	}
}
