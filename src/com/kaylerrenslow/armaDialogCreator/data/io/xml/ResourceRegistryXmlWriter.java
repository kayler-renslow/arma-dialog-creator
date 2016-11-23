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

import java.io.IOException;

/**
 Created by Kayler on 09/08/2016.
 */
public class ResourceRegistryXmlWriter {
	protected ResourceRegistry resourceRegistry;

	public static class GlobalResourceRegistryXmlWriter extends ResourceRegistryXmlWriter {
		public GlobalResourceRegistryXmlWriter() {
			super(ResourceRegistry.getGlobalRegistry());
		}

		private GlobalResourceRegistryXmlWriter(@NotNull ResourceRegistry resourceRegistry) {
			super(resourceRegistry);
		}

		@NotNull
		private XmlWriterOutputStream getXmlWriterOutputStream(@NotNull ResourceRegistry.GlobalResourceRegistry registry) throws IOException {
			return new XmlWriterOutputStream(registry.getGlobalResourcesXmlFile());
		}


		@Override
		public void write(@NotNull XmlWriterOutputStream fos) throws IOException {
			fos.writeDefaultProlog();
			super.write(fos);
		}

		public static void writeAndClose() throws IOException {
			writeAndClose(ResourceRegistry.getGlobalRegistry());
		}


		public static void writeAndClose(@NotNull ResourceRegistry.GlobalResourceRegistry registry) throws IOException {
			new GlobalResourceRegistryXmlWriter(registry).doWriteAndClose();
		}

		public void doWriteAndClose() throws IOException {
			XmlWriterOutputStream fos = getXmlWriterOutputStream((ResourceRegistry.GlobalResourceRegistry) this.resourceRegistry);
			write(fos);
			fos.flush();
			fos.close();
		}
	}

	public ResourceRegistryXmlWriter(@NotNull ResourceRegistry resourceRegistry) {
		this.resourceRegistry = resourceRegistry;
	}

	public void write(@NotNull XmlWriterOutputStream stm) throws IOException {
		stm.write("<external-resources>");
		for (ExternalResource resource : resourceRegistry.getExternalResourceList()) {
			writeResource(stm, resource);
		}
		stm.write("</external-resources>");
	}

	private void writeResource(@NotNull XmlWriterOutputStream fos, ExternalResource resource) throws IOException {
		String attrs = "";
		for (KeyValueString keyValue : resource.getProperties()) {
			attrs += String.format("<resource-property key='%s'>%s</resource-property>", keyValue.getKey(), keyValue.getValue());
		}
		fos.write("<external-resource>");
		fos.write(resource.getExternalFile().getPath());
		fos.write(attrs);
		fos.write("</external-resource>");
	}
}
