package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author Kayler
 A SerializableValue implementation for storing an image file
 Created on 07/16/2016. */
public class SVImage extends SerializableValue {
	
	public static final ValueConverter<SVImage> CONVERTER = new ValueConverter<SVImage>() {
		@Override
		public SVImage convert(DataContext context, @NotNull String... values) throws Exception {
			return new SVImage(new File(values[0]));
		}
	};
	
	private File imageRelativePath;
	
	public SVImage(File imageRelativePath) {
		super(imageRelativePath.getPath());
		setImageRelativePath(imageRelativePath);
	}
	
	public File getImageRelativePath() {
		return imageRelativePath;
	}
	
	public void setImageRelativePath(File imageRelativePath) {
		this.imageRelativePath = imageRelativePath;
		valuesAsArray[0] = imageRelativePath.getPath();
	}
	
	@Override
	public String toString() {
		return valuesAsArray[0];
	}
	
	@Override
	public SerializableValue deepCopy() {
		return new SVImage(imageRelativePath);
	}
}
