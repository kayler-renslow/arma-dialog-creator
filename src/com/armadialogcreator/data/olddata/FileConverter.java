package com.armadialogcreator.data.olddata;

import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 Simple implementation for a File {@link ValueConverter}
 @author Kayler
 @since 07/31/2016. */
public class FileConverter implements ValueConverter<File> {
	public static final FileConverter INSTANCE = new FileConverter();
	
	@Override
	public File convert(DataContext context, @NotNull String... values) throws Exception {
		return new File(values[0]);
	}
}
