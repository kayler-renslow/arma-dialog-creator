package com.armadialogcreator.util;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 5/27/19 */
public class ImageReference {
	private int refCount = 0;
	private final Image image;

	public ImageReference(@NotNull Image image) {
		this.image = image;
	}

	@NotNull
	public Image getImage() {
		return image;
	}

	public void decrementRefCount() {
		refCount--;
	}

	public void incrementRefCount() {
		refCount++;
	}

	public boolean isUnused() {
		return refCount <= 0;
	}
}
