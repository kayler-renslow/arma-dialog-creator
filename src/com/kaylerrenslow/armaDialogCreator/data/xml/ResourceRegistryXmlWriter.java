package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.data.ExternalResource;
import com.kaylerrenslow.armaDialogCreator.data.GlobalResourceRegistry;
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
			super(GlobalResourceRegistry.getInstance());
		}

		private GlobalResourceRegistryXmlWriter(@NotNull ResourceRegistry resourceRegistry) {
			super(resourceRegistry);
		}

		@NotNull
		private XmlWriterOutputStream getXmlWriterOutputStream(@NotNull GlobalResourceRegistry registry) throws IOException {
			if (!registry.getResourcesFile().exists()) {
				registry.getResourcesFile().createNewFile();
			}
			if (registry.getResourcesFile().isDirectory()) {
				throw new IOException("registry xml file is a directory");
			}
			return new XmlWriterOutputStream(registry.getResourcesFile());
		}


		@Override
		public void write(@NotNull XmlWriterOutputStream fos) throws IOException {
			fos.writeDefaultProlog();
			super.write(fos);
		}

		public static void writeAndClose() throws IOException {
			writeAndClose(GlobalResourceRegistry.getInstance());
		}


		public static void writeAndClose(@NotNull GlobalResourceRegistry registry) throws IOException {
			new GlobalResourceRegistryXmlWriter(registry).doWriteAndClose();
		}

		public void doWriteAndClose() throws IOException {
			XmlWriterOutputStream fos = getXmlWriterOutputStream((GlobalResourceRegistry) this.resourceRegistry);
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
