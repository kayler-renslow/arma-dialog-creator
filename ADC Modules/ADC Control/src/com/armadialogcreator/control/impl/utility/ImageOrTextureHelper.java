package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ImagePool;
import com.armadialogcreator.control.Texture;
import com.armadialogcreator.control.TextureParser;
import com.armadialogcreator.core.sv.SVNull;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.DataInvalidator;
import com.armadialogcreator.util.ImageReference;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 Utility class to combine the use cases of a property that supports textures and images.

 @author Kayler
 @since 07/06/2017 */
public class ImageOrTextureHelper implements DataInvalidator {
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
	private volatile ImageReference image;
	private volatile Mode mode = Mode.ImageError;
	private SerializableValue value;

	/**
	 @param renderer the renderer that will be notified of when to render again
	 */
	public ImageOrTextureHelper(@NotNull ArmaControlRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 Updates the internal state of this helper. This method isn't async, unless an image is being loaded. When an
	 image is being loaded, {@link #getMode()} will return {@link Mode#LoadingImage}. This method will also invoke
	 {@link ArmaControlRenderer#requestRender()}

	 @param value the value to update the helper with
	 */
	public void updateAsync(@Nullable SerializableValue value) {
		updateAsync(value, null);
	}

	/**
	 Updates the internal state of this helper. This method isn't async, unless an image is being loaded. When an
	 image is being loaded, {@link #getMode()} will return {@link Mode#LoadingImage}. This method will also invoke
	 {@link ArmaControlRenderer#requestRender()}.
	 <p>
	 If <code>completionCallback</code> isn't null, it will be invoked before the render request. Also, the function will
	 be executed on the JavaFX Thread

	 @param value the value to update the helper with
	 @param completionCallback function to use when this method's internal functionality is complete, or null if don't care
	 */
	public void updateAsync(@Nullable SerializableValue value, @Nullable Consumer<Mode> completionCallback) {
		this.value = value == null ? SVNull.instance : value;
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
				if (completionCallback != null) {
					completionCallback.accept(mode);
				}

				renderer.requestRender();
				return;
			}
		}

		texture = null;
		mode = Mode.LoadingImage;

		ImagePool.getImageAsync(this.value, image -> {
			if (this.image != null) {
				this.image.decrementRefCount();
			}
			this.image = image;
			if (image == null) {
				mode = Mode.ImageError;
			} else {
				image.incrementRefCount();
				mode = Mode.Image;
			}
			if (completionCallback != null) {
				completionCallback.accept(mode);
			}
			renderer.requestRender();
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
		if (image == null) {
			return null;
		}
		return image.getImage();
	}

	/** @return the last value passed through {@link #updateAsync(SerializableValue)} */
	@Nullable
	public SerializableValue getValue() {
		return value;
	}

	@Override
	public void invalidate() {
		if (this.image != null) {
			this.image.decrementRefCount();
		}
	}
}
