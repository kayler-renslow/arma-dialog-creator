package com.kaylerrenslow.armaDialogCreator.control.sv;

import java.io.File;
import java.util.Arrays;

/**
 @author Kayler
 A SerializableValue implementation for storing an image file
 Created on 07/16/2016. */
public class SVImage extends SerializableValue {
	private File imageRelativePath;
	
	public SVImage(File imageRelativePath) {
		super(imageRelativePath.getPath());
		setImageRelativePath(imageRelativePath);
		System.out.println(Arrays.toString(valuesAsArray));
	}
	
	public File getImageRelativePath() {
		return imageRelativePath;
	}
	
	public void setImageRelativePath(File imageRelativePath) {
		this.imageRelativePath = imageRelativePath;
		valuesAsArray[0] = imageRelativePath.getPath();
	}
//
//	@Override
//	public String[] getAsStringArray() {
//		return arr;
//	}
	
	@Override
	public String toString() {
		return valuesAsArray[0];
	}
	
	@Override
	public SerializableValue deepCopy() {
		return new SVImage(imageRelativePath);
	}
}
