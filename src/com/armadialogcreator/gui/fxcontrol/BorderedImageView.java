package com.armadialogcreator.gui.fxcontrol;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

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

	@NotNull
	@Override
	public Image getImage() {
		return imageView.getImage();
	}

	@NotNull
	@Override
	public ImageContainer copy() {
		return new BorderedImageView(imageView.getImage());
	}

	@NotNull
	@Override
	public Node getNode() {
		return this;
	}

}
