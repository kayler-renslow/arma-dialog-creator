package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 Used for creating a link between a .paa image and the converted .png image stored inside the project

 @author Kayler
 @since 07/19/2016. */
public class PaaImageExternalResource extends ExternalResource {
	private static final String keyPaaImagePath = "paa-image-path";

	/**
	 Create a link for a .paa image

	 @param paaImagePath path to the .paa image that was used
	 @param convertedImage image that was converted
	 */
	public PaaImageExternalResource(@NotNull File paaImagePath, @NotNull File convertedImage) {
		super(convertedImage, new KeyValueString[]{new KeyValueString(keyPaaImagePath, paaImagePath.getPath())});
	}

	@Nullable
	public static File getPaaImagePath(@NotNull ExternalResource resource) {
		KeyValueString kv = resource.getPropertyValue(keyPaaImagePath);
		if (kv == null) {
			return null;
		}
		return new File(kv.getValue());
	}

	/** Get the path to the converted .paa image (stored as a .png) */
	@Nullable
	public File getPaaImagePath() {
		return getPaaImagePath(this);
	}
}
