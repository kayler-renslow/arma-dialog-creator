package com.armadialogcreator.application;

import com.armadialogcreator.util.KeyValueString;
import com.armadialogcreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;

/**
 Used to for handling file dependencies.

 @author Kayler
 @since 07/19/2016. */
public class FileDependency {
	private KeyValueString[] properties;
	private final ValueObserver<File> externalFile = new ValueObserver<>();

	/**
	 An FileDependency is something that is referenced in the Project, but the actual file isn't inside the Project folder.

	 @param resourceFile file of the external resource
	 @param properties other data to save in the resource
	 */
	public FileDependency(@NotNull File resourceFile, @NotNull KeyValueString[] properties) {
		externalFile.updateValue(resourceFile);
		this.properties = properties;
	}

	/**
	 An FileDependency is something that is referenced in the Project, but the actual file isn't inside the Project folder.

	 @param resourceFile file name of the external resource
	 */
	public FileDependency(@NotNull File resourceFile) {
		this(resourceFile, KeyValueString.EMPTY);
	}

	protected final void setProperties(@NotNull KeyValueString[] properties) {
		this.properties = properties;
	}

	/** @return true if the external resource links to a file that exists, return false if the linked false doesn't exist */
	public boolean resourceExists() {
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
	public ValueObserver<File> getExternalFileObserver() {
		return externalFile;
	}

	public final void setExternalFile(@NotNull File externalFile) {
		this.externalFile.updateValue(externalFile);
	}

	@Override
	public String toString() {
		return "FileDependency{" +
				"properties=" + Arrays.toString(properties) +
				", externalFile=" + externalFile +
				'}';
	}
}
