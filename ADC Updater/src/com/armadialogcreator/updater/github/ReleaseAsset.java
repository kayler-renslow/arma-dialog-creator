package com.armadialogcreator.updater.github;

import org.json.simple.JSONObject;

/**
 Created by Kayler on 10/20/2016.
 */
public class ReleaseAsset {
	private String name, downloadUrl;

	ReleaseAsset(JSONObject object) {
		name = object.get("name").toString();
		downloadUrl = object.get("browser_download_url").toString();
	}

	public String getName() {
		return name;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}
}
