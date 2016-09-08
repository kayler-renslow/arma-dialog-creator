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
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.util.KeyValue;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 Created by Kayler on 09/08/2016.
 */
public class ResourceRegistryXmlWriter {
	private final ResourceRegistry resourceRegistry;

	public static class GlobalResourceRegistryXmlWriter extends ResourceRegistryXmlWriter {
		public GlobalResourceRegistryXmlWriter() {
			super(ResourceRegistry.getGlobalRegistry());
			try {
				FileOutputStream fos = new FileOutputStream(ResourceRegistry.getGlobalRegistry().getGlobalResourcesXmlFile());
				fos.write("<?xml version='1.0' encoding='UTF-8' ?>".getBytes());
				write(fos);
			} catch (IOException e) {
				ExceptionHandler.error(e);
			}
		}
	}

	public ResourceRegistryXmlWriter(@NotNull ResourceRegistry resourceRegistry) {
		this.resourceRegistry = resourceRegistry;
	}

	public void write(@NotNull FileOutputStream fos) throws IOException {
		fos.write("<resources>".getBytes());
		for (ExternalResource resource : resourceRegistry.getExternalResourceList()) {
			writeResource(fos, resource);
		}
		fos.write("</resources>".getBytes());
		fos.flush();
		fos.close();
	}

	private void writeResource(@NotNull FileOutputStream fos, ExternalResource resource) throws IOException {
		String attrs = "";
		for (KeyValue<String, ? extends ValueConverter> keyValue : resource.getOtherData()) {
			attrs += " " + keyValue.getKey() + "=\"" + keyValue.getValue().toString() + "\"";
		}
		fos.write(String.format("<resource%s>", attrs).getBytes());
		fos.write(resource.getExternalPath().getPath().getBytes());
		fos.write("</resource>".getBytes());
	}
}
