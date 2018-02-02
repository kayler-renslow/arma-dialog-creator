package com.kaylerrenslow.armaDialogCreator.installer;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	 Get all paths that can be extracted with {@link #extract(String, String)}.

	 @return list of paths
	 */
	@NotNull
	public abstract List<String> getAllToExtract();

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
			int bufSize = 0;
			while ((i = stream.read()) >= 0) {
				fos.write(i);
				bufSize++;
				if (bufSize > 1000) {
					fos.flush();
				}
			}
			fos.flush();

			stream.close();
			fos.close();
		}

		@Override
		public boolean packageExists() {
			return true;
		}

		@Override
		@NotNull
		public List<String> getAllToExtract() {
			List<String> paths = new ArrayList<>();
			CodeSource src = getClass().getProtectionDomain().getCodeSource();
			if (src == null) {
				throw new IllegalStateException("src == null");
			}

			URL jar = src.getLocation();
			ZipInputStream zip = null;
			try {
				zip = new ZipInputStream(jar.openStream());
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			while (true) {
				ZipEntry e = null;
				try {
					e = zip.getNextEntry();
				} catch (IOException e1) {
					throw new IllegalStateException(e1);
				}
				if (e == null) {
					break;
				}
				paths.add(e.getName());
			}

			return paths;
		}
	}

}
