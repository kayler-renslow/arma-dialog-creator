package com.armadialogcreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 @author Kayler
 @since 12/09/2017 */
public interface HeaderFileTextProvider {
	/**
	 @return an unused Scanner instance that will be used to read the text for the header file
	 @throws IOException if the scanner couldn't be created
	 */
	@NotNull Scanner newTextScanner() throws IOException;

	/** @return the File name this instance represents. This should not return a path! */
	@NotNull String getFileName();

	/** @return the file path for this file */
	@NotNull String getFilePath();

	/** @return he length, in bytes, of the file */
	long getFileLength();

	/**
	 Used for getting text for an #include.
	 The path is relative to the file that this {@link HeaderFileTextProvider} represents.

	 @param path a file path
	 @return a {@link HeaderFileTextProvider} instance that can get the file text for the
	 provided path, or null if couldn't be resolved
	 */
	@Nullable HeaderFileTextProvider resolvePath(@NotNull String path);

	/**
	 A basic {@link File} implementation for {@link HeaderFileTextProvider}
	 */
	class BasicFileInput implements HeaderFileTextProvider {

		private final File file;

		public BasicFileInput(@NotNull File file) {
			this.file = file;
		}

		@Override
		@NotNull
		public Scanner newTextScanner() throws IOException {
			return new Scanner(file);
		}

		@Override
		@NotNull
		public String getFileName() {
			return file.getName();
		}

		@Override
		@NotNull
		public String getFilePath() {
			return file.getAbsolutePath();
		}

		@Override
		public long getFileLength() {
			return file.length();
		}

		@Override
		@Nullable
		public HeaderFileTextProvider resolvePath(@NotNull String path) {
			File f = file.getParentFile().toPath().resolve(path).toFile();
			if (f == null) {
				return null;
			}
			return new BasicFileInput(f);
		}
	}
}
