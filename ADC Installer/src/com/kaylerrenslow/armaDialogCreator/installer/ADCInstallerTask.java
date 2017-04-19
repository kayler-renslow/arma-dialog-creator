package com.kaylerrenslow.armaDialogCreator.installer;

import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

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
	/** Extract only what we need */
	private static final String[] toExtract = {
			"adc_updater.jar",
			"adc.jar",
			"Arma Dialog Creator.exe"
	};

	private final File extractDirectory;
	private PrintStream ps;
	private InstallPackage installPackage;

	/**
	 Create an installer task that will extract an InstallPackage

	 @param installPackage package to extract
	 @param ps stream to print messages to
	 */
	public ADCInstallerTask(@NotNull InstallPackage installPackage, @NotNull File extractDir, @NotNull PrintStream ps) {
		this.installPackage = installPackage;
		this.ps = ps;

		if (!extractDir.isDirectory()) {
			throw new IllegalArgumentException("extractDir is not a directory");
		}

		this.extractDirectory = extractDir;
	}

	@NotNull
	private String getDestPath(@NotNull String fileName) {
		return extractDirectory.getAbsolutePath() + "/" + fileName;
	}

	@Override
	protected File call() throws Exception {
		updateProgress(-1, 1);

		if (!installPackage.packageExists()) {
			updateMessage(bundle.getString("Installer.extract_package_dne"));
			cancel();
			return null;
		}

		updateMessage(bundle.getString("Installer.backing_up"));
		File backupFile = new File(extractDirectory.getAbsolutePath() + ".backup");
		Files.copy(extractDirectory.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		updateProgress(1, 1);

		updateMessage(bundle.getString("Installer.backup_finished"));

		Thread.sleep(1000);//sleep to show user files have been backed up

		updateProgress(-1, 1);
		updateMessage(bundle.getString("Installer.extracting"));

		updateProgress(0, toExtract.length);
		int numExtract = toExtract.length;
		int p = 0;
		for (String f : toExtract) {
			installPackage.extract(f, getDestPath(f));
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

				cancel();
				return null;
			}
		}

		updateMessage(bundle.getString("Installer.finished"));

		updateProgress(1, 1);

		return extractDirectory;
	}

	private void restoreOld(File backupFile) throws Exception {
		updateProgress(-1, 0);
		updateMessage(bundle.getString("Installer.restoring_backup"));
		Files.copy(backupFile.toPath(), extractDirectory.toPath(), StandardCopyOption.REPLACE_EXISTING);
		updateMessage(bundle.getString("Installer.backup_restored"));
	}
}
