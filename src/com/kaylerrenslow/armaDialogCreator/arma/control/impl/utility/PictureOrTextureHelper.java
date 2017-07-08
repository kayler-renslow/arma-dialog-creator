package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.Texture;
import com.kaylerrenslow.armaDialogCreator.arma.util.TextureParser;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Utility class to combine the use cases of a property that supports textures and images.

 @author Kayler
 @since 07/06/2017 */
public class PictureOrTextureHelper {
	public enum Mode {
		/** The property has an image */
		Image,
		/** The property has a texture */
		Texture,
		/** Couldn't load the image */
		ImageError,
		/** Couldn't load the texture */
		TextureError,
		/** Doing async computation for retrieving the image at the current moment */
		LoadingImage
	}

	private final ArmaControlRenderer renderer;
	private volatile Texture texture;
	private volatile Image image;
	private volatile Mode mode = Mode.LoadingImage;

	/**
	 @param renderer the renderer that will be notified of when to render again
	 */
	public PictureOrTextureHelper(@NotNull ArmaControlRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 Updates the internal state of this helper. This method isn't async, unless an image is being loaded. When an
	 image is being loaded, {@link #getMode()} will return {@link Mode#LoadingImage}. This method will also invoke
	 {@link ArmaControlRenderer#requestRender()}

	 @param value the value to update the helper with
	 */
	public void updateAsync(@Nullable SerializableValue value) {
		if (value != null) {
			String textValue = value.toString();
			if (textValue.length() > 0 && textValue.charAt(0) == '#') {
				this.texture = null;
				try {
					this.texture = new TextureParser(textValue).parse();
					image = null;
					mode = Mode.Texture;
				} catch (IllegalArgumentException ignore) {
					mode = Mode.TextureError;
				}
				renderer.requestRender();
				return;
			}
		}

		texture = null;
		mode = Mode.LoadingImage;

		ImageHelper.getImageAsync(value, image -> {
			synchronized (PictureOrTextureHelper.this) {
				//synchronized so that the mode and image to paint are set at the same time
				if (image == null) {
					this.image = null;
					mode = Mode.ImageError;
				} else {
					this.image = image;
					mode = Mode.Image;
				}
				renderer.requestRender();
			}
			return null;
		});
	}

	/**
	 Get the mode. This method is synchronized to ensure that the mode is set at the same time as {@link #getImage()}
	 or {@link #getTexture()} is set.

	 @return the current mode
	 @see #updateAsync(SerializableValue)
	 */
	@NotNull
	public synchronized Mode getMode() {
		return mode;
	}

	/**
	 Get the texture. This method is synchronized to ensure that the mode is set at the same time as
	 {@link #getImage()} or {@link #getMode()} is set.

	 @return the texture, or null if {@link #getMode()} is not {@link Mode#Texture}
	 */
	@Nullable
	public synchronized Texture getTexture() {
		return texture;
	}

	/**
	 Get the texture. This method is synchronized to ensure that the mode is set at the same time as
	 {@link #getTexture()} or {@link #getMode()} is set.

	 @return the image, or null if {@link #getMode()} is not {@link Mode#Image}
	 */
	@Nullable
	public synchronized Image getImage() {
		return image;
	}
}
