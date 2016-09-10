/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.data.ExternalResource;
import com.kaylerrenslow.armaDialogCreator.data.ResourceRegistry;
import com.kaylerrenslow.armaDialogCreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 Created by Kayler on 09/08/2016.
 */
public class ResourceRegistryXmlWriter {
	private final ResourceRegistry resourceRegistry;

	/** xml tag used for holding all {@link #EXTERNAL_INDIV_RESOURCE_TAG_NAME} tags */
	public static final String EXTERNAL_RESOURCES_TAG_NAME = "external-resources";
	/** xml tag used for holding all {@link #RESOURCE_PROPERTY_TAG_NAME} tags */
	public static final String EXTERNAL_INDIV_RESOURCE_TAG_NAME = "external-resource";
	/** xml tag used for holding a resource property */
	public static final String RESOURCE_PROPERTY_TAG_NAME = "resource-property";
	/** attribute name used for tag {@link #RESOURCE_PROPERTY_TAG_NAME} */
	public static final String RESOURCE_PROPERTY_KEY = "key";

	public static class GlobalResourceRegistryXmlWriter extends ResourceRegistryXmlWriter {
		public GlobalResourceRegistryXmlWriter() {
			super(ResourceRegistry.getGlobalRegistry());
		}

		@NotNull
		public FileOutputStream getFileOutputStream() throws FileNotFoundException {
			return new FileOutputStream(ResourceRegistry.getGlobalRegistry().getGlobalResourcesXmlFile());
		}


		@Override
		public void write(@NotNull FileOutputStream fos) throws IOException {
			fos.write("<?xml version='1.0' encoding='UTF-8' ?>".getBytes());
			super.write(fos);
		}

		public void writeAndClose() throws IOException {
			FileOutputStream fos = getFileOutputStream();
			write(fos);
			fos.flush();
			fos.close();
		}
	}

	public ResourceRegistryXmlWriter(@NotNull ResourceRegistry resourceRegistry) {
		this.resourceRegistry = resourceRegistry;
	}

	public void write(@NotNull FileOutputStream fos) throws IOException {
		fos.write(("<" + EXTERNAL_RESOURCES_TAG_NAME + ">").getBytes());
		for (ExternalResource resource : resourceRegistry.getExternalResourceList()) {
			writeResource(fos, resource);
		}
		fos.write(("</" + EXTERNAL_RESOURCES_TAG_NAME + ">").getBytes());
	}

	private void writeResource(@NotNull FileOutputStream fos, ExternalResource resource) throws IOException {
		String attrs = "";
		for (KeyValueString keyValue : resource.getProperties()) {
			attrs += String.format("<%s %s='%s'>%s</%1$s>", RESOURCE_PROPERTY_TAG_NAME, RESOURCE_PROPERTY_KEY, keyValue.getKey(), keyValue.getValue());
		}
		fos.write(("<" + EXTERNAL_INDIV_RESOURCE_TAG_NAME + ">").getBytes());
		fos.write(resource.getExternalPath().getPath().getBytes());
		fos.write(attrs.getBytes());
		fos.write(("</" + EXTERNAL_INDIV_RESOURCE_TAG_NAME + ">").getBytes());
	}
}
