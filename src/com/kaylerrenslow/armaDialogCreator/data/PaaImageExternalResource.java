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

import com.kaylerrenslow.armaDialogCreator.data.io.FileConverter;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;

import java.io.File;

/**
 @author Kayler
 Used for creating a link between a .paa image and the converted .png image stored inside the project
 Created on 07/19/2016. */
public class PaaImageExternalResource extends ExternalResource {
	private File paaImagePath;

	private static final String keyPaaImagePath = "paa-image-path";

	/**
	 Create a link for a .paa image

	 @param convertedImageName name of the image that was converted
	 */
	public PaaImageExternalResource(@NotNull String convertedImageName) {
		super(convertedImageName);
	}

	public KeyValueConverterWrapper[] getNewOtherDataInstance(@NotNull File paaImagePath) {
		return new KeyValueConverterWrapper[]{new FileKeyValue(keyPaaImagePath, paaImagePath)};
	}

	@Override
	@SuppressWarnings("unchecked")
	public KeyValueConverterWrapper<String, ?>[] getOtherDataInstance(@NotNull Attr[] attrs) {
		KeyValueConverterWrapper[] arr = new KeyValueConverterWrapper[1];
		for (Attr attr : attrs) {
			if (attr.getName().equals(keyPaaImagePath)) {
				arr[0] = new FileKeyValue(keyPaaImagePath, new File(attr.getValue()));
			}
		}
		if (arr[0] == null) {
			return KeyValueConverterWrapper.EMPTY;
		}
		return arr;
	}

	/** Get the path to the converted .paa image (stored as a .png) */
	@NotNull
	public File getPaaImagePath() {
		FileKeyValue fkv = (FileKeyValue) getOtherDataValue(keyPaaImagePath);
		if (fkv == null) {
			throw new NullPointerException("fkv should not be null");
		}
		return fkv.getValue();
	}

	private static class FileKeyValue extends KeyValueConverterWrapper<String, File> {

		public FileKeyValue(String key, File value) {
			super(key, value, FileConverter.INSTANCE);
		}
	}
}
