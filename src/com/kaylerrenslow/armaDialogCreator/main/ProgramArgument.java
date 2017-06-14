package com.kaylerrenslow.armaDialogCreator.main;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 10/12/2016.
 */
public enum ProgramArgument {
	ShowDebugFeatures("-showDebugFeatures"), LogInitProgress("-logInitProgress");

	private final String argText;

	ProgramArgument(String argText) {
		this.argText = argText;
	}

	@NotNull
	public String getArgKey() {
		return argText;
	}
}
