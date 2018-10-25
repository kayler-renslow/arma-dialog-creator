package com.kaylerrenslow.armaDialogCreator.installer;

import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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
	private final InstallPackage installPackage;

	/**
	 Create an installer task that will extract an InstallPackage

	 @param installPackage package to extract
	 @param extractDir where to extract files to
	 */
	public ADCInstallerTask(@NotNull InstallPackage installPackage, @NotNull File extractDir) {
		this.installPackage = installPackage;

		if (!extractDir.isDirectory()) {
			throw new IllegalArgumentException("extractDir is not a directory");
		}

		this.extractDirectory = extractDir;
	}

	@NotNull
	private String getDestPath(@NotNull String fileName) {
		return extractDirectory.getAbsolutePath() + File.separator + fileName;
	}

	@Override
	protected File call() throws Exception {
		updateProgress(-1, 1);

		if (!installPackage.packageExists()) {
			message(bundle.getString("Installer.extract_package_dne"));
			cancel();
			return null;
		}
		final int MAX_PROGRESS = 1 /*backup*/ + toExtract.length * 2 /*extract and verify*/ + 1 /*delete backup*/ + 1/*done*/;
		updateProgress(0, MAX_PROGRESS);

		int progress = 0;

		message(bundle.getString("Installer.backing_up"));
		File backupFile = new File(extractDirectory.getAbsolutePath() + ".backup");
		//todo make backup
		updateProgress(++progress, MAX_PROGRESS);

		message(bundle.getString("Installer.backup_finished"));

		updateProgress(++progress, MAX_PROGRESS);
		message(bundle.getString("Installer.extracting"));

		for (String f : toExtract) {
			message(bundle.getString("Installer.extract") + f);
			boolean s = installPackage.extract(f, getDestPath(f));
			if (!s) {
				break;
			}
			updateProgress(++progress, MAX_PROGRESS);
		}

		message(bundle.getString("Installer.verifying"));

		for (String f : toExtract) {
			updateProgress(++progress, MAX_PROGRESS);
			if (new File(getDestPath(f)).exists()) {
				continue;
			}

			message(
					String.format(
							bundle.getString("Installer.verify_fail_f"),
							String.format(bundle.getString("Installer.file_didnt_extract_f"), f)
					)
			);

			try {
				restoreOld(backupFile);
			} catch (Exception ex) {
				String e1 = String.format(bundle.getString("Installer.backup_restore_failed_f"), ex.getMessage());
				message(e1);
				throw ex;
			}

			return null;
		}

		message(bundle.getString("Installer.deleting_backup"));
		try {
			backupFile.delete();
			updateProgress(++progress, MAX_PROGRESS);
		} catch (SecurityException ignore) {

		}

		message(bundle.getString("Installer.finished"));

		updateProgress(++progress, MAX_PROGRESS);

		return extractDirectory;
	}

	private void restoreOld(File backupFile) throws Exception {
		updateProgress(-1, 0);
		message(bundle.getString("Installer.restoring_backup"));
		Files.copy(backupFile.toPath(), extractDirectory.toPath(), StandardCopyOption.REPLACE_EXISTING);

		message(bundle.getString("Installer.backup_restored"));
		updateProgress(1, 2);

		message(bundle.getString("Installer.deleting_backup"));
		backupFile.delete();

		updateProgress(2, 2);
	}

	private void message(String msg) {
		updateMessage(msg);
		System.out.println(msg);
		try {
			Thread.sleep(500); //give javafx time to see each new message
		} catch (InterruptedException ignore) {

		}
	}

}
