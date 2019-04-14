package com.armadialogcreator.gui.fxcontrol;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 An ImageView with an overlayable image

 @author K
 @since 4/7/19 */
public class OverlayImageView extends StackPane {
	public OverlayImageView(@NotNull Node n, @NotNull Image overlayImage) {
		this.getChildren().add(n);
		this.getChildren().add(new ImageView(overlayImage));
	}

	private OverlayImageView(@NotNull Node n) {
		getChildren().add(n);
	}

	@NotNull
	public static OverlayImageView withBorderedImageView(@NotNull Node n, @NotNull Image i) {
		OverlayImageView view = new OverlayImageView(n);
		view.getChildren().add(new BorderedImageView(i));
		return view;
	}
}
