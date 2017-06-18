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

	private File imageFile;

	public SVImage(@NotNull File imageFile) {
		super(imageFile.getPath());
		setImageFile(imageFile);
	}

	@NotNull
	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(@NotNull File imageFile) {
		this.imageFile = imageFile;
		valuesAsArray[0] = imageFile.getPath();
	}
	
	@Override
	public String toString() {
		return valuesAsArray[0];
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVImage(imageFile);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Image;
	}

	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof SVImage){
			SVImage other = (SVImage) o;
			return this.imageFile.equals(other.imageFile);
		}
		return false;
	}
}
