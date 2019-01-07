package com.armadialogcreator.data;

import com.armadialogcreator.util.KeyValueString;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;

/**
 A {@link FileDependency} used for handling files that are referenced in the application,
 but isn't related to any ADC files.

 @author Kayler
 @since 07/19/2016. */
public class FileDependency {
	private KeyValueString[] properties;
	private final NotNullValueObserver<File> externalFile;

	/**
	 @param file file
	 @param properties other data to save
	 */
	public FileDependency(@NotNull File file, @NotNull KeyValueString[] properties) {
		externalFile = new NotNullValueObserver<>(file);
		this.properties = properties;
	}

	/**
	 @param resourceFile file
	 */
	public FileDependency(@NotNull File resourceFile) {
		this(resourceFile, KeyValueString.EMPTY);
	}

	protected final void setProperties(@NotNull KeyValueString[] properties) {
		this.properties = properties;
	}

	/** @return true if the external resource links to a file that exists, return false if the linked false doesn't exist */
	public boolean fileExists() {
		return externalFile.getValue().exists();
	}

	@Nullable
	public final String getPropertyValue(@NotNull String keyName) {
		for (KeyValueString keyValue : properties) {
			if (keyValue.getKey().equals(keyName)) {
				return keyValue.getValue();
			}
		}
		return null;
	}

	public final void setPropertyValue(@NotNull String keyName, @NotNull String value) {
		for (KeyValueString keyValue : properties) {
			if (keyValue.getKey().equals(keyName)) {
				keyValue.setValue(value);
				break;
			}
		}
	}

	@NotNull
	public KeyValueString[] getProperties() {
		return properties;
	}

	@NotNull
	public NotNullValueObserver<File> getExternalFileObserver() {
		return externalFile;
	}

	@Override
	public String toString() {
		return "FileDependency{" +
				"properties=" + Arrays.toString(properties) +
				", externalFile=" + externalFile +
				'}';
	}

	/** @return shortcut for getting values from {@link #getExternalFileObserver()} */
	@NotNull
	public File getFile() {
		return externalFile.getValue();
	}
}
