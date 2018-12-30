package com.armadialogcreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author Kayler
 @since 03/22/2017 */
public class HeaderTestUtil {
	@NotNull
	public static File getFile(@NotNull String fileName) {
		return new File("tests/com/armadialogcreator/arma/header/" + fileName);
	}

	@NotNull
	public static File getTemporaryResultsFile() {
		return getFile("testsData_ignore");
	}
}
