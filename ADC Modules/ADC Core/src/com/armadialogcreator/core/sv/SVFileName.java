package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.old.FilePathUser;
import com.armadialogcreator.core.old.PropertyType;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
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
		this.f = f;
	}

	@NotNull
	public File getFile() {
		return f;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{f.getAbsolutePath()};
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

	@Override
	public String toString() {
		//don't use absolute path because new File("") would return
		//the working directory in the string, which isn't useful information
		return f.getPath();
	}

	@NotNull
	@Override
	public int[] getIndicesThatUseFilePaths() {
		return new int[]{0};
	}
}
