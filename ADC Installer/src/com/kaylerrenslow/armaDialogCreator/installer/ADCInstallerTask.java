package com.kaylerrenslow.armaDialogCreator.installer;

import javafx.concurrent.Task;
import net.lingala.zip4j.core.ZipFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.PrintStream;
import java.util.ResourceBundle;

/**
 Task that unzips the downloaded adc.zip. The task returns the directory to which the contents were extracted too

 @author kayler
 @since 4/10/17 */
public class ADCInstallerTask extends Task<File> {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.installer.InstallBundle");
	private final File zipFile;
	private final File unzipDirectory;

	/** Extract only what we need */
	private static final String[] toExtract = {
			"adc_updater.jar",
			"adc.jar"
	};
	private PrintStream ps;

	/**
	 Create an installer task that will unzip a .zip file into a directory.

	 @param zipFile zip file to unzip
	 @param unzipDirectory directory to place unzipped contents to. If null, will zip n directory containing {@code zipFile}
	 */
	public ADCInstallerTask(@NotNull File zipFile, @Nullable File unzipDirectory, @NotNull PrintStream ps) {
		this.ps = ps;
		if (unzipDirectory == null) {
			if (zipFile.isDirectory()) {
				throw new IllegalArgumentException("zipFile is directory");
			}
			unzipDirectory = zipFile.getParentFile();
		} else {
			if (!unzipDirectory.isDirectory()) {
				throw new IllegalArgumentException("unzipDirectory is not a directory");
			}
		}

		this.zipFile = zipFile;
		this.unzipDirectory = unzipDirectory;
	}

	@NotNull
	private String getDestPath(@NotNull String fileName) {
		return unzipDirectory.getAbsolutePath() + "/" + fileName;
	}

	@Override
	protected File call() throws Exception {
		ZipFile zipFileO = new ZipFile(zipFile);
		for (String f : toExtract) {
			zipFileO.extractFile(f, getDestPath(f));
			ps.println("ex:" + f);
		}
		return unzipDirectory;
	}
}
