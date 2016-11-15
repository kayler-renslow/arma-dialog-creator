/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;

/**
 Used to create a link to a resource outside the Project path (APP_SAVE_DIRECTORY/projectname) in the .resources folder (APP_SAVE_DIRECTORY/.resources).
 <br>
 <b>Do not check if a class is instanceof a sub-class of this class. When a resource is loaded from file, {@link ExternalResource} is only used for instantiation.</b>

 @author Kayler
 @since 07/19/2016. */
public class ExternalResource {
	private KeyValueString[] properties;
	private File externalFile;

	/**
	 An ExternalResource is something that is referenced in the Project, but the actual file isn't inside the Project folder.

	 @param resourceFileName file name of the external resource that is located in the .resources directory
	 @param properties other data to save in the resource
	 */
	public ExternalResource(@NotNull String resourceFileName, @NotNull KeyValueString[] properties) {
		this(ResourceRegistry.getResourcesFilePathForName(resourceFileName), properties);
	}

	/**
	 An ExternalResource is something that is referenced in the Project, but the actual file isn't inside the Project folder.

	 @param resourceFile file of the external resource
	 @param properties other data to save in the resource
	 */
	public ExternalResource(@NotNull File resourceFile, @NotNull KeyValueString[] properties) {
		this.externalFile = resourceFile;
		this.properties = properties;
	}

	/**
	 An ExternalResource is something that is referenced in the Project, but the actual file isn't inside the Project folder.

	 @param resourceFileName file name of the external resource that is located in the .resources directory
	 */
	public ExternalResource(@NotNull String resourceFileName) {
		this(resourceFileName, KeyValueString.EMPTY);
	}

	/**
	 An ExternalResource is something that is referenced in the Project, but the actual file isn't inside the Project folder.

	 @param resourceFile file name of the external resource
	 */
	public ExternalResource(@NotNull File resourceFile) {
		this(resourceFile, KeyValueString.EMPTY);
	}

	protected final void setProperties(@NotNull KeyValueString[] properties) {
		this.properties = properties;
	}

	/** Return true if the external resource links to a file that exists, return false if the linked false doesn't exist */
	public boolean resourceExists() {
		return externalFile.exists();
	}

	@Nullable
	public final KeyValueString getPropertyValue(@NotNull String keyName) {
		for (KeyValueString keyValue : properties) {
			if (keyValue.getKey().equals(keyName)) {
				return keyValue;
			}
		}
		return null;
	}

	@NotNull
	public KeyValueString[] getProperties() {
		return properties;
	}

	@NotNull
	public final File getExternalFile() {
		return externalFile;
	}

	public final void setExternalFile(@NotNull File externalFile) {
		this.externalFile = externalFile;
	}

	@Override
	public String toString() {
		return "ExternalResource{" +
				"properties=" + Arrays.toString(properties) +
				", externalFile=" + externalFile +
				'}';
	}
}
