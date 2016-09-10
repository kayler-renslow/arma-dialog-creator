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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

import java.io.File;

/**
 @author Kayler
 Used to create a link to a resource outside the Project path (APP_SAVE_DIRECTORY/projectname) in the .resources folder (APP_SAVE_DIRECTORY/.resources)
 Created on 07/19/2016.
 */
public class ExternalResource {
	private KeyValueConverterWrapper<String, ?>[] otherData;
	private File externalPath;
	
	/** An ExternalResource is something that is referenced in the Project, but the actual file isn't inside the Project folder.
	 @param resourceFileName file name of the external resource that is located in the .resources directory
	 @param otherData other data to save in the resource
	 */
	public ExternalResource(@NotNull String resourceFileName, @NotNull KeyValueConverterWrapper<String, ?>[] otherData) {
		this.externalPath = ResourceRegistry.getResourcesFilePathForName(resourceFileName);
		this.otherData = otherData;
	}

	/** An ExternalResource is something that is referenced in the Project, but the actual file isn't inside the Project folder.
	 @param resourceFileName file name of the external resource that is located in the .resources directory
	 */
	public ExternalResource(@NotNull String resourceFileName) {
		this(resourceFileName, KeyValueConverterWrapper.EMPTY);
	}

	protected final void setOtherData(@NotNull KeyValueConverterWrapper<String, ?>[] otherData){
		this.otherData = otherData;
	}

	@Nullable
	public final KeyValueConverterWrapper<String, ?> getOtherDataValue(@NotNull String keyName){
		for(KeyValueConverterWrapper<String, ?> keyValue : otherData){
			if(keyValue.getKey().equals(keyName)){
				return keyValue;
			}
		}
		return null;
	}

	/** Override this method to provide support for loading attributes from an xml file. Default implementation returns {@link KeyValueConverterWrapper#EMPTY}
	 @param attrs attributes of the xml tag
	 @return array to be used in {@link #setOtherData(KeyValueConverterWrapper[])}
	 */
	public KeyValueConverterWrapper<String, ?>[] getOtherDataInstance(@NotNull Attr[] attrs){
		return KeyValueConverterWrapper.EMPTY;
	}

	@NotNull
	public KeyValueConverterWrapper<String, ?>[] getOtherData() {
		return otherData;
	}

	@NotNull
	public final File getExternalPath() {
		return externalPath;
	}
	
	public final void setExternalPath(@NotNull File externalPath) {
		this.externalPath = externalPath;
	}
	
}
