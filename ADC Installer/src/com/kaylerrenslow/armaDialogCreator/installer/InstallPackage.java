package com.kaylerrenslow.armaDialogCreator.installer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 @author kayler
 @since 4/19/17 */
public abstract class InstallPackage {

	/**
	 Extract the given file path to the given file path

	 @param path file to extract
	 @param extractTo path to extract to
	 @return true if extract finished sucessfully, false if didn't succeed
	 */
	public abstract boolean extract(@NotNull String path, @NotNull String extractTo);

	/** Return true if the package to extract exists, false otherwise */
	public abstract boolean packageExists();

	/**
	 Used for extracting out of implicit .jar (uses getClass().getResourceAsStream())

	 @author kayler
	 @since 4/20/2017
	 */
	static class JarInstallPackage extends InstallPackage {

		@Override
		public boolean extract(@NotNull String path, @NotNull String extractTo) {
			try {
				doExtract(path, extractTo);
			} catch (Throwable ignore) {
				return false;
			}
			return true;
		}

		private void doExtract(@NotNull String path, @NotNull String extractTo) throws Exception {
			InputStream stream = getClass().getResourceAsStream("/install/" + path);
			if (stream == null) {
				throw new FileNotFoundException("/install/" + path);
			}
			File extractToFile = new File(extractTo);
			if (!extractToFile.exists()) {
				extractToFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(extractToFile);

			int i = 0;
			while ((i = stream.read()) >= 0) {
				fos.write(i);
			}

			stream.close();
			fos.close();
		}

		@Override
		public boolean packageExists() {
			return true;
		}
	}

}
