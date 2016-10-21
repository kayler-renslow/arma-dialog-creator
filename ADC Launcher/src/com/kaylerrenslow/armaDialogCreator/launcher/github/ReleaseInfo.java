/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.launcher.github;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 Created by Kayler on 10/20/2016.
 */
public class ReleaseInfo {
	private String tagName, releaseName;
	private ReleaseAsset[] assets;

	public ReleaseInfo(String url) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		URLConnection connection = new URL(url).openConnection();
		InputStreamReader reader = new InputStreamReader(connection.getInputStream());

		JSONObject object = (JSONObject) parser.parse(reader);

		reader.close();
		connection.getInputStream().close();

		dissect(object);
	}

	private void dissect(JSONObject object) {
		tagName = object.get("tag_name").toString();
		releaseName = object.get("name").toString();

		JSONArray assets = (JSONArray) object.get("assets");
		this.assets = new ReleaseAsset[assets.size()];
		int i = 0;
		for (Object assetObj : assets) {
			this.assets[i++] = new ReleaseAsset((JSONObject) assetObj);
		}
	}

	@Nullable
	public ReleaseAsset getAssestByName(@NotNull String name) {
		for (ReleaseAsset asset : assets) {
			if (asset.getName().equals(name)) {
				return asset;
			}
		}
		return null;
	}

	public String getTagName() {
		return tagName;
	}

	public String getReleaseName() {
		return releaseName;
	}

	public ReleaseAsset[] getAssets() {
		return assets;
	}
}
