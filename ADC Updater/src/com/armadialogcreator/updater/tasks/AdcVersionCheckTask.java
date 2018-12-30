/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.armadialogcreator.updater.tasks;

import com.armadialogcreator.updater.ADCUpdater;
import com.armadialogcreator.updater.NotEnoughFreeSpaceException;
import com.armadialogcreator.updater.github.ReleaseAsset;
import com.armadialogcreator.updater.github.ReleaseInfo;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 Created by Kayler on 10/22/2016.
 */
public class AdcVersionCheckTask extends Task<Boolean> {
	private final File adcJarSave;
	private final File downloadDirectory;

	public AdcVersionCheckTask(@NotNull File adcJarSave, @NotNull File downloadDirectory) {
		this.adcJarSave = adcJarSave;
		this.downloadDirectory = downloadDirectory;
	}

	@Override
	protected Boolean call() throws Exception {
		setIndeterminateProgress();

		String currentJarVersion = getCurrentJarVersion();

		setStatusText("Updater.checking_for_updates");
		ReleaseInfo releaseInfo = getLatestRelease();

		if (!releaseInfo.getTagName().equals(currentJarVersion)) {
			downloadLatestRelease(releaseInfo);
		}

		return true;
	}

	private void downloadLatestRelease(@NotNull ReleaseInfo releaseInfo) throws Exception {
		setIndeterminateProgress();

		String updateJarName = null;

		File downloadedUpdateJarFile = null;

		//get the update.properties file
		{
			setStatusText("Updater.retrieving_details");

			ReleaseAsset updatePropertiesAsset = releaseInfo.getAssetByName("update.properties");
			if (updatePropertiesAsset == null) {
				throw new Exception(ADCUpdater.bundle.getString("Updater.Fail.asset_name_not_matched"));
			}

			URL url = new URL(updatePropertiesAsset.getDownloadUrl());
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			downloadFromServer(url, 5000/*5KB*/, os, prog -> {
				updateProgress(prog, 1);
				return null;
			});

			setStatusText("Updater.parsing_update_details");
			Properties p = new Properties();
			p.load(new StringReader(os.toString()));
			updateJarName = p.getProperty("updateJar");
		}

		ReleaseAsset updateJarAsset = releaseInfo.getAssetByName("update.properties");
		if (updateJarAsset == null) {
			throw new Exception(ADCUpdater.bundle.getString("Updater.Fail.asset_name_not_matched"));
		}

		if (updateJarName != null) {
			updateJarAsset = releaseInfo.getAssetByName(updateJarName);
		}

		if (updateJarAsset == null) {
			throw new Exception(ADCUpdater.bundle.getString("Updater.Fail.asset_name_not_matched"));
		}

		//download the latest release update jar
		{
			setIndeterminateProgress();
			updateMessage(String.format(
					ADCUpdater.bundle.getString("Updater.downloading_newest_version_f"),
					releaseInfo.getTagName()
			));

			URL url = new URL(updateJarAsset.getDownloadUrl());
			downloadDirectory.mkdirs();
			downloadedUpdateJarFile = new File(downloadDirectory.getAbsolutePath() + "/" + updateJarAsset.getName());
			FileOutputStream fos = new FileOutputStream(downloadedUpdateJarFile);
			downloadFromServer(url, downloadDirectory.getFreeSpace(), fos, prog -> {
				updateProgress(prog, 1);
				return null;
			});
		}

		setStatusText("Updater.download_complete");
		Thread.sleep(1000);

		setStatusText("Updater.launching_update_installer");
		setIndeterminateProgress();
		{ //launch installer
			try {
				Process p = Runtime.getRuntime().exec("java -jar " + downloadedUpdateJarFile.getName() + " -nocfg", null, downloadDirectory);
				setStatusText("Updater.waiting_for_install");
				p.waitFor();
			} catch (IOException e) {
				return;
			}
		}

		downloadedUpdateJarFile.deleteOnExit();

		setStatusText("Updater.launching");
		Thread.sleep(2000);
	}

	@NotNull
	public static ReleaseInfo getLatestRelease() throws Exception {
		JSONParser parser = new JSONParser();
		URLConnection connection = new URL(ADCUpdater.JSON_RELEASE_INFO_URL).openConnection();
		InputStreamReader reader = new InputStreamReader(connection.getInputStream());

		JSONObject object = (JSONObject) parser.parse(reader);

		reader.close();
		connection.getInputStream().close();

		return new ReleaseInfo(object);
	}

	private String getCurrentJarVersion() throws Exception {
		setStatusText("Updater.getting_current_version");
		if (!adcJarSave.exists()) {
			return "";
		}
		Manifest m = new JarFile(adcJarSave).getManifest();
		Attributes manifestAttributes = m.getMainAttributes();
		return manifestAttributes.getValue("Specification-Version");
	}

	private void setStatusText(String bundleString) {
		updateMessage(ADCUpdater.bundle.getString(bundleString));
	}

	private void setIndeterminateProgress() {
		updateProgress(-1, 0);
	}

	private static void downloadFromServer(@NotNull URL url, long maxFileSize, @NotNull OutputStream outputStream,
										   @NotNull Function<Double, Double> progressUpdate) throws IOException, NotEnoughFreeSpaceException {

		BufferedInputStream in = null;
		URLConnection urlConnection = null;
		double workDone = 0;

		try {
			urlConnection = url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream());

			long downloadSize = urlConnection.getContentLengthLong();
			if (maxFileSize < downloadSize) {
				in.close();
				outputStream.close();
				urlConnection.getInputStream().close();
				throw new NotEnoughFreeSpaceException(ADCUpdater.bundle.getString("Updater.not_enough_free_space"));
			}

			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				outputStream.write(data, 0, count);
				workDone += count;
				progressUpdate.apply(workDone / downloadSize);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (urlConnection != null) {
				urlConnection.getInputStream().close();
			}
			outputStream.close();
		}
	}
}
