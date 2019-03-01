package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;

/**
 Used to tell the project exporter that a {@link SerializableValue} has file paths included in the output.
 This interface will be used by the exporter to replace the absolute file paths to a relative path
 determined by the project.

 @author Kayler
 @since 06/19/2017 */
public interface FilePathUser {
	/**
	 @return an int array that specifies which of the {@link SerializableValue#getAsStringArray()} indices will require the String to
	 be converted to a relative file path
	 @see FilePathUser Class level doc
	 */
	@NotNull int[] getIndicesThatUseFilePaths();
}
