package com.armadialogcreator.data.export;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 09/17/2016.
 */
public enum HeaderFileType {
	H(".h"), HH(".hh"), HPP(".hpp");

	private final String ext;

	HeaderFileType(String ext) {
		this.ext = ext;
	}

	@NotNull
	public String getExtension() {
		return ext;
	}

	public static final HeaderFileType DEFAULT = H;
}
