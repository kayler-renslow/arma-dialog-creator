package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 A SerializableValue implementation for storing an image file
 @author Kayler
 @since 07/16/2016. */
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

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVImage(imageRelativePath);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.IMAGE;
	}

	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof SVImage){
			SVImage other = (SVImage) o;
			return this.imageRelativePath.equals(other.imageRelativePath);
		}
		return false;
	}
}
