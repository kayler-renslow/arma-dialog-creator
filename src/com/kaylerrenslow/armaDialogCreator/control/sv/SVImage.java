package com.kaylerrenslow.armaDialogCreator.control.sv;

import java.io.File;

/**
 @author Kayler
 A SerializableValue implementation for storing an image file
 Created on 07/16/2016. */
public class SVImage implements SerializableValue {
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
	public String toString() {
		return arr[0];
	}
	
	@Override
	public SerializableValue deepCopy() {
		return new SVImage(imageRelativePath);
	}
}
