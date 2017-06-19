package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 Used for creating a link between a .paa image and the converted .png image stored inside the project

 @author Kayler
 @since 07/19/2016. */
public class PaaImageExternalResource extends ExternalResource {
	/**
	 Key used for the converted .paa image. To get the converted image file path,
	 use key with {@link #getPropertyValue(String)}
	 */
	public static final String KEY_CONVERTED_IMAGE = "converted-paa";

	/**
	 Create a link for a .paa image

	 @param paaImage file to the .paa image that was used
	 @param convertedImage image that was converted
	 */
	public PaaImageExternalResource(@NotNull File paaImage, @NotNull File convertedImage) {
		super(paaImage, new KeyValueString[]{new KeyValueString(KEY_CONVERTED_IMAGE, convertedImage.getAbsolutePath())});
	}
}
