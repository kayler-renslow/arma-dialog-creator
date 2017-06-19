package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.FilePathUser;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 @author Kayler
 @since 06/19/2017 */
public class SVFileName extends SerializableValue implements FilePathUser {
	public static ValueConverter<SerializableValue> CONVERTER = new ValueConverter<SerializableValue>() {
		@Override
		public SerializableValue convert(@Nullable DataContext context, @NotNull String... values) throws Exception {
			return new SVFileName(new File(values[0]));
		}
	};

	private final File f;

	public SVFileName(@NotNull File f) {
		super(f.getAbsolutePath());
		this.f = f;
	}

	@Override
	@NotNull
	public SerializableValue deepCopy() {
		return new SVFileName(f);
	}

	@Override
	public @NotNull PropertyType getPropertyType() {
		return PropertyType.FileName;
	}

	@NotNull
	@Override
	public int[] getIndicesThatUseFilePaths() {
		return new int[]{0};
	}
}
