package com.kaylerrenslow.armaDialogCreator.installer;

import javafx.concurrent.Task;
import net.lingala.zip4j.core.ZipFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static com.kaylerrenslow.armaDialogCreator.installer.ADCInstaller.bundle;

/**
 Task that unzips the downloaded adc.zip. The task returns the directory to which the contents were extracted too

 @author kayler
 @since 4/10/17 */
public class ADCInstallerTask extends Task<File> {
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
	 @param unzipDirectory directory to place unzipped contents to. If null, will zip in directory containing {@code zipFile}
	 */
	public ADCInstallerTask(@NotNull File zipFile, @Nullable File unzipDirectory, @NotNull PrintStream ps) {
		if (zipFile.isDirectory()) {
			throw new IllegalArgumentException("zipFile is directory");
		}
		if (unzipDirectory == null) {
			unzipDirectory = zipFile.getParentFile();
		} else {
			if (!unzipDirectory.isDirectory()) {
				throw new IllegalArgumentException("unzipDirectory is not a directory");
			}
		}

		this.ps = ps;
		this.zipFile = zipFile;
		this.unzipDirectory = unzipDirectory;
	}

	@NotNull
	private String getDestPath(@NotNull String fileName) {
		return unzipDirectory.getAbsolutePath() + "/" + fileName;
	}

	@Override
	protected File call() throws Exception {
		updateProgress(-1, 1);
		updateMessage(bundle.getString("Installer.backing_up"));
		File backupFile = new File(unzipDirectory.getAbsolutePath() + ".backup");
		Files.copy(unzipDirectory.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		updateProgress(1, 1);

		updateMessage(bundle.getString("Installer.backup_finished"));

		Thread.sleep(1000);//sleep to show user files have been backed up

		updateProgress(-1, 1);
		updateMessage(bundle.getString("Installer.extracting"));

		ZipFile zipFileO = new ZipFile(zipFile);
		updateProgress(0, toExtract.length);
		int numExtract = toExtract.length;
		int p = 0;
		for (String f : toExtract) {
			zipFileO.extractFile(f, getDestPath(f));
			updateProgress(++p, numExtract);
			ps.println("ex:" + f);
		}

		updateMessage(bundle.getString("Installer.verifying"));
		p = 0;
		updateProgress(0, numExtract);

		for (String f : toExtract) {
			updateProgress(++p, numExtract);
			if (!new File(getDestPath(f)).exists()) {
				String e = String.format(
						bundle.getString("Installer.verify_fail_f"),
						String.format(bundle.getString("Installer.file_didnt_extract_f"), f)
				);
				updateMessage(
						e
				);
				ps.println(e);
				Thread.sleep(1000);
				try {
					restoreOld(backupFile);
				} catch (Exception ex) {
					String e1 = String.format(bundle.getString("Installer.backup_restore_failed_f"), ex.getMessage());
					updateMessage(e1);
					ps.println(e1);
				}
				return null;
			}
		}

		updateMessage(bundle.getString("Installer.finished"));

		updateProgress(1, 1);

		return unzipDirectory;
	}

	private void restoreOld(File backupFile) throws Exception {
		updateProgress(-1, 0);
		updateMessage(bundle.getString("Installer.restoring_backup"));
		Files.copy(backupFile.toPath(), unzipDirectory.toPath(), StandardCopyOption.REPLACE_EXISTING);
		updateMessage(bundle.getString("Installer.backup_restored"));
	}
}
