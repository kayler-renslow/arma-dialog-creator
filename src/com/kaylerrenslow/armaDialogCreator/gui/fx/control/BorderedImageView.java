/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.control;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 Created by Kayler on 10/22/2016.
 */
public class BorderedImageView extends StackPane implements ImageContainer {
	private final ImageView imageView;

	public BorderedImageView(String imageViewUrl) {
		this(new Image(imageViewUrl));
	}

	public BorderedImageView(Image img) {
		this.imageView = new ImageView(img);
		getChildren().add(imageView);
		getStyleClass().add("bordered-image");
	}

	@Override
	public Image getImage() {
		return imageView.getImage();
	}

	@Override
	public ImageContainer copy() {
		return new BorderedImageView(imageView.getImage());
	}

	@Override
	public Node getNode() {
		return this;
	}

}
