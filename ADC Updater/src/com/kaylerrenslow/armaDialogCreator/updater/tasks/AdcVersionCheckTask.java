/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.updater.tasks;

import com.kaylerrenslow.armaDialogCreator.updater.ADCUpdater;
import com.kaylerrenslow.armaDialogCreator.updater.NotEnoughFreeSpaceException;
import com.kaylerrenslow.armaDialogCreator.updater.github.ReleaseAsset;
import com.kaylerrenslow.armaDialogCreator.updater.github.ReleaseInfo;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 Created by Kayler on 10/22/2016.
 */
public class AdcVersionCheckTask extends Task<Boolean> {
	private final File adcJarSave;
	private final String versionCheckUrl;

	public AdcVersionCheckTask(@NotNull File adcJarSave, @NotNull String versionCheckUrl) {
		this.adcJarSave = adcJarSave;
		this.versionCheckUrl = versionCheckUrl;
	}

	@Override
	protected Boolean call() throws Exception {
		setIndeterminateProgress();

		String currentJarVersion = getCurrentJarVersion();

		ReleaseInfo latestRelease = getLatestRelease();
		ReleaseAsset adcJarAsset = latestRelease.getAssestByName(adcJarSave.getName());
		if (adcJarAsset == null) {
			throw new Exception(ADCUpdater.bundle.getString("Updater.Fail.asset_name_not_matched"));
		}

		if (!latestRelease.getTagName().equals(currentJarVersion)) {
			downloadLatestRelease(adcJarAsset.getDownloadUrl());
		}

		return true;
	}

	private void downloadLatestRelease(String downloadUrl) throws Exception {
		setIndeterminateProgress();
		setStatusText("Updater.downloading_newest_version");

		URL url = new URL(downloadUrl);

		BufferedInputStream in = null;
		FileOutputStream fout = null;
		URLConnection urlConnection = null;
		long workDone = 0;

		try {
			urlConnection = url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream());
			fout = new FileOutputStream(adcJarSave);

			long downloadSize = urlConnection.getContentLengthLong();
			if (adcJarSave.getParentFile().getFreeSpace() < downloadSize) {
				in.close();
				fout.close();
				urlConnection.getInputStream().close();
				throw new NotEnoughFreeSpaceException(ADCUpdater.bundle.getString("Updater.not_enough_free_space"));
			}

			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
				workDone += count;
				updateProgress(workDone, downloadSize);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (urlConnection != null) {
				urlConnection.getInputStream().close();
			}
			if (fout != null) {
				fout.close();
			}
		}
		setStatusText("Updater.download_complete");
		Thread.sleep(1000);
		setStatusText("Updater.launching");
		Thread.sleep(1000);
	}

	@NotNull
	private ReleaseInfo getLatestRelease() throws Exception {
		setStatusText("Updater.checking_for_updates");

		JSONParser parser = new JSONParser();
		URLConnection connection = new URL(versionCheckUrl).openConnection();
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
}
