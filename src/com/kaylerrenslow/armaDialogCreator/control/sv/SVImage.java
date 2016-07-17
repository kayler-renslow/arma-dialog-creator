package com.kaylerrenslow.armaDialogCreator.control.sv;

import java.io.File;

/**
 Created by Kayler on 07/16/2016.
 */
public class SVImage implements SerializableValue{
	private File imageRelativePath;

	private final String[] arr = new String[1];

	public SVImage(File imageRelativePath) {
		setImageRelativePath(imageRelativePath);
	}

	public File getImageRelativePath() {
		return imageRelativePath;
	}

	public void setImageRelativePath(File imageRelativePath) {
		this.imageRelativePath = imageRelativePath;
		arr[0] = imageRelativePath.getPath();
	}

	@Override
	public String[] getAsStringArray() {
		return arr;
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVImage(imageRelativePath);
	}
}
