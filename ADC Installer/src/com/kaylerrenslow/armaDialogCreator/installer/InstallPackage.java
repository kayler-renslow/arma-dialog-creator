package com.kaylerrenslow.armaDialogCreator.installer;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.kaylerrenslow.armaDialogCreator.installer.ADCInstaller.bundle;

/**
 @author kayler
 @since 4/19/17 */
public abstract class InstallPackage {

	public abstract void extract(@NotNull String path, @NotNull String extractTo) throws Exception;

	public abstract boolean packageExists();

	static class JarInstallPackage extends InstallPackage {

		@Override
		public void extract(@NotNull String path, @NotNull String extractTo) throws Exception {
			InputStream stream = getClass().getResourceAsStream("/install/" + path);
			FileOutputStream fos = new FileOutputStream(new File(extractTo));

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

	static class ZipInstallPackage extends InstallPackage {
		private ZipFile zipFileO;
		private File zipFile;

		public ZipInstallPackage(@NotNull File zipFile) {
			if (zipFile.isDirectory()) {
				throw new IllegalArgumentException("zipFile is a directory");
			}
			this.zipFile = zipFile;

			try {
				this.zipFileO = new ZipFile(zipFile);
			} catch (ZipException ignore) {

			}
		}

		@Override
		public void extract(@NotNull String path, @NotNull String extractTo) throws Exception {
			if (zipFileO == null) {
				throw new Exception(bundle.getString("Installer.extract_package_dne"));
			}
			zipFileO.extractFile(path, extractTo);
		}

		@Override
		public boolean packageExists() {
			return zipFile.exists();
		}
	}
}
