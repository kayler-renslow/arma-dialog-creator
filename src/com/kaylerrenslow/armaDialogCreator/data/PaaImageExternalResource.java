package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author Kayler
 Used for creating a link between a .paa image and the converted .png image stored inside the project
 Created on 07/19/2016. */
public class PaaImageExternalResource extends ExternalResource {
	private final File convertedImagePath;
	
	/**
	 Create a link for a .paa image
	 
	 @param paaImagePath path of the .paa image file
	 */
	public PaaImageExternalResource(@NotNull File paaImagePath, @NotNull File convertedImagePath) {
		super(paaImagePath);
		this.convertedImagePath = convertedImagePath;
	}
	
	/**Get the path to the converted .paa image (stored as a .png)*/
	@NotNull
	public File getConvertedImagePath() {
		return convertedImagePath;
	}
}
